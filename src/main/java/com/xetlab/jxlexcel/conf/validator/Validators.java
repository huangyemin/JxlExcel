package com.xetlab.jxlexcel.conf.validator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gordian on 2016/1/11.
 */
public class Validators {
    private static Validators instance = null;
    private Map<String, Validator> validatorMap = new HashMap<String, Validator>();

    private Validators() {
        initDefaultValidators();
    }

    public static Validators instance() {
        if (instance == null) {
            instance = new Validators();
        }
        return instance;
    }

    private void initDefaultValidators() {
        validatorMap.put("num", new NumValidator());
        validatorMap.put("min", new MinValidator());
        validatorMap.put("max", new MaxValidator());
        validatorMap.put("minLength", new MinLengthValidator());
        validatorMap.put("maxLength", new MaxLengthValidator());
        validatorMap.put("rangeLength", new RangeLengthValidator());
        validatorMap.put("range", new RangeValidator());
        validatorMap.put("mobile", new MobileValidator());
        validatorMap.put("chinese", new ChineseValidator());
        validatorMap.put("email", new EmailValidator());
        validatorMap.put("required", new RequiredValidator());
        validatorMap.put("regex", new RegexValidator());
    }

    public Validator getValidator(String name) {
        if (!validatorMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Validator:%s不存在", name));
        }
        return validatorMap.get(name);
    }

    public void registValidator(String name, Validator Validator) {
        if (validatorMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Validator:%s已存在", name));
        }
        validatorMap.put(name, Validator);
    }
}
