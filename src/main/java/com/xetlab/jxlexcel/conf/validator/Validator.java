package com.xetlab.jxlexcel.conf.validator;

import org.apache.commons.lang.StringUtils;

/**
 * Created by gordian on 2016/1/5.
 */
public abstract class Validator {

    protected String errorMsg;

    protected String defaultMsg;

    public abstract boolean validate(String input);

    public String getErrorMsg() {
        if (StringUtils.isEmpty(errorMsg)) {
            return defaultMsg;
        }
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
