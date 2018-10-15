-- ------------------------------------------------
-- Axon Framework support tables

create table domain_event_entry (
  global_index         bigint       not null auto_increment,
  event_identifier     varchar(255) not null,
  meta_data            longblob,
  payload              longblob     not null,
  payload_revision     varchar(255),
  payload_type         varchar(255) not null,
  time_stamp           varchar(255) not null,
  aggregate_identifier varchar(255) not null,
  sequence_number      bigint       not null,
  type                 varchar(255),
  primary key (global_index)
)
  engine = InnoDB;


create table snapshot_event_entry (
  aggregate_identifier varchar(255) not null,
  sequence_number      bigint       not null,
  type                 varchar(255) not null,
  event_identifier     varchar(255) not null,
  meta_data            longblob,
  payload              longblob     not null,
  payload_revision     varchar(255),
  payload_type         varchar(255) not null,
  time_stamp           varchar(255) not null,
  primary key (aggregate_identifier, sequence_number, type)
)
  engine = InnoDB;

alter table domain_event_entry
  add constraint UK8s1f994p4la2ipb13me2xqm1w unique (aggregate_identifier, sequence_number);

alter table domain_event_entry
  add constraint UK_fwe6lsa8bfo6hyas6ud3m8c7x unique (event_identifier);

alter table snapshot_event_entry
  add constraint UK_e1uucjseo68gopmnd0vgdl44h unique (event_identifier);
