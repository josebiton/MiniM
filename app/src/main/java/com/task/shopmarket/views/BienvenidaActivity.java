package com.task.shopmarket.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.task.shopmarket.R;
import com.task.shopmarket.views.auth.InicioSesionActivity;

import java.util.ArrayList;
import java.util.List;

public class BienvenidaActivity extends AppCompatActivity {
    ImageSlider sliderShow;
    Button btnEmpezar;
    SharedPreferences sharedPreferences;
    String TokenUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        TokenUser = sharedPreferences.getString("token_usuario", null);
        if (TokenUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_bienvenida);


        sliderShow = findViewById(R.id.slider_bienvenida);
        btnEmpezar = findViewById(R.id.btn_empezar);
        cargarSlider();
    }

    private void cargarSlider(){
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.splash1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.splash2, ScaleTypes.FIT));
        sliderShow.setImageList(slideModels, ScaleTypes.FIT);
    }

    public void clickEmpezar(View view) {
        startActivity(new Intent(getApplicationContext(), InicioSesionActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

}