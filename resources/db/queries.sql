-- name: fetch-user-count-by-email
-- single?: true
SELECT
	COUNT(*)
FROM
	users
WHERE
	email = :email


-- name: fetch-user-by-email
SELECT
	email,
	password
FROM
	users
WHERE
	email = :email


-- name: fetch-user-permissions-by-email
SELECT
	permissions.permission
FROM
	users,
	user_roles,
	role_permissions,
	permissions
WHERE
	users.email = :email
	AND user_roles.user_id = users.user_id
	AND role_permissions.role_id = user_roles.role_id
	AND permissions.permission_id = role_permissions.permission_id
