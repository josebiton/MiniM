package com.task.shopmarket.services;

import static com.task.shopmarket.services.BaseURL.API_URL;
import static com.task.shopmarket.services.BaseURL.URL_IMG;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.task.shopmarket.models.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiProducto {

    public interface ProductosCallback {
        void onSuccess(List<Producto> productos);

        void onError(String error);
    }

    public static void obtenerProductos(Context context, final ProductosCallback callback) {
        String url = API_URL + "productos";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Producto> productos = parseJsonArray(response);
                callback.onSuccess(productos);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error en la solicitud: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Code " + error.networkResponse.statusCode;
                        }
                        Log.e("ApiManager", errorMessage, error);  // Agrega un mensaje de log con el error
                        callback.onError(errorMessage + "\n" + error.toString());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public static List<Producto> parseJsonArray(JSONArray jsonArray) {
        List<Producto> listaProductos = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProducto = jsonArray.getJSONObject(i);

                Producto producto = new Producto();
                producto.setProducto_id(jsonProducto.getInt("producto_id"));
                producto.setProducto_nombre(jsonProducto.getString("producto_nombre"));
                producto.setPrecio(jsonProducto.getString("precio"));
                /*producto.setDescripcion(jsonProducto.getString("descripcion"));
                producto.setMarcaNombre(jsonProducto.getString("marca_nombre"));
                producto.setSlug(jsonProducto.getString("slug"));
                producto.setEmpresaNombre(jsonProducto.getString("empresa_nombre"));
                producto.setCategoriaNombre(jsonProducto.getString("categoria_nombre"));
                producto.setUnidadNombre(jsonProducto.getString("unidad_nombre"));
                producto.setStock(jsonProducto.getInt("stock"));*/

                // Imágenes
                JSONArray jsonImagenes = jsonProducto.getJSONArray("imagenes");
                List<String> imagenes = new ArrayList<>();
                for (int j = 0; j < jsonImagenes.length(); j++) {
                    String imageUrl = URL_IMG + jsonImagenes.getString(j);
                    imagenes.add(imageUrl);
                    Log.d("ApiManager", "Imagen parseada: " + imageUrl);
                }
                producto.setImagenes(imagenes);

                Log.d("Mesias", "Producto parseado: " + producto.toString());
                listaProductos.add(producto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ApiManager", "Error al parsear JSON: " + e.getMessage());  // Agrega un mensaje de log en caso de error
        }


        return listaProductos;
    }

    // TODO: Implementar método para obtener productos por categoría

    public interface ProductosCategoriaCallback {
        void onSuccess(List<Producto> productos);

        void onError(String error);
    }

    public static void obtenerProductosCategoria(int categoriaId,  Context context, final ProductosCategoriaCallback callback) {
        String url = API_URL + "productos-categoria/" + categoriaId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Producto> productos = parseJsonArray(response);
                callback.onSuccess(productos);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error en la solicitud: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Code " + error.networkResponse.statusCode;
                        }
                        Log.e("ApiManager", errorMessage, error);  // Agrega un mensaje de log con el error
                        callback.onError(errorMessage + "\n" + error.toString());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public static List<Producto> parseJsonArrayCategoria(JSONArray jsonArray) {
        List<Producto> listaProductos = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonProducto = jsonArray.getJSONObject(i);

                Producto producto = new Producto();
                producto.setProducto_id(jsonProducto.getInt("producto_id"));
                producto.setProducto_nombre(jsonProducto.getString("producto_nombre"));
                producto.setPrecio(jsonProducto.getString("precio"));
                /*producto.setDescripcion(jsonProducto.getString("descripcion"));
                producto.setMarcaNombre(jsonProducto.getString("marca_nombre"));
                producto.setSlug(jsonProducto.getString("slug"));
                producto.setEmpresaNombre(jsonProducto.getString("empresa_nombre"));
                producto.setCategoriaNombre(jsonProducto.getString("categoria_nombre"));
                producto.setUnidadNombre(jsonProducto.getString("unidad_nombre"));
                producto.setStock(jsonProducto.getInt("stock"));*/

                // Imágenes
                JSONArray jsonImagenes = jsonProducto.getJSONArray("imagenes");
                List<String> imagenes = new ArrayList<>();
                for (int j = 0; j < jsonImagenes.length(); j++) {
                    String imageUrl = URL_IMG + jsonImagenes.getString(j);
                    imagenes.add(imageUrl);
                    Log.d("ApiManager", "Imagen parseada: " + imageUrl);
                }
                producto.setImagenes(imagenes);

                Log.d("Mesias", "Producto parseado: " + producto.toString());
                listaProductos.add(producto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ApiManager", "Error al parsear JSON: " + e.getMessage());  // Agrega un mensaje de log en caso de error
        }
        return listaProductos;
    }
}
