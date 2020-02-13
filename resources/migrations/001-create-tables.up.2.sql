-- users
INSERT INTO users (user_id, email, password) VALUES (1, 'user1@mail.com', 'bcrypt+sha512$86c036a7fdfb04e6ce685432f31073d6$12$60f512e74cd88a2f205efa91279f35b14653793569143588');
INSERT INTO users (user_id, email, password) VALUES (2, 'user2@mail.com', 'bcrypt+sha512$fb848fc63ccc873b7c655c9199ab915e$12$76c06803336336347ac3c48f934fb2d440a8d08693f91a10');
INSERT INTO users (user_id, email, password) VALUES (3, 'user3@mail.com', 'bcrypt+sha512$64dc6000167b2b9a8bca0c228cf7c636$12$68bbb3a5fa55dfd6e490679f0a70f9db90c895fac91eec9a');

-- roles
-- role for user #1
INSERT INTO roles (role_id, role) VALUES (1, 'role1_1');

-- roles for user #2
INSERT INTO roles (role_id, role) VALUES (2, 'role2_1');
INSERT INTO roles (role_id, role) VALUES (3, 'role2_2');

-- roles for user #3
INSERT INTO roles (role_id, role) VALUES (4, 'role3_1');
INSERT INTO roles (role_id, role) VALUES (5, 'role3_2');
INSERT INTO roles (role_id, role) VALUES (6, 'role3_3');

-- permissions
-- user1
INSERT INTO permissions (permission_id, permission) VALUES (1, 'permission1_1_1');

--user2
INSERT INTO permissions (permission_id, permission) VALUES (2, 'permission2_1_1');
INSERT INTO permissions (permission_id, permission) VALUES (3, 'permission2_1_2');
INSERT INTO permissions (permission_id, permission) VALUES (4, 'permission2_2_1');
INSERT INTO permissions (permission_id, permission) VALUES (5, 'permission2_2_2');

--user3
INSERT INTO permissions (permission_id, permission) VALUES (6, 'permission3_1_1');
INSERT INTO permissions (permission_id, permission) VALUES (7, 'permission3_1_2');
INSERT INTO permissions (permission_id, permission) VALUES (8, 'permission3_1_3');

INSERT INTO permissions (permission_id, permission) VALUES (9, 'permission3_2_1');
INSERT INTO permissions (permission_id, permission) VALUES (10, 'permission3_2_2');
INSERT INTO permissions (permission_id, permission) VALUES (11, 'permission3_2_3');

INSERT INTO permissions (permission_id, permission) VALUES (12, 'permission3_3_1');
INSERT INTO permissions (permission_id, permission) VALUES (13, 'permission3_3_2');
INSERT INTO permissions (permission_id, permission) VALUES (14, 'permission3_3_3');


-- user roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 3);

INSERT INTO user_roles (user_id, role_id) VALUES (3, 4);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 5);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 6);


-- user permissions
-- user1, role1_1
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1);

-- user2, role2_1
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 2);
INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 3);

-- user2, role2_2
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 4);
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 5);

-- user3, role3_1
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 6);
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 7);
INSERT INTO role_permissions (role_id, permission_id) VALUES (4, 8);

-- user3, role3_2
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 9);
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 10);
INSERT INTO role_permissions (role_id, permission_id) VALUES (5, 11);

-- user3, role3_3
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 12);
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 13);
INSERT INTO role_permissions (role_id, permission_id) VALUES (6, 14);
