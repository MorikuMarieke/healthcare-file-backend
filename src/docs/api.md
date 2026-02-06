Yes — do that now. It will make every next feature (and grading) easier, and it prevents security rules from drifting.

Here’s a **simple doc structure** you can drop into `README.md` or `docs/api.md` and keep updating. Keep it short and table-driven.

---

### 1) Roles

* **ADMIN**: manage staff + everything
* **EMPLOYEE**: manage client files
* **CLIENT**: view own file via `/me`

### 2) Auth rules

* JWT required for all endpoints except:

    * `POST /auth/login`
    * `POST /auth/register`

### 3) Endpoint matrix (copy/paste)

| Method | Path             | Public | Notes                                       |
| ------ | ---------------- | ------ | ------------------------------------------- |
| POST   | `/auth/register` | ✅      | Client claims file (BSN + email + password) |
| POST   | `/auth/login`    | ✅      | Returns JWT                                 |

| Method | Path                   | Auth | Intended role                                      |
| ------ | ---------------------- | ---- | -------------------------------------------------- |
| GET    | `/me`                  | ✅    | any logged-in (CLIENT/EMPLOYEE/ADMIN)              |
| GET    | `/me/client-profile`   | ✅    | CLIENT only (your MeService enforces this)         |
| GET    | `/me/employee-profile` | ✅    | EMPLOYEE/ADMIN only (your MeService enforces this) |

| Method | Path                                    | Auth | Intended role                              |
| ------ | --------------------------------------- | ---- | ------------------------------------------ |
| POST   | `/client-profiles`                      | ✅    | EMPLOYEE/ADMIN                             |
| GET    | `/client-profiles`                      | ✅    | EMPLOYEE/ADMIN                             |
| GET    | `/client-profiles/{id}`                 | ✅    | EMPLOYEE/ADMIN                             |
| PATCH  | `/client-profiles/{id}`                 | ✅    | EMPLOYEE/ADMIN                             |
| PATCH  | `/client-profiles/{id}/contact-details` | ✅    | EMPLOYEE/ADMIN                             |
| DELETE | `/client-profiles/{id}`                 | ✅    | ADMIN maybe (or EMPLOYEE/ADMIN if allowed) |

| Method | Path                          | Auth | Intended role                               |
| ------ | ----------------------------- | ---- | ------------------------------------------- |
| GET    | `/employee-profiles/{userId}` | ✅    | EMPLOYEE/ADMIN (or ADMIN only if you want)  |
| PATCH  | `/employee-profiles/{userId}` | ✅    | EMPLOYEE/ADMIN (or “only self/admin” later) |
| DELETE | `/employee-profiles/{userId}` | ✅    | ADMIN only (recommended)                    |

| Method | Path                   | Auth | Intended role                                              |
| ------ | ---------------------- | ---- | ---------------------------------------------------------- |
| GET    | `/users`               | ✅    | ADMIN only (recommended)                                   |
| GET    | `/users/{id}`          | ✅    | ADMIN only (recommended)                                   |
| POST   | `/users`               | ✅    | ADMIN only (you already treat this as admin creates staff) |
| PATCH  | `/users/{id}/password` | ✅    | currently dangerous; should become `/me/password` later    |
| DELETE | `/users/{id}`          | ✅    | ADMIN only                                                 |



### 4) Ownership rules (important for `/me`)

* Client can only access resources linked to their `User` via `client_profiles.user_id`.
* Staff endpoints require EMPLOYEE/ADMIN.

### 5) Error conventions (minimal)

* 401: missing/invalid token
* 403: authenticated but not allowed
* 404: resource not found
* 409: conflict (email exists, already claimed)

### 6) Test flow

1. login admin
2. create employee
3. login employee
4. create client profile
5. patch contact email
6. register client
7. login client
8. `/me/client-profile`
