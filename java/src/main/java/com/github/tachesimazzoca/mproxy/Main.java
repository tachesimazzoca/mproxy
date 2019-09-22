package com.github.tachesimazzoca.mproxy;

import com.github.tachesimazzoca.mproxy.smtp.SMTPServer;

import java.net.InetAddress;

public class Main {
    public static void main(String[] args) {

        String host = "localhost";
        int port = 2525;

        try {
            InetAddress bindAddress = InetAddress.getByName(host);
            SMTPServer server = new SMTPServer(bindAddress, port);
            server.startService();
            System.out.println("Press ENTER key to stop ...");
            System.in.read();
            server.stopService(5000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
