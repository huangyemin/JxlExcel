package com.xetlab.jxlexcel.conf;

import java.util.ArrayList;
import java.util.List;

public class ExcelTemplate {

    private List<TitleRow> titleRows = new ArrayList<TitleRow>();
    private DataRow dataRow;
    private String name;

    public void addTitleRow(TitleRow titleRow) {
        titleRows.add(titleRow);
    }

    public List<TitleRow> getTitleRows() {
        return titleRows;
    }

    public int getDataRowIndex() {
        return titleRows.size();
    }

    public DataRow getDataRow() {
        return dataRow;
    }

    public void setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
    }

    public int getColSize() {
        return titleRows.get(0).colSize();
    }

    public List<DataCol> getDataCols() {
        return dataRow.getDataCols();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
