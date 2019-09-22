package com.github.tachesimazzoca.mproxy.smtp.command;

import com.github.tachesimazzoca.mproxy.smtp.SMTPConnection;
import com.github.tachesimazzoca.mproxy.smtp.SMTPHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SMTPCommandHelper {
    private static final Map<String, SMTPCommand> commandMap = new HashMap<String, SMTPCommand>();

    private static final int COMMAND_LENGTH = 4;
    private static final int MAX_COMMAND_LINE_LENGTH = 512;

    static {
        commandMap.put("EHLO", new HelloCommand());
        commandMap.put("HELO", new HelloCommand());
        commandMap.put("MAIL", new MailCommand());
        commandMap.put("RCPT", new RecipientCommand());
        commandMap.put("DATA", new DataCommand());
        commandMap.put("RSET", new ResetCommand());
        commandMap.put("VRFY", new VerifyCommand());
        commandMap.put("NOOP", new NoopCommand());
        commandMap.put("QUIT", new QuitCommand());
    }

    public SMTPCommandHelper() {
        // TODO: inject supported SMTP commands
    }

    public void execute(SMTPConnection conn, String line) {
        try {
            String commandName = parseCommandName(line).toUpperCase();
            if (commandMap.containsKey(commandName)) {
                commandMap.get(commandName).execute(conn, line);
            } else {
                conn.sendSyntaxError("Command not recognized");
            }
        } catch (Exception e) {
            conn.sendSyntaxError(e.getMessage());
        }
    }

    private static String parseCommandName(String line) {
        if (line.length() < COMMAND_LENGTH) {
            throw new IllegalArgumentException("Command too short");
        }
        if (line.length() > COMMAND_LENGTH && line.charAt(COMMAND_LENGTH) != ' ') {
            throw new IllegalArgumentException("Invalid command");
        }
        if (line.length() > MAX_COMMAND_LINE_LENGTH) {
            throw new IllegalArgumentException("Line too long");
        }
        return line.substring(0, COMMAND_LENGTH);
    }
}
