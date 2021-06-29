package com.carrot.sec.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.File;
import java.nio.file.Path;

@Getter
public class CSearchConfig {

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
        cSearchConfig.setPath("/Users/wty/Downloads/temp" + File.separator + className.toLowerCase());
//        cSearchConfig.setPath("d://temp" + File.separator + obj.getClass().getSimpleName().toLowerCase());
        cSearchConfig.setAnalyzer(new StandardAnalyzer());
        return cSearchConfig;
    }

}
