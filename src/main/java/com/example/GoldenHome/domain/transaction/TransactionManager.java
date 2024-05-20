package com.example.GoldenHome.domain.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

public abstract class TransactionManager {

    @Autowired
    private DataSource dataSource;

    private DataSourceTransactionManager transactionManager;

    protected void beginTransaction() {
        transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        setTransactionStatus(transactionStatus);
    }

    protected void commitTransaction() {
        transactionManager.commit(getTransactionStatus());
    }

    protected void rollbackTransaction() {
        transactionManager.rollback(getTransactionStatus());
    }

    protected abstract TransactionStatus getTransactionStatus();

    protected abstract void setTransactionStatus(TransactionStatus transactionStatus);

}
