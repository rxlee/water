package com.linkel.water.server;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.util.*;

@Component
@Qualifier("server")
@PropertySource(value= "classpath:/application.properties")
public class Server {
    boolean started = false;
    ServerSocket ss = null;
    List<Client> clients = new ArrayList<Client>();

    @Value("${tcp.port}")
    private int tcpPort;

//    public static void main(String[] args) {
//        new Server().start();
//    }

    public void start() {
        try {
            ss = new ServerSocket(tcpPort);
            started = true;
            System.out.println("端口已开启,占用8888端口号....");
        } catch (BindException e) {
            System.out.println("端口使用中....");
            System.out.println("请关掉相关程序并重新运行服务器！");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (started) {
                Socket s = ss.accept();
                Client c = new Client(s);
                System.out.println("a client connected!");
                new Thread(c).start();
                clients.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Client implements Runnable {
        private Socket s;
        private SocketAddress ip;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private boolean bConnected = false;

        public Client(Socket s) {
            this.s = s;
            try {
                ip = s.getRemoteSocketAddress();
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                bConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(byte b) {
            try {
                dos.writeByte(b);
            } catch (IOException e) {
                try {
                    //尝试重连
                    s.connect(ip,15);
                } catch (IOException ex) {
                    clients.remove(this);
                    System.out.println("对方退出了！我从List里面去掉了！");
                }
            }
        }

        public void run() {
            try {
                while (bConnected) {
                    byte b = dis.readByte();
                    System.out.println("------------来自本地服务器:" + b+"=="+Integer.toHexString(b&0xFF));
                    for (int i = 0; i < clients.size(); i++) {
                        Client c = clients.get(i);
                        c.send(b);
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client closed!");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (dos != null)
                        dos.close();
                    if (s != null) {
                        s.close();
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }
}
