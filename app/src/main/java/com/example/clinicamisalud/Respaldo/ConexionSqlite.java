package com.example.clinicamisalud.Respaldo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSqlite extends SQLiteOpenHelper {

    public ConexionSqlite(@Nullable Context context) {
        super(context, DatosConexion.NOMBREBD, null, DatosConexion.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String consultaSql = "CREATE TABLE user (Id_Usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Nombre TEXT, Contrasenia TEXT)";
        db.execSQL(consultaSql);

    }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
