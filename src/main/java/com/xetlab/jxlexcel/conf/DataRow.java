package com.xetlab.jxlexcel.conf;

import java.util.ArrayList;
import java.util.List;

public class DataRow {

    private List<DataCol> dataCols = new ArrayList<DataCol>();

    public void addDataCol(String... names) {
        for (String name : names) {
            dataCols.add(new DataCol(name));
        }
    }

    public void addDataCol(DataCol dataCol) {
        dataCols.add(dataCol);
    }

    public List<DataCol> getDataCols() {
        return dataCols;
    }

    public int colSize() {
        return dataCols.size();
    }

}
