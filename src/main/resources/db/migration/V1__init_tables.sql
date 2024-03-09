create table areas
(
    id          bigserial
        primary key,
    title       varchar(50) not null,
    description varchar(1000)
);

create table tests
(
    id          bigserial
        primary key,
    title       varchar(50) not null,
    description varchar(1000)
);

create table candidates
(
    id          bigserial
        primary key,
    lastname    varchar(20) not null,
    name        varchar(20) not null,
    patronymic  varchar(20) not null,
    description varchar(1000),
    cv_file     varchar(100),
    photo       varchar(100)
);

create table candidates_tests
(
    id              bigserial
        primary key,
    candidate_id    bigint                    not null
        constraint cns_candidate_id
            references candidates,
    test_id         bigint                    not null
        constraint cns_test_id
            references tests,
    date_of_passing date default CURRENT_DATE not null,
    score           integer                   not null
);

create table possible_areas_for_candidates
(
    area_id      bigint not null
        constraint cns_area_id
            references areas,
    candidate_id bigint not null
        constraint cns_candidate_id
            references candidates,
    primary key (area_id, candidate_id)
);

create table compatible_tests_and_areas
(
    area_id bigint not null
        constraint cns_area_id
            references areas,
    test_id bigint not null
        constraint cns_test_id
            references tests,
    primary key (area_id, test_id)
);