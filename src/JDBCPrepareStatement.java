import java.sql.*;
import java.util.Scanner;

/**
 * ClassName: JDBCInjection
 * Package: PACKAGE_NAME
 */
public class JDBCPrepareStatement {

    public static void main(String[] args) throws Exception {
        // 1. 註冊 Driver
        //自動註冊，不做

        // 2.獲取連接
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        Connection connection = DriverManager.getConnection(url, username, password);

        // 3.獲取執行sql語句的物件
        // 能看到 preparedStatement 不支持無參創建，必須先傳入一個要執行的 sql 語句
        // 並且動態參數是以 ?占位符 方式決定，而不是使用動態拼接的方式
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees WHERE first_name = ?");
        // 4.執行動態 sql 語句;
        Scanner scanner = new Scanner(System.in);

        // 若是輸入 Steven' OR '1' = '1 會發現將資料庫的所有資料都查詢出來了，
        // 這就是透過sql injection來使得資料外洩
        System.out.println("輸入 firstName: ");
        String firstName = scanner.nextLine();
        // parameterIndex 是設定第 x 個占位符所對應的變數，因此是從 1 開始
        // preparedStatment 會對傳遞近入的變數進行參數化處理，也就是最外層加上一對''
        // 並且變數中的 ' 也都會進行轉譯等動作，也就是說若是輸入 Steven' OR '1' = '1
        // prepareStatment 會把 Steven' OR '1' = '1 變成 'Steven\' OR \'1\' = \'1'
        preparedStatement.setString(1, firstName);
        ResultSet resultSet = preparedStatement.executeQuery();
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
        preparedStatement.close();
        connection.close();
    }


}

