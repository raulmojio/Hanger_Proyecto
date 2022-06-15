package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private String usuario = "";
    private String email = "";
    private String contraseña = "";
    private String contraseña2 = "";
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Context context;
    private FirebaseDatabase db;
    private DatabaseReference root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        db = FirebaseDatabase.getInstance();
        root = db.getReference();

        binding.RegistroButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prueba = binding.RegistroEditTextUsuario.getText().toString();
                root.setValue(prueba);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.RegistroButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = binding.RegistroEditTextUsuario.getText().toString();
                email = binding.RegistroEditTextCorreo.getText().toString();
                contraseña = binding.RegistroEditTextContraseA.getText().toString();
                contraseña2 = binding.RegistroEditTextContraseA2.getText().toString();

                if(usuario.isEmpty()){
                    binding.LayoutCorreo.setError("Complete el campo");
                }
                if(email.isEmpty()){
                    binding.LayoutCorreo.setError("Complete el campo");
                }
                if(contraseña.isEmpty()){
                    binding.LayoutCorreo.setError("Complete el campo");
                }
                if(contraseña2.isEmpty()){
                    binding.LayoutCorreo.setError("Complete el campo");
                }
              if(contraseña.equals(contraseña2)){
                  if(contraseña.length()>=6 ){
                      if(contraseña2.length()>=6 ){
                          registrarusuario();
                      }
                      else{
                          binding.LayoutPassword2.setError("La contraseña tiene que tener 6 o más carácteres");
                      }
                  }
                  else{
                      binding.LayoutPassword1.setError("La contraseña tiene que tener 6 o más carácteres");
                  }
              }
              else{
                  binding.LayoutPassword1.setError("Las contraseñas no son iguales");
              }
                }
        });
    }

    private void registrarusuario() {
        auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = auth.getCurrentUser();
                    String id = user.getUid();
                    DocumentReference dr = firestore.collection("Users").document(id);
                    context = getApplicationContext();

                    Bitmap bitimagen = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultprofile);
                    String imagendefault = bitmapToBase64(bitimagen);

                    Map<String, Object> map = new HashMap<>();
                    map.put("usuario", usuario);
                    map.put("email", email);
                    map.put("contraseña", contraseña);
                    map.put("imagenperfil", imagendefault);
                    dr.set(map);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();



                } else {
                    Toast.makeText(getApplicationContext(), "No se pudo registrar el usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static String bitmapToBase64(Bitmap bitmap) {


        final Bitmap scaledBitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

    }
}