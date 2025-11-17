# DOCUMENTACIÓN COMPLETA - SISTEMA BANCARIO ARCBANK

## 🌐 ACCESO PÚBLICO - APLICACIONES EN LA NUBE

### ✅ URLs de Producción en Google Cloud Run

**APLICACIONES PÚBLICAS (Accesibles desde cualquier lugar del mundo):**

- **🏦 Banca Web (Clientes):** https://bancaweb-frontend-845403368692.us-central1.run.app
- **🏧 Cajero Automático:** https://cajero-frontend-845403368692.us-central1.run.app

**SERVICIOS BACKEND (No acceder directamente, solo para desarrollo):**
- **API Gateway:** https://api-gateway-service-845403368692.us-central1.run.app
- **Backend Core:** https://cbs-service-845403368692.us-central1.run.app
- **Backend Web:** https://bancaweb-service-845403368692.us-central1.run.app
- **Backend Cajero:** https://cajero-service-845403368692.us-central1.run.app

---

## 🔑 CREDENCIALES DE ACCESO PARA PRUEBAS

### Banca Web (Usuario Cliente)
```
Usuario: jperez
Contraseña: pass123
```

**O crear un nuevo usuario desde el frontend**

### Cajero Automático
```
Usuario: cajero1
Contraseña: clave123
```

### Cuentas de Prueba Disponibles
```
Número de Cuenta: 100100000001
Cliente: Carlos Andrés Morales Vega
Cédula: 1724589630
Tipo: PERSONAL (Ahorro/Corriente)
Saldo: $4250.00
```

---

## 🏗️ ARQUITECTURA DEL SISTEMA EN GOOGLE CLOUD

### Componentes Desplegados
```
┌─────────────────────────────────────────────────────────────────┐
│                    INTERNET (Público)                            │
│          Accesible desde cualquier lugar del mundo               │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    GOOGLE CLOUD RUN                              │
│                    Region: us-central1                           │
├──────────────────────┬──────────────────────────────────────────┤
│ Frontend Web (React) │ Frontend Cajero (React + Vite)           │
│ Nginx Alpine         │ Nginx Alpine                             │
│ Puerto: 80           │ Puerto: 80                               │
└──────────────────────┴──────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    API GATEWAY (Cloud Run)                       │
│                    Puerto: 8080                                  │
│  Spring Cloud Gateway con CORS configurado                       │
│  Rutas:                                                          │
│  /cbs/** → Backend Core                                         │
│  /bancaweb/** → Backend Web                                     │
│  /cajero/** → Backend Cajero                                    │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│              BACKENDS (Cloud Run - Java 21)                      │
├──────────────────────┬──────────────────────┬──────────────────┤
│ Backend Core         │ Backend Web          │ Backend Cajero   │
│ Puerto: 8080         │ Puerto: 8080         │ Puerto: 8080     │
│ Spring Boot 3.5.7    │ Spring Boot 3.5.7    │ Spring Boot 3.5.7│
└──────────────────────┴──────────────────────┴──────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    CLOUD SQL (PostgreSQL)                        │
│  Instance: arcbank-db-instance                                   │
│  Region: us-central1                                             │
│  Connection: aqueous-depth-478400-k3:us-central1:arcbank-db-...│
├──────────────────────┬──────────────────────┬──────────────────┤
│ DB: core             │ DB: bancaweb         │ DB: cajero_db    │
│ Usuario: postgres    │ Usuario: postgres    │ Usuario: postgres│
│ Password: Arqui123@  │ Password: Arqui123@  │ Password: Arqui123@│
│ Tablas PascalCase    │ Tablas PascalCase    │ Tablas PascalCase│
└──────────────────────┴──────────────────────┴──────────────────┘
```

---

## BASES DE DATOS

### 1. DATABASE: `core`
**Propósito:** Base de datos central del sistema bancario - maneja clientes, cuentas y transacciones

#### Tablas Principales:

**Cliente**
- IdCliente (PK)
- TipoCliente (P=Persona, E=Empresa)
- Identificacion (cédula o RUC)
- Estado
- FechaRegistro

**Persona**
- IdCliente (PK, FK → Cliente)
- Identificacion
- Nombres
- Apellidos
- FechaNacimiento
- DireccionPrincipal

**Empresa**
- IdCliente (PK, FK → Cliente)
- Ruc
- RazonSocial
- FechaConstitucion
- DireccionPrincipal

**CuentaAhorro**
- IdCuenta (PK)
- NumeroCuenta (UNIQUE, 12 dígitos)
- IdCliente (FK → Cliente)
- IdTipoCuenta (FK → TipoCuentaAhorro)
- SaldoActual
- SaldoDisponible
- FechaApertura
- Estado (ACTIVA/BLOQUEADA/CERRADA)

**Transaccion**
- IdTransaccion (PK)
- IdCuenta (FK → CuentaAhorro)
- Tipo (DEPOSITO/RETIRO)
- Monto
- Balance (saldo después de la transacción)
- FechaHora
- Descripcion
- Referencia
- Estado (COMPLETADA/PENDIENTE/RECHAZADA)

**TipoCuentaAhorro**
- IdTipoCuenta (PK)
- Nombre (PERSONAL/EMPRESARIAL)
- Descripcion

**Sucursal**
- IdSucursal (PK)
- Nombre
- CodigoUnico
- Direccion
- Telefono
- Estado

#### Datos de Prueba Actuales:

