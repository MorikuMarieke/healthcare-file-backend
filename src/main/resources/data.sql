-- =========================================================
-- SEED: Healthcare File Backend
-- =========================================================

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
    r.id,
    NOW(),
    NOW()
FROM roles r
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'admin@local.test');

-- =========================
-- EMPLOYEE USER 1 (password = Employee123!)
-- =========================
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
SELECT
    'employee@test.local',
    '$2a$12$kZcV5nXth0FzW/UPA7wChum1VMSpTBNcXDvnqklrqmhLIksxRC2ya',
    r.id,
    NOW(),
    NOW()
FROM roles r
WHERE r.name = 'EMPLOYEE'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'employee@test.local');

-- EMPLOYEE PROFILE 1 (MapsId: id == user_id)
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
-- EMPLOYEE USER 2 (password = Employee123!)
-- =========================
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
SELECT
    'employee2@test.local',
    '$2a$12$kZcV5nXth0FzW/UPA7wChum1VMSpTBNcXDvnqklrqmhLIksxRC2ya',
    r.id,
    NOW(),
    NOW()
FROM roles r
WHERE r.name = 'EMPLOYEE'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'employee2@test.local');

-- EMPLOYEE PROFILE 2 (MapsId: id == user_id)
INSERT INTO employee_profiles (user_id, first_name, last_name, work_phone_number, personal_phone_number, personal_email)
SELECT
    u.id,
    'Test2',
    'Employee',
    '0633333333',
    '0644444444',
    'employee2.personal@test.local'
FROM users u
WHERE u.email = 'employee2@test.local'
  AND NOT EXISTS (
    SELECT 1
    FROM employee_profiles ep
    WHERE ep.user_id = u.id
);

-- =========================
-- CLIENT PROFILE
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
    WHERE NOT EXISTS (SELECT 1 FROM client_profiles cp WHERE cp.bsn = '123456789');

-- CONTACT DETAILS
INSERT INTO contact_details (email, client_profile_id)
SELECT
    'client@test.local',
    cp.id
FROM client_profiles cp
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM contact_details cd
    WHERE cd.client_profile_id = cp.id
);

-- =========================
-- CLIENT USER (password = Client123!)
-- =========================
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
SELECT
    'client@test.local',
    '$2a$12$tSLkYygquDx1dVBg2I1LwePoULcDUBrdLxwo/zbrjvvtVQVmKhOl.',
    r.id,
    NOW(),
    NOW()
FROM roles r
WHERE r.name = 'CLIENT'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'client@test.local');

-- Link client profile to client user
UPDATE client_profiles
SET user_id = (SELECT u.id FROM users u WHERE u.email = 'client@test.local')
WHERE bsn = '123456789'
  AND user_id IS NULL;

-- =========================
-- CARE TEAM (seed)
-- =========================
INSERT INTO care_teams (team_name, team_phone_number, team_email)
SELECT
    'Seed Team A',
    '0612345678',
    'teamA@test.local'
    WHERE NOT EXISTS (
    SELECT 1
    FROM care_teams ct
    WHERE ct.team_email = 'teamA@test.local'
);

-- =========================
-- CARE PLAN (seed for the seeded client)
-- notes NOT NULL
-- =========================
INSERT INTO care_plans (client_profile_id, notes)
SELECT
    cp.id,
    ''
FROM client_profiles cp
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM care_plans cpl
    WHERE cpl.client_profile_id = cp.id
);

-- =========================
-- REPORTS (seed)
-- =========================
INSERT INTO reports (title, text, created_at, updated_at, care_plan_id, author_employee_id)
SELECT
    'Intake verslag',
    'Client oogt vermoeid, intake gesprek gevoerd.',
    NOW(),
    NOW(),
    cpl.id,
    u.id
FROM care_plans cpl
         JOIN client_profiles cp ON cp.id = cpl.client_profile_id
         JOIN users u ON u.email = 'employee@test.local'
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM reports r
    WHERE r.title = 'Intake verslag'
      AND r.care_plan_id = cpl.id
);

INSERT INTO reports (title, text, created_at, updated_at, care_plan_id, author_employee_id)
SELECT
    'Weekrapportage',
    'Doel besproken en acties afgestemd.',
    NOW(),
    NOW(),
    cpl.id,
    u.id
FROM care_plans cpl
         JOIN client_profiles cp ON cp.id = cpl.client_profile_id
         JOIN users u ON u.email = 'employee@test.local'
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM reports r
    WHERE r.title = 'Weekrapportage'
      AND r.care_plan_id = cpl.id
);

INSERT INTO reports (title, text, created_at, updated_at, care_plan_id, author_employee_id)
SELECT
    'Observatie',
    'Korte observatie tijdens bezoek.',
    NOW(),
    NOW(),
    cpl.id,
    u.id
FROM care_plans cpl
         JOIN client_profiles cp ON cp.id = cpl.client_profile_id
         JOIN users u ON u.email = 'employee2@test.local'
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM reports r
    WHERE r.title = 'Observatie'
      AND r.care_plan_id = cpl.id
);