package study.spring.transactiondemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import study.spring.transactiondemo.service.FooService;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService fooService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        programTransactionTest();
        declareTransactionTest();
    }

    private void declareTransactionTest() {
        fooService.insertRecord();
        log.info("AAA: {}", jdbcTemplate.queryForObject("select count(*) from foo where bar='AAA'", Long.class));

        try {
            fooService.insertThenRollback();
        } catch (Exception e) {
            log.info("BBB: {}", jdbcTemplate.queryForObject("select count(*) from foo where bar ='BBB'", Long.class));
        }

        try {
            fooService.invokeInsertThenRollback();
        } catch (Exception e) {
            log.info("2-BBB: {}", jdbcTemplate.queryForObject("select count (*) from foo where bar ='BBB'", Long.class));
        }
    }

    private void programTransactionTest() {
        log.info("COUNT BEFORE TRANSACTION: {}", getCount());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("insert into foo (id, bar) values (1, 'aaa')");
                log.info("COUNT IN TRANSACTION: {}", getCount());
                transactionStatus.setRollbackOnly();
            }
        });
        log.info("COUNT AFTER TRANSACTION: {}", getCount());
    }

    private long getCount() {
        return jdbcTemplate.queryForObject("select count(*) from foo", Long.class);
    }
}
