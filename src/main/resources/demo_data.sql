insert into users (name,username,password,session)
values ('Aboba', 'asd@gmail.com', '$2a$10$3jRAtonCE7HN4dZ1tkXCS.guptFexNuKOGhJJfMxqNsYKFgyy5Isi', 1),
       ('Dima', 'dima@gmail.com', '$2a$10$YFzZLWPq.hHM59Tb8V1T3eDUW3BRBHfwt1ej2QsnJyxPQ6qp5BhcG',1);

insert into users_roles (user_id, role)
values (1,'ROLE_ADMIN'),
       (2,'ROLE_PARTICIPANT');
