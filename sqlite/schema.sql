create table PROJECT
(
    id          INTEGER not null
        constraint PROJECT_pk
            primary key autoincrement,
    name        TEXT    not null,
    code        TEXT    not null,
    description TEXT
);

create table ROLE
(
    ID   INTEGER     not null
        primary key autoincrement,
    NAME varchar(50) not null
        unique
);

create table USER
(
    ID        INTEGER           not null
        primary key autoincrement,
    USER_NAME varchar(50)       not null
        unique,
    PASSWORD  varchar,
    role_id   INTEGER default 1 not null
        constraint USER_to_ROLE_fk
            references ROLE,
    IMAGE     TEXT
);

create table ISSUE
(
    id               INTEGER not null
        constraint ISSUE_pk
            primary key autoincrement,
    title            TEXT    not null,
    description      TEXT,
    project_id       INTEGER not null
        constraint ISSUE_PROJECT_fk
            references PROJECT
            on delete cascade,
    user_id          INTEGER
        constraint ISSUE_USER_null_fk
            references USER
            on delete set null,
    status           TEXT    not null,
    estimated_points INTEGER null on conflict replace,
    real_points      INTEGER
);

create table ISSUE_HISTORICAL
(
    id         INTEGER                             not null
        constraint ISSUE_HISTORICAL_pk
            primary key,
    issue_id   INTEGER                             not null
        constraint ISSUE_HISTORICAL_ISSUE_fk
            references ISSUE
            on delete cascade,
    user_id    INTEGER
        constraint ISSUE_HISTORICAL_USER_ID_fk
            references USER
            on delete set null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null
);

create table PROJECT_USER
(
    user_id    INTEGER not null
        constraint PROJECT_USER_USER_fk
            references USER
            on delete cascade,
    project_id INTEGER not null
        constraint PROJECT_USER_PROJECT_fk
            references PROJECT
            on delete cascade,
    constraint PROJECT_USER_pk
        primary key (user_id, project_id)
);

create table sqlite_master
(
    type     TEXT,
    name     TEXT,
    tbl_name TEXT,
    rootpage INT,
    sql      TEXT
);

create table sqlite_sequence
(
    name,
    seq
);
