package com.example.proyecto;

public class ClaseForo {

    private String imagen;
    private String pregunta;
    private String usuario;

    private ClaseForo() {

    }

    private ClaseForo(String imagen, String pregunta, String usuario) {
        this.imagen = imagen;
        this.pregunta = pregunta;
        this.usuario = usuario;

    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
