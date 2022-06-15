package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;

import com.example.proyecto.databinding.ActivityImagenPerfilBinding;
import com.example.proyecto.databinding.ActivityLoginBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImagenPerfilActivity extends AppCompatActivity {

    private ActivityImagenPerfilBinding binding;
    int SELECT_PICTURE = 200;
    private FirebaseAuth auth;
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;
    FirebaseFirestore firestore;
    private Uri selectedImageUri;
    private String id;
    DocumentReference dr;
    private String usuario;
    private String contraseña;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_perfil);
        binding = ActivityImagenPerfilBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        setContentView(v);
        FirebaseUser user = auth.getCurrentUser();
        id = user.getUid();
        firestore = FirebaseFirestore.getInstance();
        dr = firestore.collection("Users").document(id);

        dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                usuario = value.getString("usuario");
                contraseña = value.getString("contraseña");
                email = value.getString("email");

                binding.guardarprofile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        try {
                            Bitmap bimagen = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                            String b64imag = bitmapToBase64(bimagen);

                            Map<String, Object> map = new HashMap<>();
                            map.put("imagenperfil", b64imag);
                            map.put("usuario", usuario);
                            map.put("contraseña", contraseña);
                            map.put("email", email);


                            dr.set(map);

                            Intent intent = new Intent(getApplicationContext(), PerfilActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });



        binding.cambioimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        binding.ImagenPerfilButtonNavegaciN.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    binding.cambioimagen.setImageURI(selectedImageUri);


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