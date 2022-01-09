package io.login.security.dao;

import io.login.security.models.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class UserDaoRepository implements IUserRepository {

    private static Logger LOGGER = LoggerFactory.getLogger(UserDaoRepository.class);
    private static final  String SAVE_DRAFT_TOKEN ="INSERT  INTO DRAFT_TOKEN VALUES(?,?);";

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
    public LoginUser getUserByUUID(String uuid) {
        return null;
    }
    @Override
    public LoginUser getUserByUsername(String username) {

        return null;
    }

    @Override
    public LoginUser updateUserPassword(LoginUser loginUser) {
        return null;
    }

    @Override
    public LoginUser updateUserStatus(LoginUser loginUser) {
        return null;
    }

    @Override
    public Integer getResetPasswordTokenCount(String username, String resetPasswordToken) {
        return null;
    }

    @Override
    public boolean saveResetPasswordToken(String username, String resetPasswordToken)
    {
        Object[] objects = new Object[]{username,resetPasswordToken};

        if (jdbcTemplate.update(SAVE_DRAFT_TOKEN,objects) == 1)
            return true;
        else
            return false;
    }

}
