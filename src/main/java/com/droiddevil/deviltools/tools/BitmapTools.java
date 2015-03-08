package com.droiddevil.deviltools.tools;

import com.droiddevil.deviltools.log.Log;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;

/**
 * Utility class useful for handling bitmaps.
 * 
 * @author tperrien
 * 
 */
public class BitmapTools {

    /**
     * Returns a constant value as defined by {@link ExifInterface}
     * 
     * @param path
     *            The path to the bitmap.
     * @return The image orientation or
     *         {@link ExifInterface#ORIENTATION_UNDEFINED} otherwise.
     */
    public static int getImageOrientation(String path) {
        int imageOrientation = ExifInterface.ORIENTATION_UNDEFINED;

        if (path.startsWith(ContentResolver.SCHEME_FILE)) {
            path = Uri.parse(path).getPath();
        }

        try {
            ExifInterface exif = new ExifInterface(path);
            imageOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, imageOrientation);
        } catch (Exception exception) {
            Log.e("BitmapHelper::getImageOrientation: exif failed on path["
                    + path + "]", exception);
        }

        return imageOrientation;
    }

    /**
     * Decode a bitmap from the given path, targeted at the given dimensions.
     * 
     * @param path
     *            The file path to the bitmap.
     * @param targetWidth
     *            The targeted width.
     * @param targetHeight
     *            The targeted height.
     * @param options
     *            The bitmap options used to decode the bitmap.
     * @return A bitmap.
     */
    public static Bitmap decodeBitmap(String path, int targetWidth,
            int targetHeight, BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        calcBitmapSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * Decode a bitmap from the given path, targeted at the given dimensions.
     * 
     * @param resources
     *            The resources.
     * @param id
     *            The resource ID.
     * @param path
     *            The file path to the bitmap.
     * @param targetWidth
     *            The targeted width.
     * @param targetHeight
     *            The targeted height.
     * @param options
     *            The bitmap options used to decode the bitmap.
     * @return A bitmap.
     */
    public static Bitmap decodeBitmap(Resources resources, int id,
            int targetWidth, int targetHeight, BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, id, options);
        calcBitmapSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, id, options);
    }

    /**
     * Calculate the nearest power of 2 sample size for the given Options and
     * target dimensions. This method sets the inSampleSize field of the given
     * Options.
     * 
     * @param options
     *            The bitmap options holding the bitmap's dimensions.
     * @param targetWidth
     *            The targeted width.
     * @param targetHeight
     *            The targeted height.
     */
    public static void calcBitmapSampleSize(BitmapFactory.Options options,
            int targetWidth, int targetHeight) {
        if ((targetWidth < 1) || (targetHeight < 1)) {
            return;
        }

        int sampleSize = 1;
        int nextWidth = options.outWidth >> 1;
        int nextHeight = options.outHeight >> 1;
        while (nextWidth > targetWidth && nextHeight > targetHeight) {
            sampleSize <<= 1;
            nextWidth >>= 1;
            nextHeight >>= 1;
        }

        options.inSampleSize = sampleSize;
    }

}
