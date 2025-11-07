# College ERP - Role-Based Access Control (RBAC) Documentation

## Table of Contents
1. [Role Hierarchy](#role-hierarchy)
2. [Public Endpoints](#public-endpoints)
3. [Role-Based Permissions](#role-based-permissions)
4. [Endpoint Reference](#endpoint-reference)
5. [Special Access Rules](#special-access-rules)
6. [Security Notes](#security-notes)

## Role Hierarchy

The system implements a hierarchical role structure where higher roles inherit all permissions of the roles below them:

```
ADMIN > PRINCIPAL > FACULTY > STAFF > STUDENT
```

## Public Endpoints

These endpoints are accessible without authentication:

- `POST /api/auth/**` - Authentication endpoints
- `POST /api/users/register` - User registration
- `GET /actuator/health` - Health check
- `GET /api/test/public` - Public test endpoint
- `GET /v3/api-docs/**` - API documentation
- `GET /swagger-ui/**` - Swagger UI

## Role-Based Permissions

### 1. ADMIN
**Full system access including:**
- User Management (CRUD operations on all users)
- Department Management (full access)
- Subject Management (full access)
- System Configuration
- Audit Logs Access

### 2. PRINCIPAL
**Can manage:**
- User Management (create/update FACULTY, STAFF, STUDENT)
- Department Management (view all, limited modifications)
- Subject Management (view all, limited modifications)
- Academic Records (view)

### 3. FACULTY
**Can access:**
- View user lists and profiles
- Manage assigned subjects
- Grade students
- View department information
- Update own profile

### 4. STAFF
**Can access:**
- View basic user information
- Update own profile
- View department information
- Limited system access

### 5. STUDENT
**Can access:**
- View own profile
- View enrolled subjects
- View grades
- Update personal information
- View department information

## Endpoint Reference

### User Management
| Endpoint | Method | Roles | Description |
|----------|--------|-------|-------------|
| `/api/users` | GET | ADMIN, FACULTY | List all users |
| `/api/users/{id}` | GET | ADMIN, FACULTY, OWN_PROFILE | Get user by ID |
| `/api/users` | POST | ADMIN, PRINCIPAL | Create new user |
| `/api/users/{id}` | PUT | ADMIN, PRINCIPAL, OWN_PROFILE | Update user |
| `/api/users/{id}` | DELETE | ADMIN | Delete user |
| `/api/users/register` | POST | PUBLIC | Register new user (first user becomes ADMIN) |

### Department Management
| Endpoint | Method | Roles | Description |
|----------|--------|-------|-------------|
| `/api/departments` | GET | AUTHENTICATED | List all departments |
| `/api/departments/{id}` | GET | AUTHENTICATED | Get department by ID |
| `/api/departments` | POST | ADMIN, PRINCIPAL | Create department |
| `/api/departments/{id}` | PUT | ADMIN, PRINCIPAL | Update department |
| `/api/departments/{id}` | DELETE | ADMIN | Delete department |

### Subject Management
| Endpoint | Method | Roles | Description |
|----------|--------|-------|-------------|
| `/api/subjects` | GET | AUTHENTICATED | List all subjects |
| `/api/subjects/{id}` | GET | AUTHENTICATED | Get subject by ID |
| `/api/subjects` | POST | ADMIN, PRINCIPAL | Create subject |
| `/api/subjects/{id}` | PUT | ADMIN, PRINCIPAL | Update subject |
| `/api/subjects/{id}` | DELETE | ADMIN | Delete subject |

## Special Access Rules

### User Registration
- First user is automatically assigned ADMIN role
- Subsequent registrations require authentication
- Only ADMIN/PRINCIPAL can create new users after initial setup
- PRINCIPAL cannot create ADMIN/PRINCIPAL users

### Self-Service
All authenticated users can:
- View their own profile
- Update their own information (except role)
- Change their password

### Department Access
- All authenticated users can view department lists
- Only ADMIN/PRINCIPAL can modify departments
- Department heads have additional access to their department

## Security Notes

1. **Authentication**
   - JWT-based authentication
   - Stateless session management
   - Token expiration and refresh mechanism

2. **Authorization**
   - Role-based access control (RBAC)
   - Method-level security with `@PreAuthorize`
   - Hierarchical role inheritance

3. **Data Protection**
   - BCrypt password hashing
   - HTTPS required for all endpoints
   - Input validation on all endpoints

4. **Audit & Logging**
   - All security events are logged
   - Failed login attempts are monitored
   - Sensitive operations are audited

5. **Best Practices**
   - Principle of least privilege
   - Secure defaults
   - Regular security reviews

## Testing Access Control

To verify RBAC implementation:

1. Test each role with different endpoints
2. Verify unauthorized access attempts are rejected
3. Check role inheritance works as expected
4. Test edge cases and boundary conditions

## Maintenance

1. **Adding New Roles**
   - Update `RoleHierarchy` in `SecurityConfig`
   - Add new role to `UserRole` enum
   - Update documentation

2. **Modifying Permissions**
   - Update `@PreAuthorize` annotations
   - Update test cases
   - Update documentation

3. **Audit Logs**
   - Regularly review access logs
   - Monitor for suspicious activities
   - Keep audit logs secure
