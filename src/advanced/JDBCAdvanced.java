package advanced;

import advanced.pojo.Employee;
import advanced.pojo.Student;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: JDBCAdvanced
 * Package: advanced
 */
public class JDBCAdvanced {

    // 封裝一筆數據到物件
    @Test
    public void testORM() throws Exception {
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees WHERE employee_id = ?");
        preparedStatement.setInt(1, 100);
        ResultSet resultSet = preparedStatement.executeQuery();
        Employee employee = null;
        // 將資料庫數據封裝到一個物件中，就是所謂的 ORM
        while (resultSet.next()) {
            employee = new Employee();
            employee.setEmployeeId(resultSet.getInt("employee_id"));
            employee.setFirstName(resultSet.getString("first_name"));
            employee.setLastName(resultSet.getString("last_name"));
            employee.setEmail(resultSet.getString("email"));
            employee.setPhoneNumber(resultSet.getString("phone_number"));
            employee.setHireDate(resultSet.getDate("hire_date"));
            employee.setJobId(resultSet.getString("job_id"));
            employee.setSalary(resultSet.getDouble("salary"));
            employee.setCommissionPct(resultSet.getDouble("commission_pct"));
            employee.setManagerId(resultSet.getInt("manager_id"));
            employee.setDepartmentId(resultSet.getInt("department_id"));
        }
        System.out.println(employee);
    }

    // 封裝多筆資料到物件
    @Test
    public void testORMMultipleDate() throws Exception {
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Employee> list = new ArrayList<>();
        Employee employee = null;
        while (resultSet.next()) {
            employee = new Employee();
            employee.setEmployeeId(resultSet.getInt("employee_id"));
            employee.setFirstName(resultSet.getString("first_name"));
            employee.setLastName(resultSet.getString("last_name"));
            employee.setEmail(resultSet.getString("email"));
            employee.setPhoneNumber(resultSet.getString("phone_number"));
            employee.setHireDate(resultSet.getDate("hire_date"));
            employee.setJobId(resultSet.getString("job_id"));
            employee.setSalary(resultSet.getDouble("salary"));
            employee.setCommissionPct(resultSet.getDouble("commission_pct"));
            employee.setManagerId(resultSet.getInt("manager_id"));
            employee.setDepartmentId(resultSet.getInt("department_id"));
            list.add(employee);
        }
        System.out.println(list);
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }

    // 當新增一筆資料後，得到此筆資料的 ID
    // 這裡在 auguigudb 建了一張有 自增值的表 students
    @Test
    public void testReturnPK() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);
        String sql = "INSERT INTO STUDENTS(student_name, student_age)\n" +
                " VALUES (?, ?)";
        // 第一步: 告訴 preparedStatement 要 return 一個 PK 回來
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


        Student student = new Student(null, "爪哇島", 30);
        preparedStatement.setString(1, student.getStudentName());
        preparedStatement.setInt(2, student.getStudentAge());

        ResultSet generatedKeys = null;
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            // return 的 key 值會以 ResultSet 的型態拿到，而裡面就只有一個 key 值，所以是單行單列的
            generatedKeys = preparedStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                student.setStudentId(generatedKeys.getInt(1));
                System.out.println("剛剛插入的資料的 key value:" + student.getStudentId() );
            }
            System.out.println("成功");
        } else {
            System.out.println("失敗");
        }
        System.out.println("student info:" + student);
        if (generatedKeys != null) {
        generatedKeys.close();
        }
        preparedStatement.close();
        connection.close();
    }

    // 批量新增操作，第一種方法:透過 for loop 反覆執行
    @Test
    public void testMoreInsert() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);
        String sql = "INSERT INTO STUDENTS(student_name, student_age)\n" +
                " VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            preparedStatement.setString(1, "Mark" + i);
            preparedStatement.setInt(2, 30 + i);

            preparedStatement.executeUpdate();
        }
        long end = System.currentTimeMillis();
        //執行時間: 3517
        System.out.println("執行時間:" + (end - start));
        preparedStatement.close();
        connection.close();
    }

    //批量新增操作，第二種方法:透過 Batch 執行
    @Test
    public void testBatchInsert() throws SQLException {
        // step 1: 設定 rewriteBatchedStatements = true
        String url = "jdbc:mysql://localhost:3306/atguigudb?rewriteBatchedStatements=true";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);

        // SQL 語句必須用VALUES，不能只是 VALUE，而且語句最後不能加 ;
        // 教學是說會把 sql 語句拼裝成 VALUES(?, ?),(?, ?) ...
        String sql = "INSERT INTO STUDENTS(student_name, student_age)\n" +
                " VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            preparedStatement.setString(1, "Rush" + i);
            preparedStatement.setInt(2, 30 + i);

            // 調用 addBatch() 方法，將sql語句進行批量操作
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        long end = System.currentTimeMillis();
        System.out.println("執行時間:" + (end - start));
        preparedStatement.close();
        connection.close();
    }
}
