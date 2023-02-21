-- run it with this command: psql postgres -f ~/Downloads/playground.sql or you can use other command depend on the user that you've been created
-- drop db playground if exists
DROP DATABASE IF EXISTS playground;

-- create database playground
CREATE DATABASE playground;

-- check whether role is exist, if not it will create the role user
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT                       -- SELECT list can stay empty for this
      FROM   pg_catalog.pg_roles
      WHERE  rolname = 'postgres') THEN
      CREATE ROLE postgres;
      
   END IF;
END
$do$;

-- alter the owner to postgres
ALTER DATABASE playground OWNER TO postgres;

-- alter user for password
ALTER USER postgres WITH PASSWORD 'playground';

-- GIVE USER the capability to login on db
ALTER ROLE "postgres" WITH LOGIN; 

-- connect to playground then create the table
\c playground;


-- create the table in playground database
CREATE TABLE public.otps ( id SERIAL, email character varying(50) NOT NULL, otp integer, user_id integer, CONSTRAINT otps_otp_check CHECK ((otp > 999)) );
CREATE TABLE public.users ( id SERIAL, name character varying(50) NOT NULL, email character varying(50) NOT NULL, token character varying(50) );
CREATE TABLE public.todos ( id SERIAL, user_name character varying(50) NOT NULL, message character varying(250) NOT NULL, user_id integer );

-- grant all privileges to database playground for postgres
GRANT ALL PRIVILEGES ON DATABASE playground to postgres;
ALTER TABLE public.otps OWNER to postgres;
ALTER TABLE public.users OWNER to postgres;
ALTER TABLE public.todos OWNER to postgres;