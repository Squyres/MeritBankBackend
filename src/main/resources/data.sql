INSERT INTO account_holder(id, username, password, authority, active, value) 
	VALUES(0, 'admin', 'admin', 'ADMIN', TRUE, 0)
	ON DUPLICATE KEY UPDATE authority = 'ADMIN';