package com.xetlab.jxlexcel.conf;

public class DataCol {

	private String name;

	private String convertor;

	public DataCol(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConvertor() {
		return convertor;
	}

	public void setConvertor(String convertor) {
		this.convertor = convertor;
	}
}
