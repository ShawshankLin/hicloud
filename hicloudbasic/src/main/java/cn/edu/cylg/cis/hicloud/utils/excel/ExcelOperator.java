/*通联支付网络服务股份有限公司版权所有,未经授权严禁查看传抄
 *
 *作者: JesseyHu
 *日期:2010-3-17 下午04:32:33
 */
package cn.edu.cylg.cis.hicloud.utils.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import R.MH;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;

import com.jxcell.CellException;
import com.jxcell.CellFormat;
import com.jxcell.View;

/**
 * 操作Excel的基本方法
 * 
 * @author JesseyHu
 * @version 1.0 通联支付网络服务股份有限公司版权所有,未经授权严禁查看传抄
 */
@SuppressWarnings("all")
public class ExcelOperator {
	// 日志对象
	private static final Log log = LogFactory.getLog(ExcelOperator.class);
	/** Excel最大容量 */
	private static final int MAX_LENTH = 65536;
	/** 单元格类型 */
	public static final int GENERAL = 0;
	public static final int NUMBER = 1;
	public static final int CURRENCY_NUMBER = 2;
	public static final int DATE = 3;
	public static final int TIME = 4;
	public static final int PERCENTAGE = 5;
	public static final int FRACTION = 6;
	public static final int SCIENCE_NUMBER = 7;
	public static final int TEXT = 8;
	/** 整数标识 */
	public static final String INTEGER_SIGN = "0_ 000000 ";
	/** 列标识 */
	private static final String[] COL_KEY = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * 写入Excel 97-2003模板
	 * 
	 * @param filePath
	 * @param data
	 * @param indexSheet
	 *            : 访问的某个Sheet页
	 * @throws Exception
	 */
	public static View writeXLS(String fileName,
			List<Map<String, Object>> data, int indexSheet, String sheetName)
			throws Exception {
		log.info("写入数据到 Excel 97-2003模板: ");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View view = new View();
		// 读取指定数据源
		view.read(fileName);
		if (MAX_LENTH < data.size()) {
			throw new Exception("当前Excel容量超界!");
		}
		writeData(view, indexSheet, sheetName, data);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return view;
	}

	/**
	 * 写入Excel 97-2003模板
	 * 
	 * @param filePath
	 * @param data
	 * @param indexSheet
	 *            : 访问的某个Sheet页
	 * @throws Exception
	 */
	public static View writeXLS1(String filePath, List<Object[]> data,
			int indexSheet, String sheetName) throws Exception {
		log.info("写入数据到 Excel 97-2003模板: ");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View view = new View();
		// 读取指定数据源
		view.read(filePath);
		if (MAX_LENTH < data.size()) {
			throw new Exception("当前Excel容量超界!");
		}
		writeData1(view, indexSheet, sheetName, data);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return view;
	}

	/**
	 * 写入Excel 2007模板
	 * 
	 * @param filePath
	 * @param data
	 * @param indexSheet
	 *            : 访问的某个Sheet页
	 * @throws Exception
	 */
	public static View writeXLSX(String fileName,
			List<Map<String, Object>> data, int indexSheet, String sheetName)
			throws Exception {
		log.info("写入数据到 Excel 2007 模板: ");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View view = new View();
		// 读取指定数据源
		view.readXLSX(fileName);
		if (MAX_LENTH < data.size()) {
			throw new Exception("当前Excel容量超界!");
		}
		writeData(view, indexSheet, sheetName, data);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return view;
	}

	/**
	 * 写入Excel 2007模板
	 * 
	 * @param filePath
	 * @param data
	 * @param indexSheet
	 *            : 访问的某个Sheet页
	 * @throws Exception
	 */
	public static View writeXLSX1(String fileName, List<Object[]> data,
			int indexSheet, String sheetName) throws Exception {
		log.info("写入数据到 Excel 2007 模板: ");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View view = new View();
		// 读取指定数据源
		view.readXLSX(fileName);
		if (MAX_LENTH < data.size()) {
			throw new Exception("当前Excel容量超界!");
		}
		writeData1(view, indexSheet, sheetName, data);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return view;
	}

