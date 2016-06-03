package cn.edu.cylg.cis.hicloud.utils.excel;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


public class CellData {
	
	private static final Log log = LogFactory.getLog(CellData.class);
	
	/** 单元格格式 - 表头格式-加粗 */
	public static final int cellstyle_headrow = 0;
	
	/** 单元格格式 - 数字格式-#,##0 */
	public static final int cellstyle_num = 1;
	
	/** 单元格格式 - 金额格式-#0.00 */
	public static final int cellstyle_number = 2;
	
	/** 单元格格式 - 货币格式-#,###0.00 */
	public static final int cellstyle_currency = 3;
	
	/** 单元格格式 - 时间格式-yyyy-MM-dd HH:mm:ss */
	public static final int cellstyle_datetime = 4;
	
	/** 单元格格式 - 时间格式-yyyy-MM-dd HH:mm */
	public static final int cellstyle_datetime1 = 5;
	
	/** 单元格格式 - 时间格式-yyyy-MM-dd */
	public static final int cellstyle_date = 6;
	
	/** 单元格格式 - 时间格式-yyyy-MM */
	public static final int cellstyle_month = 7;
	
	/** 单元格格式 - 百分比-0.00% */
	public static final int cellstyle_bfb = 8;
	
	
	private Object value;
	private int style = -1;
	private short cellborder = 0;
	private int colnum = 1;//横向单元格数
	private boolean center;
	
	public CellData() {
	}
	
	public CellData(Object value) {
		this.value = value;
	}
	
	public CellData(Object value, short cellborder) {
		this.value = value;
		this.cellborder = cellborder;
	}
	
	public CellData(Object value, int style) {
		this.value = value;
		this.style = style;
	}
	
	public CellData(Object value, int style,boolean center) {
		this.value = value;
		this.style = style;
		this.center = center;
	}
	
	public CellData(Object value, int style, short cellborder) {
		this.value = value;
		this.style = style;
		this.cellborder = cellborder;
	}
	
	public CellData(Object value, int style, short cellborder,boolean center) {
		this.value = value;
		this.style = style;
		this.cellborder = cellborder;
		this.center = center;
	}
	
	public CellData(Object value, int style, short cellborder,int colnum) {
		this.value = value;
		this.style = style;
		this.cellborder = cellborder;
		this.colnum = colnum;
	}
	
	public CellData(Object value, int style, short cellborder,int colnum,boolean center) {
		this.value = value;
		this.style = style;
		this.cellborder = cellborder;
		this.colnum = colnum;
		this.center = center;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	public short getCellborder() {
		return cellborder;
	}
	public void setCellborder(short cellborder) {
		this.cellborder = cellborder;
	}
	public int getColnum() {
		return colnum;
	}
	public void setColnum(int colnum) {
		this.colnum = colnum;
	}
	public boolean isCenter() {
		return center;
	}
	public void setCenter(boolean center) {
		this.center = center;
	}

	public HSSFCellStyle createCellStyle(HSSFWorkbook workBook){
		if(this.style==-1){
			if(cellborder <=0){
				return null;
			}else{
				HSSFCellStyle style = workBook.createCellStyle();
				setBorder(style);
				return style;
			}
		}
		try{
			Method method = this.getClass().getMethod("style"+this.style, HSSFWorkbook.class);
			HSSFCellStyle style = (HSSFCellStyle)method.invoke(this, workBook);
			setBorder(style);
			if(center){
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			}
			return style;
		}catch(Throwable e){
			log.warn("create style:"+this.style+" error.", e);
			return null;
		}
	}
	
	public HSSFCellStyle style0(HSSFWorkbook workBook){
		HSSFFont hFont = workBook.createFont();
		hFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle styleHead = workBook.createCellStyle();
		styleHead.setFont(hFont);
		styleHead.setFillForegroundColor(HSSFColor.LIME.index);
		styleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHead.setBorderBottom((short)1);
		styleHead.setBorderRight((short)1);
		return styleHead;
	}
	
	public HSSFCellStyle style1(HSSFWorkbook workBook){
		HSSFCellStyle styleNum = workBook.createCellStyle();
		styleNum.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		return styleNum;
	}
	
	public HSSFCellStyle style2(HSSFWorkbook workBook){
		HSSFCellStyle styleNum = workBook.createCellStyle();
		styleNum.setDataFormat(HSSFDataFormat.getBuiltinFormat("#0.00"));
		return styleNum;
	}
	
	public HSSFCellStyle style3(HSSFWorkbook workBook){
		HSSFCellStyle styleNum = workBook.createCellStyle();
		styleNum.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		return styleNum;
	}
	
	public HSSFCellStyle style4(HSSFWorkbook workBook){
		HSSFCellStyle dateStyle = workBook.createCellStyle();
        dateStyle.setDataFormat(workBook.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        return dateStyle;
	}
	
	public HSSFCellStyle style5(HSSFWorkbook workBook){
		HSSFCellStyle dateStyle = workBook.createCellStyle();
        dateStyle.setDataFormat(workBook.createDataFormat().getFormat("yyyy-MM-dd HH:mm"));
        return dateStyle;
	}
	
	public HSSFCellStyle style6(HSSFWorkbook workBook){
		HSSFCellStyle dateStyle = workBook.createCellStyle();
        dateStyle.setDataFormat(workBook.createDataFormat().getFormat("yyyy-MM-dd"));
        return dateStyle;
	}
	
	public HSSFCellStyle style7(HSSFWorkbook workBook){
		HSSFCellStyle dateStyle = workBook.createCellStyle();
        dateStyle.setDataFormat(workBook.createDataFormat().getFormat("yyyy-MM"));
        return dateStyle;
	}
	
	public HSSFCellStyle style8(HSSFWorkbook workBook){
		 HSSFCellStyle bfbStyle_total = workBook.createCellStyle();
	     bfbStyle_total.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
	     return bfbStyle_total;
	}
	
	private void setBorder(HSSFCellStyle style){
		if(style == null){
			return;
		}
		if(cellborder <=0){
			return;
		}
		style.setBorderBottom(cellborder);
		style.setBorderRight(cellborder);
	}
}
