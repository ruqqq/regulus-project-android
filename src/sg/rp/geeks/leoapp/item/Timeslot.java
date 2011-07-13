package sg.rp.geeks.leoapp.item;

public class Timeslot {
    protected String title;
    protected String date;
    protected String venue;
    protected String time;

    public Timeslot(String title, String date, String venue, String time) {
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.time = time;
    }

    public Timeslot() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Timeslot{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", venue='" + venue + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
