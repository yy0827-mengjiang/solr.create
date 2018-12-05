package com.nassoft.qbyp.solr.create.mysql;

import org.springframework.jdbc.core.JdbcTemplate;

public interface IRdbService {
    /**
     * 获取业务交互库
     * @return
     */
	JdbcTemplate getTransactionJdbcTemplate();

}
