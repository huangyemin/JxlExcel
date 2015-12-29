package com.xetlab.jxlexcel.conf;

import java.util.ArrayList;
import java.util.List;

public class DataRow {

    private List<DataCol> properties = new ArrayList<DataCol>();

    public void addProperty(String... names) {
        for (String name : names) {
            properties.add(new DataCol(name));
        }
    }

    public List<DataCol> getDataCols() {
        return properties;
    }

    public int colSize() {
        return properties.size();
    }

}
