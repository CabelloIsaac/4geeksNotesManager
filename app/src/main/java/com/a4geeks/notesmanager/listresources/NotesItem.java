package com.a4geeks.notesmanager.listresources;

/**
 * Clase para los Items de la lista de Notas
 */

public class NotesItem {

    String id, titulo, descripcion, categoria, completada, date;

    public NotesItem(String id, String categoria, String titulo, String descripcion, String completada, String date) {

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