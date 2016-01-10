package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class RangeValidator extends Validator {

    private Float min;
    private Float max;

    public RangeValidator(Float min, Float max) {
        this.min = min;
        this.max = max;
        errorMsg = String.format("值必须在[%s-%s]间", min, max);
    }

    @Override
    public boolean validate(String input) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("参数设置不正确");
        }
        Float inputVal = new Float(input);
        return inputVal >= min && inputVal <= max;
    }

}
