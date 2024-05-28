package com.task.shopmarket.views;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.task.shopmarket.R;
import com.task.shopmarket.adapters.CarritoAdapter;
import com.task.shopmarket.models.Carrito;
import com.task.shopmarket.utils.EscuchaCarrito;
import com.task.shopmarket.utils.LocalStorage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CarritoActivity extends AppCompatActivity implements EscuchaCarrito {

    private CarritoAdapter carritoAdapter;
    private TextView verTotal;
    private List<Carrito> listaCarrito;
    Toolbar toolbar;
    RecyclerView carrito_recycler;
    RelativeLayout carrito_vacio;
    LinearLayout check_montos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable flechaPersonalizada = obtenerDrawableConColor(R.drawable.ic_atras, Color.BLACK);
            actionBar.setHomeAsUpIndicator(flechaPersonalizada);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listaCarrito = LocalStorage.obtenerCarrito(this);

        carrito_vacio = findViewById(R.id.carrito_vacio);
        check_montos = findViewById(R.id.check_montos);

        carrito_recycler = findViewById(R.id.cart_recycler);
        carritoAdapter = new CarritoAdapter(listaCarrito, this);
        carrito_recycler.setAdapter(carritoAdapter);
        carrito_recycler.setLayoutManager(new LinearLayoutManager(this));

        verTotal = findViewById(R.id.cart_total);
        actualizarTotalPrecio();
        actualizarInterfazDeUsuario();

    }

    private void actualizarInterfazDeUsuario() {
        if (listaCarrito.isEmpty()) {
            carrito_vacio.setVisibility(View.VISIBLE);
            check_montos.setVisibility(View.GONE);
        } else {
            carrito_vacio.setVisibility(View.GONE);
            check_montos.setVisibility(View.VISIBLE);
        }
    }

    public void actualizarTotalPrecio() {
        double total = calcularTotal(listaCarrito);
        Locale localePeru = new Locale("es", "PE");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localePeru);
        String totalFormateado = currencyFormat.format(total);

        if (verTotal != null) {
            verTotal.setText(String.format("Total: %s", totalFormateado));
        } else {
            verTotal.setText(String.format("Total: %s", totalFormateado));
        }

        if (listaCarrito.isEmpty()) {
            carrito_vacio.setVisibility(View.VISIBLE);
            check_montos.setVisibility(View.GONE);
        }
    }

    private double calcularTotal(List<Carrito> listaCarrito) {
        double total = 0;
        for (Carrito item : listaCarrito) {
            String precioLimpio = item.getPrecio().replaceAll("[^\\d.]+", "");
            precioLimpio = precioLimpio.replaceFirst("\\.(?=.*\\.)", "");

            if (!precioLimpio.isEmpty()) {
                double precioComoDouble = Double.parseDouble(precioLimpio);
                total += item.getCantidad() * precioComoDouble;
            }
        }

        DecimalFormat formatoDosDecimales = new DecimalFormat("#.##");
        String totalFormateado = formatoDosDecimales.format(total);
        total = Double.parseDouble(totalFormateado);
        return total;
    }

    @Override
    public void onCarritoChange(List<Carrito> listaCarrito) {
        actualizarTotalPrecio();
        carritoAdapter.actualizarCarrito(listaCarrito);
        carritoAdapter.notifyDataSetChanged();
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

    public void btn_comprar(View view) {
        // CAPTURAR EL TOTAL DE LA COMPRA Y GUARDARLO EN EL LOCALSTORAGE
        double totalCompra = calcularTotal(listaCarrito);
        LocalStorage localStorage = new LocalStorage(this);
        localStorage.guardarTotalCompra(totalCompra);
        Toast.makeText(this, "Total de la compra: " + totalCompra, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ProcesoCompraActivity.class);
        startActivity(intent);
    }
}