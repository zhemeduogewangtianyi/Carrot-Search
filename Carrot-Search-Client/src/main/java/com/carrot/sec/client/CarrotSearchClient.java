package com.carrot.sec.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CarrotSearchClient{

    private final Socket socket;

    private final String ip;

    public CarrotSearchClient(String host, int port) throws IOException {
        this.ip = host;
        socket = new Socket(host,port);
    }

    public String getIp(){
        return ip;
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
