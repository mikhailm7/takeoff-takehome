CREATE TABLE IF NOT EXISTS users (
  user_id bigserial PRIMARY KEY,
  email VARCHAR (320) UNIQUE NOT NULL,
  password varchar(255) NOT NULL,
  created_at timestamp NOT NULL default current_timestamp
);

CREATE TABLE IF NOT EXISTS roles (
   role_id serial PRIMARY KEY,
   role VARCHAR (255) UNIQUE NOT NULL,
   created_at timestamp NOT NULL default current_timestamp
);

CREATE TABLE IF NOT EXISTS permissions (
   permission_id serial PRIMARY KEY,
   permission VARCHAR (255) UNIQUE NOT NULL,
   created_at timestamp NOT NULL default current_timestamp
);

CREATE TABLE IF NOT EXISTS user_roles (
   user_id integer NOT NULL,
   role_id integer NOT NULL,
   created_at timestamp NOT NULL default current_timestamp,
   PRIMARY KEY (user_id, role_id),
   CONSTRAINT user_role_user_id_fkey FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT user_role_role_id_fkey FOREIGN KEY (role_id)
      REFERENCES roles (role_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS role_permissions (
   role_id integer NOT NULL,
   permission_id integer NOT NULL,
   created_at timestamp NOT NULL default current_timestamp,
   PRIMARY KEY (role_id, permission_id),
   CONSTRAINT role_permission_id_fkey FOREIGN KEY (role_id)
      REFERENCES roles (role_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT role_permission_permission_id_fkey FOREIGN KEY (permission_id)
      REFERENCES permissions (permission_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
