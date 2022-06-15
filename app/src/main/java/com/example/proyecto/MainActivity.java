package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto.databinding.ActivityMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewreceta;
    private FirebaseAuth auth;
    Adaptador adapter;


    private ActivityMainBinding binding;
    FirebaseFirestore firestore;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if(auth.getCurrentUser() != null) {

            binding.ActivityMainRecyclerviewLista.setLayoutManager(new LinearLayoutManager(this));
            Query query = firestore.collection("Publicacion");

            FirestoreRecyclerOptions<Clase> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Clase>().setQuery(query, Clase.class).build();
            adapter = new Adaptador(firestoreRecyclerOptions);
            adapter.notifyDataSetChanged();
            adapter.setOnItemClickListener(new Adaptador.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Clase clase = documentSnapshot.toObject(Clase.class);
                    String id = documentSnapshot.getId();
                    Intent intent = new Intent(getApplicationContext(), AmpliadoActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id", id);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            binding.ActivityMainRecyclerviewLista.setAdapter(adapter);
            adapter.startListening();
        }
        else{
            binding.Mostarinfo.setText("Inicie sesión o registrese");

        }

        binding.ActivityMainButtonNavegaciN.setSelectedItemId(R.id.menu);
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
                        return true;

                    case R.id.perfil:
                        startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        binding.ActivityMainRecyclerviewLista.setHasFixedSize(true);
        binding.ActivityMainRecyclerviewLista.setLayoutManager(new LinearLayoutManager(this));



    }



    public void siguienteAdd(View view){
        if(auth.getCurrentUser() != null) {
            Intent siguienteRegistro = new Intent(this, AddActivity.class);
            startActivity(siguienteRegistro);
            overridePendingTransition(0, 0);

        }
        else{
            Toast.makeText(getApplicationContext(), "Debe iniciar sesión o registrarse", Toast.LENGTH_SHORT).show();
        }

    }






}

