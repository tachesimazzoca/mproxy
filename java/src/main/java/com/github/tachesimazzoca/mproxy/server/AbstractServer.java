package com.github.tachesimazzoca.mproxy.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractServer extends Thread {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final InetAddress bindAddress;
    private final int port;

    private volatile ServerState state;
    private ServerSocket serverSocket = null;

    private Set<ProtocolHandler> handlers =
            Collections.synchronizedSet(new HashSet<ProtocolHandler>());

    private enum ServerState {
        RUNNING, STOPPING, STOPPED
    }

    public AbstractServer(InetAddress bindAddress, int port) {
        this.bindAddress = bindAddress;
        this.port = port;
        this.state = ServerState.STOPPED;
    }

    protected abstract ProtocolHandler createProtocolHandler(Socket clientSocket);

    private void handleClientSocket(Socket clientSocket) {
        final ProtocolHandler handler = createProtocolHandler(clientSocket);
        handlers.add(handler);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.run();
                } catch (Exception e) {
                    // any exceptions can never be caught.
                } finally {
                    log.debug("Removing handler: " + handler);
                    handlers.remove(handler);
                }
            }
        });
        thread.start();
    }

    @Override
    public void run() {
        try {
            initServerSocket();
            while (isRunning()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    if (isRunning()) {
                        handleClientSocket(clientSocket);
                    } else {
                        clientSocket.close();
                    }
                } catch (Exception e) {
                    // TODO: Just log error messages without throwing exception
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            quit();
        }
    }

    private synchronized void initServerSocket() {
        log.info("Initializing server");
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(bindAddress, port));
            log.info("Running with " + serverSocket);
            state = ServerState.RUNNING;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to open server socket", e);
        }
    }

    private synchronized void closeServerSocket() {
        if (serverSocket == null || serverSocket.isClosed()) {
            return;
        }
        try {
            log.info("Closing serverSocket: " + serverSocket);
            serverSocket.close();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to close server socket", e);
        }
    }

    private synchronized void quit() {
        closeServerSocket();
        synchronized (handlers) {
            for (ProtocolHandler handler : handlers) {
                log.info("Closing protocol handler: " + handler);
                handler.close();
            }
            handlers.clear();
        }
        state = ServerState.STOPPED;
    }

    public boolean isRunning() {
        return state == ServerState.RUNNING;
    }

    public boolean isStopped() {
        return state == ServerState.STOPPED;
    }

    public synchronized void startService() {
        if (!isStopped()) {
            throw new IllegalStateException("Server is running");
        }
        log.info("Starting service");
        start();
    }

    public synchronized void stopService(long timeout) {
        if (!isRunning()) {
            throw new IllegalStateException("Server is not running");
        }
        log.info("Stopping service");
        state = ServerState.STOPPING;
        try {
            interrupt();
            quit();
            join(timeout);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
