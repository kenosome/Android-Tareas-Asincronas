package com.cali.uao.tareasasincronas;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {

    TextView output;
    Button btn_descarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output=(TextView) findViewById(R.id.output);
        btn_descarga= (Button) findViewById(R.id.btn_descarga);

        //TareaAsincrona tsk_reloj=new TareaAsincrona();
        //tsk_reloj.execute();

    }

    public void btn_descarga_onClick(View view) {
        String url="http://www.jorgesanchez.net/programacion/manuales/Java.pdf";


        TareaAsincrona descarga=new TareaAsincrona();
        descarga.execute(url);
    }

    public class TareaAsincrona extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            String url=params[0];

            try {
                URL objurl=new URL(url);
                URLConnection connection= objurl.openConnection();
                connection.connect();
                InputStream is= connection.getInputStream();

                File carpeta_descargas= new File(Environment.getExternalStorageDirectory()+"/descarga_asincrona");

                if(!carpeta_descargas.exists()){
                    carpeta_descargas.mkdirs();
                }

                File archivo=new File(carpeta_descargas+"/"+"manual.pdf");

                FileOutputStream fos=new FileOutputStream(archivo);

                int bytes_leidos;

                byte[] buffer=new byte[1024];

                while((bytes_leidos=is.read(buffer))!= -1){
                    fos.write(buffer,0,bytes_leidos);
                }

                is.close();

                fos.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return "OK";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            output.setText("Iniciando la descarga");
            btn_descarga.setText("Descargando...");
            btn_descarga.setEnabled(false);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("OK")){
                output.setText("Descarga finalizada.");
                btn_descarga.setText("Iniciar descarga");
                btn_descarga.setEnabled(true);

                Vibrator v= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                v.vibrate(1000);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


/*
    public class TareaAsincrona extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             reloj.setText("Ha Iniciado el proceso");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reloj.setText("Ha Finalizado el proceso");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    */



}
