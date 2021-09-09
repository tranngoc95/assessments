package learn.solarpanel;

import com.mysql.cj.jdbc.MysqlDataSource;
import learn.solarpanel.ui.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

//@ComponentScan
//@PropertySource("classpath:data.properties")
public class App {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        Controller controller = context.getBean(Controller.class);

        controller.run();
    }

    @Bean
    public DataSource getDataSource(){
        MysqlDataSource result = new MysqlDataSource();
        result.setURL("jdbc:mysql://localhost:3306/solar_panel_db");
        result.setUser("root");
        result.setPassword("root");
        return result;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
