package com.gep.guille.pesoschilenosargentinos;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.gep.guille.pesoschilenosargentinos.Contrato.Datas.DESCRIPCION;
import static com.gep.guille.pesoschilenosargentinos.Contrato.Datas.ICONO;
import static com.gep.guille.pesoschilenosargentinos.Contrato.Datas.PESOS_ARGENTINOS;
import static com.gep.guille.pesoschilenosargentinos.Contrato.Datas.PESOS_CHILENOS;

public class ListaArticulos extends ListActivity {
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_articulos);

            final ListView listView = getListView();



        MetodosDB bbdd = new MetodosDB(this);

        bbdd.abrir();
        cursor = bbdd.todos();


        String[] from = new String[]{PESOS_CHILENOS,
                                     PESOS_ARGENTINOS,
                                     ICONO,
                                     DESCRIPCION};

        int[] to = new int[]{R.id.textView62,R.id.textView72,R.id.imagen,R.id.textView10};

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);

        listView.setAdapter(cursorAdapter);

        registerForContextMenu(listView);
        cursorAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Mantenga pulsado para ver menú",Toast.LENGTH_LONG).show();

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo)menuInfo;



            inflater.inflate(R.menu.menu_lista, menu);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

       AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();



        switch(item.getItemId())
        {
            case R.id.mercadoLibre:

                String detalle = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPCION));
                String linki = "http://listado.mercadolibre.com.ar/" + detalle.replace(" ", "-") + "_DisplayType_LF";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(linki));
                startActivity(intent);

                return true;
            case R.id.modificar:
                Intent i;
                i = new Intent(getApplicationContext(),AgregarArticulo.class);
                Bundle b=new Bundle();
                b.putStringArray("envioDatos", new String[]{"ListaDeArticulos",cursor.getString(cursor.getColumnIndexOrThrow(_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PESOS_CHILENOS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PESOS_ARGENTINOS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ICONO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPCION))});
                i.putExtras(b);

                startActivity(i);

                return true;

            case R.id.eliminar:
                int Id=Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(_ID)));
                MetodosDB db=new MetodosDB(this);
                db.abrir();
                db.eliminar(Id);
                Intent intent1;
                intent1 = new Intent(getApplicationContext(),ListaArticulos.class);
                startActivity(intent1);
                return true;
            case R.id.agregar:
                Intent intent2=new Intent(getApplicationContext(),AgregarArticulo.class);
                Bundle bu=new Bundle();
                bu.putStringArray("envioDatos", new String[]{"otro","","",null,"",""});
                intent2.putExtras(bu);
                startActivity(intent2);
                return true;
            case R.id.Conversor:
                Intent intent3=new Intent(getApplicationContext(),Conversor.class);
                startActivity(intent3);
                return true;


       }
        return super.onContextItemSelected(item);
    }


}
