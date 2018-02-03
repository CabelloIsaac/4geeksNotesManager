package com.a4geeks.notesmanager.listresources;

/**
 * Esta clase es utilizada para el almacenamiento de los datos de las notas mostradas en la lista en la
 * pantalla principal.
 *
 * Cada nota contiene un ID, Título, Descripción, Categoría, Si está completada y la fecha.
 */

public class NotesItem {

     String id;
     String titulo;
     String descripcion;
     String categoria;
     String completada;
     String date;

    /**
     * Constructor de la clase NotesItem
     * @param id
     * @param categoria
     * @param titulo
     * @param descripcion
     * @param completada
     * @param date
     */
    public NotesItem(String id, String categoria, String titulo, String descripcion, String completada, String date) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.completada = completada;
        this.date = date;

    }

    /**
     * Devuelve el ID del item
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el titulo del item
     * @return
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Devuelve la descripcion del item
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve la categoria del item
     * @return
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Devuelve el estado completado del item
     * @return
     */
    public String getCompletada() {
        return completada;
    }

    /**
     * Devuelve la fecha del item
     * @return
     */
    public String getDate() {
        return date;
    }

}