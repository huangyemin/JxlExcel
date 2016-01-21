package com.xetlab.jxlexcel;

import com.xetlab.jxlexcel.conf.DataCol;
import com.xetlab.jxlexcel.conf.DummyTitleCol;
import com.xetlab.jxlexcel.conf.TitleCol;
import com.xetlab.jxlexcel.conf.TitleRow;
import com.xetlab.jxlexcel.conf.convertor.ConvertorUtil;
import com.xetlab.jxlexcel.conf.validator.ColValidateResult;
import com.xetlab.jxlexcel.conf.validator.RowValidateResult;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JxlExcelReader extends JxlExcel {

    private static Log logger = LogFactory.getLog(JxlExcelReader.class);

    private InputStream is;

    private List<RowValidateResult> rowValidateResults = new ArrayList<RowValidateResult>();

    public JxlExcelReader(File excelFile) throws FileNotFoundException {
        this(new FileInputStream(excelFile));
    }

    public JxlExcelReader(InputStream is) {
        this.is = is;
    }

    public boolean isDataValid() {
        return rowValidateResults.size() == 0;
    }

    public List<RowValidateResult> getRowValidateResults() {
        return rowValidateResults;
    }

    public <T> List<T> readBeans(final Class<T> clasz) {
        return read(new ReadPolicy<T>() {
            @Override
            protected T newRowData() {
                try {
                    return clasz.newInstance();
                } catch (InstantiationException e) {
                    throw new JxlExcelException(e);
                } catch (IllegalAccessException e) {
                    throw new JxlExcelException(e);
                }
            }

            @Override
            protected void setColData(T rowData, DataCol dataCol, Object colDataVal) {
                try {
                    BeanUtils.setProperty(rowData, dataCol.getName(), colDataVal);
                } catch (IllegalAccessException e) {
                    throw new JxlExcelException(e);
                } catch (InvocationTargetException e) {
                    throw new JxlExcelException(e);
                }
            }

        });
    }

    public List<String[]> readArrays() {
        return read(new ReadPolicy<String[]>() {
            @Override
            protected String[] newRowData() {
                return new String[excelTemplate.getColSize()];
            }

            @Override
            protected void setColData(String[] rowData, DataCol dataCol, Object colDataVal) {
                rowData[dataCol.getColIndex()] = ObjectUtils.toString(colDataVal);
            }

        });
    }

    public List<Map<String, Object>> readMaps() {
        return read(new ReadPolicy<Map<String, Object>>() {
            @Override
            protected Map<String, Object> newRowData() {
                return new HashMap<String, Object>();
            }

            @Override
            protected void setColData(Map<String, Object> rowData, DataCol dataCol, Object colDataVal) {
                rowData.put(dataCol.getName(), colDataVal);
            }
        });
    }

    private <T> List<T> read(ReadPolicy<T> readPolicy) {
        checkTemplate();
        Workbook wb;
        try {
            wb = Workbook.getWorkbook(is);
        } catch (BiffException e) {
            throw new JxlExcelException(e);
        } catch (IOException e) {
            throw new JxlExcelException(e);
        }
        try {
            Sheet sheet = wb.getSheet(0);
            readPolicy.checkTemplateTitles(sheet);
            return readPolicy.readDatasFromSheet(sheet);
        } finally {
            wb.close();
        }
    }

    abstract class ReadPolicy<T> {
        List<T> readDatasFromSheet(Sheet sheet) {
            List<T> datas = new ArrayList<T>();
            for (int row = excelTemplate.getDataRowIndex(); row < sheet
                    .getRows(); row++) {
                List<DataCol> dataCols = excelTemplate.getDataCols();
                T rowData = newRowData();

                boolean isRowDataValid = true;
                RowValidateResult rowValidateResult = new RowValidateResult();
                for (int col = 0; col < dataCols.size(); col++) {
                    String value = sheet.getCell(col, row).getContents()
                            .trim();
                    DataCol dataCol = dataCols.get(col);
                    if (dataCol.hasValidator()) {
                        ColValidateResult colValidateResult = dataCol.validate(value);
                        rowValidateResult.setRowIndex(row);
                        boolean isColDataValid = colValidateResult.isSuccess();
                        isRowDataValid = isRowDataValid && isColDataValid;
                        if (!isColDataValid) {
                            rowValidateResult.addColValidateResult(colValidateResult);
                        }
                    }
                    if (isRowDataValid) {
                        String convertor = dataCol.getConvertor();
                        Object colDataVal = null;
                        if (StringUtils.isNotEmpty(convertor)) {
                            colDataVal = ConvertorUtil.convertToType(value, convertor);
                        } else {
                            colDataVal = value;
                        }
                        setColData(rowData, dataCol, colDataVal);
                    }
                }
                if (isRowDataValid) {
                    datas.add(rowData);
                } else {
                    rowValidateResults.add(rowValidateResult);
                }
            }
            return datas;
        }

        protected abstract void setColData(T rowData, DataCol dataCol, Object colDataVal);

        protected abstract T newRowData();

        void checkTemplateTitles(Sheet sheet) {
            if (sheet.getColumns() != excelTemplate.getColSize()) {
                throw new JxlExcelException(String.format(
                        "读取的excel与模板不匹配：期望%s列，实际为%s列",
                        excelTemplate.getColSize(), sheet.getColumns()));
            }
            List<TitleRow> titleRows = excelTemplate.getTitleRows();
            StringBuffer errorMsg = new StringBuffer();
            for (int row = 0; row < titleRows.size(); row++) {
                TitleRow titleRow = titleRows.get(row);
                for (int col = 0; col < titleRow.colSize(); col++) {
                    TitleCol titleCol = titleRow.getCol(col);
                    if (titleCol instanceof DummyTitleCol) {
                        continue;
                    }
                    String value = sheet.getCell(col, row).getContents().trim();
                    if (!value.equals(titleCol.getTitle())) {
                        errorMsg.append(String.format(
                                "第%s行第%s列期望[%s]，实际为[%s]\n", row + 1, col + 1,
                                titleCol.getTitle(), value));
                    }
                }
            }
            if (errorMsg.length() > 0) {
                errorMsg.deleteCharAt(errorMsg.length() - 1);
                throw new JxlExcelException("读取的excel与模板不匹配：" + errorMsg);
            }
        }
    }

}
