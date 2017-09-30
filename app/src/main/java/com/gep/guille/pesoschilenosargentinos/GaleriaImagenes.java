package com.gep.guille.pesoschilenosargentinos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GaleriaImagenes extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria_imagenes);

        getSupportActionBar().setTitle("Libreta de Artículos");

        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new AdaptadorGaleria(this));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), AgregarArticulo.class);
                i.putExtra("id", position);
                setResult(RESULT_OK, i);
                finish();
            }
        });


    }
}

