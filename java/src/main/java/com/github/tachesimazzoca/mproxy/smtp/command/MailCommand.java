package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MAIL
 */
public class MailCommand implements SMTPCommand {
    private final static Pattern MAIL_FROM_PATTERN = Pattern.compile("^MAIL FROM: *([^ ]+) *$");

    @Override
    public void execute(SMTPConnection conn, String line) {
        if (!conn.hasStarted()) {
            throw new IllegalStateException("Issue EHLO or HELO first");
        }

        Matcher m = MAIL_FROM_PATTERN.matcher(line);
        if (!m.matches() || m.groupCount() != 1) {
            throw new IllegalArgumentException("Invalid syntax MAIL FROM: <foo@example.net>");
        }
        conn.getState().setReturnPath(m.group(1));
        conn.send("250 OK");
    }
}
