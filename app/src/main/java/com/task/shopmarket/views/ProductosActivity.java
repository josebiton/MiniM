package com.task.shopmarket.views;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.task.shopmarket.R;
import com.task.shopmarket.adapters.ProductoAdapter;
import com.task.shopmarket.models.Producto;
import com.task.shopmarket.services.ApiProducto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_productos;
    ProductoAdapter p_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable flechaPersonalizada = obtenerDrawableConColor(R.drawable.ic_atras, Color.BLACK);
            actionBar.setHomeAsUpIndicator(flechaPersonalizada);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        rv_productos = findViewById(R.id.rv_productos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductosActivity.this, 2);
        rv_productos.setLayoutManager(gridLayoutManager);
        p_Adapter = new ProductoAdapter(new ArrayList<>(), ProductosActivity.this, null, "Productos");
        rv_productos.setAdapter(p_Adapter);
        obtenerProductos();

    }

    private void obtenerProductos() {
        ApiProducto.obtenerProductos(ProductosActivity.this, new ApiProducto.ProductosCallback() {
            @Override
            public void onSuccess(List<Producto> productos) {
                Collections.shuffle(productos);
                p_Adapter.setProductos(productos);
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(ProductosActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Drawable obtenerDrawableConColor(@DrawableRes int resId, @ColorInt int color) {
        Drawable drawable = ContextCompat.getDrawable(this, resId);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
        return drawable;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}