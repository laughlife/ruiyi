package com.liwei.ruiyi.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件公用类
 * 
 * @author 李伟
 * @time 2014-6-26 下午4:41:48
 */
public class ReadProUtils {

	/**
	 * 读取配置文件
	 * @param key 搜索的配置文件key
	 * @param configName 配置文件名字
	 * @return
	 */
	public static String ReadProperties(String key, String configName) {
		String value = "";
		if (StringUtils.isEmpty(configName)) {
			configName = "conf.properties";
		}
		try {
			Resource resource = new ClassPathResource(configName);
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			value = props.getProperty(key);
			props.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}
