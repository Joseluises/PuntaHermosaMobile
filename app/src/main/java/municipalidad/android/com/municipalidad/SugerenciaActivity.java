package municipalidad.android.com.municipalidad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import municipalidad.android.com.municipalidad.util.JsonConector;

public class SugerenciaActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "PHPREFS";
    private static final String TAG = "SugerenciaActivity";
    Button btn_sug;
    private EditText des;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerencia);
        btn_sug=(Button)findViewById(R.id.enviasug_button);
        des = (EditText) findViewById(R.id.input_sug);
        btn_sug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                envio();
            }
        });
    }
    public void envio(){
        Log.d(TAG, "Envio de sugerencia");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Datos Invalidos.", Toast.LENGTH_LONG).show();
            return;
        }

        btn_sug.setEnabled(false);


        final String s = des.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url ="http://phmobile-jbolodes.c9users.io/phalertmanagement/api/suggestions";
                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        int idVecino= prefs.getInt("idVecino", 0);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("description", s);
                        params.put("neighbor_id", String.valueOf(idVecino));
                        JsonConector request = new JsonConector(Request.Method.POST, url, params,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "Success");
                                        Log.d(TAG,response.toString());
                                        Gson gson = new Gson();
                                        GenRspModel modelo = gson.fromJson(response, GenRspModel.class);
                                        btn_sug.setEnabled(true);

                                            Toast.makeText(getBaseContext(), modelo.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getBaseContext(), "Fallo conexion con Servicio suggestions...", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "Failure",error);
                            }
                        });
                        queue.add(request);

                    }
                }, 3000);
    }

    public boolean validate() {
        boolean valid = true;

        String s = des.getText().toString();
        if (s.isEmpty() || s.length() < 4 || s.length() > 200) {
            des.setError("Sugerencia debe ser entre 4 a 200 caracteres");
            valid = false;
        } else {
            des.setError(null);
        }

        return valid;
    }
}
