package com.alexandracastrillonvalencia.practica7login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lenovo on 15/12/2015.
 */
public class databaseCreatorHelper extends SQLiteOpenHelper{
    private static String TABLE_NAME="registrousuariosroyal";
    final static String CREATE_TABLE="create table "+TABLE_NAME+" (id integer primary key autoincrement,"+" nombre text, apellido text, email text, contraseña text, codigotarjeta text)";

    public databaseCreatorHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
