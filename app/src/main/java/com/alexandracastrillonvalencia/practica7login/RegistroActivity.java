package com.alexandracastrillonvalencia.practica7login;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistroActivity extends AppCompatActivity {
    private EditText nombre,apellido,correo,codigo,contraseña;
    private Button aceptar;
    SQLiteDatabase data2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nombre=(EditText)findViewById(R.id.name);
        apellido=(EditText)findViewById(R.id.apellido);
        correo=(EditText)findViewById(R.id.correo);
        contraseña=(EditText)findViewById(R.id.pass);
        codigo=(EditText)findViewById(R.id.codigo);



    }

    public void inicio(View view){
        Intent backData = new Intent(this,MainActivity.class);
        backData.putExtra("nombre1",nombre.getText().toString());
        backData.putExtra("apellido1", apellido.getText().toString());
        backData.putExtra("email1", correo.getText().toString());
        backData.putExtra("contraseña1", contraseña.getText().toString());
        backData.putExtra("codigotarjeta1", codigo.getText().toString());
        setResult(1, backData);
        finish();
       // Intent intent=new Intent(this,MainActivity.class);
        //startActivity(intent);



    }


}
