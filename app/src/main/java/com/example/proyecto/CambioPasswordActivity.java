package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto.databinding.ActivityCambioPasswordBinding;
import com.example.proyecto.databinding.ActivityForoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class CambioPasswordActivity extends AppCompatActivity {

    private ActivityCambioPasswordBinding binding;
    private String email;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_password);
        binding = ActivityCambioPasswordBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        mauth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.botoncambiarpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.EditTextCorreo.getText().toString();
                if(!email.isEmpty()) {
                    cambiocontraseña();
                }
                else{
                    binding.LayoutCorreo.setError("Complete los campos");
                }
            }
        });
    }

    private void cambiocontraseña() {
        mauth.setLanguageCode("es");
        mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Se ha enviado el correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), "No se pudo enviar el correo", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}