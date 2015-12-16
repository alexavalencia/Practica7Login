package com.alexandracastrillonvalencia.practica7login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {
    SQLiteDatabase database1;
    private EditText usuario;
    private EditText contraseña;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromstart();
        usuario=(EditText)findViewById(R.id.email);
        contraseña=(EditText)findViewById(R.id.contraseña);

    }

    private void fromstart() {
        databaseCreatorHelper help = new databaseCreatorHelper(this, "DATABASEROYAL", null,1);
        database1=help.getWritableDatabase();//hago que a base de datos sea modificable
        Toast.makeText(this, "Nombre: " + help.getDatabaseName().toString(), Toast.LENGTH_SHORT).show();
    }


    public void inicio(View v) {
        String tabla="registrousuariosroyal";
        String[] columna={"nombre","apellido"};
        String[] selectionArgs={usuario.getText().toString()};
        String selection="email = ?";
        Cursor c=database1.query(tabla,columna,selection,selectionArgs,null,null,null);
        String resultado="";
        if (c.moveToFirst()){
            do {
                for (int i =0; i<c.getColumnCount();i++){
                    resultado=resultado+c.getString(i);
                }
            }while (c.moveToNext());
        }else{
            resultado="No se encuentra registrado";
        }
        Toast.makeText(this,resultado.toString(),Toast.LENGTH_LONG).show();
    }

    public void registro(View view){
        Intent intent=new Intent(this,RegistroActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1){
            if (resultCode==1){
                String nombreR=data.getStringExtra("nombre1");
                String apellidoR=data.getStringExtra("apellido1");
                String emailR=data.getStringExtra("email1");
                String contraseñaR=data.getStringExtra("contraseña1");
                String codigoR=data.getStringExtra("codigotarjeta1");
                String tabla="registrousuariosroyal";
                ContentValues valor=new ContentValues();
                valor.put("nombre",nombreR);
                valor.put("apellido",apellidoR );
                valor.put("email",emailR );
                valor.put("contraseña",contraseñaR );
                valor.put("codigotarjeta", codigoR);
                if(database1.insert(tabla, null, valor)==-1){
                    Toast.makeText(getApplicationContext().getApplicationContext(),"Ocurrio un error"+"no se inserto el valor",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext().getApplicationContext(),"valor insertado"+"correctamente",Toast.LENGTH_LONG).show();

                }

            }
        }
    }

}
