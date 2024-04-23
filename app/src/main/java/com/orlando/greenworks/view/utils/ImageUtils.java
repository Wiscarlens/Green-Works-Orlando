package com.orlando.greenworks.view.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class ImageUtils {
    public static int getImageResId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public static Drawable getDrawableFromName(Context context, String imageName) {
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return context.getResources().getDrawable(imageResId, null);
    }

    /**
     * Converts a byte array to a Drawable.
     *
     * @param imageData The image data in byte array format.
     * @param resources The resources object.
     * @return The Drawable object.
     */
    public static Drawable byteArrayToDrawable(byte[] imageData, Resources resources) {
        // Convert the image byte array to a Bitmap
        Bitmap itemImageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        // Convert the Bitmap to a Drawable if needed
        return new BitmapDrawable(resources, itemImageBitmap);
    }

    /**
     * Converts a Drawable object to a Bitmap.
     *
     * @param drawable The Drawable object to convert.
     * @return The Bitmap representation of the Drawable object.
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    // Store images as BLOBs
    public static byte[] getBitmapAsByteArray(int drawableId, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
