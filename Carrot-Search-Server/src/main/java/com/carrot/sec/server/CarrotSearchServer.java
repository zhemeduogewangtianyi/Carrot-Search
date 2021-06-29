package com.carrot.sec.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carrot.sec.context.JsonSearchContext;
import com.carrot.sec.enums.OperationTypeEnum;
import com.carrot.sec.operations.create.CreateDoc;
import com.carrot.sec.operations.delete.DeleteDoc;
import com.carrot.sec.operations.query.QueryDoc;
import com.carrot.sec.operations.update.UpdateDoc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CarrotSearchServer extends ServerSocket implements Runnable {


    public CarrotSearchServer(int port) throws IOException {
        super(port);
    }

    @Override
    public void run() {

        while (true) {

            Socket socket = null;
            try {

                socket = this.accept();
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String data;
                while ((data = br.readLine()) != null) {

                    JsonSearchContext jsonSearchContext = JSONObject.parseObject(data, JsonSearchContext.class);
                    OperationTypeEnum operationTypeEnum = OperationTypeEnum.getByValue(jsonSearchContext.getOperationType());
                    Map<String, Object> res = new HashMap<>(8);
                    switch (operationTypeEnum) {
                        case ADD:
                            try{
                                boolean doc = new CreateDoc().createDoc(jsonSearchContext);
                                res.put("code", 200);
                                res.put("msg", "success");
                                res.put("data", doc);
                            }catch(Exception e){
                                res.put("code", 500);
                                res.put("msg", e.getMessage());
                                res.put("data", null);
                            }
                            break;
                        case QUERY:
                            try{
                                List<Map<String, Object>> result = new QueryDoc().queryDoc(jsonSearchContext);
                                res.put("code", 200);
                                res.put("msg", "success");
                                res.put("data", result);
                            }catch(Exception e){
                                e.printStackTrace();
                                res.put("code", 500);
                                res.put("msg", e.getMessage());
                                res.put("data", null);
                            }
                            break;
                        case DELETE:
                            try{
                                boolean doc = new DeleteDoc().deleteDoc(jsonSearchContext);
                                res.put("code", 200);
                                res.put("msg", "success");
                                res.put("data", doc);
                            }catch(Exception e){
                                res.put("code", 500);
                                res.put("msg", e.getMessage());
                                res.put("data", null);
                            }
                            break;
                        case UPDATE:
                            try{
                                boolean doc = new UpdateDoc().updateDoc(jsonSearchContext,jsonSearchContext.getTargetJsonSearchContext());
                                res.put("code", 200);
                                res.put("msg", "success");
                                res.put("data", doc);
                            }catch(Exception e){
                                res.put("code", 500);
                                res.put("msg", e.getMessage());
                                res.put("data", null);
                            }
                            break;
                        default:
                            break;
                    }

                    OutputStream os = socket.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                    bw.write(JSON.toJSONString(res));
                    bw.write("\n");
                    bw.flush();

                }

                Thread.sleep(ThreadLocalRandom.current().nextInt(100) + 1);

            } catch (Exception e) {
                e.printStackTrace();

                try{
                    OutputStream os = socket.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));

                    Map<String, Object> res = new HashMap<>(8);
                    res.put("code", 500);
                    res.put("msg", e.getMessage());
                    res.put("data", null);
                    bw.write(JSON.toJSONString(res));
                    bw.write("\n");
                    bw.flush();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                continue;
            }

        }

    }

}
