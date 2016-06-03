package cn.edu.cylg.cis.hicloud.core.sqlbuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParam {

	private String sql;
	private Object[] param;
	
	//是否查历史库
	private boolean his = false;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Object[] getParam() {
		return param;
	}
	public void setParam(Object[] param) {
		this.param = param;
	}
	@Override
	public String toString() {
		if(param==null || param.length==0){
			return sql;
		}
		Pattern pattern = Pattern.compile("([^\\?]*)(\\?)([^\\?]*)");
		Matcher macher = pattern.matcher(sql);
		StringBuilder sb = new StringBuilder();
		int index = 0;
		while(macher.find()){
			if(index==param.length)
				break;
			sb.append(macher.group(1)).append("'").append(param[index]).append("'").append(macher.group(3));
			index++;
		}
		return sb.toString();
	}
	public boolean isHis() {
		return his;
	}
	public void setHis(boolean his) {
		this.his = his;
	}
	
}