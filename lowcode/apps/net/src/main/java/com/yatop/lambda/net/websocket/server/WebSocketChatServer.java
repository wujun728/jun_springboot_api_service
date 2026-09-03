package com.yatop.lambda.net.websocket.server;


import com.yatop.lambda.net.websocket.initializer.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Objects;

/**
 * 引导Netty服务器
 *
 * @date 2019-04-20
 */
public class WebSocketChatServer {

    private final WebSocketServerInitializer webSocketServerInitializer;
    private int port;

    public static Channel serverChannel = null;

    public WebSocketChatServer(WebSocketServerInitializer webSocketServerInitializer,int port) {
        this.port = port;
        this.webSocketServerInitializer = webSocketServerInitializer;
    }

    /**
     * 开始引导服务器
     * 注意：不带 child 的是设置服务端的 Channel，带 child 的方法是设置每一条连接
     */
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                // 指定线程模型，这里是主从线程模型
                .group(bossGroup, workerGroup)
                // 指定服务端的 Channel 的 I/O 模型
                .channel(NioServerSocketChannel.class)
                // 设置发送缓冲大小
                .option(ChannelOption.SO_RCVBUF, 40 * 1024)
                // 指定处理新连接数据的读写处理逻辑:每次有新连接到来，都会去执行ChannelInitializer.initChannel()，并new一大堆handler。所以如果handler中无成员变量，则可写成单例
                .childHandler(webSocketServerInitializer);

        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind(port).sync();
            serverChannel = channelFuture.channel();
            System.out.println("websocket端口已经打开 " + port);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }


    public void stop(){
        if(!Objects.isNull(serverChannel)){
            serverChannel.close();
            serverChannel = null;
        }
        System.out.println("websocket端口已经关闭");
    }

}
