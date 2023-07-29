select * from entity_permissions ep 
where ep.id in 
	(select rpm.entity_permission_id from role_permission_mapping rpm 
	 where rpm.role_id in 
	 	(select r.id from roles r where 
		 	r.is_active=1 and
			r.id in (select urm.role_id from user_role_mapping urm where urm.user_id=1043)
		)
	);