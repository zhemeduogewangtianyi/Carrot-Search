package com.carrot.sec;

import com.carrot.sec.config.CSearchConfig;
import com.carrot.sec.server.CarrotSearchServer;

import java.io.IOException;
import java.nio.file.Path;

public class CarrotServerMain {

    public static void main(String[] args) throws IOException {

//        CSearchConfig cSearchConfig = new CSearchConfig();
//        Path path = cSearchConfig.getPath();

        CSearchConfig student = CSearchConfig.getConfig("student");
//        new Thread(new CarrotSearchServer(9527)).start();
        System.out.println("started !" + " " + System.getProperty("user.dir"));
    }

}
