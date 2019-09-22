package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

/**
 * NOOP
 */
public class NoopCommand implements SMTPCommand {
    @Override
    public void execute(SMTPConnection conn, String line) {
        conn.send("250 OK");
    }
}
