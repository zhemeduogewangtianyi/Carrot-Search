package com.carrot.sec;

import com.carrot.sec.server.CarrotSearchServer;

import java.io.IOException;

public class CarrotServerMain {

    public static void main(String[] args) throws IOException {
        new Thread(new CarrotSearchServer(9527)).start();
    }

}