**Personas (5):**
1. Carlos Andrés Morales Vega - Cédula: 1724589630
2. María Fernanda Salazar Torres - Cédula: 0926547831
3. Luis Alberto Paredes Ruiz - Cédula: 1715823694
4. Ana Patricia Mendoza Castro - Cédula: 0945621378
5. Jorge Eduardo Hidalgo Moreno - Cédula: 1703254896

**Empresas (5):**
6. TechnoSolutions S.A. - RUC: 1792567890001
7. Distribuidora Nacional - RUC: 0992345678001
8. Importadora del Pacífico - RUC: 1791234567001
9. Constructora Andina - RUC: 0993456789001
10. Comercializadora Global - RUC: 1794567890001

**Cuentas Activas:**
- 100100000001 (IdCliente: 1 - Carlos Morales) - Saldo: ~$4,000
- 100100000003 (IdCliente: 3 - Luis Paredes) - Saldo: ~$8,200
- 100100000005 (IdCliente: 5 - Jorge Hidalgo) - Saldo: ~$6,500
- 100200000002 (IdCliente: 3 - Luis Paredes) - Saldo: ~$4,100
- 200100000006 (IdCliente: 6 - TechnoSolutions) - Saldo: ~$45,000
- 200100000008 (IdCliente: 8 - Importadora Pacífico) - Saldo: ~$125,000
- 200100000010 (IdCliente: 10 - Comercializadora Global) - Saldo: ~$95,000
- 200200000007 (IdCliente: 7 - Dist. Nacional) - Saldo: ~$78,500
- 200200000009 (IdCliente: 9 - Constructora Andina) - Saldo: ~$62,000

---

### 2. DATABASE: `bancaweb`
**Propósito:** Gestiona usuarios de banca web y auditoría

#### Tablas:

**usuariosistema**
- idusuario (PK)
- nombreusuario
- clavehash (BCrypt)
- identificacion (FK lógica → core.Cliente.Identificacion)
- estado (ACTIVO/BLOQUEADO)
- idsucursal
- fechacreacion
- ultimoacceso

**AuditoriaTransferencias**
- idauditoria (PK)
- idusuario (FK → usuariosistema)
- cuentaorigen
- cuentadestino
- monto
- fechaoperacion
- resultado
- mensaje

**operacioncaja**
- idoperacion (PK)
- idusuario (FK → usuariosistema)
- tipo
- monto
- fechahora

#### Usuarios Web Activos:

| Usuario     | Password | Identificacion | IdCliente Core |
|-------------|----------|----------------|----------------|
| cmorales    | 12345    | 1724589630     | 1              |
| lparedes    | 12345    | 1715823694     | 3              |
| amendoza    | 12345    | 0945621378     | 4              |
| jhidalgo    | 12345    | 0945621378     | 5              |
| admin       | 12345    | 1703254896     | 5              |
| technosol   | 12345    | 1792567890001  | 6              |
| distnacional| 12345    | 0992345678001  | 7              |
| impacifico  | 12345    | 1791234567001  | 8              |
| conandinas  | 12345    | 0993456789001  | 9              |

---

### 3. DATABASE: `cajero_db`
**Propósito:** Gestiona cajeros y transacciones de ATM

#### Tablas:

**Cajero**
- IdCajero (PK)
- Usuario
- Clave (BCrypt)
- NombreCompleto
- IdSucursal (FK → Sucursal)
- Estado

**Sucursal** (replica de core.Sucursal)
- IdSucursal (PK)
- Nombre
- CodigoUnico
- Direccion
- Telefono
- Estado

**TransaccionCajero**
- IdTransaccionCajero (PK)
- NumeroCuenta (referencia a core.CuentaAhorro.NumeroCuenta)
- IdCajero (FK → Cajero)
- Tipo (DEPOSITO/RETIRO)
- Monto
- Balance
- Descripcion
- Referencia
- FechaHora
- Estado

**LoginCajero**
- IdLogin (PK)
- IdCajero (FK → Cajero)
- FechaHoraLogin
- FechaHoraLogout
- IpAddress

#### Usuarios Cajero:

| Usuario  | Password | Nombre Completo        | Sucursal |
|----------|----------|------------------------|----------|
| admin    | 12345    | Administrador Sistema  | 1        |
| rmendez  | 12345    | Roberto Méndez López   | 2        |

---

## FLUJO DE COMUNICACIÓN

### 1. BANCA WEB (Frontend → Backend Web → Gateway → Core)

**Login:**
```
Usuario ingresa: lparedes/12345
↓
Frontend Web (3000) → POST /api/auth/login
↓
Backend Web (8081) valida en bancaweb.usuariosistema
↓
Devuelve: { usuario, identificacion: "1715823694" }
```

**Consulta Posición Consolidada:**
```
Frontend Web solicita cuentas
↓
Frontend (3000) → GET /api/cuentas/consolidada/1715823694
↓
Backend Web (8081) → Gateway (8085) → GET /cbs/api/core/consultas/posicion-consolidada/1715823694
↓
Backend Core (8080) consulta:
  - core.Cliente WHERE Identificacion = '1715823694'
  - core.CuentaAhorro WHERE IdCliente = 3
  - core.Persona WHERE IdCliente = 3
↓
Devuelve: { cuentas: [100100000003, 100200000002], saldoTotal: $12,300 }
```

