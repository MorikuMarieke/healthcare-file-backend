-- Roles
INSERT INTO roles (name) VALUES ('CLIENT');
INSERT INTO roles (name) VALUES ('EMPLOYEE');
INSERT INTO roles (name) VALUES ('ADMIN');

-- ADMIN user
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
VALUES (
           'admin@local.test',
           '$2a$12$NQpmP1wgsmtzF90lMZU.GO.jjaURL4jEWf75q/MOyIXHMgqyfsGq6',
           (SELECT id FROM roles WHERE name = 'ADMIN'),
           NOW(),
           NOW()
       );

-- password is Admin123!

-- EMPLOYEE user
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
VALUES (
           'employee@test.local',
           '$2a$12$kZcV5nXth0FzW/UPA7wChum1VMSpTBNcXDvnqklrqmhLIksxRC2ya',
           (SELECT id FROM roles WHERE name = 'EMPLOYEE'),
           NOW(),
           NOW()
       );

-- password is Employee123!

-- CLIENT PROFILE (no user linked yet)
INSERT INTO client_profiles (bsn, first_name, last_name, active, created_at)
VALUES (
           '123456789',
           'Test',
           'Client',
           true,
           NOW()
       );

-- CONTACT DETAILS linked to client profile
INSERT INTO contact_details (email, client_profile_id)
VALUES (
           'client@test.local',
           (SELECT id FROM client_profiles WHERE bsn = '123456789')
       );

-- CLIENT user
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
VALUES (
           'client@test.local',
           '$2a$12$tSLkYygquDx1dVBg2I1LwePoULcDUBrdLxwo/zbrjvvtVQVmKhOl.',
           (SELECT id FROM roles WHERE name = 'CLIENT'),
           NOW(),
           NOW()
       );

-- Link client profile to client user
UPDATE client_profiles
SET user_id = (SELECT id FROM users WHERE email = 'client@test.local')
WHERE bsn = '123456789';

