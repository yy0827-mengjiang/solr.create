package com.nassoft.qbyp.solr.create.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RdbServiceImpl implements IRdbService {

    private JdbcTemplate transactionJdbcTemplate;

    /**
     * Setter注入
     *
     * @param transactionJdbcTemplate
     */
    @Autowired
    @Qualifier("transactionJdbcTemplate")
    public void setTransactionJdbcTemplate(JdbcTemplate transactionJdbcTemplate) {
        this.transactionJdbcTemplate = transactionJdbcTemplate;
    }

    @Override
    public JdbcTemplate getTransactionJdbcTemplate(){
        return transactionJdbcTemplate;
    }

}
