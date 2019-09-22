package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

public class QuitCommand implements SMTPCommand {
    @Override
    public void execute(SMTPConnection conn, String line) {
        conn.send("221 Bye");
        conn.quit();
    }
}
