package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        //커넥션을 생성해서 DB와 연결
        //DriverManager는 커넥션을 획득할 때 마다 파라미터를 계속 전달
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("con={}, class={}" , con1, con1.getClass());
        log.info("con={}, class={}" , con2, con2.getClass());
    }

    //위두가지의 가장 큰 차이점은 설정과 사용을 분리하였다.

    //설정
    @Test 
    void dataSourceDataManager() throws SQLException {
        //DriverManagerDataSource - 항상 새로운 커넥션 획득
        //DataSource는 생성할때만 파라미터를 남기고 커넥션을 획득할때는 단순히  dataSource.getConnection()만 호출
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);

    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //커넥션 풀링: HikariProxyConnection(Proxy) -> JdbcConnection(Target)
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10); //기본이 10개
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000); //커넥션 풀에서 커넥션 생성 시간 대기
    }

    //사용
    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("con={}, class={}" , con1, con1.getClass());
        log.info("con={}, class={}" , con2, con2.getClass());
    }
}
