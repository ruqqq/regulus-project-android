package sg.rp.geeks.leoapp.item;

public class ModuleSlot extends Timeslot {
    private String id;
    private String problem;

    public ModuleSlot(String title, String date, String venue, String time) {
        super(title, date, venue, time);

        String[] date_array = date.split(" ");

        this.day = date_array[1];
        this.day = this.day.substring(1, this.day.length()-1).toLowerCase();

        String[] day_date_array = date_array[0].split("/");
        this.day_date = day_date_array[0];
    }
    
    public ModuleSlot(String id, String problem, String title, String date, String venue, String time) {
        this(title, date, venue, time);
        this.id = id;
        this.problem = problem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @Override
    public String toString() {
        return "ModuleSlot{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", venue='" + venue + '\'' +
                ", problem='" + problem + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
