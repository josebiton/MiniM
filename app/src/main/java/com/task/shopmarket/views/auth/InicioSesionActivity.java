package com.task.shopmarket.views.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.task.shopmarket.services.ServiceAuth;
import com.task.shopmarket.utils.CustomToast;
import com.task.shopmarket.utils.LocalStorage;
import com.task.shopmarket.R;
import com.task.shopmarket.utils.Utils;
import com.task.shopmarket.views.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InicioSesionActivity extends AppCompatActivity {

    EditText login_email, login_password;
    Button loginBtn;
    ImageView close_activity;
    String getEmail, getPassword;
    ProgressDialog progressDialog;
    ServiceAuth serviceAuth;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        localStorage = new LocalStorage(getApplicationContext());
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        progressDialog = new ProgressDialog(InicioSesionActivity.this);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacionDeDatos();
            }
        });

        close_activity = findViewById(R.id.close_activity);
        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioSesionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validacionDeDatos (){
        getEmail = login_email.getText().toString();
        getPassword = login_password.getText().toString();
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmail);

        if (getEmail.isEmpty() || getPassword.isEmpty()) {
            CustomToast.showError(getApplicationContext(), "Todos los campos son obligatorios");
            vibracion(200);
        } else if (!m.find()) {
            CustomToast.showError(getApplicationContext(), "Correo electrónico inválido");
            vibracion(200);
        } else {
            login();

        }
    }

    public void login() {
        progressDialog.setMessage("Espere por favor....");
        progressDialog.show();

        Handler mHand = new Handler();
        mHand.postDelayed(new Runnable() {
            @Override
            public void run() {
                ServiceAuth serviceAuth = new ServiceAuth(InicioSesionActivity.this);
                serviceAuth.login(getEmail, getPassword, response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.has("status") && jsonObject.getString("status").equals("success")) {
                            String jwt = jsonObject.getString("Token");

                            // Obtener datos adicionales del usuario
                            JSONObject userData = jsonObject.getJSONObject("data");
                            String id = userData.getString("id");
                            String name = userData.getString("name");
                            String email = userData.getString("email");
                            String personaId = userData.getString("persona_id");
                            String telefono = userData.getString("telefono");
                            String roleId = userData.getString("role_id");

                            localStorage.CapturarDatosUsuario(jwt, id, name, email, personaId, telefono, roleId);

                            // Redirige a la actividad MainActivity
                            startActivity(new Intent(InicioSesionActivity.this, MainActivity.class));
                            InicioSesionActivity.this.finish();
                            InicioSesionActivity.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                        } else {
                            CustomToast.showError(InicioSesionActivity.this, jsonObject.optString("message", "Contraseña incorrecta"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        CustomToast.showError(InicioSesionActivity.this, "Error del servidor");
                    }
                    progressDialog.dismiss();
                }, error -> {
                    CustomToast.showError(InicioSesionActivity.this, "Error en la conexión de la APP");
                    progressDialog.dismiss();
                });
            }
        }, 2000);
    }

    public void vibracion(int duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
            } else {
                vibrator.vibrate(duration);
            }
        }
    }

    public void btnCrearCuenta(View view) {
        startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void btnOlvidarPassword(View view) {
        startActivity(new Intent(getApplicationContext(), OlvidarPasswordActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

}