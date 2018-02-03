package com.a4geeks.notesmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * En esta clase se especifican todas las constantes que se usarán en la base de datos
 * En las clases donde se haga uso de la base de datos del sistema, se hará uso de estas constantes
 * para mantener la consistencia en las palabras clave y evitar errores de consulta.
 */

public class DBNotesManager extends SQLiteOpenHelper {

    public static final String ID = "id";

    /*DATA BASE STRINGS*/
    public static final String DB_NAME = "db_notesmanager";
    //TABLE_USUARIOS
    public static final String USUARIO_TABLE_NAME = "usuarios";
    public static final String USUARIO_ID = ID;
    public static final String USUARIO_TIPO = "tipo";
    public static final String USUARIO_EMAIL = "email";
    public static final String USUARIO_PASSWORD = "password";
    //TABLE_CATEGORIAS
    public static final String CATEGORIA_TABLE_NAME = "categorias";
    public static final String CATEGORIA_ID = ID;
    public static final String CATEGORIA_ID_USUARIO = "id_usuario";
    public static final String CATEGORIA_NOMBRE = "nombre";
    //TABLE_NOTAS
    public static final String NOTAS_TABLE_NAME = "notas";
    public static final String NOTAS_ID = ID;
    public static final String NOTAS_ID_CATEGORIA = "id_categoria";
    public static final String NOTAS_ID_USUARIO = "id_usuario";
    public static final String NOTAS_TITULO = "titulo";
    public static final String NOTAS_DESCRIPCION = "descripcion";
    public static final String NOTAS_COMPLETADA = "completada";
    public static final String NOTAS_DATE = "date";

    /*SENTENCIAS DE CREACIÓN DE TABLAS*/

    String TABLE_CATEGORIAS = "CREATE TABLE '" + CATEGORIA_TABLE_NAME + "' ( " +
            " '" + CATEGORIA_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " '" + CATEGORIA_ID_USUARIO + "' TEXT, " +
            " '" + CATEGORIA_NOMBRE + "' TEXT" +
            ");";

    String TABLE_USUARIOS = "CREATE TABLE '" + USUARIO_TABLE_NAME + "' ( " +
            " '" + USUARIO_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " '" + USUARIO_TIPO + "' TEXT, " +
            " '" + USUARIO_EMAIL + "' TEXT, " +
            " '" + USUARIO_PASSWORD + "' TEXT" +
            ");";

    String TABLE_NOTAS = "CREATE TABLE '" + NOTAS_TABLE_NAME + "' ( " +
            " '" + NOTAS_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " '" + NOTAS_ID_CATEGORIA + "' TEXT, " +
            " '" + NOTAS_ID_USUARIO + "' TEXT, " +
            " '" + NOTAS_TITULO + "' TEXT, " +
            " '" + NOTAS_DESCRIPCION + "' TEXT, " +
            " '" + NOTAS_COMPLETADA + "' TEXT, " +
            " '" + NOTAS_DATE + "' TEXT" +
            ");";

    public DBNotesManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Se ejecuta al ser instanciada por primera vez en la aplicación y luego no se ejecuta más.
     * Aquí se crean las tablas y entradas que deben de estar presentes al inicio de la aplicación.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USUARIOS);
        db.execSQL(TABLE_CATEGORIAS);
        db.execSQL(TABLE_NOTAS);
        db.execSQL("INSERT INTO categorias (id_usuario, nombre) VALUES ('all','Sin categoría')");
    }

    /**
     * Se llama para actualizar la base de datos sin tener que crearla de nuevo
     * @param sqLiteDatabase la base de datos a actuqlizar
     * @param i la version actual de la base de datos
     * @param i1 la nueva version de la base de datos
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
