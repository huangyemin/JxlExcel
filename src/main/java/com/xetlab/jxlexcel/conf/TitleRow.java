package com.xetlab.jxlexcel.conf;

import java.util.ArrayList;
import java.util.List;

public class TitleRow {

	private List<TitleCol> cols = new ArrayList<TitleCol>();

	public TitleRow() {
		this(null);
	}

	public TitleRow(String[] titles) {
		if (titles != null) {
			addCol(titles);
		}
	}

	public boolean hasSpanCol() {
		for (TitleCol titleCol : cols) {
			if (titleCol.getColSpan() > 1) {
				return true;
			}
		}
		return false;
	}

	public void addCol(String title, int colSpan) {
		cols.add(new TitleCol(title, colSpan));
		if (colSpan > 1) {
			for (int i = 0; i < colSpan - 1; i++) {
				cols.add(new DummyTitleCol());
			}
		}
	}

	public void addCol(String... titles) {
		for (int i = 0; i < titles.length; i++) {
			cols.add(new TitleCol(titles[i]));
		}
	}

	public List<TitleCol> getCols() {
		return cols;
	}

	public int colSize() {
		return cols.size();
	}

	public TitleCol getCol(int col) {
		return cols.get(col);
	}

}
