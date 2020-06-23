package study.spring.transactiondemo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring.transactiondemo.exception.RollbackException;
import study.spring.transactiondemo.service.FooService;

/**
 * @author wuguokai
 */
@Service
public class FooServiceImpl implements FooService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void insertRecord() {
        jdbcTemplate.execute("insert into foo (bar) values ('AAA')");
    }

    @Transactional(rollbackFor = RollbackException.class)
    @Override
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("insert into foo (bar) values ('BBB')");
        throw new RollbackException();
    }

    @Override
    public void invokeInsertThenRollback() throws RollbackException {
        insertThenRollback();
    }
}
