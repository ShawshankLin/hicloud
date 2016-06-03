package cn.edu.cylg.cis.hicloud.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropUtil {
	
	private static String propertiesPath = System.getProperty("user.home") + "/hicloud.properties";
	
	// 根据key读取value
	public static String readValue(String key) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath));
			props.load(in);
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 读取properties的全部信息
	public static void readProperties() {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath));
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				System.out.println(key + "=" + Property);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
