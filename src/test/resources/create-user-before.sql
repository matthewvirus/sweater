delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$LN21POEch.GQQ23WTuY/dOiiedz3QevPkuGi6FKoez1y2bMT4JcFy', 'Mike'),
(2, true, '$2a$08$LN21POEch.GQQ23WTuY/dOiiedz3QevPkuGi6FKoez1y2bMT4JcFy', 'Mot');

insert into user_role(user_id, role_set) values
(1, 'ROLE_USER'),
(2, 'ROLE_USER'), (2, 'ROLE_ADMIN');