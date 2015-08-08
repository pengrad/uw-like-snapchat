package io.github.pengrad.uw_like_snapchat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * stas
 * 8/8/15
 */
public class NetworkStatus {

    public static Status checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return new Status(netInfo.getTypeName(), isOnline());
        } else {
            return new Status(null, false);
        }
    }

    private static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            // probably we are offline
            return false;
        }
    }

    public static class Status {
        public final String networkType;
        public final boolean isInternet;

        public Status(String networkType, boolean isInternet) {
            this.networkType = networkType;
            this.isInternet = isInternet;
        }
    }
}
