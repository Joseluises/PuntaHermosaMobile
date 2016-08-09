package municipalidad.android.com.municipalidad;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import municipalidad.android.com.municipalidad.model.LoginModel;
import municipalidad.android.com.municipalidad.util.JsonConector;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String MY_PREFS_NAME = "PHPREFS";
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button _loginButton;
    private Button _signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.input_email);
        mPasswordView = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.sign_in_button);
        _signupButton = (Button) findViewById(R.id.register_button);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int idVecino= prefs.getInt("idVecino", 0);
        if (idVecino >0) {
            Intent intent = new Intent(LoginActivity.this, EventosActivity.class);
            startActivity(intent);
        }else{
            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegistrarUsuarioActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url ="http://phmobile-jbolodes.c9users.io/phalertmanagement/api/authentications";
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", email.toString().trim());
                        params.put("password", password.toString().trim());
                        JsonConector request = new JsonConector(Request.Method.POST, url, params,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "Success");
                                        Log.d(TAG,response.toString());
                                        Gson gson = new Gson();
                                        LoginModel modelo = gson.fromJson(response, LoginModel.class);
                                        onLoginSuccess(modelo);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        onLoginFailed();
                                        Log.d(TAG, "Failure",error);
                                    }
                                });
                        queue.add(request);

                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(LoginModel modelo) {
        _loginButton.setEnabled(true);
        Log.d(TAG,"RESULTADO "+modelo.isResult());
        if(modelo.isResult()){
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt("idVecino", modelo.getId());
            editor.apply();
            Intent intentAcciones = new Intent(LoginActivity.this, EventosActivity.class);
            startActivity(intentAcciones);
        }else{
            Toast.makeText(getBaseContext(), modelo.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Fallo conexion con Servicio authentications...", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError("Ingrese un email valido");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            mPasswordView.setError("Entre 4 a 20 caracteres");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }
}