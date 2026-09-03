package com.yatop.lambda.net.model;

import com.yatop.lambda.net.websocket.initializer.WebSocketServerInitializer;
import com.yatop.lambda.net.websocket.server.WebSocketChatServer;
import com.yuyaogc.lowcode.engine.annotation.APP;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.enums.AppTypeEnum;
import com.yuyaogc.lowcode.engine.util.ConfigUtils;

import java.util.Properties;

@APP(name = "net", displayName = "网络模块", type = AppTypeEnum.MODULE)
@Table(name = "im_netty_booter")
public class NettyBooter {
    WebSocketChatServer webSocketChatServer ;

    @Service(displayName = "事件", event = true)
    public void onStart() {
        ConfigUtils configUtils = new ConfigUtils();
        Properties properties = configUtils.getApplicationProperties(this.getClass());
        int port = Integer.valueOf(properties.getProperty("netty.websocket.port"));
        webSocketChatServer =  new WebSocketChatServer(new WebSocketServerInitializer(), port);
        System.err.println(port);
        webSocketChatServer.start();
    }

    @Service(displayName = "stop", stop = true)
    public void onStop(){
        System.err.println("---------------stop-------------");
        webSocketChatServer.stop();
    }
}
