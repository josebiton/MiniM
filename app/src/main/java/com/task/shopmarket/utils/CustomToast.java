package com.task.shopmarket.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.task.shopmarket.R;

public class CustomToast {

    public static void showError(Context context, String message) {
        showToast(context, message, android.R.drawable.ic_dialog_alert, "#EBFFD7", R.color.alert_warning, R.color.alert_error);
    }

    public static void showSuccess(Context context, String message) {
        showToast(context, message, R.drawable.ic_check, "#2ecc71", R.color.alert_success, R.color.alert_info);
    }

    private static void showToast(Context context, String message, int iconRes, String backgroundColor, int iconTint, int textColor) {
        try {
            // Inflar el diseño personalizado
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.custom_toast, null);

            // Configurar el mensaje y el icono
            TextView textView = layout.findViewById(R.id.ErrorMessage);
            textView.setText(message);
            textView.setTextColor(ContextCompat.getColor(context, textColor));

            ImageView imageView = layout.findViewById(R.id.imageView);
            imageView.setImageResource(iconRes);
            imageView.setColorFilter(ContextCompat.getColor(context, iconTint), android.graphics.PorterDuff.Mode.SRC_IN);

            // Configurar el fondo
            LinearLayout toastLayout = layout.findViewById(R.id.toast_layout_root);
            toastLayout.setBackgroundColor(Color.parseColor(backgroundColor));

            // Crear y mostrar el Toast personalizado
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            // posicionar en el centro
            toast.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.FILL_HORIZONTAL, 0, 1130);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



