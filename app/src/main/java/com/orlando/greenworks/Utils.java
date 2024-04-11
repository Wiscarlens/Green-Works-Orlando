package com.orlando.greenworks;

import android.content.Context;

public class Utils {
    public static int getImageResId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }
}
