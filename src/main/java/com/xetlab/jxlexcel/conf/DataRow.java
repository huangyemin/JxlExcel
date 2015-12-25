package com.xetlab.jxlexcel.conf;

import java.util.ArrayList;
import java.util.List;

public class DataRow {

	private List<Property> properties = new ArrayList<Property>();

	public void addProperty(String... names) {
		for (int i = 0; i < names.length; i++) {
			properties.add(new Property(names[i]));
		}
	}

	public List<Property> getProperties() {
		return properties;
	}

	public int colSize() {
		return properties.size();
	}

}
