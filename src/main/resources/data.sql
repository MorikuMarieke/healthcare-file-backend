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
-- USERS
-- =========================

-- ADMIN USER (password = Admin123!)
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

-- EMPLOYEE USER 1 (password = Employee123!)
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

-- EMPLOYEE USER 2 (password = Employee123!)
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

-- EMPLOYEE USER 3 (password = Employee123!)
INSERT INTO users (email, password, role_id, password_changed_at, created_at)
SELECT
    'employee3@test.local',
    '$2a$12$kZcV5nXth0FzW/UPA7wChum1VMSpTBNcXDvnqklrqmhLIksxRC2ya',
    r.id,
    NOW(),
    NOW()
FROM roles r
WHERE r.name = 'EMPLOYEE'
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'employee3@test.local');

-- CLIENT USER (password = Client123!)
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

-- =========================
-- CARE TEAMS
-- =========================
INSERT INTO care_teams (team_name, team_phone_number, team_email)
SELECT
    'Seed Team 1',
    '0612345678',
    'team1@test.local'
    WHERE NOT EXISTS (
    SELECT 1
    FROM care_teams ct
    WHERE ct.team_name = 'Seed Team 1'
);

INSERT INTO care_teams (team_name, team_phone_number, team_email)
SELECT
    'Seed Team 2',
    '0699999999',
    'team2@test.local'
    WHERE NOT EXISTS (
    SELECT 1
    FROM care_teams ct
    WHERE ct.team_name = 'Seed Team 2'
);

-- =========================
-- EMPLOYEE PROFILES (MapsId: id == user_id)
-- Employee profiles may exist without a care team.
-- In this seed:
-- - employee1 belongs to Seed Team 1
-- - employee2 belongs to Seed Team 2
-- - employee3 starts without a team for assignment testing
-- =========================
INSERT INTO employee_profiles (
    user_id,
    care_team_id,
    first_name,
    last_name,
    work_phone_number,
    personal_phone_number,
    personal_email
)
SELECT
    u.id,
    ct.id,
    'Test',
    'Employee',
    '0611111111',
    '0622222222',
    'employee.personal@test.local'
FROM users u
         JOIN care_teams ct ON ct.team_name = 'Seed Team 1'
WHERE u.email = 'employee@test.local'
  AND NOT EXISTS (
    SELECT 1
    FROM employee_profiles ep
    WHERE ep.user_id = u.id
);

INSERT INTO employee_profiles (
    user_id,
    care_team_id,
    first_name,
    last_name,
    work_phone_number,
    personal_phone_number,
    personal_email
)
SELECT
    u.id,
    ct.id,
    'Test2',
    'Employee',
    '0633333333',
    '0644444444',
    'employee2.personal@test.local'
FROM users u
         JOIN care_teams ct ON ct.team_name = 'Seed Team 2'
WHERE u.email = 'employee2@test.local'
  AND NOT EXISTS (
    SELECT 1
    FROM employee_profiles ep
    WHERE ep.user_id = u.id
);

INSERT INTO employee_profiles (
    user_id,
    care_team_id,
    first_name,
    last_name,
    work_phone_number,
    personal_phone_number,
    personal_email
)
SELECT
    u.id,
    NULL,
    'Test3',
    'Employee',
    '0655555555',
    '0666666666',
    'employee3.personal@test.local'
FROM users u
WHERE u.email = 'employee3@test.local'
  AND NOT EXISTS (
    SELECT 1
    FROM employee_profiles ep
    WHERE ep.user_id = u.id
);

-- =========================
-- CLIENT PROFILE
-- Clients always belong to exactly one care team.
-- =========================
INSERT INTO client_profiles (bsn, first_name, last_name, birth_date, sex, active, created_at, care_team_id)
SELECT
    '123456789',
    'Test',
    'Client',
    DATE '1999-01-01',
    'FEMALE',
    true,
    NOW(),
    (SELECT ct.id FROM care_teams ct WHERE ct.team_name = 'Seed Team 1')
    WHERE NOT EXISTS (
    SELECT 1
    FROM client_profiles cp
    WHERE cp.bsn = '123456789'
);

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

-- Link client profile to client user
UPDATE client_profiles
SET user_id = (SELECT u.id FROM users u WHERE u.email = 'client@test.local')
WHERE bsn = '123456789'
  AND user_id IS NULL;

-- =========================
-- CARE PLAN
-- =========================
INSERT INTO care_plans (client_profile_id, notes, medical_history)
SELECT
    cp.id,
    '',
    ''
FROM client_profiles cp
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM care_plans cpl
    WHERE cpl.client_profile_id = cp.id
);

-- =========================
-- GOALS
-- =========================
INSERT INTO goals (
    care_plan_id,
    date_created,
    evaluation_date,
    care_goal,
    instructions,
    last_modified_at,
    last_modified_by_employee_id
)
SELECT
    cpl.id,
    CURRENT_DATE,
    CURRENT_DATE + INTERVAL '30 days',
    'Client werkt aan meer zelfstandigheid in dagelijkse routine.',
    'Dagelijks structuurmoment bespreken en voortgang evalueren.',
    NOW(),
    (SELECT ep.user_id FROM employee_profiles ep WHERE ep.personal_email = 'employee.personal@test.local')
FROM care_plans cpl
    JOIN client_profiles cp ON cp.id = cpl.client_profile_id
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM goals g
    WHERE g.care_plan_id = cpl.id
  AND g.care_goal = 'Client werkt aan meer zelfstandigheid in dagelijkse routine.'
    );

INSERT INTO goals (
    care_plan_id,
    date_created,
    evaluation_date,
    care_goal,
    instructions,
    last_modified_at,
    last_modified_by_employee_id
)
SELECT
    cpl.id,
    CURRENT_DATE,
    CURRENT_DATE + INTERVAL '45 days',
    'Client oefent met het uitbreiden van sociale participatie.',
    'Wekelijks contactmoment plannen en ervaringen bespreken.',
    NOW(),
    (SELECT ep.user_id FROM employee_profiles ep WHERE ep.personal_email = 'employee.personal@test.local')
FROM care_plans cpl
    JOIN client_profiles cp ON cp.id = cpl.client_profile_id
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM goals g
    WHERE g.care_plan_id = cpl.id
  AND g.care_goal = 'Client oefent met het uitbreiden van sociale participatie.'
    );

-- =========================
-- REPORTS
-- employee1 writes reports for Team 1 client
-- =========================
INSERT INTO reports (title, text, created_at, updated_at, care_plan_id, author_employee_id)
SELECT
    'Intake verslag',
    'Client oogt vermoeid, intake gesprek gevoerd.',
    NOW(),
    NOW(),
    cpl.id,
    (SELECT ep.user_id FROM employee_profiles ep WHERE ep.personal_email = 'employee.personal@test.local')
FROM care_plans cpl
         JOIN client_profiles cp ON cp.id = cpl.client_profile_id
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
    (SELECT ep.user_id FROM employee_profiles ep WHERE ep.personal_email = 'employee.personal@test.local')
FROM care_plans cpl
         JOIN client_profiles cp ON cp.id = cpl.client_profile_id
WHERE cp.bsn = '123456789'
  AND NOT EXISTS (
    SELECT 1
    FROM reports r
    WHERE r.title = 'Weekrapportage'
      AND r.care_plan_id = cpl.id
);