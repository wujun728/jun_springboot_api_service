package com.yatop.lambda.im.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WebSocketNettyClient {
    private static WebSocketNettyClient instance;

    public static WebSocketNettyClient me(Packet packet){
        return new WebSocketNettyClient(packet);
    }

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(9, 17,
            4500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    public WebSocketNettyClient(Packet packet) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            URI websocketURI = new URI("ws://127.0.0.1:9998/chat");
            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String) null, true, httpHeaders);
            final WebSocketClientHandler handler = new WebSocketClientHandler(webSocketClientHandshaker);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpClientCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                            pipeline.addLast(WebSocketPacketCodec.INSTANCE);
                            pipeline.addLast(handler);
                        }
                    });


            ChannelFuture channelFuture = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync();
            handler.handshakeFuture().sync();
            Channel  ch = channelFuture.channel();
            ch.writeAndFlush(packet);

            //ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }



}