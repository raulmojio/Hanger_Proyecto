package com.example.proyecto;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends FirestoreRecyclerAdapter<Clase, Adaptador.viewHolder> implements View.OnClickListener {

    private OnItemClickListener listener;
    FirebaseAuth auth;
    private String id;
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adaptador(@NonNull FirestoreRecyclerOptions<Clase> options, Context context) {
        super(options);
        this.context = context;

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
        holder.profilepic.setImageBitmap(AddActivity.base64ToBitmap(publicacion.getProfilepic()));


        DocumentReference dr = firestore.collection("Publicacion").document(publicacion.getIdpublicacion());
        DocumentReference dr2 = firestore.collection(publicacion.getIdpublicacion()).document(iduser);

        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String enlace = documentSnapshot.getString("descripcion");
                String text2 = "Ver en el sitio web";
                SpannableString ss2 = new SpannableString(text2);

                ClickableSpan clickableSpan2 = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(enlace));
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                };
                ss2.setSpan(clickableSpan2, 0, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvenlances.setText(ss2);
                holder.tvenlances.setMovementMethod(LinkMovementMethod.getInstance());

            }
        });

        firestore.collection(publicacion.getIdpublicacion()).document(iduser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String estado = documentSnapshot.getString("like");

                if(estado != null) {
                    if (estado.equals("liked")) {
                        holder.likeButton.setLiked(true);
                    }  if (estado.equals("unliked")) {
                        holder.likeButton.setLiked(false);
                    }
                }
                else{
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("like", "unliked");
                    dr2.set(map2);
                }
            }
        });






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

                        Map<String, Object> map = new HashMap<>();
                        map.put("titulo", titulo);
                        map.put("descripcion", descripcion);
                        map.put("imagen", imagen);
                        map.put("usuario", usuario);
                        map.put("profilepic", profilepic);
                        map.put("idpublicacion", idpublicacion);
                        dr.set(map);


                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("like", "liked");
                        dr2.set(map2);

                        likeButton.setLiked(true);


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

                        Map<String, Object> map = new HashMap<>();
                        map.put("titulo", titulo);
                        map.put("descripcion", descripcion);
                        map.put("imagen", imagen);
                        map.put("usuario", usuario);
                        map.put("profilepic", profilepic);
                        map.put("idpublicacion", idpublicacion);
                        dr.set(map);



                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("like", "unliked");
                        dr2.set(map2);
                        likeButton.setLiked(false);




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
        CircleImageView profilepic;
        LikeButton likeButton;
        TextView tvenlances;

        int position = getLayoutPosition();

        public viewHolder(@NonNull View itemView) {

            super(itemView);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            imagen = itemView.findViewById(R.id.Main_Activity_Imagen);
            profilepic = itemView.findViewById(R.id.Main_Activity_Profile_Pic);
            titulo = itemView.findViewById(R.id.Main_Activity_Titulo);
            usuario = itemView.findViewById(R.id.Main_Activity_Usuario);
            likeButton = itemView.findViewById(R.id.btnFavoritos_MA);
            mostrarid = itemView.findViewById(R.id.ActivityMain_ID);
            auth = FirebaseAuth.getInstance();
            tvenlances = itemView.findViewById(R.id.textviewenlaces);
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

