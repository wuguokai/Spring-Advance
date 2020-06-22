package study.spring.simplejdbcdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import study.spring.simplejdbcdemo.dao.FooDao;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private FooDao fooDao;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Autowired
    public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("foo").usingGeneratedKeyColumns("id");
    }

    @Override
    public void run(String... args) throws Exception {
        fooDao.insertData();
        fooDao.batchInsert();
        fooDao.listData();
    }
}
