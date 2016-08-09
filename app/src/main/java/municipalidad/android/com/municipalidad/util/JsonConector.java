package municipalidad.android.com.municipalidad.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by JaredCalendar on 8/8/16.
 */
public class JsonConector extends StringRequest {

        private final Map<String, String> params;

        public JsonConector(int method,
                             String url, Map<String, String> params,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener){
            super(method, url, listener, errorListener);

            this.params = params;

        }

        @Override
        public String getBodyContentType() {
            return "application/x-www-form-urlencoded; charset=UTF-8";
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params;
        }

}
