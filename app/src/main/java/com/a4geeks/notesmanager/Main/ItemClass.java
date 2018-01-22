package com.a4geeks.notesmanager.Main;

/**
 * Created by cabel on 21/1/2018.
 */

public class ItemClass {

    String id, titulo, descripcion, categoria;

    public ItemClass(String id, String titulo, String descripcion, String categoria) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;

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

}
