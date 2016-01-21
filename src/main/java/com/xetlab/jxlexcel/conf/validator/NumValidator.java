package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class NumValidator extends Validator {

    public NumValidator() {
        defaultMsg = "不是有效的数字";
    }

    @Override
    public boolean validate(String input) {
        try {
            new Float(input);
            return true;
        } catch (NumberFormatException e) {
            defaultMsg = "不是有效的数字";
            return false;
        }
    }
}
