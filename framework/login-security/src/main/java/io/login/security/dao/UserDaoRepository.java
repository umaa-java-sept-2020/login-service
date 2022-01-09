package io.login.security.dao;

import io.login.client.models.UserStatus;
import io.login.security.models.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDaoRepository implements IUserRepository {

    private static Logger LOGGER = LoggerFactory.getLogger(UserDaoRepository.class);

    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM TBL_LOGIN_USER WHERE USERNAME=?";

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
        Object[] objects = new Object[]{username};
        return jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME, new UserRowMapper(), objects);
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
    public boolean saveResetPasswordToken(String username, String resetPasswordToken) {
        return false;
    }


    private static class UserRowMapper implements RowMapper<LoginUser>{

        @Override
        public LoginUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            String uuid = rs.getString("UUID");
            String username = rs.getString("USERNAME");
            String password = rs.getString("PASSWORD");
            String  status = rs.getString("STATUS");

            LoginUser loginUser = new LoginUser();
            loginUser.setUuid(uuid);
            loginUser.setUsername(username);
            loginUser.setPassword(password);
            loginUser.setUserStatus(UserStatus.valueOf(status));
            return loginUser;
        }
    }
}
