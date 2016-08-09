package municipalidad.android.com.municipalidad.model;

/**
 * Created by JaredCalendar on 8/8/16.
 */
public class EventoModel {

    private int id;
    private String name;
    private String description;
    private String date_event;
    private int suscribed;

    public EventoModel(int id, String name, String description, String date_event, int suscribed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date_event = date_event;
        this.suscribed = suscribed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_event() {
        return date_event;
    }

    public void setDate_event(String date_event) {
        this.date_event = date_event;
    }

    public int getSuscribed() {
        return suscribed;
    }

    public void setSuscribed(int suscribed) {
        this.suscribed = suscribed;
    }
}
