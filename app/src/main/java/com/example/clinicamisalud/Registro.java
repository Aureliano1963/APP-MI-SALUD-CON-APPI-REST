package com.example.clinicamisalud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Registro extends AppCompatActivity {
    EditText name,user,pass;
    Button tnRegistrar;
    String nombre,usuario,contraseña,respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        name = findViewById(R.id.name);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        tnRegistrar = findViewById(R.id.tnRegistrar);

        tnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( name.getText().toString().trim().length() > 0 && user.getText().toString().trim().length() > 0 && pass.getText().toString().trim().length() > 0){
                    nombre= name.getText().toString().replaceAll(" ","%20");
                    usuario= user.getText().toString();
                    contraseña= pass.getText().toString();
            //api:    https://apiapp.misalud.com.sv/citas/crear_usuario?nombre=Prueba&usuario=user&clave=quesi
                    String crearnuevacita =( "https://apiapp.misalud.com.sv/citas/crear_usuario?nombre="+nombre+"&usuario="+usuario+"&clave="+contraseña);
                    NuevaCita(crearnuevacita);

                } else {
                    Toast.makeText(Registro.this, "COMPLETE TODOS LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    // para vereficar lo que retorna al crear nuevo usuario
    public void NuevaCita(String URL){
        Log.i("url consulta api: ",""+URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //modificando
                String response1 = response.replace("msg","");
                String response2 = response1.replace(":",",");
                String response5 = response2.replace("{","[");
                String response6 = response5.replace("}","]");


                if (response6.length()>0){
                    try {
                        JSONArray je =new JSONArray(response6);
                        CargarNuevaCita(je);
                        switch (respuesta) {
                            case "Usuario creado con exito":
                                Toast.makeText(Registro.this, "Usuario creado con exito ",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Registro.this, Login.class);
                                startActivity(intent);
                                break;
                            case "Usuario ya existe":
                                Toast.makeText(Registro.this, "Usuario ya existe",Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                break;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                 // Toast.makeText(Cita.this, "Complete todos lo campos",Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

    }

    public void CargarNuevaCita(JSONArray je){

        ArrayList<String> lista = new ArrayList<>();

        for(int i=0;i<je.length();i+=1){
            try {
                String resultado=je.getString(i+1);
                respuesta=resultado;
                lista.add(resultado);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}