package com.task.shopmarket.views;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.task.shopmarket.R;
import com.task.shopmarket.adapters.CategoriaAdapter;
import com.task.shopmarket.models.Categoria;
import com.task.shopmarket.services.ApiCategoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    ApiCategoria apiCategoria;
    RecyclerView rv_categorias;
    CategoriaAdapter c_Adapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable flechaPersonalizada = obtenerDrawableConColor(R.drawable.ic_atras, Color.BLACK);
            actionBar.setHomeAsUpIndicator(flechaPersonalizada);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        rv_categorias = findViewById(R.id.rv_categorias);
        rv_categorias.setLayoutManager(new LinearLayoutManager(CategoriaActivity.this, LinearLayoutManager.VERTICAL, false));
        c_Adapter = new CategoriaAdapter(new ArrayList<>(), CategoriaActivity.this, "Categoria");
        rv_categorias.setAdapter(c_Adapter);
        obtenerCategorias();

    }

    private void obtenerCategorias() {
        ApiCategoria.obtenerCategorias(CategoriaActivity.this, new ApiCategoria.CategoriasCallback() {
            @Override
            public void onSuccess(List<Categoria> listaCategorias) {
                c_Adapter.setCategorias(listaCategorias);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CategoriaActivity.this, error, Toast.LENGTH_SHORT).show();
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

