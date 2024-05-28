package com.task.shopmarket.services;

import static com.task.shopmarket.services.BaseURL.API_URL;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiPersonas {


    // TODO: Implementar el método para obtener una persona por su id
    public interface PersonaCallback {
        void onSuccess(JSONObject personaJson);
        void onError(String errorMessage);
    }

    public static void obtenerPersonaPorId(String personaId, Context context, PersonaCallback callback) {
        String  url = API_URL + "personas/" + personaId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error en la solicitud: " + error.getMessage());
                    }
                });

        // Agregar la solicitud a la cola de Volley
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public static void guardarPersona(String dni, String nombres, String apellidos, String email, String telefono, String direccion, String referencia, String envio, PersonaCallback personaCallback) {
        String url = API_URL + "/guardar_persona";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("dni", dni);
            jsonBody.put("nombres", nombres);
            jsonBody.put("apellidos", apellidos);
            jsonBody.put("email", email);
            jsonBody.put("telefono", telefono);
            jsonBody.put("direccion", direccion);
            jsonBody.put("referencia", referencia);
            jsonBody.put("envio", envio);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        personaCallback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        personaCallback.onError("Error en la solicitud: " + error.getMessage());
                    }
                });

        // Agregar la solicitud a la cola de Volley
        RequestQueue queue = Volley.newRequestQueue((Context) personaCallback);
        queue.add(request);
    }





}
