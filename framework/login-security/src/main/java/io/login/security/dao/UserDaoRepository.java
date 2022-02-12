package io.login.security.dao;

import io.login.client.models.RoleUpdate;
import io.login.client.models.UserAccount;
import io.login.client.models.UserRole;
import io.login.client.models.UserStatus;
import io.login.security.models.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDaoRepository implements IUserRepository {

    private static Logger LOGGER = LoggerFactory.getLogger(UserDaoRepository.class);
    private static final  String SAVE_DRAFT_TOKEN ="INSERT INTO TBL_RESET_PASSWORD(USERNAME,RESET_PASSWORD_TOKEN,STATUS,CREATED_AT) VALUES(?,?,?,?);";
    private static final  String COUNT_SAVE_DRAFT_TOKEN ="SELECT COUNT(*) FROM TBL_RESET_PASSWORD WHERE USERNAME=? AND RESET_PASSWORD_TOKEN=? AND STATUS='NEW';";

    private static final String SAVE_USER = "INSERT INTO TBL_LOGIN_USER(UUID, USERNAME, PASSWORD, STATUS) VALUES (?, ?, ?, ?)";
    private static final String SAVE_USER_TO_USER_ROLE_MAPPING = "INSERT INTO TBL_LOGIN_USER_ROLE_MAPPING(UUID, " +
            "USER_UUID, ROLE_UUID) VALUES (?, ?, ?)";

    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM TBL_LOGIN_USER WHERE USERNAME=?";
    private static final String UPDATE_USER_PASSWORD_BY_USERNAME = "UPDATE TBL_LOGIN_USER SET PASSWORD=? WHERE USERNAME=?";
    private static final String UPDATE_USER_STATUS_BY_USERNAME = "UPDATE TBL_LOGIN_USER SET STATUS=? WHERE USERNAME=?";

    private static final String GET_ROLE_UUID = "SELECT UUID FROM TBL_LOGIN_ROLE WHERE ROLE_NAME = ?";
    private static final String DELETE_ROLES_ASSIGNED_TO_USER_BY_USER_UUID = "DELETE FROM TBL_LOGIN_USER_ROLE_MAPPING" +
            " WHERE " +
            "USER_UUID = ?";

    // private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM TBL_LOGIN_USER WHERE USERNAME=?";

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



    String[] getRoleUUIDFromRole(List<UserRole> roles){
        String[] rolesUUID = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++){
            Object[] objects = new Object[]{roles.get(i).name()};
            UserAccount userDetails  = this.jdbcTemplate.queryForObject(GET_ROLE_UUID, new RoleRowMapper(),
                    objects);
            System.out.println(userDetails);
            rolesUUID[i] = userDetails.getUuid();
        }
        return rolesUUID;
    }


    @Override
    public void insertUserToDB(UserAccount userRequest) {
        System.out.println(userRequest.getUuid() + userRequest.getUsername()+
                userRequest.getPassword()+
                userRequest.getStatus().name());
        jdbcTemplate.update(SAVE_USER, new Object[]{userRequest.getUuid(), userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getStatus().name()});
    }


    @Override
    public void insertIntoRoleMapping(UserAccount userRequest) {
        List<UserRole> roles = userRequest.getRoles();
        String[] rolesUUIDs =  getRoleUUIDFromRole(roles);
        // deleteUSerRoleMapping(uuid, roleUUIDs);
        batchInsert(userRequest.getUuid(), rolesUUIDs);
    }

    @Override
    public RoleUpdate updateUSerRoleInDB(RoleUpdate updateRole) {
        deleteUSerRoleMapping(updateRole.getUserName());
        Object[] objects = new Object[]{updateRole.getUserName()};
        LoginUser loginUser = jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME, new UserRowMapper(), objects);
        String uuid = loginUser.getUuid();
        String[] rolesUUIDs = getRoleUUIDFromRole(updateRole.getUserRole());
        batchInsert(uuid, rolesUUIDs);
        return updateRole;
    }
    void deleteUSerRoleMapping(String userName){
        Object[] objects = new Object[]{userName};
        LoginUser loginUser = jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME, new UserRowMapper(), objects);
        String uuid = loginUser.getUuid();
        jdbcTemplate.update(DELETE_ROLES_ASSIGNED_TO_USER_BY_USER_UUID, uuid);
    }
    private int[] batchInsert(String userUUID, String[] roleUUIDs) {
        return jdbcTemplate.batchUpdate(SAVE_USER_TO_USER_ROLE_MAPPING, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, userUUID);
                ps.setString(3, roleUUIDs[i]);
            }

            @Override
            public int getBatchSize() {
                return roleUUIDs.length;
            }
        });
    }

    @Override
    public LoginUser getUserByUUID(String uuid) {
        return null;
    }
    @Override
    public LoginUser getUserByUsername(String username) {
        Object[] objects = new Object[]{username};
        LoginUser l = jdbcTemplate.queryForObject(SELECT_USER_BY_USERNAME, new UserRowMapper(), objects);
        return l;
    }

    @Override
    public LoginUser updateUserPassword(LoginUser loginUser) {
        jdbcTemplate.update(UPDATE_USER_PASSWORD_BY_USERNAME, new Object[]{loginUser.getPassword(), loginUser.getUsername()});
        return getUserByUsername(loginUser.getUsername());
    }

    @Override
    public LoginUser updateUserStatus(LoginUser loginUser) {
        jdbcTemplate.update(UPDATE_USER_STATUS_BY_USERNAME, new Object[]{loginUser.getStatus().name(), loginUser.getUsername()});
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
            loginUser.setStatus(UserStatus.valueOf(status));
            return loginUser;
        }
    }

    private static class RoleRowMapper implements RowMapper<UserAccount> {

        @Override
        public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
            String uuid = rs.getString("UUID");

            UserAccount ua = new UserAccount();
            ua.setUuid(uuid);

            return ua;
        }
    }
}
