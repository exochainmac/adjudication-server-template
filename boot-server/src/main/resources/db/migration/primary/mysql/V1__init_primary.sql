-- ------------------------------------------------
-- Axon Framework Support Tables

create table association_value_entry (
  id bigint not null auto_increment,
  association_key varchar(255) not null,
  association_value varchar(255),
  saga_id varchar(255) not null,
  saga_type varchar(255),
  primary key (id)
) engine=InnoDB;

create table saga_entry (
  saga_id varchar(255) not null,
  revision varchar(255),
  saga_type varchar(255),
  serialized_saga longblob,
  primary key (saga_id)
) engine=InnoDB;

create table token_entry (
  processor_name varchar(255) not null,
  segment integer not null,
  owner varchar(255),
  timestamp varchar(255) not null,
  token longblob,
  token_type varchar(255),
  primary key (processor_name, segment)
) engine=InnoDB;

create index IDXk45eqnxkgd8hpdn6xixn8sgft on association_value_entry (saga_type, association_key, association_value);
create index IDXgv5k1v2mh6frxuy5c0hgbau94 on association_value_entry (saga_id, saga_type);

-- Axon GDPR Key storage table

create table axoniq_gdpr_keys (
  key_id varchar(255) not null,
  secret_key varchar(255),
  primary key (key_id)
) engine=InnoDB;

-- ------------------------------------------------
-- Exochain Tables

create table exo_account (
  id varchar(255) not null,
  c_span_id varchar(255),
  c_time datetime(6),
  c_trace_id varchar(255),
  data_version bigint,
  m_span_id varchar(255),
  m_time datetime(6),
  m_trace_id varchar(255),
  cell_verified bit not null,
  email_verified bit not null,
  ethereum_account varchar(255),
  password varchar(255),
  primary_cell varchar(255),
  primary_email varchar(255),
  user_id varchar(255),
  user_id_hash varchar(255),
  primary key (id)
) engine=InnoDB;

create table login_channel (
  id varchar(255) not null,
  description varchar(255),
  title varchar(255),
  primary key (id)
) engine=InnoDB;

create table login_channel_account (
  channel_id varchar(255) not null,
  exo_id varchar(255) not null,
  channel_user_id varchar(255),
  password varchar(255),
  primary key (channel_id, exo_id)
) engine=InnoDB;

create table claim_type_summary (
  id varchar(255) not null,
  c_span_id varchar(255),
  c_time datetime(6),
  c_trace_id varchar(255),
  data_version bigint,
  m_span_id varchar(255),
  m_time datetime(6),
  m_trace_id varchar(255),
  description varchar(255),
  title varchar(255),
  primary key (id)
) engine=InnoDB;

alter table exo_account
  add constraint UK1ap2ijxk27f7v9dtj7t5pq0nt unique (user_id_hash);
