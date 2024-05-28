package com.task.shopmarket.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.task.shopmarket.R;
import com.task.shopmarket.utils.LocalStorage;
import com.task.shopmarket.views.auth.InicioSesionActivity;

public class CuentaUsuarioFragment extends Fragment {

    LocalStorage localStorage;
    LinearLayout btn_cerrar_sesion;
    TextView nombre_usuario, correo_usuario;
    String nombreUsuario, correoUsuario;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta_usuario, container, false);

        localStorage = new LocalStorage(requireContext());
        nombre_usuario = view.findViewById(R.id.nombre_usuario);
        correo_usuario = view.findViewById(R.id.correo_usuario);
        btn_cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion);
        btn_cerrar_sesion.setOnClickListener(v -> cerrarSesion());
        mostrarDatosUusario();
        return view;
    }

    private void mostrarDatosUusario(){
        sharedPreferences = requireContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        nombreUsuario = sharedPreferences.getString("name_usuario", "");
        correoUsuario = sharedPreferences.getString("email_usuario", "");

        if (nombreUsuario != null && correoUsuario != null) {
            nombre_usuario.setText(nombreUsuario);
            correo_usuario.setText(correoUsuario);
        } else {
            Log.d("CuentaUsuarioFragment", "No se pudo obtener los datos del usuario");
        }
    }

    private void cerrarSesion(){
        localStorage.cerrarSesion();
        LocalStorage.eliminarCarrito(requireContext());
        startActivity(new Intent(requireActivity(), InicioSesionActivity.class));
        requireActivity().finish();
        requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}