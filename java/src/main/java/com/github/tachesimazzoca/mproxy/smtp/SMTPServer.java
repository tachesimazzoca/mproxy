package com.github.tachesimazzoca.mproxy.smtp;

import com.github.tachesimazzoca.mproxy.server.AbstractServer;
import com.github.tachesimazzoca.mproxy.server.ProtocolHandler;

import java.net.InetAddress;
import java.net.Socket;

public class SMTPServer extends AbstractServer {
    public SMTPServer(InetAddress bindAddress, int port) {
        super(bindAddress, port);
    }

    @Override
    protected ProtocolHandler createProtocolHandler(Socket clientSocket) {
        return new SMTPHandler(clientSocket);
    }
}
