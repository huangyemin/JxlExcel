package com.xetlab.jxlexcel.conf.convertor;

import com.xetlab.jxlexcel.JxlExcelException;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by gordian on 2016/1/10.
 */
public class DateTimeConvertor extends BaseConvertor {

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String doConvert(Object input) {
        return DateFormatUtils.format((Date) input, DATETIME);
    }

    @Override
    public Object doConvertToType(String input) {
        try {
            return DateUtils.parseDate(input, new String[]{DATETIME});
        } catch (ParseException e) {
            throw new JxlExcelException(e);
        }
    }
}
