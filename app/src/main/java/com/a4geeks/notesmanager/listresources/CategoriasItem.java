package com.a4geeks.notesmanager.listresources;

/**
 * Esta clase es utilizada para el almacenamiento de los datos de las categorías mostradas en la lista en la
 * pantalla principal.
 *
 * Cada nota contiene un ID y un Nombre.
 */


public class CategoriasItem {

    private String id, nombre;

    public CategoriasItem(String id, String nombre) {

        this.id = id;
        this.nombre = nombre;

    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

}