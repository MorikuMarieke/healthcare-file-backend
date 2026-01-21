-- Roles
INSERT INTO roles (name) VALUES ('CLIENT');
INSERT INTO roles (name) VALUES ('EMPLOYEE');
INSERT INTO roles (name) VALUES ('ADMIN');

-- Client profiles
INSERT INTO client_profiles (bsn, first_name, last_name, active, created_at, user_id)
VALUES
    ('123456782', 'Mali', 'Test', true, now(), NULL),
    ('111222333', 'Sam', 'Example', true, now(), NULL);
