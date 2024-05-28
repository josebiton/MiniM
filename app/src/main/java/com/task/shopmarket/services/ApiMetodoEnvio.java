package com.task.shopmarket.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.task.shopmarket.models.MetodoEnvio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiMetodoEnvio {

    public interface MetodoEnvioCallback {
        void onSuccess(List<MetodoEnvio> metodoEnvios);
        void onError(String errorMessage);
    }

    public static void obtenerMetodoEnvio(MetodoEnvioCallback callback, Context context) {
        String url = BaseURL.API_URL + "metodo-envio-emp";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<MetodoEnvio> listaMetodoEnvio = parseJsonArray(response);
                        callback.onSuccess(listaMetodoEnvio);

                        Log.d("MetodoEnvio", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error en la solicitud: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Code " + error.networkResponse.statusCode;
                            Log.e("Error", errorMessage);
                        }
                        callback.onError(errorMessage + "\n" + error.toString());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public static List<MetodoEnvio> parseJsonArray(JSONArray jsonArray) {
        List<MetodoEnvio> listaMetodoEnvio = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int metodoenvio_id = jsonObject.getInt("metodo_envio_emp_id");
                String nombre = jsonObject.getString("metodo_envio_nombre");
                String descripcion = jsonObject.getString("metodo_envio_descripcion");
                double precio = jsonObject.getDouble("precio");
                MetodoEnvio metodoEnvio = new MetodoEnvio(metodoenvio_id, nombre, descripcion, precio);
                listaMetodoEnvio.add(metodoEnvio);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listaMetodoEnvio;
    }
}
