package com.task.shopmarket.views.modal;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.task.shopmarket.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class YapeQrFragment extends DialogFragment {

    private ImageView qrImageView;
    private Button btnClose, btnDownload;
    TextView total_pago;
    SharedPreferences sharedPreferences;
    String totalCompra, precioEnvio,sumatotal ;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    public YapeQrFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yape, container, false);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        sharedPreferences = requireContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        totalCompra = sharedPreferences.getString("totalCompra", "");
        precioEnvio = sharedPreferences.getString("precioEnvio", "");

        qrImageView = view.findViewById(R.id.qrImageView);
        btnClose = view.findViewById(R.id.btnClose);
        btnDownload = view.findViewById(R.id.btnDownload);
        total_pago = view.findViewById(R.id.total_pago);
        sumatotal = decimalFormat.format(Double.parseDouble(totalCompra) + Double.parseDouble(precioEnvio));
        total_pago.setText("Total a pagar: S/ " + sumatotal);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.yape)).getBitmap();
        qrImageView.setImageBitmap(bitmap);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    downloadQrCode();
                    dismiss();
                } else {
                    requestStoragePermission();
                }
            }

            private boolean isStoragePermissionGranted() {
                return requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            }

            private void requestStoragePermission() {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(requireContext(), "Se necesita permiso para guardar el código QR", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        });



        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadQrCode();
            } else {
                Toast.makeText(requireContext(), "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void downloadQrCode() {
        Bitmap bitmap = ((BitmapDrawable) qrImageView.getDrawable()).getBitmap();
        String fileName = "yape_qr_code.png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(requireContext(), "Código QR descargado en: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            Log.d("QR", "Código QR descargado en: " + file.getAbsolutePath());

            // Agregar la imagen a la galería
            MediaScannerConnection.scanFile(
                    requireContext(),
                    new String[]{file.getAbsolutePath()},
                    new String[]{"image/png"},
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al descargar el código QR", Toast.LENGTH_SHORT).show();
        }
    }



}
