package com.xetlab.jxlexcel.conf;

public class TitleCol {

	private String title;
	private int colSpan = 1;

	public TitleCol(String title) {
		this(title, 1);
	}

	public TitleCol(String title, int colSpan) {
		this.colSpan = colSpan;
		this.title = title;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
