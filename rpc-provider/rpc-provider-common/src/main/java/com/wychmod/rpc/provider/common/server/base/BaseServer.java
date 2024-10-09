package com.wychmod.rpc.provider.common.server.base;

import com.wychmod.rpc.provider.common.handler.RpcProviderHandler;
import com.wychmod.rpc.provider.common.server.api.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wychmod
 * @date: 2024-10-10
 */
public class BaseServer implements Server {

    private final Logger logger = LoggerFactory.getLogger(BaseServer.class);
    //主机域名或者IP地址
    protected String host = "127.0.0.1";
    //端口号
    protected int port = 27110;
    //存储的是实体类关系
    protected Map<String, Object> handlerMap = new HashMap<>();

    public BaseServer(String serverAddress) {
        if (!StringUtils.isEmpty(serverAddress)) {
            String[] serverArray = serverAddress.split(":");
            this.host = serverArray[0];
            this.port = Integer.parseInt(serverArray[1]);
        }
    }

    /**
     * 启动Netty服务器
     * 该方法用于启动一个RPC服务器，使用Netty作为网络通信框架
     * 它通过配置ServerBootstrap来创建一个服务器实例，并绑定到指定的主机和端口
     * 同时，它还配置了事件循环组、通道初始化器、通道选项等重要组件
     */
    @Override
    public void startNettyServer() {
        // 创建Boss组和Worker组，用于处理网络事件
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap实例，并进行配置
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup) // 绑定Boss组和Worker组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel通道
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 设置通道初始化器
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 初始化通道管道，添加编解码器和处理器
                            channel.pipeline()
                                    // TODO 预留编解码，需要实现自定义协议
                                    .addLast(new StringDecoder()) // 添加StringDecoder
                                    .addLast(new StringEncoder()) // 添加StringEncoder
                                    .addLast(new RpcProviderHandler(handlerMap)); // 添加自定义的RpcProviderHandler
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置服务器通道的SO_BACKLOG选项
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 设置客户端通道的SO_KEEPALIVE选项
            // 绑定服务器到指定的主机和端口，并等待直到完成
            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.info("Server started on port {}", port);
            // 等待直到服务器通道关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("RPC Server start error", e);
        } finally {
            // 关闭Worker组和Boss组，释放资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
