package com.carrot.net;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.Socket;

@Data
public class NetUnion {

    private final String url;

    private final Socket socket;

    private final String document;

    public NetUnion(String url,String urlPrefix) throws IOException {

        if(StringUtils.isEmpty(url)){
            throw new IllegalArgumentException("url can't null !");
        }

        this.url = url;

        String allAddr = url.replaceAll(urlPrefix, "");
        boolean contains = allAddr.contains("/");

        String ip ;
        String port = "9527";

        if(contains){
            String[] split = allAddr.split("/");
            String address = split[0];

            //TODO ip and port mapping
            if(!StringUtils.isEmpty(address)){
                try{
                    String[] temps = address.split(":");
                    ip = temps[0];
                    try{
                        port = temps[1];
                    }catch (Exception e){
                        //TODO use default port
                        e.printStackTrace();
                    }

                }catch(Exception e){
                    throw new IllegalArgumentException("address error !");
                }
            }else {
                throw new IllegalArgumentException("address error !");
            }

            document = split[1];
            socket = new Socket(ip,Integer.parseInt(port));

        }else{
            throw new IllegalArgumentException("document can't null !");
        }

        if(StringUtils.isEmpty(port)){
            throw new IllegalArgumentException("port can't null !");
        }
    }

    public void close() throws IOException {
        this.socket.close();
    }

}
