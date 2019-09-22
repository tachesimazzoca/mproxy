package com.github.tachesimazzoca.mproxy.smtp;

import com.github.tachesimazzoca.mproxy.server.ProtocolHandler;
import com.github.tachesimazzoca.mproxy.smtp.command.SMTPCommandHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.net.SocketTimeoutException;

public class SMTPHandler implements ProtocolHandler {
    private static final Logger log = LoggerFactory.getLogger(SMTPHandler.class);

    private final Socket socket;
    private boolean closing;
    private SMTPCommandHelper commandHelper;

    public SMTPHandler(Socket socket) {
        this.socket = socket;
        closing = false;
        commandHelper = new SMTPCommandHelper();
    }

    protected Socket getSocket() {
        return socket;
    }

    @Override
    public void close() {
        closing = true;
        if (socket != null && !socket.isClosed()) {
            log.debug("Closing SMTP client socket: " + socket);
            try {
                socket.close();
            } catch (Exception e) {
                log.warn("An error occurred when closing " + socket + ":" + e);
            }
        }
    }

    @Override
    public void run() {
        try {
            SMTPConnection conn = new SMTPConnection(this);
            log.debug("Created new connection: " + conn);
            closing = false;
            conn.sendGreeting();
            while (!closing) {
                String line = conn.readLine();
                if (line == null) {
                    close();
                } else {
                    commandHelper.execute(conn, line);
                    log.debug(conn.getState().toString());
                }
            }
        } catch (SocketTimeoutException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            if (!closing) {
                throw new IllegalStateException(e);
            }
        }
    }
}
