create table course_request
(
    id          bigint(20) auto_increment primary key,
    name        varchar(50)                        not null,
    type        varchar(30)                        not null,
    description varchar(200)                       null,
    contract_id bigint(20)                         null,
    created_at  datetime                           not null,
    expired_at  datetime                           not null
)
    comment '课程录制请求';

create index course_request_name_type_index
    on course_request (name, type);
