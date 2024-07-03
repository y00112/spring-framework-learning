package com.zhaoyss.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcDaoSupport {

    private JdbcTemplate jdbcTemplate;

    public final void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }
}
