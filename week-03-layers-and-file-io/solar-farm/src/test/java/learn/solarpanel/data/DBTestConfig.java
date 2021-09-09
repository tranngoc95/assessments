//package learn.solarpanel.data;
//
//import com.mysql.cj.jdbc.MysqlDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
//@ComponentScan
//public class DBTestConfig {
//    @Bean
//    public DataSource getDataSource(){
//        MysqlDataSource result = new MysqlDataSource();
//        result.setURL("jdbc:mysql://localhost:3306/solar_panel_test");
//        result.setUser("root");
//        result.setPassword("root");
//        return result;
//    }
//
//    @Bean
//    public JdbcTemplate getJdbcTemplate(DataSource dataSource){
//        return new JdbcTemplate(dataSource);
//    }
//}