**Transferencia:**
```
Usuario transfiere $100 de cuenta 100200000002 a 100100000001
↓
Frontend (3000) → POST /api/transferencias
Body: { cuentaOrigen: "100200000002", cuentaDestino: "100100000001", monto: 100 }
↓
Backend Web (8081) → Gateway (8085) → POST /cbs/api/core/transacciones
↓
Backend Core (8080):
  1. Valida cuentas existan
  2. Valida saldo suficiente
  3. Ejecuta transacción atómica:
     - INSERT Transaccion (RETIRO, -100, cuenta origen)
     - INSERT Transaccion (DEPOSITO, +100, cuenta destino)
     - UPDATE CuentaAhorro SET SaldoActual -= 100 WHERE origen
     - UPDATE CuentaAhorro SET SaldoActual += 100 WHERE destino
↓
Devuelve: { success: true, referencia: "TRF-20251116-XXXXX" }
```

**Consulta Movimientos:**
```
Frontend (3000) → GET /api/movimientos/100200000002
↓
Backend Web (8081) → Gateway (8085) → GET /cbs/api/core/consultas/movimientos/100200000002
↓
Backend Core (8080) consulta:
  - core.Transaccion WHERE IdCuenta = (SELECT IdCuenta FROM CuentaAhorro WHERE NumeroCuenta = '100200000002')
  - ORDER BY FechaHora DESC
↓
Devuelve: [ { tipo: "DEPOSITO", monto: 100, balance: 4100, fecha: "2025-11-16 19:30" } ]
```

---

### 2. CAJERO ATM (Frontend → Backend Cajero → Gateway → Core)

**Login Cajero:**
```
Usuario ingresa: admin/12345
↓
Frontend Cajero (3001) → POST /api/auth/login
↓
Backend Cajero (8082) valida en cajero_db.Cajero
↓
Devuelve: { idCajero: 1, nombreCompleto: "Administrador Sistema" }
↓
Frontend guarda en localStorage('cajero')
```

**Búsqueda de Cuenta:**
```
Cajero busca cuenta: 100200000002
↓
Frontend (3001) → GET /api/cuentas/numero/100200000002
↓
Backend Cajero (8082) → Gateway (8085) → GET /cbs/api/core/consultas/cuenta/100200000002
↓
Backend Core (8080):
  SELECT ca.NumeroCuenta, ca.SaldoActual, ca.TipoCuenta,
         p.Nombres, p.Apellidos, p.Identificacion, c.TipoCliente
  FROM CuentaAhorro ca
  JOIN Cliente c ON ca.IdCliente = c.IdCliente
  LEFT JOIN Persona p ON c.IdCliente = p.IdCliente
  WHERE ca.NumeroCuenta = '100200000002'
↓
Devuelve: {
  numeroCuenta: "100200000002",
  nombres: "Luis Alberto",
  apellidos: "Paredes Ruiz",
  identificacion: "1715823694",
  tipoCuenta: "PERSONAL",
  saldoActual: 4100.00
}
```

**Depósito en Cajero:**
```
Cajero deposita $500 a cuenta 100200000002
Depositante: Carlos Morales (1724589630)
↓
Frontend (3001) → POST /api/cajero/depositar
Body: {
  numeroCuenta: "100200000002",
  monto: 500,
  idCajero: 1,
  cedulaDepositante: "1724589630",
  nombresDepositante: "Carlos Andrés",
  apellidosDepositante: "Morales Vega"
}
↓
Backend Cajero (8082):
  1. Guarda en cajero_db.TransaccionCajero (registro local)
  2. Llama a CoreSyncService → Gateway (8085) → POST /cbs/api/core/transacciones
↓
Backend Core (8080):
  1. Busca cuenta: SELECT IdCuenta FROM CuentaAhorro WHERE NumeroCuenta = '100200000002'
  2. Ejecuta transacción:
     - INSERT INTO Transaccion (IdCuenta, Tipo='DEPOSITO', Monto=500, Balance=4600)
     - UPDATE CuentaAhorro SET SaldoActual = 4600, SaldoDisponible = 4600
↓
Backend Cajero actualiza TransaccionCajero.Balance = 4600
↓
Devuelve: { success: true, nuevoSaldo: 4600, referencia: "DEP-ATM-..." }
```

**Retiro en Cajero:**
```
Cajero retira $200 de cuenta 100200000002
↓
Frontend (3001) → POST /api/cajero/retirar
Body: { numeroCuenta: "100200000002", monto: 200, idCajero: 1 }
↓
Backend Cajero (8082) → Gateway (8085) → POST /cbs/api/core/transacciones
Body: { numeroCuenta: "100200000002", tipo: "RETIRO", monto: 200 }
↓
Backend Core (8080):
  1. Valida saldo >= 200
  2. Ejecuta:
     - INSERT Transaccion (RETIRO, -200, Balance=4400)
     - UPDATE CuentaAhorro SET SaldoActual = 4400
↓
Devuelve: { success: true, nuevoSaldo: 4400 }
```

---

## REGLAS DE ARQUITECTURA CRÍTICAS

### ⚠️ REGLA ABSOLUTA: TODO AL CORE VA POR GATEWAY

**NUNCA hacer:**
```javascript
// ❌ PROHIBIDO - Llamada directa al Core
fetch('http://localhost:8080/api/core/...')
```

**SIEMPRE hacer:**
```javascript
// ✅ CORRECTO - A través del Gateway
fetch('http://localhost:8085/cbs/api/core/...')
```

