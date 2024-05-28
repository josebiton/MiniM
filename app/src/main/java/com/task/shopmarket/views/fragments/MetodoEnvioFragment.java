package com.task.shopmarket.views.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.task.shopmarket.R;
import com.task.shopmarket.adapters.EnviosAdapter;
import com.task.shopmarket.models.MetodoEnvio;
import com.task.shopmarket.services.ApiMetodoEnvio;
import com.task.shopmarket.utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

public class MetodoEnvioFragment extends Fragment {

    View view;
    EnviosAdapter enviosAdapter;
    RecyclerView rv_metodo_envio;
    Button btn_siguiente_direcion;
    private MetodoEnvio metodoEnvioSeleccionado;
    LocalStorage localStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_metodo_envio, container, false);


        rv_metodo_envio = view.findViewById(R.id.rv_metodo_envio);
        rv_metodo_envio.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        enviosAdapter = new EnviosAdapter(new ArrayList<>(), getContext());
        enviosAdapter.setOnItemClickListener(new EnviosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MetodoEnvio metodoEnvio) {
                metodoEnvioSeleccionado = metodoEnvio;
                Toast.makeText(getContext(), "Elemento seleccionado: " + metodoEnvio.getNombre() + metodoEnvio.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        rv_metodo_envio.setAdapter(enviosAdapter);
        obtenerMetodoEnvio();

        btn_siguiente_direcion = view.findViewById(R.id.btn_siguiente_direcion);
        btn_siguiente_direcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (metodoEnvioSeleccionado != null) {
                    int idMetodoEnvio = metodoEnvioSeleccionado.getId();
                    double selectedPrecio = metodoEnvioSeleccionado.getPrecio();

                    localStorage = new LocalStorage(getContext());
                    localStorage.capturarMetodoEnvio(idMetodoEnvio, selectedPrecio);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new DireccionEnvioFragment()).commit();

                    // Ejemplo de mensaje para verificar que la lógica funciona
                    Toast.makeText(getContext(), "Continuar con ID: " + idMetodoEnvio + ", Precio: " + selectedPrecio, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Seleccione un método de envío antes de continuar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void obtenerMetodoEnvio() {
        Context context = getContext();
        ApiMetodoEnvio.obtenerMetodoEnvio(new ApiMetodoEnvio.MetodoEnvioCallback() {
            @Override
            public void onSuccess(List<MetodoEnvio> metodoEnvios) {
                Log.d("MetodoEnvioFragment", "Número de métodos de envío recibidos: " + metodoEnvios.size());
                enviosAdapter.setMetodoEnvioList(metodoEnvios);
                enviosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("MetodoEnvioFragment", "Error al obtener métodos de envío: " + errorMessage);
                Toast.makeText(getContext(), "Error al obtener métodos de envío. Por favor, inténtelo de nuevo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }, context);
    }


}