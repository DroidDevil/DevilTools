package com.droiddevil.deviltools.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings.Secure;

public class DeviceTools {

    /**
     * <p>
     * Get a unique device ID.
     * </p>
     * <p>
     * There are downsides: First, this is not 100% reliable on releases of
     * Android prior to 2.2 (“Froyo”). Also, there has been at least one
     * widely-observed bug in a popular handset from a major manufacturer, where
     * every instance has the same ANDROID_ID.
     * </p>
     * 
     * @param context
     *            The context.
     * @return A unique device ID.
     */
    public static String getUniqueDeviceId(Context context) {
        return Secure
                .getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * <p>
     * Get a unique ID based on the installation of an app.
     * </p>
     * <p>
     * If the app is uninstalled, this ID will we reset. This is ideal for
     * tracking individual installations.
     * </p>
     * 
     * @param context
     *            The context.
     * @return The unique app install ID, or null if one could not be generated.
     */
    public static String getUniqueAppInstallId(Context context) {
        final String INSTALL_NAME = "DD_DeviceTools_InstallID";
        String id = null;

        File installFile = new File(context.getFilesDir(), INSTALL_NAME);
        try {
            if (!installFile.exists()) {
                FileOutputStream out = new FileOutputStream(installFile);
                id = UUID.randomUUID().toString();
                out.write(id.getBytes());
                out.close();
            }

            RandomAccessFile f = new RandomAccessFile(installFile, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        } catch (Exception e) {
            // ignore
        }

        return id;
    }
    
    public static boolean isLandscape(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    
    public static boolean isTablet(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.smallestScreenWidthDp < 600;
    }
}