### Configuración Gateway (application.yml)
```yaml
spring:
  cloud:
    gateway:
      routes:
        # Core Banking System
        - id: cbs-service
          uri: http://localhost:8080
          predicates:
            - Path=/cbs/**
          filters:
            - StripPrefix=1

        # Backend Web
        - id: bancaweb-service
          uri: http://localhost:8081
          predicates:
            - Path=/bancaweb/**
          filters:
            - StripPrefix=1

        # Backend Cajero
        - id: cajero-service
          uri: http://localhost:8082
          predicates:
            - Path=/cajero/**
          filters:
            - StripPrefix=1

      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "http://localhost:3000"
              - "http://localhost:3001"
              - "http://localhost:5173"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
            allowedHeaders: "*"
            allowCredentials: true
```

---

## CONFIGURACIÓN DE CADA COMPONENTE

### Backend Core (application.properties)
```properties
server.port=8080
spring.application.name=cbs

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5433/core
spring.datasource.username=postgres
spring.datasource.password=123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.com.arcbank.cbs=INFO
```

### API Gateway (application.yml)
```yaml
server:
  port: 8085

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: cbs-service
          uri: http://localhost:8080
          predicates:
            - Path=/cbs/**
          filters:
            - StripPrefix=1
        - id: bancaweb-service
          uri: http://localhost:8081
          predicates:
            - Path=/bancaweb/**
          filters:
            - StripPrefix=1
        - id: cajero-service
          uri: http://localhost:8082
          predicates:
            - Path=/cajero/**
          filters:
            - StripPrefix=1
```

### Backend Web (application.properties)
```properties
server.port=8081
spring.application.name=bancaweb

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5433/bancaweb
spring.datasource.username=postgres
spring.datasource.password=123

# Core API (VIA GATEWAY)
core.api.base=http://localhost:8085/cbs

# JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
```

### Backend Cajero (application.properties)
```properties
server.port=8082
spring.application.name=cajero

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5433/cajero_db
spring.datasource.username=postgres
spring.datasource.password=123

# Core API (VIA GATEWAY)
core.api.base=http://localhost:8085/cbs

# JPA
spring.jpa.hibernate.ddl-auto=none
```

### Frontend Web (package.json + código)
```json
{
  "name": "frontend-web",
  "version": "0.1.0",
  "dependencies": {
    "react": "^19.0.0",
    "react-dom": "^19.0.0",
    "react-router-dom": "^6.28.0"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build"
  },
  "proxy": "http://localhost:8081"
}
```

**services/api.js:**
```javascript
const BASE_URL = "http://localhost:8081";

export const auth = {
  login: (usuario, clave) =>
    fetch(`${BASE_URL}/api/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ usuario, clave })
    }).then(res => res.json())
};

export const cuentas = {
  getConsolidada: (identificacion) =>
    fetch(`${BASE_URL}/api/cuentas/consolidada/${identificacion}`)
      .then(res => res.json())
};
```

### Frontend Cajero (package.json + código)
```json
{
  "name": "frontend-cajero",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "react": "^19.0.0",
    "react-dom": "^19.0.0",
    "react-router-dom": "^7.1.1"
  },
  "devDependencies": {
    "@vitejs/plugin-react": "^4.3.4",
    "vite": "^5.4.11"
  }
}
```

**services/api.js:**
```javascript
const BASE_URL = "http://localhost:8082";

export const cuentas = {
  getCuenta: (numeroCuenta) =>
    fetch(`${BASE_URL}/api/cuentas/numero/${numeroCuenta}`)
      .then(res => res.json())
};

export const transacciones = {
  deposito: (body) =>
    fetch(`${BASE_URL}/api/cajero/depositar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    }).then(res => res.json()),
  
  retiro: (body) =>
    fetch(`${BASE_URL}/api/cajero/retirar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    }).then(res => res.json())
};
```

---

## ORDEN DE ARRANQUE

### 1. Iniciar PostgreSQL
```powershell
# Asegurarse que PostgreSQL esté corriendo en puerto 5433
# Verificar que existan las 3 bases de datos: core, bancaweb, cajero_db
```

### 2. Iniciar Backends (orden recomendado)
```powershell
# Terminal 1: Backend Core
cd backendCore
.\mvnw.cmd spring-boot:run

# Terminal 2: API Gateway
cd api-gateway
.\mvnw.cmd spring-boot:run

# Terminal 3: Backend Web
cd backendWeb
.\mvnw.cmd spring-boot:run

# Terminal 4: Backend Cajero
cd backendCajero
.\mvnw.cmd spring-boot:run
```

### 3. Iniciar Frontends
```powershell
# Terminal 5: Frontend Web
cd frontendWeb
npm start

