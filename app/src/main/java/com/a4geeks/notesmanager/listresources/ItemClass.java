package com.a4geeks.notesmanager.listresources;

/**
 * Created by cabel on 21/1/2018.
 */

public class ItemClass {

    String id, titulo, descripcion, categoria, completada, date;

    public ItemClass(String id, String categoria, String titulo, String descripcion, String completada, String date) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.completada = completada;
        this.date = date;

    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCompletada() {
        return completada;
    }

    public String getDate() {
        return date;
    }

}