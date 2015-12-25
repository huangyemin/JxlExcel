package com.xetlab.jxlexcel.conf;

import java.util.ArrayList;
import java.util.List;

import com.xetlab.jxlexcel.JxlExcelException;

public class ExcelTemplate {

	private List<TitleRow> titleRows = new ArrayList<TitleRow>();
	private DataRow dataRow;

	public void addTitleRow(TitleRow titleRow) throws JxlExcelException {
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

	public List<Property> getProperties() {
		return dataRow.getProperties();
	}

}
