package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;

/**
 * EHLO/HELO
 */
public class HelloCommand implements SMTPCommand {
    @Override
    public void execute(SMTPConnection conn, String line) {
        if (conn.hasStarted()) {
            throw new IllegalArgumentException("Session has started");
        }

        if (line != null && line.length() > 5) {
            conn.startSession(line.substring(5).trim());
        } else {
            throw new IllegalArgumentException("Invalid syntax (EHLO|HELO) domain");
        }

        // "Normally, the response to EHLO will be a multiline reply.
        // Each line of the response contains a keyword and, optionally,
        // one or more parameters. Following the normal syntax
        // for multiline replies, these keywords follow the code (250)
        // and a hyphen for all but the last line, and the code
        // and a space for the last line."
        // - RFC2821: https://tools.ietf.org/html/rfc2821#section-4.1.1.1

        // TODO: inject what this command should reply
        conn.send("250-" + conn.getServerName());
        conn.send("250-AUTH PLAIN");
        conn.send("250 HELP");
    }
}
