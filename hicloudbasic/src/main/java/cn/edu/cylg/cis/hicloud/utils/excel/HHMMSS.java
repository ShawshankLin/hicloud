/*通联支付网络服务股份有限公司版权所有,未经授权严禁查看传抄
 *
 *作者: JesseyHu
 *日期:2010-4-25 下午05:51:57
 */
/**
 * 
 */
package cn.edu.cylg.cis.hicloud.utils.excel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * JesseyHu's code
 * @author JesseyHu
 * @version 1.0
 * 通联支付网络服务股份有限公司版权所有,未经授权严禁查看传抄
 */
public class HHMMSS implements Serializable {

	private static final long serialVersionUID = -5601176142048771726L;
	
	// Fields
	private int hh;
	private int mm;
	private int ss;
	
	public HHMMSS() {
		
	}
	
	/**
	 * hh:mm:ss
	 * @param hhmmss
	 */
	public HHMMSS(String hhmmss) {
		String[] cell = hhmmss.split(":");
		this.hh = Long.valueOf(cell[0]).intValue();
		this.mm = Long.valueOf(cell[1]).intValue();
		this.ss = Long.valueOf(cell[2]).intValue();
	}
	
	public HHMMSS(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		this.hh = Calendar.HOUR_OF_DAY;
		this.mm = Calendar.MINUTE;
		this.ss = Calendar.SECOND;
	}

	public HHMMSS(int hh, int mm, int ss) {
		super();
		this.hh = hh;
		this.mm = mm;
		this.ss = ss;
	}
	public int getHh() {
		return hh;
	}
	public void setHh(int hh) {
		this.hh = hh;
	}
	public int getMm() {
		return mm;
	}
	public void setMm(int mm) {
		this.mm = mm;
	}
	public int getSs() {
		return ss;
	}
	public void setSs(int ss) {
		this.ss = ss;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.hh).append(":");
		sb.append(this.mm).append(":");
		sb.append(this.ss);
		return sb.toString();
	}

}
