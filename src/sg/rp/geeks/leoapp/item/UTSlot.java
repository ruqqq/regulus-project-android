package sg.rp.geeks.leoapp.item;

import org.apache.commons.logging.Log;

import java.security.Key;

public class UTSlot extends Timeslot {
    private String UT;
    final String[] MONTHS = {"Jan","Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public UTSlot(String title, String date, String venue, String time) {
        super(title, date, venue, time);

        String[] date_array = date.split(" ");

        this.day = date_array[0];
        this.day = this.day.substring(1, this.day.length()-1).toLowerCase();

        String[] temp = this.day.split("/");
        this.day = MONTHS[Integer.parseInt(temp[1]) - 1];

        String[] day_date_array = date_array[0].split("/");
        this.day_date = day_date_array[0];
    }

    public UTSlot(String UT, String title, String date, String venue, String time) {
        this(title, date, venue, time);
        this.UT = UT;
    }

    public String getUT() {
        return UT;
    }

    public void setUT(String UT) {
        this.UT = UT;
    }
}
