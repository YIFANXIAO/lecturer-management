create table course_payment_request
(
    id          bigint(20) auto_increment primary key,
    course_id   bigint(20)                         not null,
    amount      bigint(20)                         not null,
    description varchar(200)                       null,
    created_at  datetime                           not null,
    expired_at  datetime                           not null
)
    comment '课程付款请求';
