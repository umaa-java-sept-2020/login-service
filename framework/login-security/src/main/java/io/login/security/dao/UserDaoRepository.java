package io.login.security.dao;

import io.login.security.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Repository
public class UserDaoRepository implements IUserRepository {

    private static Logger LOGGER = LoggerFactory.getLogger(UserDaoRepository.class);

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init()
    {
        LOGGER.info("jdbc template initialized: {}", jdbcTemplate);
    }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object getUserByUUID(String uuid) {
        return null;
    }

    @Override
    public Object getUserByUserId(String userId) {
        return null;
    }
}
