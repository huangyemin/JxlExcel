package com.xetlab.jxlexcel;

import com.xetlab.jxlexcel.conf.DataRow;
import com.xetlab.jxlexcel.conf.ExcelTemplate;
import com.xetlab.jxlexcel.conf.TitleRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JxlExcelReaderTest extends Assert {

    @Test
    public void testReadArrays() throws Exception {
        JxlExcelReader reader = prepareReader(false);

        List<String[]> datas = reader.readArrays();
        assertEquals(293, datas.size());
    }

    @Test
    public void testReadMaps() throws Exception {
        JxlExcelReader reader = prepareReader(false);

        List<Map<String, Object>> datas = reader.readMaps();
        assertEquals(293, datas.size());
    }

    @Test
    public void testReadBeans() throws Exception {
        JxlExcelReader reader = prepareReader(false);

        List<Account> datas = reader.readBeans(Account.class);
        assertEquals(293, datas.size());
    }

    @Test
    public void testConfedTemplate() throws Exception {
        JxlExcelReader reader = getJxlExcelReader();
        reader.setExcelTemplate("testRead");
        List<String[]> datas = reader.readArrays();
        assertEquals(293, datas.size());
        reader = getJxlExcelReader();
        reader.setExcelTemplate("testRead");
        List<Account> beans = reader.readBeans(Account.class);
        assertEquals(293, beans.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals("2015-01-01 12:00:00", sdf.format(beans.get(0).getCreateTime()));
        reader = getJxlExcelReader();
        reader.setExcelTemplate("testRead");
        List<Map<String, Object>> maps = reader.readMaps();
        assertEquals(293, maps.size());
    }

    @Test
    public void testErrorTemplate() throws Exception {
        JxlExcelReader reader = prepareReader(true);

        doErrorTest(reader);
    }

    @Test
    public void testErrorTemplateColsNotEuqals() throws Exception {
        JxlExcelReader reader = prepareErrorTemplateReaderColNotEquals();

        doErrorTest(reader);
    }

    @Test
    public void testErrorProperty() throws Exception {
        JxlExcelReader reader = prepareErrorPropertyReader();

        doErrorTest(reader);
    }

    private void doErrorTest(JxlExcelReader reader) {
        try {
            reader.readBeans(Account.class);
            fail("模板错误，应抛出异常");
        } catch (Exception e) {
            assertTrue(e instanceof JxlExcelException);
        }
    }

    private JxlExcelReader prepareReader(boolean isErrorTpl)
            throws JxlExcelException {
        JxlExcelReader reader = getJxlExcelReader();

        ExcelTemplate template = new ExcelTemplate();
        TitleRow titleRow = new TitleRow();
        if (isErrorTpl) {
            titleRow.addCol("艓栽植林在地asdfasdf", 6);
        } else {
            titleRow.addCol("艓栽植林在地", 6);
        }

        template.addTitleRow(titleRow);
        titleRow = new TitleRow();
        titleRow.addCol("编制单位（盖章）：");
        titleRow.addCol("");
        if (isErrorTpl) {
            titleRow.addCol("编制日期1234：", 3);
        } else {
            titleRow.addCol("编制日期：", 3);
        }

        titleRow.addCol("单位：元");

        template.addTitleRow(titleRow);
        if (isErrorTpl) {
            template.addTitleRow(new TitleRow(new String[]{"村（社区）1243", "姓名",
                    "身份证", "账号", "金额", "备注1234"}));
        } else {
            template.addTitleRow(new TitleRow(new String[]{"村（社区）", "姓名",
                    "身份证", "账号", "金额", "创建时间"}));
        }

        DataRow dataRow = new DataRow();
        if (isErrorTpl) {
            dataRow.addDataCol("area", "name", "idCard",
                    "bankAccount", "amount", "remark111");
        } else {
            dataRow.addDataCol("area", "name", "idCard",
                    "bankAccount", "amount", "remark");
        }

        template.setDataRow(dataRow);
        reader.setExcelTemplate(template);
        return reader;
    }

    private JxlExcelReader prepareErrorPropertyReader()
            throws JxlExcelException {
        JxlExcelReader reader = getJxlExcelReader();

        ExcelTemplate template = new ExcelTemplate();
        TitleRow titleRow = new TitleRow();
        titleRow.addCol("艓栽植林在地", 6);

        template.addTitleRow(titleRow);
        titleRow = new TitleRow();
        titleRow.addCol("编制单位（盖章）：");
        titleRow.addCol("");
        titleRow.addCol("编制日期：", 3);

        titleRow.addCol("单位：元");

        template.addTitleRow(titleRow);
        template.addTitleRow(new TitleRow(new String[]{"村（社区）", "姓名", "身份证",
                "账号", "金额", "创建时间"}));

        DataRow dataRow = new DataRow();
        dataRow.addDataCol("area", "name", "idCard",
                "bankAccount", "amount", "remark1");

        template.setDataRow(dataRow);
        reader.setExcelTemplate(template);
        return reader;
    }

    private JxlExcelReader prepareErrorTemplateReaderColNotEquals()
            throws JxlExcelException {
        JxlExcelReader reader = getJxlExcelReader();

        ExcelTemplate template = new ExcelTemplate();
        TitleRow titleRow = new TitleRow();
        titleRow.addCol("艓栽植林在地asdfasdf", 7);

        template.addTitleRow(titleRow);
        titleRow = new TitleRow();
        titleRow.addCol("编制单位（盖章）：");
        titleRow.addCol("");
        titleRow.addCol("编制日期：", 4);

        titleRow.addCol("单位：元");

        template.addTitleRow(titleRow);
        template.addTitleRow(new TitleRow(new String[]{"村（社区）1243", "姓名",
                "身份证", "账号", "金额", "备注1234", "asdfasdf"}));

        DataRow dataRow = new DataRow();
        dataRow.addDataCol("area", "name", "idCard",
                "bankAccount", "amount", "remark", "aaa");

        template.setDataRow(dataRow);
        reader.setExcelTemplate(template);
        return reader;
    }

    private JxlExcelReader getJxlExcelReader() {
        InputStream is = JxlExcelReaderTest.class
                .getResourceAsStream("/testRead.xls");
        return new JxlExcelReader(is);
    }

}
