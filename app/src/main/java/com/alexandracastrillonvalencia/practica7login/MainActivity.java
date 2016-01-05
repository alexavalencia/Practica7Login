package com.alexandracastrillonvalencia.practica7login;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    SQLiteDatabase database1;
    private EditText usuario;
    private EditText contraseña;
    CallbackManager callbackManager;
    LoginButton loginButton;
    Intent inicio;
    private static final String TAG = "MainActivity";
    private  static final int RC_SING_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private TextView mStatusUser ,mStatusEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        fromstart();
        usuario=(EditText)findViewById(R.id.email);
        contraseña=(EditText)findViewById(R.id.contraseña);
        loginButton=(LoginButton)findViewById(R.id.login_button);
        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile email");

        //google
        mStatusUser =(TextView)findViewById(R.id.id_tvStatusUser);
        mStatusEmail=(TextView)findViewById(R.id.id_tvStatusEmail);
        findViewById(R.id.id_sign_in_button).setOnClickListener(this);
        findViewById(R.id.id_sign_out_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this/*FragmentActivity*/,this/*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();



        //recupero las preferencias
        SharedPreferences preferences=getSharedPreferences("miprefencia",Context.MODE_PRIVATE);
        String name=preferences.getString("email","");
        String pass=preferences.getString("pass","");
        // se comprueba el nombre y la clave
        String tabla="registrousuariosroyal";
        String[] columna={"nombre","apellido"};
        String[] selectionArgs={name, pass};
        String selection="email = ?"+" and "+"contraseña = ?";
        Cursor c=database1.query(tabla, columna, selection, selectionArgs, null, null, null);
        String resultado="";

        if (c.moveToFirst()){
            do {
                for (int i =0; i<c.getColumnCount();i++){
                    resultado=resultado+c.getString(i);
                }
            }while (c.moveToNext());
            Intent inicio=new Intent(this,InicioActivity.class);
            inicio.putExtra("nombrecompleto",resultado);
            startActivity(inicio);
        }

        if (AccessToken.getCurrentAccessToken()!=null){
            RequestData();
           // inicio = new Intent(MainActivity.this,InicioActivity.class);
           // startActivity(inicio);
            //finish();
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken()!=null){
                    mStatusUser.setText("");
                    mStatusEmail.setText("");
                    RequestData();

                   // inicio = new Intent(MainActivity.this,InicioActivity.class);
                    //startActivity(inicio);
                    //finish();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    private void fromstart() {
        databaseCreatorHelper help = new databaseCreatorHelper(this, "DATABASEROYAL", null,1);
        database1=help.getWritableDatabase();//hago que a base de datos sea modificable
       // Toast.makeText(this, "Nombre: " + help.getDatabaseName().toString(), Toast.LENGTH_SHORT).show();
    }


    public void inicio(View v) {
        String tabla="registrousuariosroyal";
        String[] columna={"nombre","apellido"};
        String[] selectionArgs={usuario.getText().toString(), contraseña.getText().toString()};
        String selection="email = ?"+" and "+"contraseña = ?";
        Cursor c=database1.query(tabla,columna,selection,selectionArgs,null,null,null);
        String resultado="";

        if (c.moveToFirst()){
            do {
                for (int i =0; i<c.getColumnCount();i++){
                    resultado=resultado+c.getString(i);
                }
            }while (c.moveToNext());
            Toast.makeText(this,resultado.toString(),Toast.LENGTH_LONG).show();
            usuario.setText("");
            contraseña.setText("");
            SharedPreferences setting = getSharedPreferences("Mipreferencia", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("email",usuario.getText().toString());
            editor.putString("pass",contraseña.getText().toString());
            editor.commit();
            inicio = new Intent(MainActivity.this,InicioActivity.class);
            inicio.putExtra("nombrecompleto",resultado);
            startActivity(inicio);
            finish();
        }else{
            resultado="No se encuentra registrado";
            Toast.makeText(this,resultado.toString(),Toast.LENGTH_LONG).show();
            usuario.setText("");
            contraseña.setText("");
        }


    }

    public void registro(View view){
        Intent intent=new Intent(this,RegistroActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

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
                    //Toast.makeText(getApplicationContext().getApplicationContext(),"valor insertado"+"correctamente",Toast.LENGTH_LONG).show();

                }

            }
        }else if (requestCode==RC_SING_IN){
           GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           handleSignInResult(result);
       }
    }

    public void RequestData(){
        GraphRequest request= GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {

                        String text = json.getString("name");
                        String crr = json.getString("email");
                        inicio = new Intent(MainActivity.this, InicioActivity.class);
                        inicio.putExtra("nombrecompleto", json.getString("name"));
                        Toast.makeText(getApplicationContext().getApplicationContext(), Html.fromHtml(text), Toast.LENGTH_SHORT).show();
                        mStatusUser.setText(json.getString("name"));
                        mStatusEmail.setText(crr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters= new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onClick(View boton) {
        switch (boton.getId()){
            case R.id.id_sign_in_button:
                signIn();
                break;
            case R.id.id_sign_out_button:
                signOut();
                break;
        }

    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    private void signIn(){
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent, RC_SING_IN);
    }
    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                updateUI(false);
            }


        });
    }

     void updateUI(boolean signIn) {
        if (signIn){
          //  Toast.makeText(MainActivity.this,"In", Toast.LENGTH_LONG).show();
            findViewById(R.id.id_sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.id_sign_out_button).setVisibility(View.VISIBLE);
        }else{
           // Toast.makeText(MainActivity.this,"no entra",Toast.LENGTH_LONG).show();
            mStatusUser.setText("");
            mStatusEmail.setText("");
            findViewById(R.id.id_sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.id_sign_out_button).setVisibility(View.GONE);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusUser.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            mStatusEmail.setText(getString(R.string.signed_in_fmt2, acct.getEmail()));
            updateUI(true);
        }else {
            updateUI(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()){
            Log.d(TAG, "Got cached ssign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult result) {
                    hideProgressDialog();
                    handleSignInResult(result);
                }
            });
        }
    }
    private void showProgressDialog(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
