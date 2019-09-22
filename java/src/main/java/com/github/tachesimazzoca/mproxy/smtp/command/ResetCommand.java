package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

/**
 * RSET
 */
public class ResetCommand implements SMTPCommand {
    @Override
    public void execute(SMTPConnection conn, String line) {
        conn.getState().reset();
        conn.send("250 OK");
    }
}
