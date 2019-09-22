package com.github.tachesimazzoca.mproxy.smtp;

import com.github.tachesimazzoca.mproxy.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SMTPConnection {
    private static final String NEWLINE = "\r\n";
    private final SMTPHandler handler;
    private final SMTPState state;
    private final BufferedReader in;
    private final PrintWriter out;
    // TODO: inject serverName
    private final String serverName = "localhost";

    private String helloName;

    public SMTPConnection(SMTPHandler handler) throws IOException {
        this.handler = handler;
        state = new SMTPState();
        Socket socket = handler.getSocket();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }

    public boolean hasStarted() {
        return helloName != null;
    }

    public SMTPState getState() {
        return state;
    }

    public String getServerName() {
        return serverName;
    }

    public void startSession(String msg) {
        helloName = msg;
        state.reset();
    }

    public void sendGreeting() {
        // TODO: inject the greeting message
        send("220 " + serverName + " Simple Mail Transfer Service Ready");
    }

    public void sendSyntaxError(String msg) {
        send("500 " + msg);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public void send(String line) {
        out.print(line);
        out.print(NEWLINE);
        out.flush();
    }

    public void quit() {
        IOUtils.closeQuietly(out);
        handler.close();
    }
}
