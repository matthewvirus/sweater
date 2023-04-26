delete from message;

alter table message auto_increment 1;

insert into message (id, tag, text, user_id) values
(1, 'tag', 'text1', 1),
(2, 'tag1', 'text2', 2),
(3, 'tag2', 'text3', 1),
(4, 'tag3', 'text4', 2),
(5, 'tag1', 'text5', 2);

alter table message auto_increment 10;