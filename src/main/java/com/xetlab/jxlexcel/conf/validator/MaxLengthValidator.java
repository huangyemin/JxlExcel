package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class MaxLengthValidator extends Validator {

    private Integer maxLength;

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
        defaultMsg = "最大长度为" + maxLength;
    }

    @Override
    public boolean validate(String input) {
        if (maxLength == null) {
            throw new IllegalArgumentException("max未设置");
        }
        return input.length() <= maxLength;
    }

}
