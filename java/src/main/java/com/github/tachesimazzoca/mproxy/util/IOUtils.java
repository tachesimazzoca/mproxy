package com.github.tachesimazzoca.mproxy.util;

import java.io.Closeable;

public class IOUtils {
    public static void closeQuietly(Closeable o) {
        if (o != null) {
            try {
                o.close();
            } catch (Exception e) {
            }
        }
    }
}
