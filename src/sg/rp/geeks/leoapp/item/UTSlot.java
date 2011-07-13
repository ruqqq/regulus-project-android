package sg.rp.geeks.leoapp.item;

public class UTSlot extends Timeslot {
    private String UT;

    public UTSlot(String title, String date, String venue, String time) {
        super(title, date, venue, time);
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
