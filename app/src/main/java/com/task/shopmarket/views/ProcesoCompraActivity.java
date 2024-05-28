package com.task.shopmarket.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.task.shopmarket.R;
import com.task.shopmarket.views.fragments.DireccionEnvioFragment;
import com.task.shopmarket.views.fragments.MetodoEnvioFragment;

public class ProcesoCompraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_compra);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        ft.replace(R.id.fragment_container, new MetodoEnvioFragment());
        ft.commit();
    }
}