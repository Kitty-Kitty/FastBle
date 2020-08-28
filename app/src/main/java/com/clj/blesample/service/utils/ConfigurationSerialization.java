package com.clj.blesample.service.utils;

import android.util.Log;

import com.clj.blesample.service.beans.Configuration;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ConfigurationSerialization {

    public static boolean saveConfiguration(File xmlFile, Configuration conf) {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);

        try {
            xmlMapper.writeValue(xmlFile, conf);
            //System.out.println(xmlMapper.writeValueAsString(conf));
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage(), e.getCause());
            return false;
        }

        return true;
    }

    public static Configuration getConfiguration(File xmlFile) {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        JacksonXmlModule module = new JacksonXmlModule();
        // and then configure, for example:
        module.setDefaultUseWrapper(false);
        XmlMapper mapper = new XmlMapper(module);
        //Reading from xml file and creating XMLStreamReader
        XMLStreamReader reader = null;
        Configuration conf = null;

        try {
            reader = factory.createXMLStreamReader(new FileInputStream(xmlFile));
            conf = mapper.readValue(reader, Configuration.class);
        } catch (XMLStreamException e) {
            //e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage(), e.getCause());
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage(), e.getCause());
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage(), e.getCause());
        }

        return conf;
    }
}
