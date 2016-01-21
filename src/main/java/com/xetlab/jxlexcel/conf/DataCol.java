package com.xetlab.jxlexcel.conf;

import com.xetlab.jxlexcel.conf.validator.ColValidateResult;
import com.xetlab.jxlexcel.conf.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class DataCol {

    private String name;

    private int colIndex;

    private String convertor;

    private List<Validator> validators = new ArrayList<Validator>();

    public DataCol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public String getConvertor() {
        return convertor;
    }

    public void setConvertor(String convertor) {
        this.convertor = convertor;
    }

    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    public ColValidateResult validate(String input) {
        ColValidateResult result = new ColValidateResult();
        result.setDataCol(this);
        for (Validator validator : validators) {
            if (!validator.validate(input)) {
                result.setErrorMsg(validator.getErrorMsg());
                break;
            }
        }
        return result;
    }

    public boolean hasValidator() {
        return !validators.isEmpty();
    }
}
