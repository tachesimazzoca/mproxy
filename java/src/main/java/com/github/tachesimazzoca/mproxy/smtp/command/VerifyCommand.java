package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

/**
 * VRFY
 */
public class VerifyCommand implements SMTPCommand {
    @Override
    public void execute(SMTPConnection conn, String line) {
        throw new UnsupportedOperationException("Not supported yet");
    }
}
