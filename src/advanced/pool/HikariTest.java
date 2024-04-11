package advanced.pool;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariJNDIFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ClassName: HikariTest
 * Package: advanced.pool
 */
public class HikariTest {

    @Test
    public void testHardCodeHikari() throws SQLException {
        /*
            硬編碼:指的是將資料庫連接池的配置訊息與JAVA代碼耦合在一起
            Step 1: 創建 HikariDataSource 連接池物件
            Step 2: 設置連接池的配置訊息，又分為【必須 | 非必須】
            Step 3: 透過連接池獲取連接物件
            Step 4: 回收連接【不是釋放連接，而是將連接歸還給連接池，這樣連接池才能再拿給其他線程或請求進行複用】
         */

        // Step 1: 創建 HikariDataSource 連接池物件
        HikariDataSource hikariDataSource = new HikariDataSource();
        // Step 2.1: 設置連接池的必須配置訊息
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/atguigudb");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("52091219");
        // Step 2.2: 設置連接池的非必要配置訊息
        hikariDataSource.setMinimumIdle(10);
        hikariDataSource.setMaximumPoolSize(20);
        // Step 3: 透過連接池獲取連接物件
        Connection connection = hikariDataSource.getConnection();
        System.out.println("測試是否連接成功 connection: " + connection);

        // Step 4: 將連接物件返回給連接池
        connection.close();
    }

    @Test
    public void testSoftCodeHikari() throws Exception {
        // Step 1: 創建 Properties 集合，用於儲存外部配置文件的 key-value
        Properties properties = new Properties();
        // Step 2: 讀取外部配置文件，獲取輸入流，加載至 Properties 集合
        InputStream inputStream = HikariTest.class.getClassLoader().getResourceAsStream("hikari.properties");
        properties.load(inputStream);
        // Step 3: 創建 HikariConfig 連接池配置物件，將 Properties 集合傳進去
        HikariConfig config = new HikariConfig(properties);
        // Step 4: 基於 HikariConfig 物件創建 HikariDataSource 物件
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        // Step 5: 獲取連接
        Connection connection = hikariDataSource.getConnection();
        System.out.println("測試是否獲取連接 connection: " + connection);
        // 基於 connection 的 CRUD CODE

        // 回收資源
        connection.close();
    }
}
