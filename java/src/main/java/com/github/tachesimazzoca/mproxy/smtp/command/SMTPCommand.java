package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

public interface SMTPCommand {
    void execute(SMTPConnection conn, String line);
}
