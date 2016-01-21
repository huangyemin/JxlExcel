package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class MinValidator extends Validator {

    private Float min;

    public void setMin(Float min) {
        this.min = min;
        defaultMsg = "最小值为" + min;
    }

    @Override
    public boolean validate(String input) {
        if (min == null) {
            throw new IllegalArgumentException("min未设置");
        }
        try {
            return new Float(input) >= min;
        } catch (NumberFormatException e) {
            defaultMsg = "不是有效的数值";
            return false;
        }
    }

}
