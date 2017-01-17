package com.xetlab.jxlexcel.conf.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordian on 2016/1/21.
 */
public class RowValidateResult {
  private int rowIndex;

  private List<ColValidateResult> colValidateResults = new ArrayList<ColValidateResult>();

  public int getRowIndex() {
    return rowIndex;
  }

  public void setRowIndex(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  public List<ColValidateResult> getColValidateResults() {
    return colValidateResults;
  }

  public void addColValidateResult(ColValidateResult colValidateResult) {
    colValidateResults.add(colValidateResult);
  }

}
