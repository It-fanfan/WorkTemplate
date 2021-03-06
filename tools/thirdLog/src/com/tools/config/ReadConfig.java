package com.tools.config;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件路径
 *
 * @author xuweihua
 */
public class ReadConfig {
    // 配置文件路径
    private static final String CONFIG_FILE_NAME = "config.properties";

    // 唯一事例
    private static ReadConfig instance;
    // 配置缓存
    private Map<String, String> configMap;

    static {
        init();
    }

    /**
     * 初始化配置
     */
    public static void init() {
        instance = new ReadConfig();
    }

    private ReadConfig() {
        configMap = new HashMap<String, String>();
        try {
            InputStream in = ReadConfig.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
            Properties properties = new Properties();
            properties.load(in);
            for (Object key : properties.keySet()) {
                configMap.put((String) key, properties.getProperty((String) key));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行匹配KEY值
     *
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        return instance.configMap.containsKey(key);
    }

    /**
     * 获取values值
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return instance.configMap.get(key);
    }

    /**
     * 向配置文件写入(顺序会改变)
     *
     * @param name
     * @param value
     */
    public static void setValue(String name, String value) {
        Properties properties = new Properties();
        try {
            InputStream in = ReadConfig.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
            properties.load(in);
            in.close();
            String path = ReadConfig.class.getClassLoader().getResource("/").getPath();
            FileOutputStream out = new FileOutputStream(path + CONFIG_FILE_NAME);
            properties.setProperty(name, value);
            properties.store(out, "");
            out.close();
            instance.configMap.put(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ReadConfig();
    }
}
