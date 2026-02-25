-- =========================
-- ROLES (idempotent)
-- =========================
INSERT INTO roles (name)
SELECT 'CLIENT'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CLIENT');

INSERT INTO roles (name)
SELECT 'EMPLOYEE'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'EMPLOYEE');

INSERT INTO roles (name)
SELECT 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

-- =========================
-- ADMIN USER (password = Admin123!)
-- =========================
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
SELECT
    'admin@local.test',
    '$2a$12$NQpmP1wgsmtzF90lMZU.GO.jjaURL4jEWf75q/MOyIXHMgqyfsGq6',
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    NOW(),
    NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@local.test');

-- =========================
-- EMPLOYEE USER (password = Employee123!)
-- =========================
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
SELECT
    'employee@test.local',
    '$2a$12$kZcV5nXth0FzW/UPA7wChum1VMSpTBNcXDvnqklrqmhLIksxRC2ya',
    (SELECT id FROM roles WHERE name = 'EMPLOYEE'),
    NOW(),
    NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'employee@test.local');

-- =========================
-- EMPLOYEE PROFILE (MapsId: id == user_id)
INSERT INTO employee_profiles (user_id, first_name, last_name, work_phone_number, personal_phone_number, personal_email)
SELECT
    u.id,
    'Test',
    'Employee',
    '0611111111',
    '0622222222',
    'employee.personal@test.local'
FROM users u
WHERE u.email = 'employee@test.local'
  AND NOT EXISTS (
    SELECT 1
    FROM employee_profiles ep
    WHERE ep.user_id = u.id
);

-- =========================
-- CLIENT PROFILE (no user linked yet) + required fields birth_date + sex
-- =========================
INSERT INTO client_profiles (bsn, first_name, last_name, birth_date, sex, active, created_at)
SELECT
    '123456789',
    'Test',
    'Client',
    DATE '1999-01-01',
    'FEMALE',
    true,
    NOW()
    WHERE NOT EXISTS (SELECT 1 FROM client_profiles WHERE bsn = '123456789');

-- CONTACT DETAILS linked to client profile
INSERT INTO contact_details (email, client_profile_id)
SELECT
    'client@test.local',
    (SELECT id FROM client_profiles WHERE bsn = '123456789')
    WHERE NOT EXISTS (
    SELECT 1
    FROM contact_details cd
    WHERE cd.client_profile_id = (SELECT id FROM client_profiles WHERE bsn = '123456789')
);

-- =========================
-- CLIENT USER (password = Client123!)
-- =========================
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
SELECT
    'client@test.local',
    '$2a$12$tSLkYygquDx1dVBg2I1LwePoULcDUBrdLxwo/zbrjvvtVQVmKhOl.',
    (SELECT id FROM roles WHERE name = 'CLIENT'),
    NOW(),
    NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'client@test.local');

-- Link client profile to client user
UPDATE client_profiles
SET user_id = (SELECT id FROM users WHERE email = 'client@test.local')
WHERE bsn = '123456789'
  AND user_id IS NULL;
