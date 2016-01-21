package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class MaxValidator extends Validator {

    private Float max;

    public void setMax(Float max) {
        this.max = max;
        defaultMsg = "最大值为" + max;
    }

    @Override
    public boolean validate(String input) {
        if (max == null) {
            throw new IllegalArgumentException("max未设置");
        }
        try {
            return new Float(input) <= max;
        } catch (NumberFormatException e) {
            defaultMsg = "不是有效的数值";
            return false;
        }
    }

}
