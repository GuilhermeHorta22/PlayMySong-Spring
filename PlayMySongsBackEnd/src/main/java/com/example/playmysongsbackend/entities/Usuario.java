package com.example.playmysongsbackend.entities;

public class Usuario
{
    private String fileName;
    private String nome;

    public Usuario(String fileName, String nome)
    {
        this.fileName = fileName;
        this.nome = nome;
    }

    public String getFileName()
    {
        return fileName;
    }
    public void setEmail(String fileName)
    {
        this.fileName = fileName;
    }

    public String getNome()
    {
        return nome;
    }
    public void setNome(String nome)
    {
        this.nome = nome;
    }
}
