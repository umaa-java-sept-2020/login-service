package io.login.security.dao;

import io.login.client.models.*;
import io.login.security.models.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserDaoRepository implements IUserRepository {

    private static Logger LOGGER = LoggerFactory.getLogger(UserDaoRepository.class);
    private static final String SAVE_DRAFT_TOKEN = "INSERT INTO TBL_RESET_PASSWORD(USERNAME,RESET_PASSWORD_TOKEN,STATUS,CREATED_AT) VALUES(?,?,?,?);";
    private static final String COUNT_SAVE_DRAFT_TOKEN = "SELECT COUNT(*) FROM TBL_RESET_PASSWORD WHERE USERNAME=? " +
            "AND RESET_PASSWORD_TOKEN=? AND STATUS='NEW'";

    private static final String SAVE_USER = "INSERT INTO TBL_LOGIN_USER(UUID, USERNAME, PASSWORD, STATUS) VALUES (?, ?, ?, ?)";
    private static final String SAVE_USER_TO_USER_ROLE_MAPPING = "INSERT INTO TBL_LOGIN_USER_ROLE_MAPPING(UUID, " +
            "USER_UUID, ROLE_UUID) VALUES (?, ?, ?)";

    private static final String SAVE_USER_PROFILE_DETAILS = "INSERT INTO TBL_USER_PROFILE (FIRST_NAME, LAST_NAME, " +
            "EMAIL, MOBILE, CITY, USER_UUID) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_PROFILE_DETAILS = "UPDATE TBL_USER_PROFILE SET FIRST_NAME=?, LAST_NAME = " +
            "?, EMAIL=?, MOBILE=?, CITY=? WHERE  USER_UUID=?";

    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM TBL_LOGIN_USER WHERE USERNAME=?";
    private static final String UPDATE_USER_PASSWORD_BY_USERNAME = "UPDATE TBL_LOGIN_USER SET PASSWORD=? WHERE USERNAME=?";
    private static final String UPDATE_USER_STATUS_BY_USERNAME = "UPDATE TBL_LOGIN_USER SET STATUS=? WHERE USERNAME=?";

    private static final String GET_ROLE_UUID = "SELECT UUID,ROLE_NAME FROM TBL_LOGIN_ROLE WHERE ROLE_NAME = ?";
    private static final String DELETE_ROLES_ASSIGNED_TO_USER_BY_USER_UUID = "DELETE FROM TBL_LOGIN_USER_ROLE_MAPPING" +
            " WHERE " +
            "USER_UUID = ?";
    private static final String GET_ROLES_BY_USER_NAME = "SELECT UUID, ROLE_NAME FROM TBL_LOGIN_ROLE WHERE UUID IN( " +
            "SELECT " +
            "ROLE_UUID FROM TBL_LOGIN_USER_ROLE_MAPPING WHERE USER_UUID IN (SELECT UUID FROM TBL_LOGIN_USER WHERE " +
            "USERNAME = ?))";

    private static final String SAVE_APPLICANT_RECORD = "INSERT INTO TBL_JOB_APPLICANT (PAN_CARD, JOB_ID, EMAIL, " +
            "RESUME_PATH, STATUS) VALUES (?, ?, ?, ?, ?)";


    // private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM TBL_LOGIN_USER WHERE USERNAME=?";

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        LOGGER.info("jdbc template initialized: {}", jdbcTemplate);
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    String[] getRoleUUIDFromRole(List<UserRole> roles) {
        String[] rolesUUID = new String[roles.size()];
        try {

            for (int i = 0; i < roles.size(); i++) {
                String role =roles.get(i).name();
                Object[] objects = new Object[]{role};
                UserRoleMapper userDetails = this.jdbcTemplate.queryForObject(GET_ROLE_UUID, new RoleRowMapper(),
                        objects);
                System.out.println(userDetails);
                rolesUUID[i] = userDetails.getUuid();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return rolesUUID;
    }

    @Override
    public List<UserRole> getRoles(String userName) {
        List<UserRoleMapper> ua = this.jdbcTemplate.query(GET_ROLES_BY_USER_NAME, new RoleRowMapper(),
                new Object[]{userName});
        return ua.stream().map(e -> {
            return e.getUserRole();
        }).collect(Collectors.toList());
    }


    @Override
    public void insertUser(UserAccount userRequest) {
        jdbcTemplate.update(SAVE_USER, new Object[]{userRequest.getUuid(), userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getStatus().name()});
    }


    @Override
    public void insertIntoRoleMapping(UserAccount userRequest) {
        List<UserRole> roles = userRequest.getRoles();
        String[] rolesUUIDs = getRoleUUIDFromRole(roles);
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

    @Override
    public void insertUserProfileDetails(UserProfile userProfile) {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAuthContext userAuthContext = (UserAuthContext) object;
        UserAccount userRequest = userAuthContext.getUserRequest();
        String uuid = userRequest.getUuid();
        Object[] userProfileDetails = new Object[]{userProfile.getFirstName(),
                userProfile.getLastName(), userProfile.getEmail(),
                userProfile.getMobile(), userProfile.getCity(), uuid};
        jdbcTemplate.update(SAVE_USER_PROFILE_DETAILS, userProfileDetails);
    }

    @Override
    public void updateUserProfileDetails(UserProfile userProfile) {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAuthContext userAuthContext = (UserAuthContext) object;
        UserAccount userRequest = userAuthContext.getUserRequest();
        String uuid = userRequest.getUuid();
        Object[] userProfileDetails = new Object[]{userProfile.getFirstName(),
                userProfile.getLastName(), userProfile.getEmail(),
                userProfile.getMobile(), userProfile.getCity(), uuid};
        jdbcTemplate.update(UPDATE_USER_PROFILE_DETAILS, userProfileDetails);
    }

    void deleteUSerRoleMapping(String userName) {
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
        l.setRoles(getRoles(username));
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
                new Object[]{username, resetPasswordToken});
    }

    @Override
    public boolean saveResetPasswordToken(String username, String resetPasswordToken) {
        String status = "NEW";
        Long createdAt = System.currentTimeMillis();
        Object[] objects = new Object[]{username, resetPasswordToken, status, createdAt};
        if (jdbcTemplate.update(SAVE_DRAFT_TOKEN, objects) == 1)
            return true;
        else
            return false;
    }

    private static class UserRowMapper implements RowMapper<LoginUser> {

        @Override
        public LoginUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            String uuid = rs.getString("UUID");
            String username = rs.getString("USERNAME");
            String password = rs.getString("PASSWORD");
            String status = rs.getString("STATUS");

            LoginUser loginUser = new LoginUser();
            loginUser.setUuid(uuid);
            loginUser.setUsername(username);
            loginUser.setPassword(password);
            loginUser.setStatus(UserStatus.valueOf(status));
            return loginUser;
        }
    }

    private static class RoleRowMapper implements RowMapper<UserRoleMapper> {

        @Override
        public UserRoleMapper mapRow(ResultSet rs, int rowNum) throws SQLException {
            String uuid = rs.getString("UUID");
            UserRole userRole = UserRole.valueOf(rs.getString("ROLE_NAME"));

            UserRoleMapper userRoleMapper = new UserRoleMapper();
            userRoleMapper.setUuid(uuid);
            userRoleMapper.setUserRole(userRole);

            return userRoleMapper;
        }
    }

    public static class UserRoleMapper {
        String uuid;
        UserRole userRole;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public UserRole getUserRole() {
            return userRole;
        }

        public void setUserRole(UserRole userRole) {
            this.userRole = userRole;
        }
    }
}
