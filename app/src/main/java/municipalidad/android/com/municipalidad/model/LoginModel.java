package municipalidad.android.com.municipalidad.model;

/**
 * Created by JaredCalendar on 8/8/16.
 */
public class LoginModel extends GenRspModel{

    private int id;
    private String token;

    public LoginModel(String message, boolean result, int id, String token) {
        super(message, result);
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
