package com.task.shopmarket.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.task.shopmarket.adapters.CategoriaAdapter;
import com.task.shopmarket.models.Categoria;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ApiCategoria {

    private ApiCategoria() {
        // Constructor privado para evitar la instanciación directa

    }



    public interface CategoriasCallback {
        void onSuccess(List<Categoria> categorias);

        void onError(String errorMessage);
    }

    public static void obtenerCategorias(Context context, CategoriasCallback callback) {
        String url = BaseURL.API_URL + "categorias";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Categoria> listaCategorias = parseJsonArray(response);
                        callback.onSuccess(listaCategorias);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error en la solicitud: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Code " + error.networkResponse.statusCode;
                        }
                        callback.onError(errorMessage + "\n" + error.toString());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public static List<Categoria> parseJsonArray(JSONArray jsonArray) {
        List<Categoria> listaCategorias = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Categoria categoria = new Categoria();
                categoria.setId(jsonArray.getJSONObject(i).getInt("categoria_id"));
                categoria.setNombre(jsonArray.getJSONObject(i).getString("nombre"));
                categoria.setImagen(jsonArray.getJSONObject(i).getString("imagen"));
                listaCategorias.add(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCategorias;
    }

    }

