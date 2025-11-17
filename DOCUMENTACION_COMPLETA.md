# DOCUMENTACIÓN COMPLETA - SISTEMA BANCARIO ARCBANK

## ARQUITECTURA DEL SISTEMA

### Componentes Principales
```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND LAYER                            │
├──────────────────────┬──────────────────────────────────────┤
│ Frontend Web (React) │ Frontend Cajero (React + Vite)       │
│ Puerto: 3000         │ Puerto: 3001                         │
└──────────────────────┴──────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    BACKEND LAYER                             │
├──────────────────────┬──────────────────────────────────────┤
│ Backend Web          │ Backend Cajero                       │
│ Puerto: 8081         │ Puerto: 8082                         │
└──────────────────────┴──────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    API GATEWAY                               │
│                    Puerto: 8085                              │
│  Rutas:                                                      │
│  /cbs/** → Core:8080                                        │
│  /bancaweb/** → Web:8081                                    │
│  /cajero/** → Cajero:8082                                   │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    BACKEND CORE                              │
│                    Puerto: 8080                              │
│  Endpoints principales:                                      │
│  - POST /api/core/transacciones                             │
│  - GET /api/core/consultas/posicion-consolidada/{id}       │
│  - GET /api/core/consultas/movimientos/{numeroCuenta}      │
│  - GET /api/core/consultas/cuenta/{numeroCuenta}           │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                            │
│              PostgreSQL 16 - Puerto 5433                     │
├──────────────────────┬──────────────────────┬───────────────┤
│ DB: core             │ DB: bancaweb         │ DB: cajero_db │
│ Usuario: postgres    │ Usuario: postgres    │ User: postgres│
│ Password: 123        │ Password: 123        │ Pass: 123     │
└──────────────────────┴──────────────────────┴───────────────┘
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

## TROUBLESHOOTING COMÚN

### Error: "Cannot read properties of undefined"
**Causa:** Frontend intentando acceder a campo que no existe en respuesta
**Solución:** Verificar que backend devuelva el campo correcto (ej: `identificacion` no `cedula`)

### Error: CORS
**Causa:** Frontend llamando directamente al Core sin pasar por Gateway
**Solución:** Cambiar URLs para usar Gateway en puerto 8085

### Error: "No existe la relación"
**Causa:** Nombres de tablas con/sin comillas en PostgreSQL
**Solución:** Usar comillas dobles: `"CuentaAhorro"`, `"Persona"`, etc.

### Transacción no aparece en movimientos
**Causa:** Posible falta de sincronización entre bases
**Solución:** Verificar que CoreSyncService esté llamando correctamente al Gateway

### Usuario tiene cuentas incorrectas
**Causa:** Identificación incorrecta en usuariosistema
**Solución:** Actualizar `identificacion` en bancaweb.usuariosistema para que coincida con core.Cliente.Identificacion

---

## MIGRACIÓN A GOOGLE CLOUD

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
