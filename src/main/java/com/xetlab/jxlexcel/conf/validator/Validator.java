package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public abstract class Validator {

    protected String errorMsg;

    public abstract boolean validate(String input);

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
