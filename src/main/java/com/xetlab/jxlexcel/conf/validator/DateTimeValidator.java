package com.xetlab.jxlexcel.conf.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by gordian on 2016/1/12.
 */
public class DateTimeValidator extends Validator {
  private String dateFormat;

  public DateTimeValidator() {
    defaultMsg = "无效的日期格式";
  }

  @Override
  public boolean validate(String input) {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    try {
      sdf.parse(input);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }
}
