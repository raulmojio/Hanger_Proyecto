package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.proyecto.databinding.ActivityAmpliadoBinding;
import com.example.proyecto.databinding.ActivityAmpliadoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmpliadoActivity extends AppCompatActivity {

    private ActivityAmpliadoBinding binding;
    private FirebaseFirestore firestore;
    private DocumentReference dr;
    private String id;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_ampliado);
        binding = ActivityAmpliadoBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widht = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (widht * .8), (int) (height * .6));


    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        id = auth.getCurrentUser().getUid();
        obtenerdatos();

    }

    private void obtenerdatos() {
        Intent in = getIntent();
        Bundle b = in.getExtras();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        String id = b.getString("id");
        FirebaseUser user = auth.getCurrentUser();
        String iduser = user.getUid();

        firestore.collection("Publicacion").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    FirebaseUser user = auth.getCurrentUser();
                    String iduser = user.getUid();

                    dr = firestore.collection("Publicacion").document(id);
                    String descripcion = documentSnapshot.getString("descripcion");
                    binding.Enlaces.setText(Html.fromHtml(descripcion));


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




