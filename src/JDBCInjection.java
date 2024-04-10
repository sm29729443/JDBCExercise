import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * ClassName: JDBCInjection
 * Package: PACKAGE_NAME
 */
public class JDBCInjection {

    public static void main(String[] args) throws Exception {
        // 1. 註冊 Driver
        //自動註冊，不做

        // 2.獲取連接
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);

        // 3.獲取執行sql語句的物件
        Statement statement = connection.createStatement();

        // 4.執行動態 sql 語句;
        Scanner scanner = new Scanner(System.in);

        // 若是輸入 Steven' OR '1' = '1 會發現將資料庫的所有資料都查詢出來了，
        // 這就是透過sql injection來使得資料外洩
        System.out.println("輸入 firstName: ");
        String firstName = scanner.nextLine();
        String sql = "SELECT * FROM employees WHERE first_name = '" + firstName + "'";
        ResultSet resultSet = statement.executeQuery(sql);

        // 5.遍歷結果集
        while (resultSet.next()) {
            System.out.print("employee_id:" + resultSet.getInt("employee_id"));
            System.out.print(", first_name:" + resultSet.getString("first_name"));
            System.out.print(", last_name:" + resultSet.getString("last_name"));
            System.out.print(", email:" + resultSet.getString("email"));
            System.out.print(", phone_number:" + resultSet.getString("phone_number"));
            System.out.print(", hire_date:" + resultSet.getDate("hire_date"));
            System.out.print(", job_id:" + resultSet.getString("job_id"));
            System.out.print(", salary:" + resultSet.getDouble("salary"));
            System.out.print(", commission_pct:" + resultSet.getDouble("commission_pct"));
            System.out.print(", manager_id:" + resultSet.getInt("manager_id"));
            System.out.print(", department_id:" + resultSet.getInt("department_id"));
            System.out.println();
        }

        // 6. 關閉資源，先開後關原則
        resultSet.close();
        statement.close();
        connection.close();
    }


}
