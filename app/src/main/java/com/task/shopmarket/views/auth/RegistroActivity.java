package com.task.shopmarket.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.task.shopmarket.R;
import com.task.shopmarket.utils.CustomToast;
import com.task.shopmarket.utils.Utils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText register_name, register_email, register_password,register_phone;
    Button registerBtn;
    CheckBox terms_conditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        iniciarVariables();

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

    }
    public void iniciarVariables() {
        register_name = findViewById(R.id.register_name);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_phone = findViewById(R.id.register_phone);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);
        terms_conditions = findViewById(R.id.terms_conditions);
    }
    public void register_login(View view) {
        startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void checkValidation(){

        String getFullName = register_name.getText().toString();
        String getEmailId = register_email.getText().toString();
        String getMobileNumber = register_phone.getText().toString();
        String getPassword = register_password.getText().toString();

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        if (getFullName.length() == 0) {
            register_name.setError("Ingrese su Usuario");
            register_name.requestFocus();
        } else if (getEmailId.length() == 0) {
            register_email.setError("Ingresa tu e-mail");
            register_email.requestFocus();
        } else if (!m.find()) {
            register_email.setError("Ingresa un e-mail valido");
            register_email.requestFocus();
        } else if (getMobileNumber.length() == 0) {
            register_phone.setError("Ingresa tu numero de telefono");
            register_phone.requestFocus();
        } else if (getMobileNumber.length() != 9) {
            register_phone.setError("Ingresa un número de teléfono válido (9 dígitos)");
            register_phone.requestFocus();
        } else if (getPassword.length() == 0) {
            register_password.setError("Ingresa una contraseña");
            register_password.requestFocus();
        } else if (getPassword.length() < 6) {
            register_password.setError("La contraseña debe tener al menos 6 caracteres");
            register_password.requestFocus();
        } else if (!terms_conditions.isChecked()) {
            CustomToast.showError(RegistroActivity.this, "Acepta los terminos y condiciones");
        } else {
            enviarCodigo();
        }
    }

    public void enviarCodigo(){
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.INVISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+51" + register_phone.getText().toString(),
                60,
                TimeUnit.SECONDS,
                RegistroActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(RegistroActivity.this, "Error al verificar el número de teléfono: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(RegistroActivity.this, ConfirmacionOTP.class);
                        intent.putExtra("name", register_name.getText().toString().trim());
                        intent.putExtra("email", register_email.getText().toString().trim());
                        intent.putExtra("phone", register_phone.getText().toString().trim());
                        intent.putExtra("password", register_password.getText().toString().trim());
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }

                }
        );
    }

}