package com.wychmod.socket;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO (Blocking I/O) 类用于演示基于Java的阻塞I/O模型的简单服务器实现
 * 该服务器监听指定端口，接受客户端连接，并对每个连接的客户端发送的数据进行响应
 */
public class BIO {
    // 标记是否停止接受新连接和处理现有连接
    static boolean stop = false;

    /**
     * 主函数实现服务器的启动和运行逻辑
     * @param args 命令行参数，本例中未使用
     * @throws Exception 如果服务器Socket初始化失败或在I/O操作中发生错误
     */
    public static void main(String[] args) throws Exception {
        // 初始化连接计数器
        int connectionNum = 0;
        // 定义服务器监听的端口号
        int port = 8888;
        ExecutorService service = Executors.newCachedThreadPool();
        // 创建ServerSocket实例，监听指定端口
        ServerSocket serverSocket = new ServerSocket(port);
        while (!stop) {
            // 达到最大连接数时，设置停止标志为true
            if (10 == connectionNum) {
                stop = true;
            }
            // 接受一个新的客户端Socket连接
            Socket socket = serverSocket.accept();
            // 将处理客户端连接的任务提交到线程池执行
            service.execute(() -> {
                try {
                    // 使用Scanner读取客户端发送的数据
                    Scanner scanner = new Scanner(socket.getInputStream());
                    // 使用PrintStream向客户端发送响应数据
                    PrintStream printStream = new PrintStream(socket.getOutputStream());
                    // 循环读取并响应客户端的数据，直到停止标志为true
                    while (!stop) {
                        // 读取客户端发送的字符串，并去除前后空白
                        String s = scanner.next().trim();
                        // 向客户端发送响应数据
                        printStream.println("PONG:" + s);
                    }
                } catch (Exception ex) {
                    // 打印异常堆栈跟踪信息
                    ex.printStackTrace();
                }
            });
            // 连接计数器增加
            connectionNum++;
        }
        service.shutdown();
        serverSocket.close();
    }
}
