package com.gep.guille.pesoschilenosargentinos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Conversor extends AppCompatActivity {
    private EditText chile;
    private TextView args,uss;
    private String fechaDeHoy;
    private String [] valoresCambio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversor);
        String appKey = "fba2a77704b4694aeec0bf3d0425249388e7b5e1264f17a0";
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(this, appKey, Appodeal.REWARDED_VIDEO);
    }

    public void Convierte (View view) {
        chile = (EditText) findViewById(R.id.editText4);
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(chile.getWindowToken(), 0);

        Date df = new java.util.Date();
        fechaDeHoy = new SimpleDateFormat("dd-MM-yyyy").format(df);


        Gson gson = new Gson();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String arrayCambio = prefs.getString("cambio", "default");

        try {

            if (arrayCambio != "default") {

                String valoresCambio[] = gson.fromJson(arrayCambio, String[].class);

                double cambioChile = Double.valueOf(valoresCambio[1]).doubleValue();
                double cambioDolar = Double.valueOf(valoresCambio[2]).doubleValue();
                double chilenos = Double.valueOf(chile.getText().toString()).doubleValue();

                if (valoresCambio[0].equals(fechaDeHoy)) {

                    double argentinos = (chilenos / cambioChile);
                    double dolares = (argentinos * cambioDolar);
                    MuestraResult(argentinos, dolares);
                } else {
                    if (isOnlineNet()) {
                        // Operaciones http
                        new LeerDeInternet().execute("http://www.floatrates.com/daily/ars.json");

                    } else {
                        Toast.makeText(this, "Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {

                if (isOnlineNet()) {
                    // Operaciones http
                    new LeerDeInternet().execute("http://www.floatrates.com/daily/ars.json");

                } else {
                    Toast.makeText(this, "Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"introduzca un Número",Toast.LENGTH_SHORT).show();
        }
    }

    public void AgregaArt(View view){
        chile = (EditText) findViewById(R.id.editText4);
        args=(TextView)findViewById(R.id.textView42);
        String chilean="";
        if(chile.getText().toString().equals(null)){
             chilean="0";
        }else{
             chilean=chile.getText().toString();
        }
        String argens= args.getText().toString();
        if(argens.equals("$_______")){
            argens="";
        }
        Intent intent;
        intent = new Intent(getApplicationContext(),AgregarArticulo.class);

        Bundle b=new Bundle();
        b.putStringArray("envioDatos", new String[]{"Conversor","0",chilean,argens,"0",""});
        intent.putExtras(b);

        startActivity(intent);


    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public class LeerDeInternet extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog = new ProgressDialog(Conversor.this);

        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Cargando... \n Por favor espere! ");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                URL url=new URL(urls[0]);

                inputStream = url.openStream();

                if(inputStream != null) {
                    BufferedReader buffer = new BufferedReader( new InputStreamReader(inputStream));
                    String line = "";

                    int i = 0;
                    while ((line = buffer.readLine()) != null) {
                        result += line;


                    }

                    inputStream.close();
                } else {

                    // ERROR;
                }

            } catch (Exception e) {
                // ERROR;
            }
            return result;
        }


        @Override
        protected void onPostExecute(String resultado) {

            // REMUEVO EL DIALOGO.
            dialog.dismiss();
            if(resultado==null){
                finish();
            }
            // En result está el texto que viene de Internet


            try {
                //    contenidoJson es tu string conteniendo el json.
                JSONObject mainObject = new JSONObject(resultado);
                Double dolarRate=mainObject.getJSONObject("usd").getDouble("rate");
                Double chileRate=mainObject.getJSONObject("clp").getDouble("rate");
                String valoresCambio[]={fechaDeHoy,chileRate.toString(),dolarRate.toString()};
                Gson gson=new Gson();
                String jsonValores= gson.toJson(valoresCambio);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("cambio", jsonValores);
                editor.apply();


                double cambioChile = Double.valueOf(valoresCambio[1]).doubleValue();
                double cambioDolar = Double.valueOf(valoresCambio[2]).doubleValue();
                double chilenos = Double.valueOf(chile.getText().toString()).doubleValue();


                    double argentinos = (chilenos/cambioChile);
                    double dolares = (argentinos * cambioDolar);

                MuestraResult(argentinos,dolares);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Parser", e.getMessage());
            }


        }
    }

    public void MuestraResult(double pesos, double dolar){


        TextView args=(TextView)findViewById(R.id.textView42);
        TextView uss=(TextView)findViewById(R.id.textView52);


        DecimalFormat df = new DecimalFormat("#.##");
        String pesoss=df.format(pesos);
        String dolars=df.format(dolar);

        args.setText(""+pesoss);
        uss.setText(""+dolars);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.agrega_articulo:
                TextView args=(TextView)findViewById(R.id.textView42);
                EditText chile=(EditText) findViewById(R.id.editText4);
                String argens= args.getText().toString();
                String chilean="";
                if(chile.getText().toString().equals(null)){
                     chilean="0";
                }else{
                    chilean=chile.getText().toString();
                }
                if(argens.equals("$_______")){
                    argens=null;
                }
                Intent intent;
                intent = new Intent(getApplicationContext(),AgregarArticulo.class);
                Bundle b=new Bundle();
                b.putStringArray("envioDatos", new String[]{"otro","0",chilean,argens,"",""});
                intent.putExtras(b);
                startActivity(intent);
                return true;
            case R.id.listado:
                Intent intent1;
                intent1 = new Intent(getApplicationContext(),ListaArticulos.class);
                startActivity(intent1);
                return true;

            case R.id.acercaD:
                Toast.makeText(this,"Aplicación hecha por GeP Desarrollos",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




