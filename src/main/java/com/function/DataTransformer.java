package com.function;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataTransformer {
    public static DataModel transform(String data) throws Exception {
        ObjectMapper mapper;

        if (data.trim().startsWith("<")) {
            mapper = new XmlMapper();
        } else {
            mapper = new ObjectMapper();
        }

        return mapper.readValue(data, DataModel.class);
    }
}

