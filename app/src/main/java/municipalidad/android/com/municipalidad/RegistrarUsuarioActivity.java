package municipalidad.android.com.municipalidad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import municipalidad.android.com.municipalidad.model.GenRspModel;
import municipalidad.android.com.municipalidad.model.LoginModel;
import municipalidad.android.com.municipalidad.util.JsonConector;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "PHPREFS";
    private static final String TAG = "RegistroActivity";
    Button btn_registro;
    private EditText nombre;
    private EditText ape;
    private EditText correo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        btn_registro=(Button)findViewById(R.id.btn_signup);
        nombre = (EditText) findViewById(R.id.input_name);
        ape = (EditText) findViewById(R.id.input_lastname);
        correo = (EditText) findViewById(R.id.input_email);
        btn_registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registro();
            }
        });
    }

    public void registro(){
        Log.d(TAG, "Registro de actividad");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Datos Invalidos.", Toast.LENGTH_LONG).show();
            return;
        }

        btn_registro.setEnabled(false);


        final String e = correo.getText().toString();
        final String n = nombre.getText().toString();
        final String a = ape.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url ="http://phmobile-jbolodes.c9users.io/phalertmanagement/api/registrations";
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", e.trim());
                        params.put("first_name", n.trim());
                        params.put("last_name", a.trim());
                        JsonConector request = new JsonConector(Request.Method.POST, url, params,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "Success");
                                        Log.d(TAG,response.toString());
                                        Gson gson = new Gson();
                                        GenRspModel modelo = gson.fromJson(response, GenRspModel.class);
                                        btn_registro.setEnabled(true);
                                        if(modelo.isResult()){
                                            Intent intentAcciones = new Intent(RegistrarUsuarioActivity.this, LoginActivity.class);
                                            startActivity(intentAcciones);
                                        }else{
                                            Toast.makeText(getBaseContext(), modelo.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getBaseContext(), "Fallo conexion con Servicio registrations...", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Failure",error);
                            }
                        });
                        queue.add(request);

                    }
                }, 3000);
    }

    public boolean validate() {
        boolean valid = true;

        String e = correo.getText().toString();
        String n = nombre.getText().toString();
        String a = ape.getText().toString();

        if (e.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            correo.setError("Ingrese un email valido");
            valid = false;
        } else {
            correo.setError(null);
        }

        if (n.isEmpty() || n.length() < 4 || n.length() > 20) {
            nombre.setError("Nombre debe ser entre 4 a 20 caracteres");
            valid = false;
        } else {
            nombre.setError(null);
        }

        if (a.isEmpty() || a.length() < 4 || a.length() > 20) {
            ape.setError("Apellido debe ser entre 4 a 20 caracteres");
            valid = false;
        } else {
            ape.setError(null);
        }

        return valid;
    }
}
