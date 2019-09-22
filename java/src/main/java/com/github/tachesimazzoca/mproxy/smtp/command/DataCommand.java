package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

/**
 * DATA
 */
public class DataCommand implements SMTPCommand {
    @Override
    public void execute(SMTPConnection conn, String line) {
        // TODO: receive message body ending with the EOD dot.
        throw new UnsupportedOperationException("Not supported yet");
    }
}
