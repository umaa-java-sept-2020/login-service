CREATE TABLE IF NOT EXISTS TBL_RESET_PASSWORD
(
USERNAME VARCHAR(256) NOT NULL,
RESET_PASSWORD_TOKEN VARCHAR(256) NOT NULL,
STATUS VARCHAR(40) NOT NULL,
CREATED_AT BIGINT NOT NULL
);

-- NEW
-- OLD