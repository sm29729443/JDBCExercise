package advanced.pojo;

/**
 * ClassName: Students
 * Package: advanced.pojo
 */
public class Student {
    private Integer studentId;
    private String studentName;
    private Integer studentAge;

    public Student() {
    }

    public Student(Integer studentId, String studentName, Integer studentAge) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentAge = studentAge;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(Integer studentAge) {
        this.studentAge = studentAge;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", studentAge=" + studentAge +
                '}';
    }
}
