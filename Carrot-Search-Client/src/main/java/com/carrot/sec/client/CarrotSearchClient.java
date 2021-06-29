package com.carrot.sec.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class CarrotSearchClient extends Thread {

    private final Socket socket;

    private final String ip;

    private final InputStream is;
    private final OutputStream os;

    private boolean off = true;

    public CarrotSearchClient(String host, int port, String name) throws IOException {
        super(name);
        this.ip = host;
        socket = new Socket(host,port);
        is = socket.getInputStream();
        os = socket.getOutputStream();

    }

    public String getIp(){
        return ip;
    }

    public boolean shutdown(){
        this.off = false;
        return true;
    }


    public String send(String msg){

        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            OutputStream os = socket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            bw.write(msg);
            bw.write("\n");
            bw.flush();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
