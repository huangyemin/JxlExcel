package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/5.
 */
public class ChineseValidator extends RegexValidator {

    public ChineseValidator() {
        setRegex("^[\\u4e00-\\u9fa5]+$");
        defaultMsg = "不是中文";
    }

}
