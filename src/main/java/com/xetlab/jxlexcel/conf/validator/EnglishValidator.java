package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class EnglishValidator extends RegexValidator {

    public EnglishValidator() {
        setRegex("^[A-Za-z]+$");
        defaultMsg = "不是英文";
    }

}
