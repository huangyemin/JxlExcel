package com.xetlab.jxlexcel.conf.convertor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gordian on 2016/1/10.
 */
public class Convertors {
    private static Convertors instance = null;
    private Map<String, Convertor> convertorMap = new HashMap<String, Convertor>();

    private Convertors() {
        initDefaultConvertors();
    }

    public static Convertors instance() {
        if (instance == null) {
            instance = new Convertors();
        }
        return instance;
    }

    private void initDefaultConvertors() {
        convertorMap.put("date", new DateConvertor());
        convertorMap.put("datetime", new DateTimeConvertor());
    }

    public Convertor getConvertor(String name) {
        if (!convertorMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("convertor:%s不存在", name));
        }
        return convertorMap.get(name);
    }

    public void registConvertor(String name, Convertor convertor) {
        if (convertorMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("convertor:%s已存在", name));
        }
        convertorMap.put(name, convertor);
    }
}
