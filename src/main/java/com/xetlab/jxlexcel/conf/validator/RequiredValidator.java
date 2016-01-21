package com.xetlab.jxlexcel.conf.validator;

import org.apache.commons.lang.StringUtils;

/**
 * Created by gordian on 2016/1/5.
 */
public class RequiredValidator extends Validator {

    public RequiredValidator() {
        defaultMsg = "不能为空";
    }

    @Override
    public boolean validate(String input) {
        return StringUtils.isNotEmpty(input);
    }
}
