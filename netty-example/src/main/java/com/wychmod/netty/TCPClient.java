package com.wychmod.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class TCPClient {

    public static void main(String[] args) throws InterruptedException {

        // 客户端只需要一个事件循环组，可以看做 BossGroup
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            // 创建客户端的启动对象
            //Bootstrap 客户端引导类
            Bootstrap bootstrap = new Bootstrap();
            // 配置参数
            bootstrap
                    // 设置线程组
                    .group(eventLoopGroup)
                    // 说明客户端通道的实现类（便于 Netty 做反射处理）
                    //客户端创建一个 NioSocketChannel 作为客户端通道，去连接服务器
                    .channel(NioSocketChannel.class)
                    // handler()方法用于给 BossGroup 设置业务处理器
                    .handler(
                            // 创建一个通道初始化对象
                            //当一个链接建立时，我们需要知道怎么来接收或者发送数据，
                            // 当然，我们有各种各样的Handler实现来处理它，
                            // 那么ChannelInitializer便是用来配置这些Handler，
                            // 它会提供一个ChannelPipeline，并把Handler加入到ChannelPipeline
                            new ChannelInitializer<SocketChannel>() {
                                // 向 Pipeline 添加业务处理器
                                @Override
                                protected void initChannel(
                                        SocketChannel socketChannel
                                ) throws Exception {
                                    socketChannel.pipeline().addLast(
                                            new NettyClientHandler()
                                    );
                                    // 可以继续调用 socketChannel.pipeline().addLast()
                                    // 添加更多 Handler
                                }
                            }
                    );

            System.out.println("client is ready...");

            // 启动客户端去连接服务器端，ChannelFuture 涉及到 Netty 的异步模型
            //在Netty中所有的IO操作都是异步的，因此，你不能立刻得知消息是否被正确处理，
            // 但是我们可以过一会等它执行完成或者直接注册一个监听，
            // 具体的实现就是通过Future和ChannelFutures,
            // 他们可以注册一个监听，当操作执行成功或失败时监听会自动触发。
            ChannelFuture channelFuture = bootstrap.connect(
                    "127.0.0.1",
                    8080).sync();
            // 对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    /**
     * 自定义一个 Handler，需要继承 Netty 规定好的某个 HandlerAdapter（规范）
     * InboundHandler 用于处理数据流入本端（客户端）的 IO 事件
     * InboundHandler 用于处理数据流出本端（客户端）的 IO 事件
     */
    static class NettyClientHandler extends ChannelInboundHandlerAdapter {
        /**
         * 通道就绪时执行
         *
         * @param ctx 上下文对象
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx)
                throws Exception {
            // 向服务器发送数据
            ctx.writeAndFlush(
                    // Unpooled 类是 Netty 提供的专门操作缓冲区的工具
                    // 类，copiedBuffer 方法返回的 ByteBuf 对象类似于
                    // NIO 中的 ByteBuffer，但性能更高
                    Unpooled.copiedBuffer(
                            "hello server!",
                            CharsetUtil.UTF_8
                    )
            );
        }

        /**
         * 当通道有数据可读时执行
         *
         * @param ctx 上下文对象
         * @param msg 服务器端发送的数据
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            // 接收服务器端发来的数据

            System.out.println("server address: "
                    + ctx.channel().remoteAddress());

            // ByteBuf 是 Netty 提供的类，比 NIO 的 ByteBuffer 性能更高
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("data from server: "
                    + byteBuf.toString(CharsetUtil.UTF_8));
        }

        /**
         * 发生异常时执行
         *
         * @param ctx   上下文对象
         * @param cause 异常对象
         * @throws Exception
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            // 关闭与服务器端的 Socket 连接
            ctx.channel().close();
        }
    }
}