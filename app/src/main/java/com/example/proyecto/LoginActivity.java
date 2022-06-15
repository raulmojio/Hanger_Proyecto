package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto.databinding.ActivityLoginBinding;
import com.example.proyecto.databinding.ActivityPerfilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private String email = "";
    private String contraseña = "";
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.splashtheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        auth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        intentregistro();
        binding.LoginActivityButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.LoginActivityEditTextCorreo.getText().toString();
                contraseña = binding.LoginActivityEditTextContraseA.getText().toString();


                if (!email.isEmpty() && !contraseña.isEmpty()){
                    login();

                }
                else{
                    if (email.isEmpty()){
                        binding.LayoutCorreo.setError("Complete los campos");

                    }
                    if (contraseña.isEmpty()){
                        binding.LayoutPassword.setError("Complete los campos");
                    }
                }

            }
        });

        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
    private void login() {
        auth.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void intentregistro() {
        String text2 = "¿Olvidaste tu contraseña?";
        String text = "Si no tiene cuenta, registrese aquí";
        SpannableString ss = new SpannableString(text);
        SpannableString ss2 = new SpannableString(text2);


        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getApplicationContext(), CambioPasswordActivity.class);
                startActivity(intent);
            }
        };
        ss2.setSpan(clickableSpan2, 0, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.TextViewcambiocontr.setText(ss2);
        binding.TextViewcambiocontr.setMovementMethod(LinkMovementMethod.getInstance());




        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
            }
        };
        ss.setSpan(clickableSpan1, 31, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.LoginActivityTextViewRegistro.setText(ss);
        binding.LoginActivityTextViewRegistro.setMovementMethod(LinkMovementMethod.getInstance());


    }


}
