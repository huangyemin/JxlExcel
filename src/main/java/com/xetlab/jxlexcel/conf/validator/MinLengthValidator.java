package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class MinLengthValidator extends Validator {

    private Integer minLength;


    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
        errorMsg = "最小长度为" + minLength;
    }

    @Override
    public boolean validate(String input) {
        if (minLength == null) {
            throw new IllegalArgumentException("min未设置");
        }
        return input.length() >= minLength;
    }

}
