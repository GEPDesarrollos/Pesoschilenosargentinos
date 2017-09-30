package com.gep.guille.pesoschilenosargentinos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.google.gson.Gson;

public class AgregarArticulo extends AppCompatActivity {
    EditText chile, argen, articulo;
    ImageView imagenArticulo;
    NroImagen nroImagen=new NroImagen(-1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_articulo);


        Appodeal.isLoaded(Appodeal.REWARDED_VIDEO);

        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            private Toast mToast;
            @Override
            public void onRewardedVideoLoaded() {
                showToast("onRewardedVideoLoaded");
            }
            @Override
            public void onRewardedVideoFailedToLoad() {
                showToast("onRewardedVideoFailedToLoad");
            }
            @Override
            public void onRewardedVideoShown() {
                showToast("onRewardedVideoShown");
            }
            @Override
            public void onRewardedVideoFinished(int amount, String name) {
                showToast(String.format("onRewardedVideoFinished. Reward: %d %s", amount, name));
            }
            @Override
            public void onRewardedVideoClosed(boolean finished) {
                showToast(String.format("onRewardedVideoClosed,  finished: %s", finished));
            }
            void showToast(final String text) {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                }
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
            }
        });

        getSupportActionBar().setTitle("Libreta de Artículos");


        Bundle b=this.getIntent().getExtras();
        String[] datosRecibidos=b.getStringArray("envioDatos");


        chile = (EditText) findViewById(R.id.editText3);
        argen = (EditText) findViewById(R.id.editText2);
        articulo = (EditText) findViewById(R.id.editText);
        imagenArticulo=(ImageView)findViewById(R.id.imageView);

        if(datosRecibidos[0].equals("Conversor")||datosRecibidos[0].equals("otro")) {

            chile.setText(datosRecibidos[2]);
            argen.setText(datosRecibidos[3]);
        }

        if(datosRecibidos[0].equals("ListaDeArticulos")) {
            chile.setText(datosRecibidos[2]);
            argen.setText(datosRecibidos[3]);
            AdaptadorGaleria adaptadorGaleria=new AdaptadorGaleria(this);

            int resID = Integer.parseInt(datosRecibidos[4]);
            imagenArticulo.setImageResource(resID);
            articulo.setText(datosRecibidos[5]);
        }

    }
    public void Agregando(View view){

        chile = (EditText) findViewById(R.id.editText3);
        argen = (EditText) findViewById(R.id.editText2);
        articulo = (EditText) findViewById(R.id.editText);

        Bundle b=this.getIntent().getExtras();
        String[] datosRecibidos=b.getStringArray("envioDatos");

        MetodosDB db = new MetodosDB(this);
        db.abrir();

        Float chilenos=null;
        Float argentinos=null;
        String descripcionArticulo="";
        String exito="";

        try{
            chilenos= Float.valueOf(chile.getText().toString());

            if(null!=argen.getText().toString()){
                     String ar=argen.getText().toString().replace(",",".");
                     argentinos = Float.parseFloat(ar);
            }else {
                Gson gson = new Gson();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String arrayCambio = prefs.getString("cambio", "default");

                if (arrayCambio != "default") {

                    String valoresCambio[] = gson.fromJson(arrayCambio, String[].class);

                    float cambioChile = (float) Double.valueOf(valoresCambio[1]).doubleValue();


                    argentinos = (chilenos / cambioChile);
                    Log.v("En ELSE, Argentinos",""+argentinos);

                }
            }
            if(""!=articulo.getText().toString()) {
                descripcionArticulo = articulo.getText().toString();
            }
        }catch (Exception e){
            exito="fallo";
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"ingrese los PRECIOS por favor!",Toast.LENGTH_SHORT).show();
                               }

        if (exito!="fallo") {
            if (datosRecibidos[0].equals("Conversor") || datosRecibidos[0].equals("otro")) {

                String mimagen = "i" + nroImagen.getNroImagen();
                int resID = getApplicationContext().getResources().getIdentifier(mimagen,
                        "drawable", getApplicationContext().getPackageName());
                mimagen = "" + resID;


                db.insertar(chilenos, argentinos, resID, descripcionArticulo);
            }
            if (datosRecibidos[0].equals("ListaDeArticulos")) {
                int id = Integer.parseInt(datosRecibidos[1]);

                if (nroImagen.getNroImagen() != -1) {
                    String mimagen = "i" + nroImagen.getNroImagen();
                    int resID = getApplicationContext().getResources().getIdentifier(mimagen,
                            "drawable", getApplicationContext().getPackageName());
                    db.actualizar(id, chilenos, argentinos, resID, descripcionArticulo);
                } else {
                    int resID = Integer.parseInt(datosRecibidos[4]);
                    db.actualizar(id, chilenos, argentinos, resID, descripcionArticulo);
                }

            }

            Intent i = new Intent(getApplicationContext(), ListaArticulos.class);
            startActivity(i);
        }

    }
    public void CambiarImagen(View v){
        Intent i = new Intent(getApplicationContext(),GaleriaImagenes.class);
        startActivityForResult(i,222);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                int position=data.getExtras().getInt("id");

                AdaptadorGaleria adaptadorGaleria=new AdaptadorGaleria(this);

                imagenArticulo=(ImageView)findViewById(R.id.imageView);
                imagenArticulo.setImageResource(adaptadorGaleria.mImagesIds[position]);

                nroImagen.setNroImagen(position+1);

            }
        }
    }

}
