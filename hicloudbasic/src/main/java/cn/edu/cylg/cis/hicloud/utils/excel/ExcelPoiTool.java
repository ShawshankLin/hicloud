package cn.edu.cylg.cis.hicloud.utils.excel;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

@SuppressWarnings("all")
public class ExcelPoiTool {
	
	private static final Log log = LogFactory.getLog(ExcelPoiTool.class);
	
	private static short DEFAULT_ENCODING = HSSFWorkbook.ENCODING_UTF_16;
	
	private static short DEFAULT_COLUMN_WIDTH = 20;

	public static byte[] createExcel(HSSFWorkbook workBook,int sheetIndex,String sheetname,List<List<Object>> datas){
		if(workBook == null){
			workBook = new HSSFWorkbook();
		}
		if(sheetIndex<0){
			sheetIndex = 0;
		}
		HSSFSheet sheet =null;
		try{
			sheet = workBook.getSheetAt(sheetIndex);
		}catch(Exception e){
		}
		if(sheet ==null){
			sheet = workBook.createSheet();
		}
		if(sheetname!=null){
			workBook.setSheetName(sheetIndex, sheetname, DEFAULT_ENCODING);
		}
		sheet.setDefaultColumnWidth(DEFAULT_COLUMN_WIDTH);
		int index = 0;
		Map<Integer,HSSFCellStyle> styleMap = new HashMap<Integer,HSSFCellStyle>();
		for(List<Object> celldatas: datas){
			int idx = index++;
			if(celldatas == null){
				continue;
			}
			HSSFRow row = sheet.createRow(idx);
			short column = 0;
			for(Object celldata :celldatas){
				short cl = column++;
				if(celldata==null){
					continue;
				}
				HSSFCell cell = row.createCell(cl);
				cell.setEncoding(DEFAULT_ENCODING);
				if(celldata instanceof CellData){
					CellData cd = (CellData)celldata;
					setCellValue(cell,cd.getValue());
					HSSFCellStyle style = null;
					if(styleMap.containsKey(cd.getStyle())){
						style = styleMap.get(cd.getStyle());
					}else{
						style = cd.createCellStyle(workBook);
						styleMap.put(cd.getStyle(), style);
					}
					if(style !=null){
						cell.setCellStyle(style);
					}
					if(cd.getColnum()>1){
						for(int k=cd.getColnum();k>1;k--){
							HSSFCell othercell = row.createCell(column++);
							if(style !=null){
								othercell.setCellStyle(style);
							}
						}
						sheet.addMergedRegion(new Region(idx,cl,idx,(short)(cl+cd.getColnum()-1)));
					}
				}else{
					setCellValue(cell,celldata);
				}
			}
		}
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		try{
			workBook.write(byteos);
			byte[] bytes = byteos.toByteArray();
			byteos.close();
			return bytes;
		}catch(Exception e){
			log.error("io操作失败", e);
			return null;
		}
	}
	
	/**
	 * 多sheet写入
	 * @param workBook
	 * @param sheetIndexes sheet序号
	 * @param sheetnames sheet名称
	 * @param datas sheet数据
	 * @return
	 */
	public static byte[] createExcel(HSSFWorkbook workBook,int[] sheetIndexes,String[] sheetnames,List<List<Object>>... datas){
		if(workBook == null){
			workBook = new HSSFWorkbook();
		}
		if(sheetIndexes == null || sheetIndexes.length == 0){
			sheetIndexes = new int[]{0};
		}
		for (int i = 0; i < sheetIndexes.length; i++) {
			createExcel(workBook, sheetIndexes[i], sheetnames[i], datas[i]);
		}	
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		try{
			workBook.write(byteos);
			byte[] bytes = byteos.toByteArray();
			byteos.close();
			return bytes;
		}catch(Exception e){
			log.error("io操作失败", e);
			return null;
		}
	}
	
	
	private static void setCellValue(HSSFCell cell,Object value){
		if(value == null){
			return;
		}
		if(value instanceof String){
			cell.setCellValue((String)value);
		}
		else if(value instanceof BigDecimal){
			cell.setCellValue(((BigDecimal)value).doubleValue());
		}
		else if(value instanceof Integer){
			cell.setCellValue(new Double(value.toString()));
		}
		else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}
		else if(value instanceof Boolean){
			cell.setCellValue((Boolean)value);
		}
		else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}
		else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}
		else if(value instanceof Long){
			cell.setCellValue(new Double(value.toString()));
		}
		else{
			cell.setCellValue(value.toString());
		}
	}
	
}
