package com.xetlab.jxlexcel.conf.validator;

/**
 * Created by gordian on 2016/1/11.
 */
public class ValidatorUtil {

    public static Validator getValidator(String name) {
        return Validators.instance().getValidator(name);
    }

    public static void registValidator(String name, Class clasz) {
        Validators.instance().registValidator(name, clasz);
    }
}
