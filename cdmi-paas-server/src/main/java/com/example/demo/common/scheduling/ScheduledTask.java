package com.example.demo.common.scheduling;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@EnableScheduling
public class ScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);
    private Date salesValidity;

    public ScheduledTask(Date salesValidity){
        this.salesValidity = salesValidity;
    }

    // 每秒执行
    @Scheduled(cron = "* * * * * ?", zone = "Asia/Shanghai")
    public void sendClientTime() throws IOException{
        long countdown = salesValidity.getTime() - new Date().getTime();
        if(countdown > 0){
            JSONObject json = new JSONObject();
            json.put("countdown", countdown);
            for (WebSocket item : WebSocket.webSocketSet ){
                item.sendMessage(json.toJSONString());
            }
        }
    }
}
