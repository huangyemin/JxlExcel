package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class RangeLengthValidator extends Validator {

    private Integer minLength;
    private Integer maxLength;

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean validate(String input) {
        if (minLength == null || maxLength == null) {
            throw new IllegalArgumentException("参数设置不正确");
        }
        errorMsg = String.format("长度必须在[%s-%s]间", minLength, maxLength);
        return input.length() >= minLength && input.length() <= maxLength;
    }

}