# Terminal 6: Frontend Cajero
cd frontendCajero
npm run dev
```

### Verificación de Servicios
```
Backend Core:    http://localhost:8080/actuator/health
API Gateway:     http://localhost:8085/actuator/health
Backend Web:     http://localhost:8081/actuator/health
Backend Cajero:  http://localhost:8082
Frontend Web:    http://localhost:3000
Frontend Cajero: http://localhost:3001
```

---

## CASOS DE USO PROBADOS

### Caso 1: Transferencia entre cuentas (Banca Web)
**Usuario:** cmorales / 12345
1. Login en http://localhost:3000
2. Ver cuentas (100100000001)
3. Ir a Transferir
4. Cuenta destino: 100200000002 (lparedes)
5. Monto: $100
6. Confirmar
7. Verificar en Movimientos

### Caso 2: Depósito en cajero
**Cajero:** admin / 12345
1. Login en http://localhost:3001
2. Seleccionar Depósito
3. Número de cuenta cliente: 100200000002
4. Buscar (aparece Luis Paredes)
5. Cédula depositante: 1724589630
6. Nombres: Carlos Andrés
7. Apellidos: Morales Vega
8. Monto: $500
9. Continuar → Confirmar
10. Verificar en banca web (lparedes) que aparece el depósito

### Caso 3: Retiro en cajero
**Cajero:** admin / 12345
1. Seleccionar Retiro
2. Número de cuenta: 100200000002
3. Buscar (aparece Luis Paredes con cédula)
4. Monto: $200
5. Continuar → Confirmar
6. Verificar saldo actualizado

---

## PASSWORDS Y CREDENCIALES

### PostgreSQL
- **Host:** localhost
- **Puerto:** 5433
- **Usuario:** postgres
- **Password:** 123
- **Databases:** core, bancaweb, cajero_db

### Banca Web (Todos password: 12345)
- cmorales (Carlos Morales - 1724589630)
- lparedes (Luis Paredes - 1715823694)
- amendoza (Ana Mendoza - 0945621378)
- jhidalgo (Jorge Hidalgo - 1703254896)
- admin (Admin - 1703254896)
- technosol (TechnoSolutions - 1792567890001)

### Cajero ATM (Todos password: 12345)
- admin (Administrador Sistema)
- rmendez (Roberto Méndez)

**Nota:** Todas las contraseñas están hasheadas con BCrypt usando `crypt('12345', gen_salt('bf'))`

---

## 🚀 DEPLOYMENT EN GOOGLE CLOUD - GUÍA COMPLETA

### Requisitos Previos
- Cuenta de Google Cloud Platform
- Proyecto creado: `aqueous-depth-478400-k3`
- Cloud SQL activado
- Cloud Run activado
- Cloud Build activado
- Repositorio GitHub: https://github.com/KrisOlalla1/ARCBANK

---

### PASO 1: Configurar Cloud SQL PostgreSQL

#### 1.1 Crear Instancia Cloud SQL
```bash
# En Google Cloud Console o Cloud Shell
gcloud sql instances create arcbank-db-instance \
  --database-version=POSTGRES_16 \
  --tier=db-f1-micro \
  --region=us-central1 \
  --root-password=Arqui123@
```

**Connection Name:** `aqueous-depth-478400-k3:us-central1:arcbank-db-instance`

#### 1.2 Crear las 3 Bases de Datos
```bash
# Conectarse a Cloud SQL
gcloud sql connect arcbank-db-instance --user=postgres

# Dentro de psql:
CREATE DATABASE core;
CREATE DATABASE bancaweb;
CREATE DATABASE cajero_db;
```

#### 1.3 Poblar las Bases de Datos
Ejecutar los scripts SQL en orden:
1. `backendCore/test-data.sql` → en database `core`
2. Scripts de `backendWeb` → en database `bancaweb`
3. Scripts de `backendCajero` → en database `cajero_db`

**IMPORTANTE:** Las tablas deben crearse con **PascalCase** (ej: `"Cliente"`, `"CuentaAhorro"`)

---

### PASO 2: Configurar Backends para Cloud SQL

#### 2.1 Configuración application.properties

**backendCore/src/main/resources/application.properties:**
```properties
server.port=8080

# Cloud SQL Configuration
spring.datasource.url=jdbc:postgresql://google/core?cloudSqlInstance=aqueous-depth-478400-k3:us-central1:arcbank-db-instance&socketFactory=com.google.cloud.sql.postgres.SocketFactory
spring.datasource.username=postgres
spring.datasource.password=Arqui123@

# JPA Configuration - PRESERVAR PASCALCASE
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

**Aplicar lo mismo para backendWeb y backendCajero** (cambiando el nombre de la base de datos)

#### 2.2 Agregar Dependencia Socket Factory en pom.xml

**En todos los backends (backendCore, backendWeb, backendCajero):**
```xml
<dependencies>
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Cloud SQL Socket Factory - DEBE IR AQUÍ -->
    <dependency>
        <groupId>com.google.cloud.sql</groupId>
        <artifactId>postgres-socket-factory</artifactId>
        <version>1.18.1</version>
    </dependency>
    
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <!-- ... otras dependencias ... -->
</dependencies>
```

---

### PASO 3: Crear Dockerfiles

#### 3.1 Dockerfile para Backends (Java)

**Ejemplo: backendCore/Dockerfile**
```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Aplicar igual para backendWeb, backendCajero y api-gateway**

#### 3.2 Dockerfile para Frontends (React)

**frontendWeb/Dockerfile:**
```dockerfile
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

