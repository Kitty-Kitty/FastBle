package com.clj.blesample.service;


import com.clj.blesample.service.beans.Configuration;
import com.clj.blesample.service.utils.ConfigurationSerialization;

import java.io.File;

/**
 * 加载需要的配置文件，主要功能是初始化系统信息、从文件中读取设备列表信息等。
 *
 * @author f
 * @version 1.0
 * @created 23-7月-2020 14:44:13
 */
public class ConfigurationLoader {

    public ConfigurationLoader() {

    }

    public void finalize() throws Throwable {

    }

    /**
     * 从指定文件中读取配置信息
     *
     * @param filePath 保存配置信息的文件名称
     */
    public static Configuration getConfiguration(String filePath) {
        return (Configuration) ConfigurationSerialization.getConfiguration(new File(filePath));
    }

    /**
     * 使用Configuration对象更新配置文件信息
     *
     * @param filePath 保存配置信息的文件名称
     * @param conf     表示需要保存的配置信息
     */
    public static boolean setConfiguration(String filePath, Configuration conf) {
        return ConfigurationSerialization.saveConfiguration(new File(filePath), conf);
    }
}//end ConfigurationLoader