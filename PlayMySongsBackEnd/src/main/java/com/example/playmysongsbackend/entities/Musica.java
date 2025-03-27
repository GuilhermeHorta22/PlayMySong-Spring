package com.example.playmysongsbackend.entities;

public class Musica {
    private String titulo,artista,estilo, filename;

    public Musica(String titulo, String artista, String estilo, String filename) {
        this.titulo = titulo;
        this.artista = artista;
        this.estilo = estilo;
        this.filename=filename;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
