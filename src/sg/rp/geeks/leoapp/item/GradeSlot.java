package sg.rp.geeks.leoapp.item;

public class GradeSlot {
    protected String moduleCode;
    protected String problem;
    protected String grade;

    public GradeSlot(String moduleCode, String problem, String grade) {
        this.moduleCode = moduleCode;
        this.problem = problem;
        this.grade = grade;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "GradeSlot{" +
                "module_code='" + moduleCode + '\'' +
                ", problem='" + problem + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
