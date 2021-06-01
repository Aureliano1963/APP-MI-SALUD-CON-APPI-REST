package com.example.clinicamisalud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CAtendidas extends AppCompatActivity {
    String usuario,contraseña,datosCita;
    int idConsulta,idExped;
    ListView LisConsultas;
    ImageView imgBuscar;
    EditText edtIdExpediente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_atendidas);
        //obtener usuario y pass
        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getString("email");
        contraseña= bundle.getString("pass");

        LisConsultas=findViewById(R.id.LisConsultas);
        imgBuscar=findViewById(R.id.imgBuscar);
        edtIdExpediente=findViewById(R.id.edtIdExpediente);

    //    String citasatendidas = ("https://apiapps.misalud.com.sv/listarconsultas/" + usuario + "/" + contraseña + "/" + idExp);
      //  ListConsultas(citasatendidas);

        imgBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( edtIdExpediente.getText().toString().trim().length() > 0){
                    idExped = Integer.parseInt( edtIdExpediente.getText().toString() );
                    if(idExped>0){
                        //id expediente:   https://apiapp.misalud.com.sv/citas/ver_citas?user= mario.hernandez &pass= developer &idestado=&idsucursal=&idespecialidad=&idmedico=&idreservacita=&idexpediente=65818
                        // idReserva:   https://apiapp.misalud.com.sv/citas/ver_citas?user=user1.1&pass=quesi&idestado=&idsucursal=&idespecialidad=&idmedico=&idreservacita=41939&idexpediente=
                        String citasatendidas = ("https://apiapp.misalud.com.sv/citas/ver_citas?user=" + usuario + "&pass=" + contraseña + "&idestado=&idsucursal=&idespecialidad=&idmedico=&idreservacita=" +"&idexpediente="+ idExped);
                        ListConsultas(citasatendidas);
                    }else {
                        Toast.makeText(getApplicationContext(), "Ingrese un valor entero "+idConsulta, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
                }


            }
        });


        LisConsultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {

                datosCita =String.valueOf(adapterView.getAdapter().getItem(i));
             //   String idExp = ("https://apiapps.misalud.com.sv/listarconsultas/" + usuario + "/" + contraseña + "/" + idExped);
               // ObtenerId(idExp);
                String idExp = ("https://apiapp.misalud.com.sv/citas/ver_citas?user=" + usuario + "&pass=" + contraseña + "&idestado=&idsucursal=&idespecialidad=&idmedico=&idreservacita=" +"&idexpediente="+ idExped);
                ObtenerId(idExp);
            }
        });


    }


    public void ListConsultas(String URL){
        Log.i("url consulta api: ",""+URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String response1 = response.replace("citas","");
                String response2 = response1.replace(":[{",",");
                String response3 = response2.replace("},{",",");
                String response4 = response3.replace(":",",");
                String response5 = response4.replace("{","[");
                String response6 = response5.replace("}]}","]");
                if (response6.length()>0){
                    try {
                        JSONArray ag =new JSONArray(response6);
                        ListaConsultas(ag);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

    public void ListaConsultas(JSONArray ja){

        ArrayList<String> lista = new ArrayList<>();

        for(int i=0;i<ja.length();i+=48){
            try {
                String fecha =ja.getString(i+4);
                String Hora1 =ja.getString(i+6);
                String Hora=Hora1.replace(",",":");

                lista.add("Fecha:     " + fecha + "      Hora: "+Hora+" \nPaciente:  "+ja.getString(i+28)+"\nRegistro:  "+ja.getString(i+2)+" \nFicha:     "+ja.getString(i+20)+" \n           ");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        LisConsultas.setAdapter(adaptador);
        LisConsultas.setEmptyView(findViewById(R.id.emptyElement));

    }

 //
 public void ObtenerId(String URL){
     Log.i("url consulta api: ",""+URL);
     RequestQueue queue = Volley.newRequestQueue(this);
     StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             String response1 = response.replace("citas","");
             String response2 = response1.replace(":[{",",");
             String response3 = response2.replace("},{",",");
             String response4 = response3.replace(":",",");
             String response5 = response4.replace("{","[");
             String response6 = response5.replace("}]}","]");

             if (response6.length()>0){
                 try {
                     JSONArray ag =new JSONArray(response6);
                     IdCitas(ag);
                   //  Toast.makeText(getApplicationContext(), "Todos "+idConsulta, Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(CAtendidas.this, Consultarecetas.class);
                     intent.putExtra("email", usuario);
                     intent.putExtra("pass", contraseña);
                     intent.putExtra("idcons", idConsulta);
                     startActivity(intent);
                 } catch (JSONException e) {
                     e.printStackTrace();

                 }

             }

         }
     }, new Response.ErrorListener(){
         @Override
         public void onErrorResponse(VolleyError error) {

         }
     });

     queue.add(stringRequest);

 }

    public void IdCitas(JSONArray ja){

        ArrayList<String> lista = new ArrayList<>();

        for(int i=0;i<ja.length();i+=48){
            try {
                String fecha =ja.getString(i+4);
                String Hora1 =ja.getString(i+6);
                String Hora=Hora1.replace(",",":");

                String arregloObtenido=("Fecha:     " + fecha + "      Hora: "+Hora+" \nPaciente:  "+ja.getString(i+28)+"\nRegistro:  "+ja.getString(i+2)+" \nFicha:     "+ja.getString(i+20)+" \n           ");

                if(datosCita.equals(arregloObtenido)){
                    String idcitaSelec =ja.getString(i+2);
                    idConsulta =  Integer.parseInt(idcitaSelec);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}