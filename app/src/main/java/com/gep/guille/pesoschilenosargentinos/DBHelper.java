package com.gep.guille.pesoschilenosargentinos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NOMBRE ="conversor";
    private final static String TABLA ="articulos";
    private final static int DBVERSION =1;

    public DBHelper(Context context) {
        super(context, DB_NOMBRE, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLA + " ( "
                + Contrato.Datas._ID+" INTEGER PRIMARY KEY , "
                + Contrato.Datas.PESOS_CHILENOS+" DECIMAL, "
                + Contrato.Datas.PESOS_ARGENTINOS+" DECIMAL, "
                + Contrato.Datas.ICONO+" INTEGER, "
                + Contrato.Datas.DESCRIPCION+" TEXT) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
