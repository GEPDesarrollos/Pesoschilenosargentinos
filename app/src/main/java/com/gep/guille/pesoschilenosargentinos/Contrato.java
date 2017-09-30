package com.gep.guille.pesoschilenosargentinos;

import android.provider.BaseColumns;



public final class Contrato {

    public Contrato() {
    }

    public static class Datas implements BaseColumns{

        public static final String PESOS_CHILENOS ="pesosChilenos";
        public static final String PESOS_ARGENTINOS ="pesosArgentinos";
        public static final String ICONO ="icono";
        public static final String DESCRIPCION ="descripcion";

    }
}
