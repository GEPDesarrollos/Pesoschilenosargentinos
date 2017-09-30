package com.gep.guille.pesoschilenosargentinos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MetodosDB {

    private Context context;
    private DBHelper dbHelper;
    private static final String TABLE_NAME = "articulos";
    private static final String PESOS_CHILENOS ="pesosChilenos";
    private static final String PESOS_ARGENTINOS ="pesosArgentinos";
    private static final String ICONO ="icono";
    private static final String DESCRIPCION ="descripcion";
    private SQLiteDatabase db;


    public MetodosDB(Context context) {
        this.context = context;
    }

    public void abrir() {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public Cursor todos() {

        /**
         *  public Cursor query (String table,
         *  					String[] columns,
         *  					String selection,
         *  					String[] selectionArgs,
         *  					String groupBy,
         *  					String having,
         *  					String orderBy)
         */

        return db.query(TABLE_NAME, new String[]{"_id", PESOS_CHILENOS, PESOS_ARGENTINOS, ICONO, DESCRIPCION},
                null, null, null, null, DESCRIPCION);
    }

    public void insertar(Float pesos_chilenos, Float pesos_argentinos, int icono, String descripcion) {

        ContentValues valores = new ContentValues();

        valores.put(PESOS_CHILENOS, pesos_chilenos);
        valores.put(PESOS_ARGENTINOS, pesos_argentinos);
        valores.put(ICONO, icono);
        valores.put(DESCRIPCION, descripcion);

        db.insert(TABLE_NAME, null, valores);
    }

    public void actualizar(int id, Float pesos_chilenos, Float pesos_argentinos, int icono, String descripcion) {

        ContentValues valores = new ContentValues();

        valores.put(PESOS_CHILENOS, pesos_chilenos);
        valores.put(PESOS_ARGENTINOS, pesos_argentinos);
        valores.put(ICONO, icono);
        valores.put(DESCRIPCION, descripcion);
        /**
         * public int update (String table,
         * 						ContentValues values,
         * 						String whereClause,
         * 						String[] whereArgs)
         */

        db.update(TABLE_NAME, valores,"_id=" + String.valueOf(id), null);
    }

    public void eliminar(int id) {
        db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }

    public void cerrar() {
        dbHelper.close();
    }
}

