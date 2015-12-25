package com.xetlab.jxlexcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.xetlab.jxlexcel.conf.Property;
import com.xetlab.jxlexcel.conf.TitleCol;
import com.xetlab.jxlexcel.conf.TitleRow;

public class JxlExcelWriter extends JxlExcel {

	private OutputStream os;

	public JxlExcelWriter(File outFile) throws FileNotFoundException {
		this(new FileOutputStream(outFile));
	}

	public JxlExcelWriter(OutputStream os) {
		this.os = os;
	}

	public void writeTemplate() throws JxlExcelException {
		checkTemplate();
		WritableWorkbook wb = null;
		try {
			wb = Workbook.createWorkbook(os);
			WritableSheet sheet = wb.createSheet("sheet", 0);
			writeTitle2Sheet(excelTemplate.getTitleRows(), sheet);
			autoResizeColumns(sheet);
			wb.write();
		} catch (IOException e) {
			throw new JxlExcelException(e);
		} catch (RowsExceededException e) {
			throw new JxlExcelException(e);
		} catch (WriteException e) {
			throw new JxlExcelException(e);
		} finally {
			close(wb);
		}

	}

	private void close(WritableWorkbook wb) throws JxlExcelException {
		if (wb != null) {
			try {
				wb.close();
			} catch (WriteException e) {
				throw new JxlExcelException(e);
			} catch (IOException e) {
				throw new JxlExcelException(e);
			}
		}
	}

	public void writeArrays(List<Object[]> datas) throws JxlExcelException {
		write(new WritePolicy<Object[]>(datas) {
			@Override
			List<String[]> copyOfRange(int from, int to)
					throws JxlExcelException {
				List<Object[]> subList = inputDatas.subList(from, to);
				List<String[]> copys = new ArrayList<String[]>();
				for (Object[] item : subList) {
					String[] rowData = new String[item.length];
					copys.add(rowData);
					for (int j = 0; j < item.length; j++) {
						rowData[j] = ObjectUtils.toString(item[j]);
					}
				}
				return copys;
			}

		});
	}

	public void writeMaps(final List<Map<String, Object>> datas)
			throws JxlExcelException {

		write(new WritePolicy<Map<String, Object>>(datas) {
			@Override
			List<String[]> copyOfRange(int from, int to)
					throws JxlExcelException {
				List<Map<String, Object>> subList = inputDatas
						.subList(from, to);
				List<String[]> copys = new ArrayList<String[]>();
				List<Property> properties = excelTemplate.getProperties();
				for (Map<String, Object> dataMap : subList) {
					String[] rowData = new String[properties.size()];
					copys.add(rowData);
					for (int j = 0; j < properties.size(); j++) {
						Property property = properties.get(j);
						rowData[j] = ObjectUtils.toString(dataMap.get(property
								.getName()));
					}
				}
				return copys;
			}

		});

	}

	public <T> void writeBeans(final List<T> datas) throws JxlExcelException {
		write(new WritePolicy<T>(datas) {
			@Override
			List<String[]> copyOfRange(int from, int to)
					throws JxlExcelException {
				List<T> subList = inputDatas.subList(from, to);
				List<String[]> copys = new ArrayList<String[]>();
				List<Property> properties = excelTemplate.getProperties();
				for (T dataObj : subList) {
					String[] rowData = new String[properties.size()];
					copys.add(rowData);
					for (int j = 0; j < properties.size(); j++) {
						Property property = properties.get(j);
						try {
							rowData[j] = BeanUtils.getSimpleProperty(dataObj,
									property.getName());
						} catch (IllegalAccessException e) {
							throw new JxlExcelException(e);
						} catch (InvocationTargetException e) {
							throw new JxlExcelException(e);
						} catch (NoSuchMethodException e) {
							throw new JxlExcelException(e);
						}
					}
				}
				return copys;
			}
		});
	}

	private <T> void write(WritePolicy<T> writePolicy) throws JxlExcelException {
		WritableWorkbook wb = null;
		try {
			wb = Workbook.createWorkbook(os);
			int dataLen = writePolicy.getDataLength();
			int lastSheetRowCnt = dataLen % MAX_ROW_PER_SHEET;
			int sheetCnt = dataLen / MAX_ROW_PER_SHEET
					+ (lastSheetRowCnt > 0 ? 1 : 0);
			if (sheetCnt == 0) {
				sheetCnt = 1;
			}
			for (int i = 0; i < sheetCnt; i++) {
				String sheetName = "sheet" + (i + 1);
				WritableSheet sheet = wb.createSheet(sheetName, i);
				int from = i * MAX_ROW_PER_SHEET;
				int to = (i + 1) * MAX_ROW_PER_SHEET;
				if (i == sheetCnt - 1) {
					to = i * MAX_ROW_PER_SHEET + lastSheetRowCnt;
				}
				writeTitle2Sheet(excelTemplate.getTitleRows(), sheet);
				List<String[]> splitedDatas = writePolicy.copyOfRange(from, to);
				writeData2Sheet(splitedDatas, sheet,
						excelTemplate.getDataRowIndex());
				autoResizeColumns(sheet);
			}
			wb.write();
		} catch (IOException e) {
			throw new JxlExcelException(e);
		} catch (RowsExceededException e) {
			throw new JxlExcelException(e);
		} catch (WriteException e) {
			throw new JxlExcelException(e);
		} finally {
			close(wb);
		}
	}

