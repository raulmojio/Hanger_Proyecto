package com.example.proyecto;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

public class Clase {

    private String titulo;
    private String imagen;
    private String usuario;
    private String idpublicacion;
    private String likes;

    public Clase(){

    }
    public Clase(String titulo, String imagen, String usuario, String idpublicacion, String likes) {
        this.titulo = titulo;
        this.usuario = usuario;
        this.imagen = imagen;
        this.idpublicacion = idpublicacion;
        this.likes = likes;

    }


    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdpublicacion() {
        return idpublicacion;
    }

    public void setIdpublicacion(String idpublicacion) {
        this.idpublicacion = idpublicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}



