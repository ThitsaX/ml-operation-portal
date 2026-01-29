# Operation Portal for Mojaloop

This document describes the required **database setup**, **Vault configuration**, and **initial application setup** for the Operation Portal.

---

## 1. Database Setup (Prerequisite)

The following MySQL databases **must be created before Vault setup**.

### Databases

- `central_ledger`
- `operation_portal`

### Database Configuration

- **Default Charset:** `utf8mb4`
- **Default Collation:** `utf8mb4_unicode_ci`

### Database Access

- The **`operation_portal` database user must have access to both databases**:
  - `central_ledger`
  - `operation_portal`

---

## 2. Vault Configuration Reference

This section shows **only the fields DevOps may change** in Vault for the Operation Portal.

| Component | Vault Path | Fields to Change |
|---------|-----------|------------------|
| **Redis** | `operation_portal/redis/settings` | `redisUrl` |
| **MySQL – Portal (Flyway)** | `operation_portal/mysql/portal_data/flyway/settings` | `url`, `username`, `password` |
| **MySQL – Portal (Write DB)** | `operation_portal/mysql/portal_data/write_db/settings` | `url`, `username`, `password` |
| **MySQL – Portal (Read DB)** | `operation_portal/mysql/portal_data/read_db/settings` | `url`, `username`, `password` |
| **MySQL – Hub (Write DB)** | `operation_portal/mysql/hub_data/write_db/settings` | `url`, `username`, `password` |
| **MySQL – Hub (Read DB)** | `operation_portal/mysql/hub_data/read_db/settings` | `url`, `username`, `password` |
| **MongoDB – Hub (Write DB)** | `operation_portal/mongo/hub_data/write_db/settings` | `uri` (includes username, password, host), `database` |
| **MongoDB – Hub (Read DB)** | `operation_portal/mongo/hub_data/read_db/settings` | `uri` (includes username, password, host), `database` |

---

## 3. Initial Setup (After Vault Configuration)

After Vault secrets are configured, the following **initial setup APIs must be executed once**.

All APIs are located in the **core services** module.

---

### 3.1 Grant Role Action List

- **API:** `/secured/grantRoleActionList`
- **Location:** core services folder
- **Request Body:**  
  `documentation/grants/GrantRoleActionList.txt`

Initializes **role-to-action mappings**.

---

### 3.2 Grant Menu Action List

- **API:** `/secured/grantMenuActionList`
- **Location:** core services folder
- **Request Body:**  
  `documentation/grants/GrantMenuActionList.txt`

Initializes **menu-to-action mappings** for UI access control.

---

### 3.3 Sync Hub Settlement Models

- **API:** `/secured/syncHubSettlementModelsToPortal`
- **Location:** core services folder
- **Request Body:** Not required

Syncs **hub settlement models** into the Operation Portal.

---

## ⚠️ Important Notes

- Steps must be executed **in the order listed**
- Typically run **once per environment** (dev / staging / prod)
- Requires appropriate **security credentials**

---

✅ **End of reference**
