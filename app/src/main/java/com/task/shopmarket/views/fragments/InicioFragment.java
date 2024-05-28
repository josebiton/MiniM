package com.task.shopmarket.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.task.shopmarket.views.CarritoActivity;
import com.task.shopmarket.R;
import com.task.shopmarket.adapters.CategoriaAdapter;
import com.task.shopmarket.adapters.ProductoAdapter;
import com.task.shopmarket.models.Carrito;
import com.task.shopmarket.models.Categoria;
import com.task.shopmarket.services.ApiCategoria;
import com.task.shopmarket.services.ApiProducto;
import com.task.shopmarket.utils.EscuchaCarrito;
import com.task.shopmarket.utils.LocalStorage;
import com.task.shopmarket.views.CategoriaActivity;
import com.task.shopmarket.views.ProductosActivity;
import com.task.shopmarket.views.auth.InicioSesionActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class InicioFragment extends Fragment implements EscuchaCarrito {
    private int contaCarrito = 0;
    private Toolbar toolbar;
    MenuItem carritoMenu;
    RecyclerView rv_productos, rv_categorias;
    private EscuchaCarrito escuchaCarrito;
    TextView contadorCarrito;
    CategoriaAdapter c_Adapter;
    ProductoAdapter p_Adapter;
    LinearLayout next_productos;
    View view, accionVer, next_categorias;
    ImageSlider sliderImagenes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inicio, container, false);
        setHasOptionsMenu(true);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        sliderImagenes = view.findViewById(R.id.sliderImagenes);
        cargarImagenSlider();

        rv_categorias = view.findViewById(R.id.rv_categoriasInicio);
        rv_categorias.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        c_Adapter = new CategoriaAdapter(new ArrayList<>(), getContext(), "Inicio");
        rv_categorias.setAdapter(c_Adapter);
        obtenerCategorias();

        next_categorias = view.findViewById(R.id.next_categorias);
        next_categorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CategoriaActivity.class);
                startActivity(intent);
            }
        });



        rv_productos = view.findViewById(R.id.rv_productosInicio);
        rv_productos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        p_Adapter = new ProductoAdapter(new ArrayList<>(), getContext(), contadorCarrito, "Inicio");
        p_Adapter.setEscuchaCarrito(this);
        rv_productos.setAdapter(p_Adapter);
        obtenerProductos();

        next_productos = view.findViewById(R.id.next_productos);
        next_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductosActivity.class);
                startActivity(intent);
            }
        });






        return view;
    }

    private void cargarImagenSlider() {

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable._1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable._2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable._3, ScaleTypes.FIT));

        sliderImagenes.setImageList(slideModels, ScaleTypes.FIT);
    }

    private void obtenerCategorias() {
        ApiCategoria.obtenerCategorias(getContext(), new ApiCategoria.CategoriasCallback() {
            @Override
            public void onSuccess(List<Categoria> listaCategorias) {
                c_Adapter.setCategorias(listaCategorias);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerProductos() {
        ApiProducto.obtenerProductos(getContext(), new ApiProducto.ProductosCallback() {
            @Override
            public void onSuccess(List<com.task.shopmarket.models.Producto> listaProductos) {
                Collections.shuffle(listaProductos);
                p_Adapter.setProductos(listaProductos);
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCarritoChange(List<Carrito> listaCarrito) {
        actualizarContadorCarrito(listaCarrito, contadorCarrito);
    }
    private void actualizarContadorCarrito(List<Carrito> listaCarrito, TextView contadorCarrito) {
        int contaCarrito = listaCarrito.size();
        if (contadorCarrito != null) {
            contadorCarrito.setText(String.valueOf(contaCarrito));
            contadorCarrito.setVisibility(contaCarrito > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        actualizarContadorCarrito(LocalStorage.obtenerCarrito(requireContext()), contadorCarrito);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inicio, menu);
        carritoMenu = menu.findItem(R.id.action_carrito);

        if(carritoMenu != null && carritoMenu.getActionView() != null) {
            accionVer = carritoMenu.getActionView();
            contadorCarrito = accionVer.findViewById(R.id.carrito_count);

            actualizarContadorCarrito(LocalStorage.obtenerCarrito(requireContext()), contadorCarrito);
            contadorCarrito.setVisibility(contadorCarrito.getText().toString().equals("0") ? View.GONE : View.VISIBLE);

            accionVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                    String authToken = sharedPreferences.getString("token_usuario", null);
                    if (authToken == null) {
                        Toast.makeText(getContext(), "Debe iniciar sesión para ver su carrito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), InicioSesionActivity.class));
                    } else {
                        abrirCarrito();
                    }
                }
            });
        }
    }

    private void abrirCarrito() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("token_usuario", null);
        if (authToken == null) {
            Toast.makeText(getContext(), "Debe iniciar sesión para ver su carrito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), InicioSesionActivity.class));
            return;
        }

        Intent intent = new Intent(requireContext(), CarritoActivity.class);
        startActivity(intent);
    }

}