package advanced.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Properties;

/*
    從之前的 code 可以觀察到，每個方法都需要重新透過 DriverManager.getConnection 去連接一次資料庫
    並且在方法執行完後透過Connection.close()去關閉它，而這實際上很消耗 JVM 資源
    因此有了 資料庫連接池的出現，透過連接池幫我們去管理連接相關的操作，具體說明看圖(connectionpool.jpg)
 */
public class DruidTest {

    // 硬編碼
    @Test
    public void testHardCodeDruid() throws SQLException {
        /*
            硬編碼:指的是將資料庫連接池的配置訊息與JAVA代碼耦合在一起
            Step 1: 創建 DruidDataSource 連接池物件
            Step 2: 設置連接池的配置訊息，又分為【必須 | 非必須】
            Step 3: 透過連接池獲取連接物件
            Step 4: 回收連接【不是釋放連接，而是將連接歸還給連接池，這樣連接池才能再拿給其他線程或請求進行複用】
         */

        // Step 1:創建 DruidDataSource 連接池物件
        DruidDataSource druidDataSource = new DruidDataSource();

        // Step 2.1:配置連接池的 必要 訊息
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/atguigudb");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("52091219");
        // Step 2.2:配置連接池的 非必要 訊息
        druidDataSource.setInitialSize(10); // 連接池一開始會創建 10 個 連接物件
        druidDataSource.setMaxActive(20); // 連接池的連接物件最大數量為 20 個
        // Step 3:透過連接池去獲得連接物件
        DruidPooledConnection connection = druidDataSource.getConnection();
        System.out.println("測試是否拿到 connection:" + connection);

        // 基於 connection 的 CRUD code
        // Step 4:將連接物件返回給連接池
        connection.close(); //一樣是調用 close()，但這不是關閉資源的意思了，而是將連接物件返還給連接池
    }

    // 軟編碼
    @Test
    public void testSoftCodeDruid() throws Exception {
        // Step 1: 創建 Properties 集合，用於儲存外部配置文件的 key-value
        Properties properties = new Properties();
        // Step 2: 讀取外部配置文件，獲取輸入流，加載至 Properties 集合
        InputStream inputStream = DruidTest.class.getClassLoader().getResourceAsStream("db.properties");
        properties.load(inputStream);
        // Step 3: 基於 Properties 集合構建 DruidDataSource 連接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println("測試是否拿到 connection :" + connection);

        // 基於 connection 的 CRUD CODE

        // 回收資源
        connection.close();
    }
}
