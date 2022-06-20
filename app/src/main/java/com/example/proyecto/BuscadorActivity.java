package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.proyecto.databinding.ActivityBuscadorBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BuscadorActivity extends AppCompatActivity{

    private ActivityBuscadorBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    Adaptador adapter;
    SearchView buscador;
    Context context;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);
        binding = ActivityBuscadorBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.BuscadorRecyclerviewLista.setLayoutManager(new LinearLayoutManager(this));
        binding.BuscadorActivityButtonNavegacion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.buscador:
                        return true;

                    case R.id.foro:
                        startActivity(new Intent(getApplicationContext(), ForoActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);

                        return true;

                    case R.id.perfil:
                        startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        if (auth.getCurrentUser() != null) {


            binding.BuscadorActivityButtonNavegacion.setSelectedItemId(R.id.buscador);
            Query query = firestore.collection("Publicacion");

            context = getApplicationContext();
            FirestoreRecyclerOptions<Clase> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Clase>().setQuery(query, Clase.class).build();
            adapter = new Adaptador(firestoreRecyclerOptions, context);
            adapter.notifyDataSetChanged();


            binding.simpleSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    processsearch(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    processsearch(s);
                    return false;
                }
            });
            binding.BuscadorRecyclerviewLista.setHasFixedSize(true);
            binding.BuscadorRecyclerviewLista.setLayoutManager(new LinearLayoutManager(this));

        }
    }


    private void processsearch(String s)
    {

        FirestoreRecyclerOptions<Clase> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Clase>().setQuery(FirebaseFirestore.getInstance().collection("Publicacion").orderBy("titulo").startAt(s).endAt(s+"\uf8ff"), Clase.class).build();

        adapter=new Adaptador(firestoreRecyclerOptions, context);
        adapter.startListening();
        adapter.setOnItemClickListener(new Adaptador.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Clase clase = documentSnapshot.toObject(Clase.class);
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getApplicationContext(), AmpliadoActivity.class);
                Bundle b = new Bundle();
                b.putString("id", id);
                intent.putExtras(b);
                //startActivity(intent);
            }
        });
        binding.BuscadorRecyclerviewLista.setAdapter(adapter);




    }





}