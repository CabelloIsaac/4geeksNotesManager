package com.a4geeks.notesmanager.listresources;

/**
 * Esta clase es utilizada para el almacenamiento de los datos de las notas mostradas en la lista en la
 * pantalla principal.
 *
 * Cada nota contiene un ID, Título, Descripción, Categoría, Si está completada y la fecha.
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