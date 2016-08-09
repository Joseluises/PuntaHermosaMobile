package municipalidad.android.com.municipalidad.model;

/**
 * Created by JaredCalendar on 8/8/16.
 */
public class GenRspModel {

    private String message;
    private boolean result;

    public GenRspModel(String message, boolean result) {
        this.message = message;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
