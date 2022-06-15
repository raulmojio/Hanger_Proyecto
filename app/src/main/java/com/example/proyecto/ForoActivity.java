package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto.databinding.ActivityForoBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForoActivity extends AppCompatActivity {

    private ActivityForoBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    Adaptador adapter;
    private String id;
    private DatabaseReference databaseReference;
    private String numero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);
        binding = ActivityForoBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        id = auth.getCurrentUser().getUid();


        Query query = firestore.collection("Foro").orderBy("fecha", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ClaseForo> options = new FirestoreRecyclerOptions.Builder<ClaseForo>().setQuery(query, ClaseForo.class).build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<ClaseForo, ForoViewHolder>(options) {
            @NonNull
            @Override
            public ForoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_foro, parent, false);
                return new ForoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ForoViewHolder holder, int position, @NonNull ClaseForo model) {
                holder.tvpregunta.setText(model.getPregunta());
                holder.tvusuario.setText(model.getUsuario());
                holder.circleImageView.setImageBitmap(AddActivity.base64ToBitmap(model.getImagen()));


            }
        };

        binding.foroRecyclerviewLista.setHasFixedSize(true);
        binding.foroRecyclerviewLista.setLayoutManager(new LinearLayoutManager(this));
        binding.foroRecyclerviewLista.setAdapter(adapter);

        binding.foroRecyclerviewLista.setLayoutManager(new LinearLayoutManager(this));

        binding.foroActivityButtonNavegacion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.buscador:
                        startActivity(new Intent(getApplicationContext(), BuscadorActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.foro:
                        return true;

                    case R.id.perfil:
                        startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

            binding.subirforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference dr = firestore.collection("Users").document(id);
                dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (auth.getCurrentUser() != null) {
                            String user = value.getString("usuario");
                            String imagen = value.getString("imagenperfil");
                            String pregunta = binding.Editextforo.getText().toString();

                            DocumentReference dr2 = firestore.collection("Foro").document();
                            Map<String, Object> map = new HashMap<>();
                            map.put("usuario", user);
                            map.put("imagen", imagen);
                            map.put("pregunta", pregunta);
                            map.put("fecha", FieldValue.serverTimestamp());
                            dr2.set(map);




                        }
                    }
                });

            }
        });

        adapter.startListening();
    }


    private class ForoViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private TextView tvpregunta;
        private TextView tvusuario;


        public ForoViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.ForoImagenPerfil);
            tvpregunta = itemView.findViewById(R.id.ForoPregunta);
            tvusuario = itemView.findViewById(R.id.ForoUsuario);

        }
    }


}