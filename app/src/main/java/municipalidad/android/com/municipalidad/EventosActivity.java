package municipalidad.android.com.municipalidad;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import municipalidad.android.com.municipalidad.util.GPSTracker;
import municipalidad.android.com.municipalidad.util.JsonConector;

public class EventosActivity extends AppCompatActivity {

    Button btn_emergencia;
    Button btn_even;
    Button btn_sug;
    private static final String MY_PREFS_NAME = "PHPREFS";

    private static final String TAG = "EventosActivity";
    GPSTracker gps;
    String[] types = {"Choque", "SOS", "Robo","Pandillas"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        btn_emergencia=(Button)findViewById(R.id.emergencia_button);
        btn_even=(Button)findViewById(R.id.eventos_button);
        btn_sug=(Button)findViewById(R.id.sugerencia_button);
        btn_emergencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(EventosActivity.this);
                b.setTitle("Lista de Emergencias");
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        enviaAlerta(which);
                    }
                });
                b.show();
            }
        });

        btn_even.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventosActivity.this, ListaEventosActivity.class);
                startActivity(intent);
            }
        });
        btn_sug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventosActivity.this, SugerenciaActivity.class);
                startActivity(intent);
            }
        });
    }
public void enviaAlerta(int indice){
    Log.d("PRUEBITA",types[indice]);
    gps = new GPSTracker(EventosActivity.this);
    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    int idVecino= prefs.getInt("idVecino", 0);
    if(gps.canGetLocation() && idVecino>0){

        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://phmobile-jbolodes.c9users.io/phalertmanagement/api/reports";
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", types[indice].toString().trim());
        params.put("longitude", String.valueOf(longitude));
        params.put("latitude", String.valueOf(latitude));
        params.put("neighbor_id", String.valueOf(idVecino));
        JsonConector request = new JsonConector(Request.Method.POST, url, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Success");
                        Log.d(TAG,response.toString());
                        Gson gson = new Gson();
                        GenRspModel modelo = gson.fromJson(response, GenRspModel.class);
                        Toast.makeText(getBaseContext(), modelo.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Fallo conexion con Servicio reports...", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Failure",error);
            }
        });
        queue.add(request);
    }else{
        gps.showSettingsAlert();
    }
}


}