**frontendCajero/Dockerfile:**
```dockerfile
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

#### 3.3 nginx.conf para Frontends
```nginx
server {
    listen 80;
    server_name _;
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # Cache static assets
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

---

### PASO 4: Crear cloudbuild.yaml

#### 4.1 Backend cloudbuild.yaml

**Ejemplo: backendCore/cloudbuild.yaml**
```yaml
steps:
  # Build Docker image
  - name: 'gcr.io/cloud-builders/docker'
    args:
      - 'build'
      - '-t'
      - 'gcr.io/$PROJECT_ID/cbs:latest'
      - '.'

  # Push to Container Registry
  - name: 'gcr.io/cloud-builders/docker'
    args:
      - 'push'
      - 'gcr.io/$PROJECT_ID/cbs:latest'

  # Deploy to Cloud Run
  - name: 'gcr.io/google.com/cloudsdktool/cloud-sdk'
    entrypoint: gcloud
    args:
      - 'run'
      - 'deploy'
      - 'cbs-service'
      - '--image'
      - 'gcr.io/$PROJECT_ID/cbs:latest'
      - '--region'
      - 'us-central1'
      - '--platform'
      - 'managed'
      - '--allow-unauthenticated'
      - '--add-cloudsql-instances'
      - 'aqueous-depth-478400-k3:us-central1:arcbank-db-instance'

images:
  - 'gcr.io/$PROJECT_ID/cbs:latest'
```

**Aplicar similar para:**
- `backendWeb/cloudbuild.yaml` → service: `bancaweb-service`
- `backendCajero/cloudbuild.yaml` → service: `cajero-service`
- `api-gateway/cloudbuild.yaml` → service: `api-gateway-service`

#### 4.2 Frontend cloudbuild.yaml

**frontendWeb/cloudbuild.yaml:**
```yaml
steps:
  - name: 'gcr.io/cloud-builders/docker'
    args:
      - 'build'
      - '-t'
      - 'gcr.io/$PROJECT_ID/bancaweb-frontend:latest'
      - '.'

  - name: 'gcr.io/cloud-builders/docker'
    args:
      - 'push'
      - 'gcr.io/$PROJECT_ID/bancaweb-frontend:latest'

  - name: 'gcr.io/google.com/cloudsdktool/cloud-sdk'
    entrypoint: gcloud
    args:
      - 'run'
      - 'deploy'
      - 'bancaweb-frontend'
      - '--image'
      - 'gcr.io/$PROJECT_ID/bancaweb-frontend:latest'
      - '--region'
      - 'us-central1'
      - '--platform'
      - 'managed'
      - '--allow-unauthenticated'

images:
  - 'gcr.io/$PROJECT_ID/bancaweb-frontend:latest'
```

**Aplicar similar para frontendCajero**

---

### PASO 5: Configurar CORS en API Gateway

**api-gateway/src/main/java/com/arcbank/gateway/config/CorsConfig.java:**
```java
package com.arcbank.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
```

**IMPORTANTE:** Remover cualquier configuración CORS de los backends (backendWeb, backendCajero) para evitar headers duplicados.

---

### PASO 6: Solucionar Problema de Cookies Cross-Domain

#### 6.1 Modificar Login para Retornar Datos Directamente

**backendWeb/.../controller/UsuarioSistemaController.java:**
```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<UsuarioSistema> optional = service.autenticar(
        request.getUsuario(),
        request.getClave()
    );

    if (optional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Credenciales inválidas");
    }

    UsuarioSistema usuario = optional.get();
    
    // Retornar usuario completo con identificacion
    return ResponseEntity.ok(new UsuarioResponse(
        usuario.getIdUsuario(),
        usuario.getNombreUsuario(),
        usuario.getIdentificacion(),
        usuario.getIdSucursal()
    ));
}
```

#### 6.2 Actualizar Frontend para No Llamar /me

**frontendWeb/src/context/AuthContext.js:**
```javascript
const login = async (username, password) => {
  try {
    const response = await fetch(`${API_BASE_URL}/api/usuarios/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ usuario: username, clave: password }),
    });

    if (!response.ok) throw new Error('Login failed');

    const body = await response.json();
    
    // Extraer datos directamente de la respuesta
    const identificacion = body?.identificacion || null;
    const idUsuarioWeb = body?.idUsuario || null;

    setUser({ username, identificacion, idUsuarioWeb });
    localStorage.setItem('username', username);
    localStorage.setItem('identificacion', identificacion);
    localStorage.setItem('idUsuarioWeb', idUsuarioWeb);
  } catch (error) {
    console.error('Login error:', error);
    throw error;
  }
};
```

---

### PASO 7: Configurar Variables de Entorno

#### 7.1 Frontend Web
**frontendWeb/.env.production:**
```env
REACT_APP_API_BASE_URL=https://api-gateway-service-845403368692.us-central1.run.app/bancaweb
```

#### 7.2 Frontend Cajero
**frontendCajero/.env.production:**
```env
VITE_API_BASE_URL=https://api-gateway-service-845403368692.us-central1.run.app/cajero
```

---

### PASO 8: Desplegar los Servicios

#### 8.1 Workflow de Deployment

**En tu máquina local:**
```bash
# 1. Hacer cambios en el código
# 2. Commit y push a GitHub
git add .
git commit -m "Descripción de cambios"
git push origin main
```

**En Google Cloud Shell:**
```bash
# 1. Clonar/actualizar repositorio
cd ~
git clone https://github.com/KrisOlalla1/ARCBANK.git
cd ARCBANK
git pull  # Si ya existe

# 2. Desplegar servicios en orden
# IMPORTANTE: Desplegar en este orden

# Backend Core primero
cd backendCore
gcloud builds submit --config cloudbuild.yaml
cd ..

# Luego Backend Web y Cajero
cd backendWeb
gcloud builds submit --config cloudbuild.yaml
cd ../backendCajero
gcloud builds submit --config cloudbuild.yaml
cd ..

# API Gateway
cd api-gateway
gcloud builds submit --config cloudbuild.yaml
cd ..

# Finalmente los Frontends
cd frontendWeb
gcloud builds submit --config cloudbuild.yaml
cd ../frontendCajero
gcloud builds submit --config cloudbuild.yaml
```

#### 8.2 Verificar Deployment
```bash
# Listar servicios desplegados
gcloud run services list --region=us-central1

# Ver logs de un servicio
gcloud run services logs read cbs-service --region=us-central1

