package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RCPT
 */
public class RecipientCommand implements SMTPCommand {
    private final static Pattern RCPT_TO_PATTERN = Pattern.compile("^RCPT TO: *([^ ]+) *$");

    @Override
    public void execute(SMTPConnection conn, String line) {
        Matcher m = RCPT_TO_PATTERN.matcher(line);
        if (!m.matches() || m.groupCount() != 1) {
            throw new IllegalArgumentException("Invalid syntax RCPT TO: <foo@example.net>");
        }
        conn.getState().addRecipient(m.group(1));
        conn.send("250 OK");
    }
}
