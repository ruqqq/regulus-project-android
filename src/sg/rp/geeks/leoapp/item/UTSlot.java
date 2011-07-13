package sg.rp.geeks.leoapp.item;

public class UTSlot extends Timeslot {
    private String UT;

    public UTSlot(String title, String date, String venue, String time) {
        super(title, date, venue, time);

        String[] date_array = date.split(" ");

        this.day = date_array[1];
        this.day = this.day.substring(1, this.day.length()-1).toLowerCase();

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
