create sequence global_event_index_seq start with 1 increment by 1;
create sequence hibernate_sequence start with 1 increment by 1;

    create table association_value_entry (
       id number(19,0) not null,
        association_key varchar2(255 char) not null,
        association_value varchar2(255 char),
        saga_id varchar2(255 char) not null,
        saga_type varchar2(255 char),
        primary key (id)
    );

    create table axoniq_gdpr_keys (
       key_id varchar2(255 char) not null,
        secret_key varchar2(255 char),
        primary key (key_id)
    );

    create table claim_type_summary (
       id varchar2(255 char) not null,
        c_span_id varchar2(255 char),
        c_time timestamp,
        c_trace_id varchar2(255 char),
        data_version number(19,0),
        m_span_id varchar2(255 char),
        m_time timestamp,
        m_trace_id varchar2(255 char),
        description varchar2(255 char),
        title varchar2(255 char),
        primary key (id)
    );

    create table domain_event_entry (
       global_index number(19,0) not null,
        event_identifier varchar2(255 char) not null,
        meta_data blob,
        payload blob not null,
        payload_revision varchar2(255 char),
        payload_type varchar2(255 char) not null,
        time_stamp varchar2(255 char) not null,
        aggregate_identifier varchar2(255 char) not null,
        sequence_number number(19,0) not null,
        type varchar2(255 char),
        primary key (global_index)
    );

    create table exo_account (
       id varchar2(255 char) not null,
        c_span_id varchar2(255 char),
        c_time timestamp,
        c_trace_id varchar2(255 char),
        data_version number(19,0),
        m_span_id varchar2(255 char),
        m_time timestamp,
        m_trace_id varchar2(255 char),
        cell_verified number(1,0) not null,
        email_verified number(1,0) not null,
        ethereum_account varchar2(255 char),
        password varchar2(255 char),
        primary_cell varchar2(255 char),
        primary_email varchar2(255 char),
        user_id varchar2(255 char),
        user_id_hash varchar2(255 char),
        primary key (id)
    );

    create table login_channel (
       id varchar2(255 char) not null,
        description varchar2(255 char),
        title varchar2(255 char),
        primary key (id)
    );

    create table login_channel_account (
       channel_id varchar2(255 char) not null,
        exo_id varchar2(255 char) not null,
        channel_user_id varchar2(255 char),
        password varchar2(255 char),
        primary key (channel_id, exo_id)
    );

    create table saga_entry (
       saga_id varchar2(255 char) not null,
        revision varchar2(255 char),
        saga_type varchar2(255 char),
        serialized_saga blob,
        primary key (saga_id)
    );

    create table snapshot_event_entry (
       aggregate_identifier varchar2(255 char) not null,
        sequence_number number(19,0) not null,
        type varchar2(255 char) not null,
        event_identifier varchar2(255 char) not null,
        meta_data blob,
        payload blob not null,
        payload_revision varchar2(255 char),
        payload_type varchar2(255 char) not null,
        time_stamp varchar2(255 char) not null,
        primary key (aggregate_identifier, sequence_number, type)
    );

    create table token_entry (
       processor_name varchar2(255 char) not null,
        segment number(10,0) not null,
        owner varchar2(255 char),
        timestamp varchar2(255 char) not null,
        token blob,
        token_type varchar2(255 char),
        primary key (processor_name, segment)
    );
create index IDXk45eqnxkgd8hpdn6xixn8sgft on association_value_entry (saga_type, association_key, association_value);
create index IDXgv5k1v2mh6frxuy5c0hgbau94 on association_value_entry (saga_id, saga_type);

    alter table domain_event_entry 
       add constraint UK8s1f994p4la2ipb13me2xqm1w unique (aggregate_identifier, sequence_number);

    alter table domain_event_entry 
       add constraint UK_fwe6lsa8bfo6hyas6ud3m8c7x unique (event_identifier);

    alter table exo_account 
       add constraint UK1ap2ijxk27f7v9dtj7t5pq0nt unique (user_id_hash);

    alter table snapshot_event_entry 
       add constraint UK_e1uucjseo68gopmnd0vgdl44h unique (event_identifier);
