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
    private static final  String SAVE_DRAFT_TOKEN ="INSERT INTO TBL_RESET_PASSWORD(USERNAME,RESET_PASSWORD_TOKEN,STATUS,CREATED_AT) VALUES(?,?,?,?);";
    private static final  String COUNT_SAVE_DRAFT_TOKEN ="SELECT COUNT(*) FROM TBL_RESET_PASSWORD WHERE USERNAME=? AND RESET_PASSWORD_TOKEN=? AND STATUS='NEW';";

    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM TBL_LOGIN_USER WHERE USERNAME=?";
    private static final String UPDATE_USER_PASSWORD_BY_USERNAME = "UPDATE TBL_LOGIN_USER SET PASSWORD=? WHERE USERNAME=?";
    private static final String UPDATE_USER_STATUS_BY_USERNAME = "UPDATE TBL_LOGIN_USER SET STATUS=? WHERE USERNAME=?";

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
        jdbcTemplate.update(UPDATE_USER_PASSWORD_BY_USERNAME, new Object[]{loginUser.getPassword(), loginUser.getUsername()});
        return getUserByUsername(loginUser.getUsername());
    }

    @Override
    public LoginUser updateUserStatus(LoginUser loginUser) {
        jdbcTemplate.update(UPDATE_USER_STATUS_BY_USERNAME, new Object[]{loginUser.getUserStatus().name(), loginUser.getUsername()});
        return getUserByUsername(loginUser.getUsername());
    }

    @Override
    public Integer getResetPasswordTokenCount(String username, String resetPasswordToken) {
        return jdbcTemplate.queryForObject(COUNT_SAVE_DRAFT_TOKEN,
                Integer.class,
                new Object[]{username,resetPasswordToken});
    }

    @Override
    public boolean saveResetPasswordToken(String username, String resetPasswordToken)
    {
        String status = "NEW";
        Long createdAt = System.currentTimeMillis();
        Object[] objects = new Object[]{username, resetPasswordToken, status, createdAt};
        if (jdbcTemplate.update(SAVE_DRAFT_TOKEN, objects) == 1)
            return true;
        else
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
