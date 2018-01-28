package com.a4geeks.notesmanager.listresources;

/**
 * Clase para los Items de la lista de Categorías
 */

public class CategoriasItem {

    String id, nombre;

    public CategoriasItem(String id, String nombre) {

        this.id = id;
        this.nombre = nombre;

    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

}