package com.task.shopmarket.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.task.shopmarket.R;
import com.task.shopmarket.views.MainActivity;

public class ConfirmarFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.confirmacion_compra, container, false);

        // Manejar el cambio después de 5 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Cambiar a MainActivity después de 5 segundos
                startActivity(new Intent(getActivity(), MainActivity.class));
                // Asegúrate de que la actividad actual se cierre después de cambiar
                getActivity().finish();
            }
        }, 5000); // 5000 milisegundos = 5 segundos

        return view;
    }
}