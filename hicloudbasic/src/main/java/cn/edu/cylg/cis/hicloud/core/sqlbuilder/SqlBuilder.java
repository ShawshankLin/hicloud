package cn.edu.cylg.cis.hicloud.core.sqlbuilder;

import java.util.Map;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;



public class SqlBuilder {
	public static String build(String sql, Map<String, Object> filters) {
		XsqlFilterResult result = new XsqlBuilder().generateSql(sql, filters);
		SqlParam sp = new SqlParam();
		sp.setSql(result.getXsql());
		sp.setParam(result.getAcceptedFilters().values().toArray());
		return sp.toString();
	}
}