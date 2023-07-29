insert into generic_permissions values
(1,'Create','Create new item.'),
(2,'View','View an item.'),
(3,'Edit','Make changes to existing item.'),
(4,'Delete','Delete an item.'),
(5,'Copy','Copy an item'),
(11,'Change Password','Change a users password');

insert into entity_permissions values
(1,'Create','Create new user.',1,1,1),
(2,'View','View a user.',1,2,1),
(3,'Edit','Make changes to existing user.',1,3,1),
(4,'Delete','Delete a user.',1,4,1),
(5,'Change Password','Change a users password',1,11,1),
(6,'Copy','Copy a user.',1,5,1);

insert into entity_permissions values
(11,'Create','Create new role.',2,1,1),
(12,'View','View a role.',2,2,1),
(13,'Edit','Make changes to existing role.',2,3,1),
(14,'Delete','Delete a role.',2,4,1),
(15,'Copy','Copy a roles.',2,5,1);

insert into entity_permissions values
(21,'View','View an audit log.',3,2,1);

insert into entities values
(1,'User','User entity','users'),
(2,'Role','Role entity','roles'),
(3,'Audit Log','Audit Long entity','audit_log');

insert into roles values
(1,'admin',current_timestamp,1,current_timestamp,1,1,1,1);

insert into role_permission_mapping values
(1,1,1,1),
(2,1,2,1),
(3,1,3,1),
(4,1,4,1),
(5,1,11,1),
(6,1,12,1),
(7,1,13,1),
(8,1,14,1),
(9,1,21,1),
(10,1,5,1),
(11,1,6,1),
(12,1,15,1);

insert into users values
(1,'admin','admin','admin','admin@gmail.com','123456789','123 Main Street',current_timestamp,current_timestamp,1,1,1,1,1,1);

insert into user_verification_info values
(1,1,1,null,null,'$2a$10$AxMZm3mP6OhG8w0CYqWo5u8U8sA7v7DCe/5kOiMXvqB7WRzdi9Mb.');

insert into user_role_mapping values
(1,1,1);

insert into navbar values
(1,'Main-1','Main-1',1),
(2,'Main-2','Main-2',1);

insert into navbar_items values
(1,'User','User',2,'/users',1,1),
(2,'Role','Role',12,'/roles',1,2);


insert into data_types values
(1,'Integer'),
(2,'String'),
(3,'Boolean'),
(4,'Date'),
(5,'Long');

insert into grid_column_config values
(1,'First Name',1,'first_name','firstName','firstName',1,1,2),
(2,'Last Name',1,'last_name','lastName','lastName',1,0,2),
(3,'Username',1,'username','username','username',1,1,2),
(4,'Created By',1,'created_by','createdBy','createdByName',1,1,2,'User','id','first_name'),
(5,'Created At',1,'created_at','createdAt','createdAt',1,1,4);

insert into grid_column_config values
(11,'Name',2,'name','name','name',1,1,2),
(12,'Created By',2,'created_by','createdBy','createdByName',1,1,2,'User','id','first_name'),
(13,'Created At',2,'created_at','createdAt','createdAt',1,1,4);

insert into grid_column_config values
(21,'Entity',3,'entity_id','entityId','entityName',1,1,2),
(22,'Action',3,'permission_id','permissionId','permissionName',1,1,2),
--(23,'Username',1,'username','username','username',1,1,2),
(24,'Performed By',3,'created_by','createdBy','createdByName',1,1,2,'User','id','first_name'),
(25,'Performed At',3,'created_at','createdAt','createdAt',1,1,4),
(26,'Attempt Status',3,'attempt_status','attemptStatus','attemptStatusString',1,1,4);


insert into filter_config values
(1,'First Name',1,'firstName',1,2),
(2,'Last Name',1,'lastName',1,2),
(3,'Username',1,'username',1,2),
(4,'Created By',1,'createdBy',1,2,'User','id','firstName');