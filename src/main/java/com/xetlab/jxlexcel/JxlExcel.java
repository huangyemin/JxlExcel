package com.xetlab.jxlexcel;

import java.util.List;

import com.xetlab.jxlexcel.conf.ExcelTemplate;
import com.xetlab.jxlexcel.conf.TitleRow;

public class JxlExcel {

    public static final int MAX_ROW_PER_SHEET = 60000;
    protected ExcelTemplate excelTemplate;

    public void setExcelTemplate(ExcelTemplate excelTemplate) {
        this.excelTemplate = excelTemplate;
    }

    private void checkTemplateNotNull() throws JxlExcelException {
        if (excelTemplate == null) {
            throw new JxlExcelException("excel模板配置未初始化");
        }
    }

    private void checkTemplateColSize() throws JxlExcelException {
        List<TitleRow> titleRows = excelTemplate.getTitleRows();
        if (titleRows.size() < 1) {
            throw new JxlExcelException("必须至少定义一个标题行");
        }
        if (titleRows.size() == 1) {
            return;
        }
        int colSize = titleRows.get(0).colSize();
        for (int i = 1; i < titleRows.size(); i++) {
            TitleRow titleRow = titleRows.get(i);
            if (colSize != titleRow.colSize()) {
                throw new JxlExcelException("标题行列数必须一致");
            }
        }
        if (excelTemplate.getDataRow() != null) {
            if (colSize != excelTemplate.getDataRow().colSize()) {
                throw new JxlExcelException("标题行列数必须与数据行列数一致");
            }
        }
    }

    protected void checkTemplate() throws JxlExcelException {
        checkTemplateNotNull();
        checkTemplateColSize();
    }

}
