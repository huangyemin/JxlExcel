package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class EmailValidator extends RegexValidator {

    public EmailValidator() {
        super("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        errorMsg = "不是正确的邮箱格式";
    }

}
