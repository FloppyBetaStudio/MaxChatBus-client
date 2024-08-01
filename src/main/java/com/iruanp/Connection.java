package com.iruanp;
import org.json.simple.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.security.cert.X509Certificate;

public class Connection {
    static TCPClient client;

    public interface MessageHandler {
        void handleMessage(String message);
    }

    public static class TCPClient extends Thread {
        private String serverIP;
        private int serverPort;
        private SSLSocket socket;
        private PrintWriter out;
        private boolean running;
        private MessageHandler messageHandler;

        private String group;
        private String name;
        private String token;

        public TCPClient(String serverIP, int serverPort, MessageHandler messageHandler) {
            this.serverIP = serverIP;
            this.serverPort = serverPort;
            this.running = true;
            this.messageHandler = messageHandler;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    TrustManager[] trustAllCerts = new TrustManager[]{
                            new X509TrustManager() {
                                public X509Certificate[] getAcceptedIssuers() {
                                    return null;
                                }

                                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                                }

                                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                                }
                            }
                    };
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                    socket = (SSLSocket) sslSocketFactory.createSocket(serverIP, serverPort);
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                    //Send header
                    JSONObject header = new JSONObject();
                    header.put("group", group);
                    header.put("name", name);
                    header.put("token", token);
                    writeLine(header.toJSONString());


                    String line;
                    while (true) {
                        line = in.readLine();
                        if (line.isEmpty()){continue;}
                        messageHandler.handleMessage(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(5000); // 重新连接前等待5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void writeLine(String line) {
            out.println(line);
            out.flush();
        }

        public void disconnect() {
            running = false;
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void init(String serverIP, int serverPort, String token, String group, String name, MessageHandler messageHandler) {
        client = new TCPClient(serverIP, serverPort, messageHandler);
        client.group = group;
        client.name = name;
        client.token = token;

        client.start();
    }

    public static void writeLine(String line) {
        client.writeLine(line);
    }

    public static void disconnect() {
        client.disconnect();
    }
}
