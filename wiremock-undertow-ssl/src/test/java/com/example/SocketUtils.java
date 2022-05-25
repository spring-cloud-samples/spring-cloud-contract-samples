//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example;

import java.net.ServerSocket;

public final class SocketUtils {
    private SocketUtils() {
    }

    public static int findAvailableTcpPort() {
        try {
            ServerSocket s = new ServerSocket(0);
            int port = s.getLocalPort();
            s.close();
            return port;
        } catch (Exception var2) {
            return 0;
        }
    }
}
