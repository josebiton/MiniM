package com.task.shopmarket.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.task.shopmarket.R;
import com.task.shopmarket.databinding.ActivityMainBinding;
import com.task.shopmarket.views.auth.InicioSesionActivity;
import com.task.shopmarket.views.fragments.CuentaUsuarioFragment;
import com.task.shopmarket.views.fragments.InicioFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new InicioFragment());

        binding.bottomNavigation.setBackground(null);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.inicio) {
                replaceFragment(new InicioFragment());
            } else if (itemId == R.id.cuenta ) {
                replaceFragment(new CuentaUsuarioFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, fragment);
        fragmentTransaction.commit();
    }


}