package com.xetlab.jxlexcel.conf.validator;

import com.xetlab.jxlexcel.conf.DataCol;

/**
 * Created by gordian on 2016/1/12.
 */
public class ColValidateResult {

  private DataCol dataCol;
  private String errorMsg;
  private boolean isSuccess = true;

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
    isSuccess = false;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public boolean isFailed() {
    return !isSuccess;
  }

  public void setDataCol(DataCol dataCol) {
    this.dataCol = dataCol;
  }
}
