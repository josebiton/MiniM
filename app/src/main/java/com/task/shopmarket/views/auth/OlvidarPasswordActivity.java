package com.task.shopmarket.views.auth;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.task.shopmarket.R;

public class OlvidarPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_password);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable flechaPersonalizada = obtenerDrawableConColor(R.drawable.ic_atras);
            actionBar.setHomeAsUpIndicator(flechaPersonalizada);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private Drawable obtenerDrawableConColor(@DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(this, resId);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN));
        }
        return drawable;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Finaliza la actividad actual
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
