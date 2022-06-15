package com.example.proyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.HashMap;
import java.util.Map;

public class Adaptador extends FirestoreRecyclerAdapter<Clase, Adaptador.viewHolder> implements View.OnClickListener {

    private OnItemClickListener listener;
    Context context;
    FirebaseAuth auth;
    private String id;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adaptador(@NonNull FirestoreRecyclerOptions<Clase> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Clase publicacion) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String iduser = user.getUid();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        holder.titulo.setText(publicacion.getTitulo());
        holder.imagen.setImageBitmap(AddActivity.base64ToBitmap(publicacion.getImagen()));
        holder.usuario.setText(publicacion.getUsuario());
        holder.mostrarid.setText(publicacion.getIdpublicacion());
        holder.mostrarlikes.setText(publicacion.getLikes());


        DocumentReference dr = firestore.collection("Publicacion").document(publicacion.getIdpublicacion());


        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                firestore.collection("Publicacion").document(publicacion.getIdpublicacion()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String titulo = documentSnapshot.getString("titulo");
                        String descripcion = documentSnapshot.getString("descripcion");
                        String imagen = documentSnapshot.getString("imagen");
                        String usuario = documentSnapshot.getString("usuario");
                        String idpublicacion = documentSnapshot.getString("idpublicacion");
                        String profilepic = documentSnapshot.getString("profilepic");
                        String likes = documentSnapshot.getString("likes");

                        int intlikes = Integer.parseInt(likes);
                        int sumlikes = intlikes + 1;
                        String likestring = Integer.toString(sumlikes);
                        holder.mostrarlikes.setText(likestring);

                        DocumentReference dr2 = firestore.collection(publicacion.getIdpublicacion()).document(iduser);
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("like", "si");
                        dr2.set(map2);


                        Map<String, Object> map = new HashMap<>();
                        map.put("titulo", titulo);
                        map.put("descripcion", descripcion);
                        map.put("imagen", imagen);
                        map.put("usuario", usuario);
                        map.put("profilepic", profilepic);
                        map.put("idpublicacion", idpublicacion);
                        map.put("likes", likestring);
                        dr.set(map);

                    }
                });

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                firestore.collection("Publicacion").document(publicacion.getIdpublicacion()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String titulo = documentSnapshot.getString("titulo");
                        String descripcion = documentSnapshot.getString("descripcion");
                        String imagen = documentSnapshot.getString("imagen");
                        String usuario = documentSnapshot.getString("usuario");
                        String idpublicacion = documentSnapshot.getString("idpublicacion");
                        String profilepic = documentSnapshot.getString("profilepic");
                        String likes = documentSnapshot.getString("likes");

                        int intlikes = Integer.parseInt(likes);
                        int sumlikes = intlikes - 1;
                        String likestring = Integer.toString(sumlikes);
                        holder.mostrarlikes.setText(likestring);

                        DocumentReference dr2 = firestore.collection(publicacion.getIdpublicacion()).document(iduser);
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("like", false);
                        dr2.set(map2);


                        Map<String, Object> map = new HashMap<>();
                        map.put("titulo", titulo);
                        map.put("descripcion", descripcion);
                        map.put("imagen", imagen);
                        map.put("usuario", usuario);
                        map.put("profilepic", profilepic);
                        map.put("idpublicacion", idpublicacion);
                        map.put("likes", likestring);
                        dr.set(map);

                    }
                });

            }
        });




    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_item_list, parent, false);
        view.setOnClickListener(this);
        return new viewHolder(view);
    }

    @Override
    public void onClick(View view) {

    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView titulo;
        TextView mostrarid;
        TextView descripcion;
        TextView usuario;
        TextView mostrarlikes;
        LikeButton likeButton;

        int position = getLayoutPosition();

        public viewHolder(@NonNull View itemView) {

            super(itemView);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            imagen = itemView.findViewById(R.id.Main_Activity_Imagen);
            titulo = itemView.findViewById(R.id.Main_Activity_Titulo);
            usuario = itemView.findViewById(R.id.Main_Activity_Usuario);
            likeButton = itemView.findViewById(R.id.btnFavoritos_MA);
            mostrarid = itemView.findViewById(R.id.ActivityMain_ID);
            mostrarlikes = itemView.findViewById(R.id.ActivityMain_Likes);
            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();


            DocumentReference dr = firestore.collection("Publicacion").document();
            String idpublicacion = dr.getId();

            String iduser = user.getUid();
            DocumentReference dr1 = firestore.collection(idpublicacion).document(iduser);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });

        }


    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

}

