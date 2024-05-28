package com.task.shopmarket.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.task.shopmarket.models.Carrito;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocalStorage {
    public static final String CLAVE_TOKEN = "token_usuario";
    public static final String CLAVE_ID = "id_usuario";
    public static final String CLAVE_NAME = "name_usuario";
    public static final String CLAVE_EMAIL = "email_usuario";
    public static final String CLAVE_PERSONA_ID = "persona_id_usuario";
    public static final String CLAVE_TELEFONO = "telefono_usuario";
    public static final String CLAVE_ROLE_ID = "role_id_usuario";


    /// Carrito
    private static final String CARRITO_PREF = "carrito_preferencias";
    private static final String CLAVE_CARRITO = "carrito";
    private static final String TOTAL_COMPRA = "totalCompra";


    // Direccion
    private static final String ID_METODO_PAGO = "metodoPago";
    private static final String ID_DIRECCION_ENVIO = "direccionEnvio";
    private static final String ID_METODO_ENVIO = "metodoEnvio";
    private static final String PRECIO_ENVIO = "precioEnvio";


   SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    public LocalStorage(Context contexto) {
        sharedPreferences = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }


    public void CapturarDatosUsuario(String token, String id, String name, String email, String personaId, String telefono, String roleId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLAVE_TOKEN, token);
        editor.putString(CLAVE_ID, id);
        editor.putString(CLAVE_NAME, name);
        editor.putString(CLAVE_EMAIL, email);
        editor.putString(CLAVE_PERSONA_ID, personaId);
        editor.putString(CLAVE_TELEFONO, telefono);
        editor.putString(CLAVE_ROLE_ID, roleId);
        editor.apply();
    }

    public void cerrarSesion() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CLAVE_TOKEN);
        editor.remove(CLAVE_ID);
        editor.remove(CLAVE_NAME);
        editor.remove(CLAVE_EMAIL);
        editor.remove(CLAVE_PERSONA_ID);
        editor.remove(CLAVE_TELEFONO);
        editor.remove(CLAVE_ROLE_ID);
        editor.apply();
    }

    // Metodos para el Carrito //
    public static void agregarProductoAlCarrito(Context context, Carrito producto) {
        List<Carrito> carrito = obtenerCarrito(context);
        carrito.add(producto);
        guardarCarrito(context, carrito);
    }

    public static void guardarCarrito(Context context, List<Carrito> carrito) {
        SharedPreferences preferences = context.getSharedPreferences(CARRITO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String carritoJson = gson.toJson(carrito);
        editor.putString(CLAVE_CARRITO, carritoJson);
        editor.apply();
    }

    public static List<Carrito> obtenerCarrito(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CARRITO_PREF , Context.MODE_PRIVATE);
        String carritoJson = preferences.getString(CLAVE_CARRITO, null);

        if (carritoJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Carrito>>() {}.getType();
            return gson.fromJson(carritoJson, type);
        } else {
            return new ArrayList<>();
        }
    }

  /* public static void eliminarProductoDelCarrito(Context context, Carrito producto) {
        List<Carrito> carrito = obtenerCarrito(context);
        Carrito productoEnCarrito = null;
        for (Carrito item : carrito) {
            if (item.getId() == producto.getId()) {
                productoEnCarrito = item;
                break;
            }
        }

        if (productoEnCarrito != null) {
            if (productoEnCarrito.getCantidad() > 1) {
                productoEnCarrito.setCantidad(productoEnCarrito.getCantidad() - 1);
            } else {
                carrito.remove(productoEnCarrito);
            }

            guardarCarrito(context, carrito);
        }
    }*/

   public static void eliminarProductoDelCarrito(Context context, Carrito producto) {
        List<Carrito> carrito = obtenerCarrito(context);
        Carrito productoEnCarrito = null;
        for (Carrito item : carrito) {
            if (item.getId() == producto.getId()) {
                productoEnCarrito = item;
                break;
            }
        }

        if (productoEnCarrito != null) {
            carrito.remove(productoEnCarrito);
            guardarCarrito(context, carrito);
        }
    }




    public static void eliminarCarrito(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CARRITO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(CLAVE_CARRITO);
        editor.apply();
    }





    public void capturarMetodoPago( int idMetodoPago) {
        SharedPreferences.Editor editor = sharedPreferences.edit();;
        editor.putString(ID_METODO_PAGO, String.valueOf(idMetodoPago));
        editor.apply();
    }

    public void capturarDireccionEnvio( int idDireccion) {
        SharedPreferences.Editor editor = sharedPreferences.edit();;
        editor.putString(ID_DIRECCION_ENVIO, String.valueOf(idDireccion));
        editor.apply();
    }

    public void capturarMetodoEnvio(int idMetodoEnvio, double selectedPrecio) {
        SharedPreferences.Editor editor = sharedPreferences.edit();;
        editor.putString(ID_METODO_ENVIO, String.valueOf(idMetodoEnvio));
        editor.putString(PRECIO_ENVIO, String.valueOf(selectedPrecio));
        editor.apply();
    }

    public void guardarTotalCompra(double totalCompra) {
        SharedPreferences.Editor editor = sharedPreferences.edit();;
        editor.putString(TOTAL_COMPRA, String.valueOf(totalCompra));
        editor.apply();
    }

}
