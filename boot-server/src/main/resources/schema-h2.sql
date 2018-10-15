DROP TABLE IF EXISTS EXO_ACCOUNT;
CREATE TABLE IF NOT EXISTS EXO_ACCOUNT (
  ID VARCHAR(36) PRIMARY KEY,
  PHONE VARCHAR(30),
  VERIFIED BIT,
  VERIFICATION_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  C_SPAN_ID VARCHAR(100),
  C_TIME TIMESTAMP,
  C_TRACE_ID VARCHAR(100),
  DATA_VERSION BIGINT,
  M_SPAN_ID VARCHAR(100),
  M_TIME TIMESTAMP,
  M_TRACE_ID VARCHAR(100)
);

DROP TABLE IF EXISTS LOGIN_CHANNEL;
CREATE TABLE IF NOT EXISTS LOGIN_CHANNEL (
  ID VARCHAR(36) PRIMARY KEY,
  TITLE VARCHAR(100),
  DESCRIPTION VARCHAR(255),
  C_SPAN_ID VARCHAR(100),
  C_TIME TIMESTAMP,
  C_TRACE_ID VARCHAR(100),
  DATA_VERSION BIGINT,
  M_SPAN_ID VARCHAR(100),
  M_TIME TIMESTAMP,
  M_TRACE_ID VARCHAR(100)
);

DROP TABLE IF EXISTS LOGIN_CREDENTIALS;
CREATE TABLE IF NOT EXISTS LOGIN_CREDENTIALS (
  CHANNEL_ID VARCHAR(36),
  USER_ID VARCHAR(100),
  PASSWORD VARCHAR(100) DEFAULT '',
  EXO_ID VARCHAR(100),
  C_SPAN_ID VARCHAR(100),
  C_TIME TIMESTAMP,
  C_TRACE_ID VARCHAR(100),
  DATA_VERSION BIGINT,
  M_SPAN_ID VARCHAR(100),
  M_TIME TIMESTAMP,
  M_TRACE_ID VARCHAR(100),

  FOREIGN KEY(EXO_ID) REFERENCES EXO_ACCOUNT(ID),
  FOREIGN KEY(CHANNEL_ID) REFERENCES LOGIN_CHANNEL(ID)
);

DROP TABLE IF EXISTS AXONIQ_GDPR_KEYS;
create table AXONIQ_GDPR_KEYS (
  KEY_ID VARCHAR(255) NOT NULL,
  SECRET_KEY VARCHAR(255),
  PRIMARY KEY (KEY_ID)
);
