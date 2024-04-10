package base;

import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * ClassName: base.JDBCOperation
 * Package: PACKAGE_NAME
 */
public class JDBCOperation {

    // 增刪改的流程都是一樣的，除了傳遞給 preparedstatment 的 sql 語句不一樣外
    // 因此只展示 insert 操作
    @Test
    public void testInsert() throws Exception {
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO employees(first_name, last_name,\n" +
                    " email, phone_number, hire_date, job_id,\n" +
                    " salary, commission_pct, manager_id, department_id, employee_id)\n" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, "Tong");
        preparedStatement.setString(2, "BO");
        preparedStatement.setString(3, "s87@gmail.com");
        preparedStatement.setString(4, "0989877877");
        preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
        preparedStatement.setString(6, "IT_PROG");
        preparedStatement.setDouble(7, 877777.666);
        preparedStatement.setDouble(8, 0.2);
        preparedStatement.setInt(9, 101);
        preparedStatement.setInt(10, 100);
        preparedStatement.setInt(11, 351);
        // 增刪改都用 executeUpdate
        int i = preparedStatement.executeUpdate();
        System.out.println("影響行數:" + i);
        preparedStatement.close();
        connection.close();
    }
}
