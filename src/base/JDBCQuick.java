package base;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCQuick {
    public static void main(String[] args) throws Exception {
        // 1.註冊驅動: 就是將 JDBC 廠商提供給我們的驅動類透過類加載的方式加載到我們的程式
        /*
            詳細步驟:
            1.  當調用Class.forName("com.mysql.cj.jdbc.Driver")時,
                它會請求類加載器去查找並加載com.mysql.cj.jdbc.Driver類。
            2.  類加載器會在classpath中尋找com.mysql.cj.jdbc.Driver類
                對應的字節碼文件(.class文件)。
            3.  如果找到了com.mysql.cj.jdbc.Driver.class文件,
                類加載器會讀取該文件,並將其轉換為JVM可以理解和執行的運行時數據結構。
                這個過程包括加載、連接和初始化等步驟。
            4.  在類的初始化過程中,會執行com.mysql.cj.jdbc.Driver類的靜態代碼塊。
                在這個靜態代碼塊中,有一行代碼DriverManager.registerDriver(new Driver())。
                這行代碼的作用是創建一個Driver類的實例,並將其註冊到JDBC的DriverManager中。
            5.  註冊驅動程序實例到DriverManager的目的是為了後續
                通過DriverManager.getConnection()方法獲取數據庫連接。
                當調用getConnection()方法時,DriverManager會查找已註冊的驅動程序,
                並使用相應的驅動程序實例來創建數據庫連接對象。
            6.  完成類的加載和初始化後,Class.forName()方法會返回一個
                代表com.mysql.cj.jdbc.Driver類的Class對象,
                該對象包含了類的所有元數據信息。但獲取這個Class對象並不是我們的最終目的,
                而是在加載類的過程中,觸發了驅動程序的註冊,為後續獲取數據庫連接做好了準備。
         */
        // 由以上說明，知道也可以自己手動註冊 Driver
        // DriverManager.registerDriver(new Driver());
        /*
            從 JDK 6.0 開始不需要在手動的去註冊 Driver，JDBC 引入了一種稱為 JDBC 4.0驅動程式自動加載
            的技術，好像是會去掃描 jar 包裡的META-INF/services 裡的檔案啥的，這裡不記太細
        */
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2.獲取連接資料庫的物件
        /*
            以前連接 mysql 資料庫都是透過 workbench 等可視化工具
            而在 java 中若要連接資料庫，則要透過 Connection 物件去連接。

            Driver類:
                    是由JDBC驅動程序提供的具體實現類,
                    如MySQL的com.mysql.cj.jdbc.Driver,Oracle的oracle.jdbc.OracleDriver等。
                    封裝了與特定數據庫連接和通信的細節,如協議、驗證、數據類型映射等。
                    每個數據庫廠商提供自己的Driver類實現,用於連接其特定的數據庫。
                    Driver類的作用是提供與特定數據庫進行通信的方式和協議,決定了如何與數據庫進行低級別的交互。
            Connection類:
                    是JDBC API中表示與數據庫的連接的接口。
                    提供了建立和管理數據庫連接的方法,如createStatement()、
                    prepareStatement()、setAutoCommit()、commit()、rollback()等。
                    使用DriverManager的getConnection()方法獲取Connection對象,
                    該方法會根據提供的URL、用戶名和密碼,使用已註冊的Driver實例來建立與數據庫的實際連接。
                    一旦獲得了Connection對象,就可以通過它使用Driver提供的通信方式與數據庫進行高級別的交互,
                    如執行SQL查詢、更新數據、管理事務等。
         */
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        String username = "root";
        String password = "52091219";
        // 獲得連接資料庫的物件
        Connection connection = DriverManager.getConnection(url, username, password);


        // 3.獲得執行sql語句的物件
        /*
            若是使用同一個 connection.createStatement() 所建立的 statement 來執行多次資料庫操作，
            這樣算是連接一次就能執行多次操作，還是每執行一次 statement.execute()都會重新連接呢?
            Ans: 問 claude 3 是說算連接一次而已，並不會每次對資料庫操作一次就要重新連接一次。
         */

        // 實際應用不會使用 Statement，因為會產生 sql injection，都是使用 prepareStatement
        Statement statement = connection.createStatement();

        // 4.編寫sql語句並且執行，並且接受返回的結果集
        String sql = "SELECT * FROM employees";
        ResultSet resultSet = statement.executeQuery(sql);

        // 5.對結果集進行數據處理，這裡就遍歷一下打印而已

        // resultSet.next():判斷是否有下一條 record
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