	abstract class WritePolicy<T> {

		protected List<T> inputDatas;

		public WritePolicy(List<T> inputDatas) {
			this.inputDatas = inputDatas;
		}

		abstract List<String[]> copyOfRange(int from, int to)
				throws JxlExcelException;

		int getDataLength() {
			return inputDatas.size();
		}

	}

	private void autoResizeColumns(WritableSheet sheet) {
		for (int i = 0; i < sheet.getColumns(); i++) {
			Cell[] cells = sheet.getColumn(i);
			int longestStrLen = -1;

			if (cells.length == 0)
				continue;

			/* Find the widest cell in the column. */
			for (int j = 0; j < cells.length; j++) {
				String content = cells[j].getContents().trim();
				int length = content.length() + 2 + getChineseCnt(content);
				if (length > longestStrLen) {
					longestStrLen = length;
				}
			}

			/* If not found, skip the column. */
			if (longestStrLen == -1)
				continue;

			/* If wider than the max width, crop width */
			if (longestStrLen > 255)
				longestStrLen = 255;
			/*
			 * resize it.
			 */
			sheet.setColumnView(i, longestStrLen);
		}
	}

	private int getChineseCnt(String input) {
		int lenOfChinese = 0;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]"); // 汉字的unicode编码范围
		Matcher m = p.matcher(input);
		while (m.find()) {
			lenOfChinese++;
		}
		return lenOfChinese;
	}

	private void writeData2Sheet(List<String[]> datas, WritableSheet sheet,
			int dataRow) throws RowsExceededException, WriteException {
		if (datas.size() == 0) {
			return;
		}
		WritableCellFormat leftNormalFormat = createLeftNormalCellFormat();
		int colSize = datas.get(0).length;
		for (int row = 0; row < datas.size(); row++) {
			for (int col = 0; col < colSize; col++) {
				sheet.addCell(new Label(col, row + dataRow,
						datas.get(row)[col], leftNormalFormat));
			}
		}
	}

	/**
	 * 写标题
	 * 
	 * @param titles
	 * @param sheet
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private void writeTitle2Sheet(List<TitleRow> titleRows, WritableSheet sheet)
			throws WriteException, RowsExceededException {
		WritableCellFormat centerBoldFormat = createCenterBoldCellFormat();
		for (int row = 0; row < titleRows.size(); row++) {
			TitleRow titleRow = titleRows.get(row);
			for (int col = 0; col < titleRow.colSize(); col++) {
				TitleCol titleCol = titleRow.getCol(col);
				sheet.addCell(new Label(col, row, titleCol.getTitle(),
						centerBoldFormat));
			}
			if (titleRow.hasSpanCol()) {
				for (int col = 0; col < titleRow.colSize(); col++) {
					TitleCol titleCol = titleRow.getCol(col);
					sheet.mergeCells(col, row, col + titleCol.getColSpan() - 1,
							row);
				}
			}
		}
	}

	/**
	 * 正文格式
	 * 
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat createLeftNormalCellFormat()
			throws WriteException {
		WritableCellFormat cellFormat = new WritableCellFormat(
				createNormalFont());
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 设置边框
		cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
		cellFormat.setAlignment(Alignment.LEFT); // 向左对齐
		cellFormat.setWrap(false); // 自动换行
		return cellFormat;
	}

	/**
	 * 标题格式
	 * 
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat createCenterBoldCellFormat()
			throws WriteException {
		WritableCellFormat cellFormat = new WritableCellFormat(createBoldFont());
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 设置边框
		cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
		cellFormat.setAlignment(Alignment.CENTRE); // 居中对齐
		cellFormat.setWrap(false); // 自动换行
		return cellFormat;
	}

	/**
	 * 粗体
	 * 
	 * @return
	 */
	private WritableFont createBoldFont() {
		WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 11,
				WritableFont.BOLD);
		return boldFont;
	}

	/**
	 * 正常格式
	 * 
	 * @return
	 */
	private WritableFont createNormalFont() {
		WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10);
		return normalFont;
	}

}
