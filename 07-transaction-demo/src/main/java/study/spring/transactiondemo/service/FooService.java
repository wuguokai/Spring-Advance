package study.spring.transactiondemo.service;

import study.spring.transactiondemo.exception.RollbackException;

/**
 * @author wuguokai
 */
public interface FooService {

    void insertRecord();

    void insertThenRollback() throws RollbackException;

    void invokeInsertThenRollback() throws RollbackException;
}