# Ver detalles de un servicio
gcloud run services describe cbs-service --region=us-central1
```

---

### PASO 9: Probar las Aplicaciones

#### 9.1 Acceder a Banca Web
1. Abrir: https://bancaweb-frontend-845403368692.us-central1.run.app
2. Login con: `jperez` / `pass123`
3. Verificar que carguen las cuentas
4. Probar transferencias

#### 9.2 Acceder a Cajero
1. Abrir: https://cajero-frontend-845403368692.us-central1.run.app
2. Login con: `cajero1` / `clave123`
3. Probar depósito con cuenta: `100100000001`
4. Probar retiro con la misma cuenta

---

### PROBLEMAS COMUNES Y SOLUCIONES

#### Error: "relation does not exist"
**Causa:** Hibernate convirtiendo nombres a minúsculas
**Solución:**
```properties
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

#### Error: ClassNotFoundException SocketFactory
**Causa:** Falta dependencia en pom.xml
**Solución:** Agregar `postgres-socket-factory:1.18.1` ENTRE postgresql y lombok

#### Error: 403 Forbidden
**Causa:** Servicios privados
**Solución:** Agregar `--allow-unauthenticated` en cloudbuild.yaml

#### Error: 405 Method Not Allowed
**Causa:** Falta CORS en Gateway
**Solución:** Crear CorsWebFilter en api-gateway (NO CorsFilter, debe ser reactivo)

#### Error: Duplicate Access-Control-Allow-Origin
**Causa:** CORS configurado en Gateway Y en backends
**Solución:** Eliminar CORS de todos los backends, dejar SOLO en Gateway

#### Error: 401 Unauthorized en /api/usuarios/me
**Causa:** Cookies cross-domain no funcionan
**Solución:** Login debe retornar datos directamente, no llamar a /me después

---

### COSTOS ESTIMADOS

**Cloud Run (con uso moderado):**
- Backends (4 servicios): ~$5-10/mes
- Frontends (2 servicios): ~$2-5/mes

**Cloud SQL:**
- db-f1-micro: ~$7/mes
- db-custom-1-3840 (recomendado para producción): ~$50/mes

**Storage (Container Registry):** ~$1-2/mes

**Total estimado (desarrollo):** $15-25/mes
**Total estimado (producción):** $60-100/mes

---

### MIGRACIÓN FUTURA A KUBERNETES (OPCIONAL)

Si el proyecto crece, se puede migrar a Google Kubernetes Engine (GKE):

1. Usar los mismos Dockerfiles
2. Crear manifiestos de Kubernetes (Deployments, Services, Ingress)
3. Configurar autoscaling
4. Implementar service mesh (Istio)

---

## TROUBLESHOOTING COMÚN

### Error: "Cannot read properties of undefined"
**Causa:** Frontend intentando acceder a campo que no existe en respuesta
**Solución:** Verificar que backend devuelva el campo correcto (ej: `identificacion` no `cedula`)

### Error: CORS
**Causa:** Frontend llamando directamente al Core sin pasar por Gateway
**Solución:** 
- Cambiar URLs para usar Gateway
- Verificar que CORS esté SOLO en Gateway
- Usar CorsWebFilter (reactivo) no CorsFilter

### Error: "No existe la relación"
**Causa:** Nombres de tablas con/sin comillas en PostgreSQL
**Solución:** Usar comillas dobles: `"CuentaAhorro"`, `"Persona"`, etc.
**En application.properties:** globally_quoted_identifiers=true

### Transacción no aparece en movimientos
**Causa:** Posible falta de sincronización entre bases
**Solución:** Verificar que CoreSyncService esté llamando correctamente al Gateway

### Usuario tiene cuentas incorrectas
**Causa:** Identificación incorrecta en usuariosistema
**Solución:** Actualizar `identificacion` en bancaweb.usuariosistema para que coincida con core.Cliente.Identificacion

### Build falla en Cloud Build
**Causa:** Timeout o recursos insuficientes
**Solución:** 
```bash
gcloud builds submit --config cloudbuild.yaml --timeout=20m
```

### Frontend muestra página en blanco
**Causa:** Error de compilación o rutas incorrectas
**Solución:**
- Verificar logs: `gcloud run services logs read [SERVICE_NAME]`
- Verificar que nginx.conf tenga `try_files $uri $uri/ /index.html;`

---

## 🌍 ACCESO PÚBLICO Y SEGURIDAD

### ¿Quién puede acceder?

✅ **SÍ - Las aplicaciones son completamente públicas:**
- Cualquier persona desde cualquier país
- Desde cualquier dispositivo (PC, móvil, tablet)
- No requiere VPN ni configuración especial
- Solo necesitan internet y un navegador

### URLs Públicas Activas:
- **Banca Web:** https://bancaweb-frontend-845403368692.us-central1.run.app
- **Cajero:** https://cajero-frontend-845403368692.us-central1.run.app

### Seguridad Actual:
- ✅ HTTPS (SSL/TLS automático por Cloud Run)
- ✅ Autenticación de usuarios (login requerido)
- ✅ Headers de seguridad en nginx
- ❌ NO hay rate limiting (pendiente para producción)
- ❌ NO hay WAF (pendiente para producción)

### Recomendaciones para Producción:
1. Implementar Cloud Armor (WAF)
2. Configurar rate limiting
3. Agregar monitoreo con Cloud Monitoring
4. Implementar alertas
5. Configurar backups automáticos de Cloud SQL
6. Usar Secret Manager para credenciales

---

## 📊 MONITOREO Y LOGS

