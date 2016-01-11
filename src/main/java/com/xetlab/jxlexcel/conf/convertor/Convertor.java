package com.xetlab.jxlexcel.conf.convertor;

/**
 * Created by gordian on 2016/1/10.
 */
public interface Convertor {
    public String convert(Object input);

    public Object convertToType(String input);
}
