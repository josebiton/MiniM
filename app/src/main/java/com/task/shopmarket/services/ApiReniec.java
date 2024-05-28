package com.task.shopmarket.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiReniec {
    private static final String BASE_URL = "https://api.apis.net.pe/v1/dni?numero=";
    public interface DniCallback {
        void onSuccess(String nombres, String apellitos);
        void onError(String error);
    }

    public static void consultarDni(String numeroDni, Context context, final DniCallback callback) {
        String url = BASE_URL + numeroDni;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nombres = response.getString("nombres");
                            String apellidoPaterno = response.getString("apellidoPaterno");
                            String apellidoMaterno = response.getString("apellidoMaterno");

                            String apellidos = apellidoPaterno + " " + apellidoMaterno;
                            callback.onSuccess(nombres, apellidos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error al parsear la respuesta del servidor");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error en la solicitud: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Code " + error.networkResponse.statusCode;
                        }
                        callback.onError(errorMessage);
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}