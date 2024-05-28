package com.task.shopmarket.views.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.task.shopmarket.R;

import com.task.shopmarket.services.ServiceAuth;
import com.task.shopmarket.views.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ConfirmacionOTP extends AppCompatActivity {

    TextView otp_resend, otp_phone ;
    private EditText otp_1, otp_2, otp_3, otp_4, otp_5, otp_6;
    private boolean resendEnabled = false;
    private String verificationId;
    private final int segundos = 60;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_otp);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable flechaPersonalizada = obtenerDrawableConColor(R.drawable.ic_atras, Color.BLACK);
            actionBar.setHomeAsUpIndicator(flechaPersonalizada);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Mostrar el número de teléfono
        otp_phone = findViewById(R.id.otp_phone);
        otp_phone.setText(String.format("+51-%s", getIntent().getStringExtra("phone")));

        // Obtener los campos de texto
        otp_1 = findViewById(R.id.otp_1);
        otp_2 = findViewById(R.id.otp_2);
        otp_3 = findViewById(R.id.otp_3);
        otp_4 = findViewById(R.id.otp_4);
        otp_5 = findViewById(R.id.otp_5);
        otp_6 = findViewById(R.id.otp_6);

        setupOTPInputs();

        verificationId = getIntent().getStringExtra("verificationId");
        otp_resend = findViewById(R.id.otp_resend);
        cuentaAtras();
    }

    public void verifyBtn(View view) {
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button verifyBtn = findViewById(R.id.verifyBtn);

        if (otp_1.getText().toString().trim().isEmpty()
                || otp_2.getText().toString().trim().isEmpty()
                || otp_3.getText().toString().trim().isEmpty()
                || otp_4.getText().toString().trim().isEmpty()
                || otp_5.getText().toString().trim().isEmpty()
                || otp_6.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un código válido", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = otp_1.getText().toString() +
                otp_2.getText().toString() +
                otp_3.getText().toString() +
                otp_4.getText().toString() +
                otp_5.getText().toString() +
                otp_6.getText().toString();

        if (verificationId != null) {
            progressBar.setVisibility(View.VISIBLE);
            verifyBtn.setVisibility(View.INVISIBLE);
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        verifyBtn.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Log.println(Log.INFO, "INFO", "Codigo correcto");
                            registrarCuenta();
                        } else {
                            Toast.makeText(ConfirmacionOTP.this, "El codigo ingresado no es valido", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void registrarCuenta() {
        // Capturar los datos del usuario traidos de la actividad anterior
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String password = getIntent().getStringExtra("password");

        ServiceAuth serviceAuth = new ServiceAuth(this);
        serviceAuth.register(name, email, phone, password, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.has("status")) {
                    String status = jsonResponse.getString("status");
                    if (status.equals("success")) {
                        // Registro exitoso
                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

                        Log.println(Log.INFO, "INFO", "Usuario registrado correctamente");
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // Registro fallido (manejar según tus necesidades)
                        Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show();
                        Log.println(Log.INFO, "INFO", "Error al crear la cuenta");
                    }
                } else {
                    // La respuesta no contiene el campo "status"
                    Toast.makeText(this, "Respuesta incorrecta del servidor", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // Error al analizar la respuesta JSON
                e.printStackTrace();
                Toast.makeText(this, "Error al analizar la respuesta del servidor", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            // Error de conexión o error en la solicitud
            Toast.makeText(this, "Error en la solicitud al servidor", Toast.LENGTH_SHORT).show();
        });
    }

    private Drawable obtenerDrawableConColor(@DrawableRes int resId, @ColorInt int color) {
        Drawable drawable = ContextCompat.getDrawable(this, resId);
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
        return drawable;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cuentaAtras() {
        resendEnabled = false;
        otp_resend.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        final Context context = this;
        new CountDownTimer(segundos * 1000, 1000) {
            @Override
            public void onTick(long l) {
                otp_resend.setText("Reenviar OTP en " + l / 1000 + " seconds");
            }
            @Override
            public void onFinish() {
                resendEnabled = true;
                otp_resend.setTextColor(ContextCompat.getColor(context, R.color.colorLink));
                otp_resend.setText("Reenviar OTP");
            }
        }.start();
        otp_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reenviarOTP();
            }
        });
    }
    private void reenviarOTP() {
        otp_resend.setEnabled(false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+51" + getIntent().getStringExtra("mobile"),
                60,
                TimeUnit.SECONDS,
                ConfirmacionOTP.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        otp_resend.setEnabled(true);
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        otp_resend.setEnabled(true);
                        Toast.makeText(ConfirmacionOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otp_resend.setEnabled(true);
                        verificationId = newVerificationId;
                        Toast.makeText(ConfirmacionOTP.this, "OTP enviado", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    private void setupOTPInputs() {
        otp_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otp_1.getText().toString().trim().length() == 1) {
                    otp_2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (otp_2.getText().toString().trim().length() == 1) {
                   otp_3.requestFocus();
               }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (otp_3.getText().toString().trim().length() == 1) {
                   otp_4.requestFocus();
               }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (otp_4.getText().toString().trim().length() == 1) {
                   otp_5.requestFocus();
               }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        otp_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (otp_5.getText().toString().trim().length() == 1) {
                   otp_6.requestFocus();
               }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}