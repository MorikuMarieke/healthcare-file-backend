-- 1. roles
INSERT INTO roles (name)
VALUES ('CLIENT') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name)
VALUES ('EMPLOYEE') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name)
VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;


-- 2. admin user -- password is Admin123!
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
VALUES ('admin@local.test',
        '$2a$12$NQpmP1wgsmtzF90lMZU.GO.jjaURL4jEWf75q/MOyIXHMgqyfsGq6',
        (SELECT id FROM roles WHERE name = 'ADMIN'),
        NOW(),
        NOW()) ON CONFLICT (email) DO NOTHING;

-- 3. employee user -- password is Employee123!
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
VALUES ('employee@test.local',
        '$2a$12$kZcV5nXth0FzW/UPA7wChum1VMSpTBNcXDvnqklrqmhLIksxRC2ya',
        (SELECT id FROM roles WHERE name = 'EMPLOYEE'),
        NOW(),
        NOW());
ON CONFLICT (email) DO NOTHING;

-- 4. employee profile
INSERT INTO employee_profiles (first_name, last_name, user_id)
VALUES (
           'Test',
           'Employee',
           (SELECT id FROM users WHERE email = 'employee@test.local')
       )
    ON CONFLICT (user_id) DO NOTHING;

-- 5. care_team
INSERT INTO care_teams (id, team_name, team_phone_number, team_email)
VALUES (1, 'Wijkteam Noord', '0612345678', 'noord@zorg.nl')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO care_teams (id, team_name, team_phone_number, team_email)
VALUES (2, 'Wijkteam Zuid', '0687654321', 'zuid@zorg.nl')
    ON CONFLICT (id) DO NOTHING;

-- 6. link employee_profile to care_team
INSERT INTO care_team_members (care_team_id, employee_profile_id)
VALUES (
           (SELECT id FROM care_teams WHERE team_name = 'Wijkteam Noord'),
           (SELECT id FROM employee_profiles
            WHERE user_id = (SELECT id FROM users WHERE email = 'employee@test.local'))
       )
    ON CONFLICT DO NOTHING;

-- CLIENT PROFILE (no user linked yet)
-- 10 client profiles (zonder accounts: user_id blijft NULL)
INSERT INTO client_profiles (id, bsn, first_name, last_name, active, created_at, user_id)
VALUES
    (1, '100000001', 'Client', 'One',   true, now(), NULL),
    (2, '100000002', 'Client', 'Two',   true, now(), NULL),
    (3, '100000003', 'Client', 'Three', true, now(), NULL),
    (4, '100000004', 'Client', 'Four',  true, now(), NULL),
    (5, '100000005', 'Client', 'Five',  true, now(), NULL),
    (6, '100000006', 'Client', 'Six',   true, now(), NULL),
    (7, '100000007', 'Client', 'Seven', true, now(), NULL),
    (8, '100000008', 'Client', 'Eight', true, now(), NULL),
    (9, '100000009', 'Client', 'Nine',  true, now(), NULL),
    (10,'100000010', 'Client', 'Ten',   true, now(), NULL)
    ON CONFLICT (id) DO NOTHING;

-- CONTACT DETAILS linked to client profile
INSERT INTO contact_details (email, client_profile_id)
VALUES ('client@test.local',
        (SELECT id FROM client_profiles WHERE bsn = '123456789'));

-- CLIENT user
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
VALUES ('client@test.local',
        '$2a$12$tSLkYygquDx1dVBg2I1LwePoULcDUBrdLxwo/zbrjvvtVQVmKhOl.',
        (SELECT id FROM roles WHERE name = 'CLIENT'),
        NOW(),
        NOW());

-- Link client profile to client user
UPDATE client_profiles
SET user_id = (SELECT id FROM users WHERE email = 'client@test.local')
WHERE bsn = '123456789';

