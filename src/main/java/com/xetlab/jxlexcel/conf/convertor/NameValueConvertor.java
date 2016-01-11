package com.xetlab.jxlexcel.conf.convertor;

import com.xetlab.jxlexcel.JxlExcelException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gordian on 2016/1/10.
 */
public abstract class NameValueConvertor extends BaseConvertor {

    protected Map<String, String> nvMap = new HashMap<String, String>();

    protected abstract void initNameValues();

    @Override
    public String doConvert(Object input) {
        if (!nvMap.containsKey(input)) {
            throw new JxlExcelException(String.format("无效名：%s", input));
        }
        return nvMap.get(input);
    }

    @Override
    public Object doConvertToType(String input) {
        for (String key : nvMap.keySet()
                ) {
            if (nvMap.get(key).equals(input)) {
                return key;
            }
        }
        throw new JxlExcelException(String.format("无效值：%s", input));
    }
}
