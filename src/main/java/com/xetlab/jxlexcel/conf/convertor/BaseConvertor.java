package com.xetlab.jxlexcel.conf.convertor;

import org.apache.commons.lang.StringUtils;

/**
 * Created by gordian on 2016/1/10.
 */
public abstract class BaseConvertor implements Convertor {
    @Override
    public String convert(Object input) {
        if (isEmptyObj(input))
            return "";
        return doConvert(input);
    }

    protected abstract String doConvert(Object input);

    private boolean isEmptyObj(Object input) {
        return input == null || input.toString() == "";
    }

    @Override
    public Object convertToType(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        return doConvertToType(input);
    }

    protected abstract Object doConvertToType(String input);
}
