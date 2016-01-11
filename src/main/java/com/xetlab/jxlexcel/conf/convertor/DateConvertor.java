package com.xetlab.jxlexcel.conf.convertor;

import com.xetlab.jxlexcel.JxlExcelException;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by gordian on 2016/1/10.
 */
public class DateConvertor extends BaseConvertor {
    public static final String DATE = "yyyy-MM-dd";

    @Override
    public String doConvert(Object input) {
        return DateFormatUtils.format((Date) input, DATE);
    }

    @Override
    public Object doConvertToType(String input) {
        try {
            return DateUtils.parseDate(input, new String[]{DATE});
        } catch (ParseException e) {
            throw new JxlExcelException(e);
        }
    }
}
