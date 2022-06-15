package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyecto.databinding.ActivityAddBinding;
import com.example.proyecto.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    Context context;

    private ActivityAddBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    private StorageReference storage;
    private Uri selectedImageUri;
    private ImageView imagen;
    DocumentReference dr2;
    String usuario;
    String profilepic;
    String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        binding = ActivityAddBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        id = user.getUid();
        firestore = FirebaseFirestore.getInstance();



        binding.AddImageViewImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        binding.AddButtonSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String tit = binding.AddEditTextTitulo.getText().toString();
                String desc = binding.AddEditTextDescripcion.getText().toString();
                String imagen = binding.AddImageViewImagen.toString();


                if (!tit.isEmpty() && !desc.isEmpty() && !imagen.isEmpty()) {

                    DocumentReference dr = firestore.collection("Publicacion").document();
                    dr2 = firestore.collection("Users").document(id);
                    String titulo = binding.AddEditTextTitulo.getText().toString();
                    String descripcion = binding.AddEditTextDescripcion.getText().toString();
                    dr2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (auth.getCurrentUser() != null) {

                                usuario = value.getString("usuario");
                                profilepic = value.getString("imagenperfil");
                                try {
                                    Bitmap bimagen = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                                    String b64imag = bitmapToBase64(bimagen);
                                    String idpublicacion = dr.getId();
                                    String likes = "0";
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("titulo", titulo);
                                    map.put("descripcion", descripcion);
                                    map.put("imagen", b64imag);
                                    map.put("usuario", usuario);
                                    map.put("profilepic", profilepic);
                                    map.put("idpublicacion", idpublicacion);
                                    map.put("likes", likes);

                                    dr.set(map);

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Introduzca todos los campos", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        binding.ActivityMainButtonNavegaciN.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.buscador:
                        startActivity(new Intent(getApplicationContext(), BuscadorActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.foro:
                        startActivity(new Intent(getApplicationContext(), ForoActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.perfil:
                        startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    binding.AddImageViewImagen.setImageURI(selectedImageUri);


                }
            }
        }
    }

    public static Bitmap base64ToBitmap(String base) {
        String base64Image = base.split(",")[0];
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
    public static String bitmapToBase64(Bitmap bitmap) {


        final Bitmap scaledBitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}




