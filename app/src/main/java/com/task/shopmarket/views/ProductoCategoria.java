package com.task.shopmarket.views;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.task.shopmarket.R;
import com.task.shopmarket.adapters.ProductoAdapter;
import com.task.shopmarket.models.Producto;
import com.task.shopmarket.services.ApiProducto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductoCategoria extends AppCompatActivity {


    RecyclerView rvProductosCategoria;
    ProductoAdapter pAdapter;
    TextView tv_titulo;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_categoria);
        int categoriaId = getIntent().getIntExtra("categoria_id", -1);
        String categoriaNombre = getIntent().getStringExtra("categoria_nombre");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable flechaPersonalizada = obtenerDrawableConColor(R.drawable.ic_atras, Color.BLACK);
            actionBar.setHomeAsUpIndicator(flechaPersonalizada);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tv_titulo = findViewById(R.id.tv_titulo);
        tv_titulo.setText(categoriaNombre);

        rvProductosCategoria = findViewById(R.id.rv_productos_categoria);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductoCategoria.this, 2);
        rvProductosCategoria.setLayoutManager(gridLayoutManager);
        pAdapter = new ProductoAdapter(new ArrayList<>(), this, null, "Productos");
        rvProductosCategoria.setAdapter(pAdapter);
        obtenerProductosPorCategoria(categoriaId);
    }

    private void obtenerProductosPorCategoria(int categoriaId) {

        ApiProducto.obtenerProductosCategoria(categoriaId, ProductoCategoria.this, new ApiProducto.ProductosCategoriaCallback() {
            @Override
            public void onSuccess(List<Producto> productos) {
                Collections.shuffle(productos);
                pAdapter.setProductos(productos);
                pAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProductoCategoria.this, error, Toast.LENGTH_SHORT).show();
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