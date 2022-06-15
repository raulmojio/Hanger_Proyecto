package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;

import com.example.proyecto.databinding.ActivityPerfilBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    private String id;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        id = auth.getCurrentUser().getUid();


    }

    @Override
    protected void onStart() {
        super.onStart();
        infousuario();

        binding.PerfilActivityButtonNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.buscador:
                        startActivity(new Intent(getApplicationContext(), BuscadorActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.foro:
                        startActivity(new Intent(getApplicationContext(), ForoActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.perfil:
                        return true;
                }
                return false;
            }
        });





        String text2 = "Cerrar sesión";
        String text = "Cambiar imagen de perfil";
        SpannableString ss = new SpannableString(text);
        SpannableString ss2 = new SpannableString(text2);


        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        ss2.setSpan(clickableSpan2, 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.PerfilActivityTVDeslog.setText(ss2);
        binding.PerfilActivityTVDeslog.setMovementMethod(LinkMovementMethod.getInstance());


        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getApplicationContext(), ImagenPerfilActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        };
        ss.setSpan(clickableSpan1, 0, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.PerfilActivityTVCambiarimagen.setText(ss);
        binding.PerfilActivityTVCambiarimagen.setMovementMethod(LinkMovementMethod.getInstance());


    }


    private void infousuario() {

            DocumentReference dr = firestore.collection("Users").document(id);
            dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (auth.getCurrentUser() != null) {

                        String user = value.getString("usuario");
                        String email = value.getString("email");
                        String imagen = value.getString("imagenperfil");

                        binding.PerfilActivityTextViewUsuario.setText(user);

                        Bitmap bitimag = base64ToBitmap(imagen);
                        binding.imagenperfil.setImageBitmap(bitimag);




                    }

                }
            });


    }

    public static Bitmap base64ToBitmap(String base) {
        String base64Image = base.split(",")[0];
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }


    }

