
package com.bj4.dev.flurryhelper.utils;

import com.bj4.dev.flurryhelper.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastHelper {
    public static void showToast(Context context, int txtRes, int iconRes) {
        showToast(context, context.getString(txtRes), iconRes);
    }

    public static void showToast(Context context, String text, int iconRes) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView txt = (TextView)layout.findViewById(R.id.toast_txt);
        ImageView img = (ImageView)layout.findViewById(R.id.toast_img);
        txt.setText(text);
        img.setImageResource(iconRes);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
