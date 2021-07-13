package com.carrot.sec.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;

@Getter
public class CSearchConfig {

    private static String dataPath ;

    static{

        String configFile = System.getProperty("user.dir") + File.separator + "config.cfg";
        File file = new File(configFile);
        if(!file.exists()){
            throw new RuntimeException("not found config file ! please create config.cfg in your path ! path is  : " + configFile);
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            Properties properties = new Properties();
            properties.load(br);

            dataPath = properties.getProperty("data.path");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Path path;

    @Setter
    private Analyzer analyzer;

    public void setPath(String path) {
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        this.path = file.toPath();
    }

    public static CSearchConfig getConfig(String className){
        if(StringUtils.isEmpty(className)){
            throw new RuntimeException("className can't null !");
        }

        CSearchConfig cSearchConfig = new CSearchConfig();
        cSearchConfig.setPath(CSearchConfig.dataPath + File.separator + className.toLowerCase());
        cSearchConfig.setAnalyzer(new StandardAnalyzer());
        return cSearchConfig;
    }

}
