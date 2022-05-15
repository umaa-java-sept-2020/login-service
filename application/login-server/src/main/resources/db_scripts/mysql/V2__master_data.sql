CREATE TABLE IF NOT EXISTS TBL_LOGIN_USER
(
UUID VARCHAR(100) PRIMARY KEY,
USERNAME VARCHAR(256) UNIQUE NOT NULL,
PASSWORD VARCHAR(256) NOT NULL,
STATUS VARCHAR(40) NOT NULL
);

INSERT INTO TBL_LOGIN_USER VALUES
(
'user-uuid1','x12345','password@123!','DRAFT'
),
(
'user-uuid2','x12346','password@123!','DRAFT'
),
(
'user-uuid3','x12347','password@123!','DRAFT'
),
(
 'user-uuid4','x12348','password@123!','DRAFT'
);

INSERT INTO TBL_LOGIN_ROLE VALUES
('role-uuid1','ADMIN'),
('role-uuid2','USER');

INSERT INTO TBL_LOGIN_USER_ROLE_MAPPING VALUES
('user-role-uuid1','user-uuid1','role-uuid1'),
('user-role-uuid2','user-uuid2','role-uuid1'),
('user-role-uuid3','user-uuid3','role-uuid1'),
('user-role-uuid4','user-uuid4','role-uuid1');

