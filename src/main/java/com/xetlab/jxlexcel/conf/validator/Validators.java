package com.xetlab.jxlexcel.conf.validator;

import com.xetlab.jxlexcel.JxlExcelException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gordian on 2016/1/11.
 */
public class Validators {
    private static Validators instance = null;
    private Map<String, Class> validatorMap = new HashMap<String, Class>();

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
        validatorMap.put("num", NumValidator.class);
        validatorMap.put("min", MinValidator.class);
        validatorMap.put("max", MaxValidator.class);
        validatorMap.put("minLength", MinLengthValidator.class);
        validatorMap.put("maxLength", MaxLengthValidator.class);
        validatorMap.put("rangeLength", RangeLengthValidator.class);
        validatorMap.put("range", RangeValidator.class);
        validatorMap.put("mobile", MobileValidator.class);
        validatorMap.put("chinese", ChineseValidator.class);
        validatorMap.put("email", EmailValidator.class);
        validatorMap.put("required", RequiredValidator.class);
        validatorMap.put("regex", RegexValidator.class);
        validatorMap.put("datetime", DateTimeValidator.class);
    }

    public Validator getValidator(String name) {
        if (!validatorMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Validator:%s不存在", name));
        }
        try {
            return (Validator) validatorMap.get(name).newInstance();
        } catch (InstantiationException e) {
            throw new JxlExcelException(e);
        } catch (IllegalAccessException e) {
            throw new JxlExcelException(e);
        }
    }

    public void registValidator(String name, Class clasz) {
        if (validatorMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Validator:%s已存在", name));
        }
        validatorMap.put(name, clasz);
    }
}
