package com.a4geeks.notesmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase de creación de Base de Datos
 */

public class DBNotesManager extends SQLiteOpenHelper {

    public static final String ID = "id";

    /*DATA BASE STRINGS*/
    public static final String DB_NAME = "db_notesmanager";
    //TABLE_USUARIOS
    public static String USUARIO_TABLE_NAME = "usuarios";
    public static String USUARIO_ID = ID;
    public static String USUARIO_TIPO = "tipo";
    public static String USUARIO_EMAIL = "email";
    public static String USUARIO_PASSWORD = "password";
    //TABLE_CATEGORIAS
    public static String CATEGORIA_TABLE_NAME = "categorias";
    public static String CATEGORIA_ID = ID;
    public static String CATEGORIA_ID_USUARIO = "id_usuario";
    public static String CATEGORIA_NOMBRE = "nombre";
    //TABLE_NOTAS
    public static String NOTAS_TABLE_NAME = "notas";
    public static String NOTAS_ID = ID;
    public static String NOTAS_ID_CATEGORIA = "id_categoria";
    public static String NOTAS_ID_USUARIO = "id_usuario";
    public static String NOTAS_TITULO = "titulo";
    public static String NOTAS_DESCRIPCION = "descripcion";
    public static String NOTAS_COMPLETADA = "completada";
    public static String NOTAS_DATE = "date";

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USUARIOS);
        db.execSQL(TABLE_CATEGORIAS);
        db.execSQL(TABLE_NOTAS);
        db.execSQL("INSERT INTO categorias (id_usuario, nombre) VALUES ('all','Sin categoría')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
