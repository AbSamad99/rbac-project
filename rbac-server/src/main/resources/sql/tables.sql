create table users (
    id numeric(10,0) primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    username varchar(50) not null,
    email varchar(50) not null,
    phone_number varchar(50) not null,
    address varchar(255) not null,
    verification_info_id numeric(10,0) not null
);

ALTER TABLE users
ADD COLUMN created_at TIMESTAMP NOT NULL,
ADD COLUMN modified_at TIMESTAMP NOT NULL;

alter table users
add column key numeric(10,0),
add column version numeric(10,0),
add column is_active numeric(1,0);

alter table users
add column created_by numeric(10,0),
add column modified_by numeric(10,0);

alter table users drop column verification_info_id;
alter table users add column verification_info_id numeric(10,0);

CREATE TABLE user_verification_info (
    id NUMERIC(10,0) PRIMARY KEY,
    user_id NUMERIC(10,0) NOT NULL,
    hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    status NUMERIC(10,0) NOT NULL,
    password_reset_code VARCHAR(255) NOT NULL,
    password_reset_code_expiration TIMESTAMP NOT NULL,
    verification_code VARCHAR(255) NOT NULL,
    verification_code_expiration TIMESTAMP NOT NULL
);

ALTER TABLE user_verification_info
ALTER COLUMN password_reset_code DROP NOT NULL,
ALTER COLUMN password_reset_code_expiration DROP NOT NULL,
ALTER COLUMN verification_code DROP NOT NULL,
ALTER COLUMN verification_code_expiration DROP NOT NULL;

alter table user_verification_info
drop column salt;
alter table user_verification_info
drop column verification_code_expiration,
drop column verification_code;

alter table user_verification_info
add column lock_count numeric(10,0) default 0;
alter table user_verification_info
rename column user_id to user_key;
alter table user_verification_info
add column verification_code varchar(255);


CREATE TABLE generic_permissions (
  id NUMERIC(10,0) PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(50) NOT NULL
);


CREATE TABLE entity_permissions (
  id NUMERIC(10,0) PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(50) NOT NULL,
  entity_id NUMERIC(10,0) NOT NULL,
  generic_permission_id NUMERIC(10,0) NOT NULL,
  is_allowed BOOLEAN DEFAULT FALSE
);

alter table entity_permissions drop column is_allowed;
alter table entity_permissions add column is_allowed numeric(1,0);


CREATE TABLE role_permission_mapping (
  id NUMERIC(10,0) PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(50) NOT NULL,
  entity_id NUMERIC(10,0) NOT NULL,
  role_id NUMERIC(10,0) NOT NULL,
  generic_permission_id NUMERIC(10,0) NOT NULL,
  is_allowed BOOLEAN DEFAULT FALSE
);

alter table role_permission_mapping
drop column name,
drop column description,
drop column entity_id,
drop column generic_permission_id,
add column entity_permission_id numeric(10,0) not null;

alter table role_permission_mapping drop column is_allowed;
alter table role_permission_mapping add column is_allowed numeric(1,0);


CREATE TABLE roles (
  id NUMERIC(10,0) PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  created_by NUMERIC(10,0) NOT NULL,
  modified_at TIMESTAMP NOT NULL,
  modified_by NUMERIC(10,0) NOT NULL
);

alter table roles
add column key numeric(10,0),
add column version numeric(10,0),
add column is_active numeric(1,0);


CREATE TABLE user_role_mapping (
  id NUMERIC(10,0) PRIMARY KEY,
  user_id NUMERIC(10,0) NOT NULL,
  role_id NUMERIC(10,0) NOT NULL
);

alter table user_role_mapping
rename column role_id to role_key;

create table entities(
	id numeric(10,0) primary key,
	name varchar(50) not null,
	description varchar(50) not null
);

alter table entities
add column table_name varchar(50);

create table audit_log (
    id numeric(10,0) primary key,
    entity_id numeric(10,0) not null,
    action_id numeric(10,0) not null,
    new_id numeric(10,0) not null,
    previous_id numeric(10,0)
);
alter table audit_log
add column performed_at timestamp not null,
add column performed_by numeric(10,0) not null,
add column attempt_status numeric(10,0) not null
add column entity_item_name varchar(50);
alter table audit_log rename column action_id to permission_id;
alter table audit_log
alter new_id drop not null;


create table navbar (
	id numeric(10,0) primary key,
	name varchar(50) not null,
	description varchar(50) not null,
	is_shown numeric(10,0)
)

create table navbar_items(
	id numeric(10,0) primary key,
	name varchar(50) not null,
	description varchar(50) not null,
	entity_permission_id numeric(10,0) not null,
	link varchar(50) not null,
	is_shown numeric(10,0)
);
alter table navbar_items
add column navbar_id numeric(10,0) not null;



create table grid_column_config(
	id numeric(10,0) primary key,
	name varchar(50) not null,
	entity_id numeric(10,0) not null,
	column_name varchar(50),
	column_logical_name varchar,
	field_name varchar(50) not null,
	is_shown numeric(10,0) not null,
	show_default numeric(10,0) not null,
	data_type_id numeric(10,0) not null
);
alter table grid_column_config
add column dependant_table varchar(50),
add column dependant_column varchar(50),
add column dependant_sort_column varchar(50);
alter table grid_column_config
rename column dependant_sort_column to dependant_filter_column;

create table data_types(
	id numeric(10,0) primary key,
	name varchar(50) not null
);


create table filter_config(
	id numeric(10,0) primary key,
	name varchar(50) not null,
	entity_id numeric(10,0) not null,
	column_logical_name varchar,
	is_filterable numeric(10,0) not null,
	data_type_id numeric(10,0) not null
);
alter table filter_config
add column dependant_table varchar(50),
add column dependant_column varchar(50),
add column dependant_sort_column varchar(50);
alter table filter_config
rename column dependant_sort_column to dependant_filter_column;