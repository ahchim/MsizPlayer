package com.ahchim.android.simplemusicplayer;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ahchim on 2017-02-08.
 */

public class MessageUtil {
    /**
     * Android toast message를 받아서 보여줌 (짧게 뜸)
     */
    public static void toastShort(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Android toast message를 받아서 보여줌 (길게 뜸)
     */
    public static void toastLong(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