### Ver logs en tiempo real:
```bash
# Logs de un servicio específico
gcloud run services logs read bancaweb-frontend --region=us-central1 --limit=50

# Logs de Cloud SQL
gcloud sql operations list --instance=arcbank-db-instance

# Seguir logs en vivo
gcloud run services logs tail cbs-service --region=us-central1
```

### Métricas en Cloud Console:
1. Ir a Cloud Run → Seleccionar servicio
2. Ver tab "METRICS"
3. Revisar:
   - Request count
   - Request latency
   - Container CPU/Memory utilization
   - Billable instance time

---

## 🔄 WORKFLOW DE DESARROLLO

### Desarrollo Local → Producción

1. **Desarrollar localmente:**
```bash
# Iniciar servicios locales
docker-compose up -d postgres
# Iniciar backends en IntelliJ/Eclipse
# Iniciar frontends con npm start
```

2. **Probar cambios localmente**

3. **Commit y push a GitHub:**
```bash
git add .
git commit -m "Descripción del cambio"
git push origin main
```

4. **Desplegar a Cloud:**
```bash
# En Cloud Shell
cd ~/ARCBANK
git pull
cd [servicio-modificado]
gcloud builds submit --config cloudbuild.yaml
```

5. **Verificar en producción:**
- Abrir URL del servicio
- Revisar logs si hay errores
- Probar funcionalidad

---

## 📝 NOTAS IMPORTANTES PARA EL EQUIPO

### Credenciales NO Commitear:
- ❌ NO subir archivos `.env` con credenciales reales
- ❌ NO hardcodear passwords en el código
- ✅ Usar variables de entorno
- ✅ Usar Secret Manager para producción

### Antes de Modificar Código:
1. ✅ Hacer `git pull` para tener última versión
2. ✅ Crear branch para features grandes
3. ✅ Probar localmente antes de desplegar
4. ✅ Revisar logs después del deploy

### Estructura de Commits:
```
feat: Nueva funcionalidad de transferencias
fix: Corregir error en retiro de cajero
refactor: Mejorar estructura de CorsConfig
docs: Actualizar documentación de deployment
```

### Contacto en Caso de Problemas:
- Revisar primero esta documentación
- Verificar logs en Cloud Console
- Revisar GitHub Issues del proyecto
- Contactar al equipo de desarrollo

---

## 🎯 FUNCIONALIDADES ACTUALES

### ✅ Banca Web (100% Funcional)
- Login de usuarios
- Visualización de cuentas
- Consulta de movimientos
- Transferencias entre cuentas
- Datos de perfil

### ✅ Cajero Automático (100% Funcional)
- Login de cajeros
- Búsqueda de cuentas por número
- Depósitos
- Retiros
- Generación de comprobantes

### 🔧 Backend Core
- Gestión de clientes
- Gestión de cuentas
- Procesamiento de transacciones
- Consultas de saldos y movimientos
- Sincronización entre sistemas

---

## 🚀 PRÓXIMAS MEJORAS SUGERIDAS

### Seguridad:
- [ ] Implementar JWT tokens
- [ ] Agregar refresh tokens
- [ ] Configurar Cloud Armor
- [ ] Implementar rate limiting
- [ ] Usar Secret Manager

### Funcionalidades:
- [ ] Notificaciones por email
- [ ] Dashboard de administración
- [ ] Reportes y estadísticas
- [ ] Soporte para múltiples monedas
- [ ] Integración con pasarelas de pago

### Infraestructura:
- [ ] CI/CD automático con GitHub Actions
- [ ] Staging environment
- [ ] Backups automáticos programados
- [ ] Monitoreo con alertas
- [ ] Disaster recovery plan

---

**Última actualización:** 17 de Noviembre de 2025
**Estado:** ✅ Producción - Totalmente funcional
**Región:** us-central1 (Google Cloud)
**Disponibilidad:** 24/7 - Acceso global

### Preparación
1. **Cambiar configuraciones de localhost a IPs/dominios reales**
2. **Configurar Cloud SQL (PostgreSQL)**
3. **Actualizar connection strings**
4. **Configurar CORS para dominios cloud**
5. **Variables de entorno para producción**

### Arquitectura Cloud Recomendada
```
Cloud Run (Frontends) → Cloud Load Balancer → Cloud Run (Gateway)
                                                      ↓
                        Cloud Run (Backends) ← Cloud SQL (PostgreSQL)
```

### Variables de Entorno Necesarias
```
DB_HOST=<cloud-sql-ip>
DB_PORT=5432
DB_NAME=core
DB_USER=postgres
DB_PASSWORD=<secure-password>
GATEWAY_URL=https://gateway.ejemplo.com
CORE_URL=https://core.ejemplo.com
```

---

## NOTAS IMPORTANTES

1. **NUNCA** llamar directamente al Core (puerto 8080) desde frontends o backends
2. **SIEMPRE** usar Gateway (puerto 8085) con prefijo `/cbs/`
3. Las bases de datos son completamente independientes
4. La comunicación entre servicios es **SOLO** vía Gateway
5. BCrypt está configurado en PostgreSQL con extensión `pgcrypto`
6. Los saldos se actualizan en tiempo real
7. Las transacciones son atómicas en el Core
8. Cada backend gestiona su propia base de datos
9. La identificación es el vínculo lógico entre bancaweb y core
10. El numeroCuenta es el vínculo entre cajero_db y core

---

**Versión:** 1.0
**Fecha:** 16 de Noviembre de 2025
**Sistema:** ARCBANK - Sistema Bancario Completo