	/**
	 * 读取Excel 97-2003的数据
	 * 
	 * @param input
	 * @param indexSheet
	 *            : 指定Sheet页, 负数时为当前页
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readXLS(InputStream input,
			int indexSheet, int startRow, int startCol) throws Exception {
		log.info("开始读取Excel 97-2003......");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View xls = new View();
		// 读取指定数据源
		xls.read(input);
		// 返回数据
		List<Map<String, Object>> data = readData(xls, indexSheet, startRow,
				startCol);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return data;
	}

	/**
	 * 读取Excel 97-2003的数据
	 * 
	 * @param input
	 * @param indexSheet
	 *            : 指定Sheet页, 负数时为当前页
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readXLS(InputStream input,
			String sheetName, int startRow, int startCol) throws Exception {
		log.info("开始读取Excel 97-2003......");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View xls = new View();
		// 读取指定数据源
		xls.read(input);
		// 返回数据
		List<Map<String, Object>> data = readData(xls, sheetName, startRow,
				startCol);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return data;
	}

	/**
	 * 读取Excel 97-2003的数据
	 * 
	 * @param input
	 * @param sheetName 指定Sheet名称
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readXLS(InputStream input,
			String sheetName, int startRow, int startCol, boolean isCloseIO)
			throws Exception {
		List<Map<String, Object>> data = readXLS(input, sheetName, startRow, startCol);
		if (isCloseIO) {
			input.close();
		}
		return data;
	}
	
	/**
	 * 读取Excel 97-2003的数据
	 * 
	 * @param input
	 * @param sheetName 指定Sheet名称
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readXLSX(InputStream input,
			String sheetName, int startRow, int startCol, boolean isCloseIO)
			throws Exception {
		List<Map<String, Object>> data = readXLSX(input, sheetName, startRow, startCol);
		if (isCloseIO) {
			input.close();
		}
		return data;
	}
	
	/**
	 * 读取Excel 97-2003的数据
	 * 
	 * @param input
	 * @param sheetName 指定Sheet名称
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readXLSX(InputStream input,
			String sheetName, int startRow, int startCol) throws Exception {
		log.info("开始读取Excel 97-2003......");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View xls = new View();
		// 读取指定数据源
		xls.readXLSX(input);
		// 返回数据
		List<Map<String, Object>> data = readData(xls, sheetName, startRow,
				startCol);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return data;
	}


	/**
	 * 读取Excel 2007的数据
	 * 
	 * @param input
	 * @param indexSheet
	 *            : 访问的某个Sheet页
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readXLSX(InputStream input,
			int indexSheet, int startRow, int startCol) throws Exception {
		log.info("开始读取Excel 2007......");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View view = new View();
		// 读取指定数据源
		view.readXLSX(input);
		// 返回数据
		List<Map<String, Object>> data = readData(view, indexSheet, startRow,
				startCol);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		return data;
	}
	
	

	/**
	 * 读取数据
	 * 
	 * @param view
	 * @param data
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readData(View view, int indexSheet,
			int startRow, int startCol) throws Exception {
		if (indexSheet < 0) {
			indexSheet = view.getSheet();
		}
		view.setSheet(indexSheet);
		// 获取某个sheet工作薄
		MH sheet = view.getBook().getSheet(indexSheet);
		int rowCount = sheet.getRowCount();
		int colCount = sheet.getLastCol() + 1;
		log.info("正在读取第" + sheet.getSheetNumber() + "个工作薄名称: "
				+ sheet.getName() + "; 行数: " + rowCount + "; 列数: " + colCount);
		if (MAX_LENTH < rowCount) {
			throw new Exception("当前Excel容量超界!");
		}
		// 列数26后用数组放回List<Object[]>
		if (COL_KEY.length < colCount) {
			throw new Exception("当前Excel列数超过26列!");
		}
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		// 起始行越界
		if (startRow > rowCount || startCol > colCount) {
			log.info("指定起始行或列有误!");
			return data;
		}
		// 单元格类型
		List<String[]> cellList = new ArrayList<String[]>();
		for (int i = 0; i < colCount; i++) {
			Map<Integer, String> cellMap = new HashMap<Integer, String>();
			CellFormat format = view.getCellFormat(1, i, 1, i);
			String formatType = String.valueOf(format.getCustomFormatType());
			String formatName = format.getCustomFormat();
			log.info("单元格类型: " + formatType + "--" + formatName);
			cellList.add(i, new String[] { formatType, formatName });
		}
		for (int r = startRow; r < rowCount; r++) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int c = startCol; c < colCount; c++) {
				Object value = getCellValue(view, r, c, cellList.get(c));
				map.put(COL_KEY[c], value);
			}
			if (map.isEmpty() || map.values().isEmpty()) {
				continue;
			}
			data.add(map);
		}
		log.info("读取数据条数: " + data.size());
		return data;
	}

	/**
	 * 读取数据
	 * 
	 * @param view
	 * @param sheetName
	 * @param data
	 * @throws Exception
	 */
	public static List<Map<String, Object>> readData(View view,
			String sheetName, int startRow, int startCol) throws Exception {
		// 获取某个sheet工作薄
		MH sheet = view.getBook().findSheet(sheetName);
		int rowCount = sheet.getRowCount();
		int colCount = sheet.getLastCol() + 1;
		log.info("正在读取第" + sheet.getSheetNumber() + "个工作薄名称: "
				+ sheet.getName() + "; 行数: " + rowCount + "; 列数: " + colCount);
		if (MAX_LENTH < rowCount) {
			throw new Exception("当前Excel容量超界!");
		}
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		// 起始行越界
		if (startRow > rowCount || startCol > colCount) {
			log.info("指定起始行或列有误!");
			return data;
		}
		// 单元格类型
		List<String[]> cellList = new ArrayList<String[]>();
		for (int i = 0; i < colCount; i++) {
			Map<Integer, String> cellMap = new HashMap<Integer, String>();
			CellFormat format = view.getCellFormat(1, i, 1, i);
			String formatType = String.valueOf(format.getCustomFormatType());
			String formatName = format.getCustomFormat();
			log.info("单元格类型: " + formatType + "--" + formatName);
			cellList.add(i, new String[] { formatType, formatName });
		}
		String[] col_keys = COL_KEY;
		if (colCount > COL_KEY.length) {
			List<String> colNames = new ArrayList<String>(colCount);
			colNames.addAll(Arrays.asList(COL_KEY));
			for (String s1 : COL_KEY) {
				for (String s2 : COL_KEY) {
					if (colNames.size() < colCount) {
						colNames.add(s1 + s2);
					}
				}
			}
			String[] new_col_keys = new String[colCount];
			for (int i = 0; i < colNames.size(); i++) {
				new_col_keys[i] = colNames.get(i);
			}
			col_keys = new_col_keys;
		}
		for (int r = startRow; r < rowCount; r++) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int c = startCol; c < colCount; c++) {
				Object value = getCellValue(sheet, r, c, cellList.get(c));
				map.put(col_keys[c], value);
			}
			if (map.isEmpty() || map.values().isEmpty()) {
				continue;
			}
			data.add(map);
		}
		log.info("读取数据条数: " + data.size());
		return data;
	}

	/**
	 * 读取Excel97-2003的数据: 所有返回值为String类型，单元格的值
	 * 
	 * @param input
	 * @param indexSheet
	 *            : 指定Sheet页, 负数时为当前页
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> readXLS(InputStream input, int indexSheet,
			int colLen) throws Exception {
		return readXLS(input, indexSheet, colLen, true);
	}

	public static List<Object[]> readXLS(InputStream input, int indexSheet,
			int colLen, boolean closeio) throws Exception {
		log.info("开始读取---Excel 97-2003......");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View xls = new View();
		// 读取指定数据源
		xls.read(input);
		// 返回数据
		List<Object[]> data = readArrayData(xls, indexSheet, colLen);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		if (closeio) {
			input.close();
		}
		xls.destroy();
		return data;
	}

	/**
	 * 读取Excel2007的数据: 所有返回值为String类型，单元格的值
	 * 
	 * @param input
	 * @param indexSheet
	 *            : 访问的某个Sheet页
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> readXLSX(InputStream input, int indexSheet,
			int colLen) throws Exception {
		log.info("开始读取---Excel 2007......");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View xls = new View();
		// 读取指定数据源
		xls.readXLSX(input);
		// 返回数据
		List<Object[]> data = readArrayData(xls, indexSheet, colLen);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		input.close();
		xls.destroy();
		return data;
	}

	public static List<Object[]> readXLSX(InputStream input, int indexSheet,
			int colLen, boolean isclose) throws Exception {
		log.info("开始读取---Excel 2007......");
		long start = Calendar.getInstance().getTimeInMillis();
		// 试图对象
		View xls = new View();
		// 读取指定数据源
		xls.readXLSX(input);
		// 返回数据
		List<Object[]> data = readArrayData(xls, indexSheet, colLen);
		long end = Calendar.getInstance().getTimeInMillis();
		log.info("读取Excel花费时间: " + (end - start));
		if (isclose) {
			input.close();
		}
		xls.destroy();
		return data;
	}

	/**
	 * 读取数据
	 * 
	 * @param view
	 * @param data
	 * @throws Exception
	 */
	public static List<Object[]> readArrayData(View view, int indexSheet,
			int colLen) throws Exception {
		if (indexSheet < 0) {
			indexSheet = view.getSheet();
		}
		List<Object[]> data = new ArrayList<Object[]>();
		// 获取某个sheet工作薄
		view.setSheet(indexSheet);
		MH sheet = view.getBook().getSheet(indexSheet);
		int rowCount = sheet.getRowCount();
		if (rowCount < 1) {
			log.info("上传Excel无数据");
			return data;
		}
		int colCount = sheet.getLastCol() + 1;
		log.info("正在读取第" + sheet.getSheetNumber() + "个工作薄名称: "
				+ sheet.getName() + "; 行数: " + rowCount + "; 列数: " + colCount);
		if (MAX_LENTH < rowCount) {
			throw new Exception("当前Excel容量超界!");
		}
		colCount = colLen;
		for (int r = 0; r < rowCount; r++) {
			Object[] info = new Object[colCount];
			for (int c = 0; c < colCount; c++) {
				String value = view.getText(r, c);
				info[c] = value;
			}
			if (FuncUtil.isEmpty(info)) {
				continue;
			}
			data.add(info);
		}
		log.info("读取数据条数: " + data.size());
		return data;
	}

	/**
	 * 读取数据
	 * 
	 * @param view
	 * @param data
	 * @throws Exception
	 */
	public static void writeData(View view, int indexSheet, String sheetName,
			List<Map<String, Object>> data) throws Exception {
		if (indexSheet < 0) {
			indexSheet = view.getSheet();
		}
		// 获取某个sheet工作薄
		MH sheet = view.getBook().getSheet(indexSheet);
		// 设置Sheet页名称
		if (!FuncUtil.isEmpty(sheetName)) {
			sheet.setName(sheetName);
		}
		int rowCount = data.size();
		int colCount = sheet.getLastCol() + 1;
		log.info("正在写入第" + sheet.getSheetNumber() + "个工作薄名称: "
				+ sheet.getName() + "; 行数: " + rowCount + "; 列数: " + colCount);
		// 单元格类型
		List<String[]> cellList = new ArrayList<String[]>();
		for (int i = 0; i < colCount; i++) {
			Map<Integer, String> cellMap = new HashMap<Integer, String>();
			CellFormat format = view.getCellFormat(1, i, 1, i);
			String formatType = String.valueOf(format.getCustomFormatType());
			String formatName = format.getCustomFormat();
			log.info("单元格类型: " + formatType + "--" + formatName);
			cellList.add(i, new String[] { formatType, formatName });
		}
		for (int r = 0; r < rowCount; r++) {
			for (int c = 0; c < colCount; c++) {
				Object value = data.get(r).get(COL_KEY[c]);
				if (null == value || "null".equals(value)) {
					continue;
				}
				setCellValue(sheet, r + 1, c, data.get(r).get(COL_KEY[c]),
						cellList);
			}
		}
	}

	/**
	 * 读取数据
	 * 
	 * @param view
	 * @param data
	 * @throws Exception
	 */
	public static void writeData1(View view, int indexSheet, String sheetName,
			List<Object[]> data) throws Exception {
		if (indexSheet < 0) {
			indexSheet = view.getSheet();
		}
		// 获取某个sheet工作薄
		MH sheet = view.getBook().getSheet(indexSheet);
		// 设置Sheet页名称
		if (!FuncUtil.isEmpty(sheetName)) {
			sheet.setName(sheetName);
		}
		int rowCount = data.size();
		int colCount = sheet.getLastCol() + 1;
		log.info("正在写入第" + sheet.getSheetNumber() + "个工作薄名称: "
				+ sheet.getName() + "; 行数: " + rowCount + "; 列数: " + colCount);
		// 单元格类型
		List<String[]> cellList = new ArrayList<String[]>();
		for (int i = 0; i < colCount; i++) {
			Map<Integer, String> cellMap = new HashMap<Integer, String>();
			CellFormat format = view.getCellFormat(1, i, 1, i);
			String formatType = String.valueOf(format.getCustomFormatType());
			String formatName = format.getCustomFormat();
			log.info("单元格类型: " + formatType + "--" + formatName);
			cellList.add(i, new String[] { formatType, formatName });
		}
		for (int r = 0; r < rowCount; r++) {
			Object[] d = data.get(r);
			if (d == null)
				continue;
			for (int c = 0; c < d.length; c++) {
				Object value = d[c];
				if (value == null || "null".equals(value) || "".equals(value)) {
					continue;
				}
				setCellValue(sheet, r, c, value, cellList);
			}
		}
	}

	/**
	 * 
	 * @param view
	 * @param rowIndex
	 * @param cellType
	 * @return
	 * @throws Exception
	 */
	public static Object getCellValue(View view, int rowIndex, int colIndex,
			String[] cellFormat) throws Exception {
		// 默认日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Object value = null;
		int type = Long.valueOf(cellFormat[0]).intValue();// [type, name]
		String formatName = cellFormat[1].toString();
		switch (type) {
		case GENERAL:
			value = view.getText(rowIndex, colIndex);
			break;
		case NUMBER:
			if (INTEGER_SIGN.indexOf(formatName) != -1) {
				value = Double.valueOf(view.getNumber(rowIndex, colIndex))
						.longValue();
			} else {
				value = view.getNumber(rowIndex, colIndex);
			}
			break;
		case CURRENCY_NUMBER:
			value = view.getNumber(rowIndex, colIndex);
			break;
		case DATE:
			try {
				value = sdf.parse(view.getFormattedText(rowIndex, colIndex));
			} catch (Exception e) {
				sdf = new SimpleDateFormat(formatName);
				value = sdf.parse(view.getFormattedText(rowIndex, colIndex));
			}
			break;
		case TIME:
			value = new HHMMSS(view.getFormattedText(rowIndex, colIndex));
			break;
		case PERCENTAGE:
			value = view.getNumber(rowIndex, colIndex);
			break;
		case FRACTION:
			value = view.getNumber(rowIndex, colIndex);
			break;
		case SCIENCE_NUMBER:
			value = view.getNumber(rowIndex, colIndex);
			break;
		case TEXT:
			value = view.getText(rowIndex, colIndex);
			break;
		default:
			value = view.getText(rowIndex, colIndex);
		}
		return value;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @param cellType
	 * @return
	 * @throws Exception
	 */
	public static Object getCellValue(MH sheet, int rowIndex, int colIndex,
			String[] cellFormat) throws Exception {
		// 默认日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Object value = null;
		int type = Long.valueOf(cellFormat[0]).intValue();// [type, name]
		String formatName = cellFormat[1].toString();
		switch (type) {
		case GENERAL:
			value = sheet.getText(rowIndex, colIndex);
			break;
		case NUMBER:
			if (INTEGER_SIGN.indexOf(formatName) != -1) {
				value = Double.valueOf(sheet.getNumber(rowIndex, colIndex))
						.longValue();
			} else {
				value = sheet.getNumber(rowIndex, colIndex);
			}
			break;
		case CURRENCY_NUMBER:
			value = sheet.getNumber(rowIndex, colIndex);
			break;
		case DATE:
			try {
				value = sdf.parse(sheet.getFormattedText(rowIndex, colIndex));
			} catch (Exception e) {
				sdf = new SimpleDateFormat(formatName);
				value = sdf.parse(sheet.getFormattedText(rowIndex, colIndex));
			}
			break;
		case TIME:
			value = new HHMMSS(sheet.getFormattedText(rowIndex, colIndex));
			break;
		case PERCENTAGE:
			value = sheet.getNumber(rowIndex, colIndex);
			break;
		case FRACTION:
			value = sheet.getNumber(rowIndex, colIndex);
			break;
		case SCIENCE_NUMBER:
			value = sheet.getNumber(rowIndex, colIndex);
			break;
		case TEXT:
			value = sheet.getText(rowIndex, colIndex);
			break;
		default:
			value = sheet.getText(rowIndex, colIndex);
		}
		return value;
	}

	/**
	 * 
	 * @param view
	 * @param rowIndex
	 * @param cellType
	 * @return
	 * @throws Exception
	 */
	public static void setCellValue(MH sheet, int rowIndex, int colIndex,
			Object value, List<String[]> cellList) throws Exception {
		// 默认日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		int type = Long.valueOf(cellList.get(colIndex)[0]).intValue();// [type,
																		// name]
		String formatName = cellList.get(colIndex)[1].toString();
		switch (type) {
		case GENERAL:
			sheet.setText(rowIndex, colIndex, value.toString());
			break;
		case NUMBER:
			if (INTEGER_SIGN.indexOf(formatName) != -1) {
				sheet.setNumber(rowIndex, colIndex,
						Long.valueOf(value.toString()).intValue());
			} else {
				sheet.setNumber(rowIndex, colIndex,
						Double.valueOf(value.toString()).doubleValue());
			}
			break;
		case CURRENCY_NUMBER:
			sheet.setNumber(rowIndex, colIndex, Double
					.valueOf(value.toString()).doubleValue());
			break;
		case DATE:
			sdf = new SimpleDateFormat(formatName);
			sheet.setText(rowIndex, colIndex, sdf.format(value));
			break;
		case TIME:
			sheet.setText(rowIndex, colIndex, ((HHMMSS) value).toString());// 自己写个类放置time
			break;
		case PERCENTAGE:
			sheet.setNumber(rowIndex, colIndex, Double
					.valueOf(value.toString()).doubleValue());
			break;
		case FRACTION:
			sheet.setNumber(rowIndex, colIndex, Double
					.valueOf(value.toString()).doubleValue());
			break;
		case SCIENCE_NUMBER:
			sheet.setNumber(rowIndex, colIndex, Double
					.valueOf(value.toString()).doubleValue());
			break;
		case TEXT:
			sheet.setText(rowIndex, colIndex, value.toString());
			break;
		default:
			sheet.setText(rowIndex, colIndex, value.toString());
		}
	}

}
