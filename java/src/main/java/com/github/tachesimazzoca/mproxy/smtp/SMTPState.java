package com.github.tachesimazzoca.mproxy.smtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SMTPState {
    private static final String NEWLINE = "\r\n";

    private String returnPath;
    private List<String> recipients;
    private List<String> headers;
    private String content;

    public SMTPState() {
        reset();
    }

    public void reset() {
        returnPath = null;
        recipients = new LinkedList<String>();
        headers = new LinkedList<String>();
        content = null;
    }

    public String getMailFrom() {
        return returnPath;
    }

    public void setReturnPath(String returnPath) {
        this.returnPath = returnPath;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void clearRecipients() {
        recipients.clear();
    }

    public void addRecipient(String recipient) {
        recipients.add(recipient);
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void clearHeaders() {
        headers.clear();
    }

    public void addHeader(String header) {
        headers.add(header);
    }

    public String getContent() {
        return content;
    }

    public void readContent(BufferedReader in) throws IOException {
        content = null;
        StringBuilder buf = new StringBuilder();
        while (true) {
            String line = in.readLine();
            if (".".equals(line)) {
                break;
            }
            if (line.startsWith(".")) {
                buf.append(line.substring(1));
            } else {
                buf.append(line);
            }
            buf.append(NEWLINE);
        }
        content = buf.toString();
    }

    private boolean hasReadContent() {
        return content != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("returnPath: " + returnPath);
        sb.append(", recipients: " + Arrays.toString(recipients.toArray(
                new String[recipients.size()])));
        sb.append(" }");
        return sb.toString();
    }
}
