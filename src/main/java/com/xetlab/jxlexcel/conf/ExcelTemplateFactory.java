package com.xetlab.jxlexcel.conf;

import com.xetlab.jxlexcel.JxlExcelException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

public class ExcelTemplateFactory {

    private static ExcelTemplateFactory instance = null;
    private static Log logger = LogFactory.getLog(ExcelTemplateFactory.class);
    private final long refreshDelay = 30000;
    private Map<String, ExcelTemplate> templateMap = new HashMap<String, ExcelTemplate>();
    private List<String> defaultPatterns = Arrays.asList(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});

    private ExcelTemplateFactory() throws JxlExcelException {
        BeanUtilsBean.setInstance(new BeanUtilsBean2());
        loadTemplates();
        monitorConfigFile();
        registerDateConverter(defaultPatterns.toArray(new String[0]));
    }

    public static ExcelTemplateFactory getInstance() throws JxlExcelException {
        if (instance == null) {
            instance = new ExcelTemplateFactory();
        }
        return instance;
    }

    private void registerDateConverter(String... patterns) {
        List<String> patterList = new ArrayList<String>();
        for (String pattern : patterns) {
            if (!defaultPatterns.contains(pattern)) {
                patterList.add(pattern);
            }
        }
        patterList.addAll(defaultPatterns);
        DateConverter converter = new DateConverter();
        converter.setPatterns(patterList.toArray(new String[0]));
        ConvertUtils.register(converter, Date.class);
    }

    private void monitorConfigFile() throws JxlExcelException {
        FileAlterationObserver observer = new FileAlterationObserver(getClass().getResource("/").getFile(), new NameFileFilter("jxl-excel.xml"));
        FileAlterationListenerAdaptor listener = new FileAlterationListenerAdaptor() {
            @Override
            public void onFileChange(File file) {
                try {
                    loadTemplates();
                } catch (JxlExcelException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        observer.addListener(listener);
        final FileAlterationMonitor monitor = new FileAlterationMonitor(refreshDelay, observer);
        try {
            monitor.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        monitor.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            throw new JxlExcelException(e);
        }
    }

    private void loadTemplates() throws JxlExcelException {
        try {
            logger.debug("loadTemplates");
            templateMap.clear();
            XMLConfiguration xmlConfig = new XMLConfiguration(getClass().getResource("/jxl-excel.xml"));
            List<HierarchicalConfiguration> templates = xmlConfig.configurationsAt("template");
            for (HierarchicalConfiguration templateConf : templates) {
                String templateName = templateConf.getString("[@name]");
                if (StringUtils.isEmpty(templateName)) {
                    throw new JxlExcelException("模板的名称属性name不能为空");
                }
                ExcelTemplate excelTemplate = new ExcelTemplate();
                excelTemplate.setName(templateName);
                List<HierarchicalConfiguration> titleRows = templateConf.configurationsAt("titleRow");
                for (HierarchicalConfiguration titleRowConf : titleRows) {
                    TitleRow titleRow = new TitleRow();
                    List<HierarchicalConfiguration> titleCols = titleRowConf.configurationsAt("titleCol");
                    for (HierarchicalConfiguration titleColConf : titleCols) {
                        Integer span = titleColConf.getInteger("[@span]", 1);
                        String title = titleColConf.getString("");
                        titleRow.addCol(title, span);
                    }
                    excelTemplate.addTitleRow(titleRow);
                }
                HierarchicalConfiguration dataRowConfig = templateConf.configurationsAt("dataRow").get(0);
                List<HierarchicalConfiguration> dataCols = dataRowConfig.configurationsAt("dataCol");
                DataRow dataRow = new DataRow();
                for (HierarchicalConfiguration dataColConf : dataCols) {
                    DataCol dataCol = new DataCol(dataColConf.getString(""));
                    String dateFormat = dataColConf.getString("[@dateFormat]");
                    if (StringUtils.isNotEmpty(dateFormat)) {
                        dataCol.setDateFormat(dateFormat);
                        registerDateConverter(dateFormat);
                    }
                    dataRow.addDataCol(dataCol);
                }
                excelTemplate.setDataRow(dataRow);
                templateMap.put(templateName, excelTemplate);
                logger.debug(String.format("加载了excel模板：%s", templateName));
            }
        } catch (ConfigurationException e) {
            logger.warn("未在classpath路径下找到jxl-excel.xml文件，将不能使用配置模式配置excel模板");
        }
    }

    public ExcelTemplate getTemplate(String templateName) throws JxlExcelException {
        if (!templateMap.containsKey(templateName)) {
            throw new JxlExcelException(String.format("名称为%s的模板不存在", templateName));
        }
        return templateMap.get(templateName);
    }

}
