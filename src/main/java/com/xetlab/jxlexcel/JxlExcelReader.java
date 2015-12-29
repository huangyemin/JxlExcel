package com.xetlab.jxlexcel;

import com.xetlab.jxlexcel.conf.DataCol;
import com.xetlab.jxlexcel.conf.DummyTitleCol;
import com.xetlab.jxlexcel.conf.TitleCol;
import com.xetlab.jxlexcel.conf.TitleRow;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JxlExcelReader extends JxlExcel {

	private InputStream is;

	public JxlExcelReader(File excelFile) throws FileNotFoundException {
		this(new FileInputStream(excelFile));
	}

	public JxlExcelReader(InputStream is) {
		this.is = is;
	}

	public <T> List<T> readBeans(final Class<T> clasz) throws JxlExcelException {
		return read(new ReadPolicy<T>() {
			@Override
			List<T> readDatasFromSheet(Sheet sheet) throws JxlExcelException {
				try {
					List<T> datas = new ArrayList<T>();
					for (int row = excelTemplate.getDataRowIndex(); row < sheet
							.getRows(); row++) {
						List<DataCol> properties = excelTemplate
								.getDataCols();
						T beanObj = clasz.newInstance();
						datas.add(beanObj);
						for (int col = 0; col < properties.size(); col++) {
							String propertyName = properties.get(col).getName();
							clasz.getDeclaredField(propertyName);
							String value = sheet.getCell(col, row)
									.getContents().trim();
							BeanUtils.setProperty(beanObj, propertyName, value);
						}
					}
					return datas;
				} catch (IllegalStateException e) {
					throw new JxlExcelException(e);
				} catch (IllegalAccessException e) {
					throw new JxlExcelException(e);
				} catch (InvocationTargetException e) {
					throw new JxlExcelException(e);
				} catch (InstantiationException e) {
					throw new JxlExcelException(e);
				} catch (NoSuchFieldException e) {
					throw new JxlExcelException(e);
				} catch (SecurityException e) {
					throw new JxlExcelException(e);
				}
			}
		});
	}

	public List<String[]> readArrays() throws JxlExcelException {
		return read(new ReadPolicy<String[]>() {
			@Override
			List<String[]> readDatasFromSheet(Sheet sheet)
					throws JxlExcelException {
				int colCnt = excelTemplate.getColSize();
				List<String[]> datas = new ArrayList<String[]>();
				for (int row = excelTemplate.getDataRowIndex(); row < sheet
						.getRows(); row++) {
					String[] rowData = new String[colCnt];
					datas.add(rowData);
					for (int col = 0; col < colCnt; col++) {
						rowData[col] = sheet.getCell(col, row).getContents()
								.trim();
					}
				}
				return datas;
			}
		});
	}

	public List<Map<String, Object>> readMaps() throws JxlExcelException {
		return read(new ReadPolicy<Map<String, Object>>() {
			@Override
			List<Map<String, Object>> readDatasFromSheet(Sheet sheet) {
				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				for (int row = excelTemplate.getDataRowIndex(); row < sheet
						.getRows(); row++) {
					List<DataCol> properties = excelTemplate.getDataCols();
					Map<String, Object> mapData = new HashMap<String, Object>();
					datas.add(mapData);
					for (int col = 0; col < properties.size(); col++) {
						String value = sheet.getCell(col, row).getContents()
								.trim();
						mapData.put(properties.get(col).getName(), value);
					}
				}
				return datas;
			}
		});
	}

	private <T> List<T> read(ReadPolicy<T> readPolicy) throws JxlExcelException {
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
		abstract List<T> readDatasFromSheet(Sheet sheet)
				throws JxlExcelException;

		void checkTemplateTitles(Sheet sheet) throws JxlExcelException {
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
