insert into usr (id, username, password, active)
values (1, 'Admin', 'root', true);

insert into user_role (user_id, role_set)
values (1, 'ROLE_USER'), (1, 'ROLE_ADMIN');