# ArcBank API Gateway# ArcBank API Gateway



Spring Cloud Gateway que expone una **única entrada (localhost:8085)** para los tres backends existentes: Spring Cloud Gateway que expone una única entrada para los tres backends existentes: Core Banking (`cbs`), banca web y cajero.

- **Core Banking (cbs)** — gestión de cuentas, transacciones y consultas

- **Banca Web (bancaweb)** — usuarios, operaciones y transferencias web## Arquitectura

- **Cajero ATM (BackendArcBank-feature-BackendCajero)** — transacciones desde cajeros automáticos- **Gateway** en el puerto `8085` con filtros globales de CORS y correlación de solicitudes (`X-Correlation-Id`).

- **Rutas**

## Arquitectura general  | Prefijo público | Backend destino | Puerto | Transformaciones |

  | --------------- | --------------- | ------ | ---------------- |

```  | `/cajero/**`    | `BackendArcBank-feature-BackendCajero` | `8082` | Reescribe a `/api/cajero/**` |

Cliente HTTP  | `/bancaweb/**`  | `bancaweb` | `8081` (ajusta al puerto real) | `StripPrefix=1` |

    ↓  | `/cbs/**`       | `cbs` | `8080` | `StripPrefix=1` |

Gateway (localhost:8085)  [CORS global, X-Correlation-Id filter, CircuitBreaker]- **Resiliencia** mediante `CircuitBreaker` con fallback JSON en `/fallback/{service}`.

    ↓

    ├─ /cbs/**       → Backend cbs (localhost:8080)## Requisitos previos

    ├─ /bancaweb/**  → Backend bancaweb (localhost:8081)1. Los tres backends deben estar ejecutándose localmente en sus respectivos puertos.

    └─ /cajero/**    → Backend cajero (localhost:8082)2. Java 21 y Maven Wrapper incluidos en este módulo.

```

## Ejecutar

| Prefijo público | Backend destino | Puerto | Transformación |```powershell

| --------------- | --------------- | ------ | --------------- |cd c:\Users\camagua\Documents\U\Arqui\Proyecto\api-gateway

| `/cbs/**` | `cbs` | `8080` | StripPrefix=1 (`/cbs/api/...` → `/api/...`) |./mvnw spring-boot:run

| `/bancaweb/**` | `bancaweb` | `8081` | StripPrefix=1 |```

| `/cajero/**` | `cajero` | `8082` | StripPrefix=1 |

## Probar rápidamente

## Requisitos previos```powershell

curl http://localhost:8085/cajero/depositar -H "Content-Type: application/json" -d "{ }"

1. **Java 21** y Maven instalados (o usa el Maven Wrapper: `./mvnw`)```

2. **Tres backends activos** en sus puertos:Ajusta cuerpo y método según los endpoints reales. Si un servicio está caído, el gateway responderá con un JSON de fallback y código `503`.

   - `cbs` en puerto `8080`
   - `bancaweb` en puerto `8081`
   - `cajero` en puerto `8082`
3. PostgreSQL con las bases de datos: `ArcBank`, `bancaweb_db`, `cajero_db`

## Ejecutar

```powershell
# Desde directorio api-gateway
cd "c:\Users\Ric\Desktop\Deberes 8vo Semestre\Arquitectura\Proyecto\api-gateway"

# Ejecutar con Maven Wrapper
.\mvnw spring-boot:run
```

El gateway estará disponible en **http://localhost:8085**

## Características

- **CORS global**: permite requests desde cualquier origen con métodos GET, POST, PUT, DELETE, PATCH, OPTIONS
- **Correlación de requests**: filtro global `X-Correlation-Id` para trazabilidad
- **Resiliencia**: CircuitBreaker por ruta con fallback automático (503 JSON si servicio no disponible)
- **Actuator**: expone `/actuator/health` e `/actuator/info`

---

# Endpoint Reference — Guía completa de solicitudes

## 1. Core Banking (CBS) — `/cbs`
Gestión de transacciones, consultas y validación de clientes.

### 1.1 Depositar dinero
**Ruta pública:** `POST /cbs/api/core/transacciones/depositar`

```powershell
curl -X POST http://localhost:8085/cbs/api/core/transacciones/depositar `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuenta": "123456789",
    "monto": 100.50,
    "descripcion": "Depósito cliente",
    "referencia": "REF001",
    "idSucursalTx": 1
  }'
```

**Respuesta (201 Created):**
```json
{
  "idTransaccion": 1,
  "numeroCuenta": "123456789",
  "tipo": "DEPOSITO",
  "monto": 100.50,
  "balance": 5100.50,
  "estado": "EXITOSA",
  "fecha": "2025-11-15T20:30:00"
}
```

---

### 1.2 Retirar dinero
**Ruta pública:** `POST /cbs/api/core/transacciones/retirar`

```powershell
curl -X POST http://localhost:8085/cbs/api/core/transacciones/retirar `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuenta": "123456789",
    "monto": 50.00,
    "descripcion": "Retiro cliente",
    "referencia": "REF002",
    "idSucursalTx": 1
  }'
```

**Respuesta (201 Created):**
```json
{
  "idTransaccion": 2,
  "numeroCuenta": "123456789",
  "tipo": "RETIRO",
  "monto": 50.00,
  "balance": 5050.50,
  "estado": "EXITOSA",
  "fecha": "2025-11-15T20:31:00"
}
```

---

### 1.3 Transferencia interna
**Ruta pública:** `POST /cbs/api/core/transacciones/transferir`

```powershell
curl -X POST http://localhost:8085/cbs/api/core/transacciones/transferir `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuentaOrigen": "123456789",
    "numeroCuentaDestino": "987654321",
    "monto": 200.00,
    "descripcion": "Transferencia interna",
    "referencia": "TRF001"
  }'
```

**Respuesta (201 Created):**
```json
{
  "idTransaccion": 3,
  "numeroCuenta": "123456789",
  "tipo": "TRANSFERENCIA",
  "monto": 200.00,
  "balance": 4850.50,
  "estado": "EXITOSA",
  "fecha": "2025-11-15T20:32:00"
}
```

---

### 1.4 Transacción externa (deposito o retiro genérico)
**Ruta pública:** `POST /cbs/api/core/transacciones`

Permite procesar depósitos y retiros desde sistemas externos (como cajeros remotos).

```powershell
curl -X POST http://localhost:8085/cbs/api/core/transacciones `
  -H "Content-Type: application/json" `
  -d '{
    "tipo": "DEPOSITO",
    "numeroCuenta": "123456789",
    "monto": 75.00,
    "descripcion": "Depósito remoto",
    "referencia": "EXT001",
    "idSucursal": 1
  }'
```

**Parámetro `tipo`**: `"DEPOSITO"` o `"RETIRO"` (case-insensitive)

**Respuesta:** igual a 1.1 o 1.2 según el tipo

---

### 1.5 Validar cliente
**Ruta pública:** `GET /cbs/api/core/consultas/cliente/{identificacion}/validar`

Valida si existe un cliente en la base de datos.

```powershell
curl http://localhost:8085/cbs/api/core/consultas/cliente/1234567890/validar
```

**Respuesta (200 OK):**
```json
{
  "identificacion": "1234567890",
  "existe": true,
  "tipoCliente": "PERSONA",
  "nombres": "Juan",
  "apellidos": "Pérez"
}
```

---

### 1.6 Posición consolidada del cliente
**Ruta pública:** `GET /cbs/api/core/consultas/posicion-consolidada/{identificacion}`

Obtiene todas las cuentas activas y saldos de un cliente.

```powershell
curl http://localhost:8085/cbs/api/core/consultas/posicion-consolidada/1234567890
```

**Respuesta (200 OK):**
```json
{
  "identificacion": "1234567890",
  "cliente": "Juan Pérez",
  "cuentas": [
    {
      "numeroCuenta": "123456789",
      "tipo": "AHORROS",
      "saldo": 5050.50,
      "estado": "ACTIVA",
      "fechaApertura": "2024-01-15"
    },
    {
      "numeroCuenta": "123456790",
      "tipo": "CORRIENTE",
      "saldo": 10000.00,
      "estado": "ACTIVA",
      "fechaApertura": "2024-02-01"
    }
  ],
  "totalSaldos": 15050.50
}
```

---

### 1.7 Movimientos de una cuenta
**Ruta pública:** `GET /cbs/api/core/consultas/movimientos/{numeroCuenta}`

Consulta el historial de transacciones de una cuenta (con filtro de fechas opcional).

```powershell
# Sin filtro de fechas
curl http://localhost:8085/cbs/api/core/consultas/movimientos/123456789

# Con filtro de fechas
curl "http://localhost:8085/cbs/api/core/consultas/movimientos/123456789?fechaInicio=2025-01-01&fechaFin=2025-11-15"
```

**Query parameters:**
- `fechaInicio` (opcional): `YYYY-MM-DD`
- `fechaFin` (opcional): `YYYY-MM-DD`

**Respuesta (200 OK):**
```json
{
  "numeroCuenta": "123456789",
  "movimientos": [
    {
      "idTransaccion": 1,
      "tipo": "DEPOSITO",
      "monto": 100.50,
      "balance": 5100.50,
      "fecha": "2025-11-15T20:30:00",
      "descripcion": "Depósito cliente",
      "referencia": "REF001"
    },
    {
      "idTransaccion": 2,
      "tipo": "RETIRO",
      "monto": 50.00,
      "balance": 5050.50,
      "fecha": "2025-11-15T20:31:00",
      "descripcion": "Retiro cliente",
      "referencia": "REF002"
    }
  ]
}
```

---

## 2. Banca Web (BANCAWEB) — `/bancaweb`
Gestión de usuarios, operaciones de caja y transferencias internas.

### 2.1 Registrar usuario
**Ruta pública:** `POST /bancaweb/api/usuarios/registro`

Crea un nuevo usuario del sistema bancaweb con contraseña encriptada (BCrypt).

```powershell
curl -X POST http://localhost:8085/bancaweb/api/usuarios/registro `
  -H "Content-Type: application/json" `
  -d '{
    "nombreUsuario": "juan.perez",
    "clave": "MiContraseña123!",
    "tipoIdentificacion": "CEDULA",
    "identificacion": "1234567890",
    "idSucursal": 1
  }'
```

**Respuesta (200 OK):**
```json
{
  "idUsuario": 1,
  "nombreUsuario": "juan.perez",
  "idSucursal": 1,
  "fechaCreacion": "2025-11-15T20:35:00",
  "estado": "ACTIVO"
}
```

**Errores posibles:**
- `409 Conflict`: si el nombre de usuario ya existe
- `400 Bad Request`: si faltan campos obligatorios

---

### 2.2 Login de usuario
**Ruta pública:** `POST /bancaweb/api/usuarios/login`

Autentica un usuario comparando contraseña con hash BCrypt.

```powershell
curl -X POST http://localhost:8085/bancaweb/api/usuarios/login `
  -H "Content-Type: application/json" `
  -d '{
    "nombreUsuario": "juan.perez",
    "clave": "MiContraseña123!"
  }'
```

**Respuesta (200 OK):**
```json
"Inicio de sesión exitoso"
```

**Respuesta (400 Bad Request):**
```json
"Usuario no encontrado"
```
o
```json
"Contraseña incorrecta"
```

---

### 2.3 Obtener usuario por ID
**Ruta pública:** `GET /bancaweb/api/usuarios/{id}`

Obtiene un usuario por su ID.

```powershell
curl http://localhost:8085/bancaweb/api/usuarios/1
```

**Respuesta (200 OK):**
```json
{
  "idUsuario": 1,
  "nombreUsuario": "juan.perez",
  "idSucursal": 1,
  "estado": "ACTIVO"
}
```

**Respuesta (404 Not Found):** si el usuario no existe

---

### 2.4 Registrar operación de caja
**Ruta pública:** `POST /bancaweb/api/operaciones`

Registra una operación de caja realizada por un usuario (simulado en el controlador).

```powershell
curl -X POST http://localhost:8085/bancaweb/api/operaciones `
  -H "Content-Type: application/json" `
  -d '{
    "idUsuario": 1,
    "tipo": "DEPOSITO",
    "monto": 500.00,
    "descripcion": "Operación de caja",
    "idCuenta": 123
  }'
```

**Respuesta (200 OK):**
```json
"Operación registrada correctamente (simulado)"
```

---

### 2.5 Consultar movimientos por usuario
**Ruta pública:** `GET /bancaweb/api/operaciones/{idUsuario}`

Consulta el historial de operaciones registradas por un usuario en un rango de fechas.

```powershell
curl "http://localhost:8085/bancaweb/api/operaciones/1?desde=2025-11-01&hasta=2025-11-15"
```

**Query parameters (obligatorios):**
- `desde`: fecha inicial (formato ISO `YYYY-MM-DD`)
- `hasta`: fecha final (formato ISO `YYYY-MM-DD`)

**Respuesta (200 OK):**
```json
[
  {
    "idOperacion": 1,
    "idUsuario": 1,
    "fecha": "2025-11-15T20:40:00",
    "tipo": "DEPOSITO",
    "monto": 500.00,
    "descripcion": "Operación de caja",
    "idCuenta": 123
  }
]
```

---

### 2.6 Realizar transferencia interna
**Ruta pública:** `POST /bancaweb/api/transferencias`

Realiza una transferencia entre cuentas (validada internamente).

```powershell
curl -X POST http://localhost:8085/bancaweb/api/transferencias `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuentaOrigen": "123456789",
    "numeroCuentaDestino": "987654321",
    "monto": 250.00,
    "descripcion": "Transferencia web"
  }'
```

**Respuesta (200 OK):**
```json
"Transferencia procesada exitosamente"
```

**Errores posibles:**
- `400 Bad Request`: si faltan campos o monto inválido

---

### 2.7 Obtener posición consolidada
**Ruta pública:** `GET /bancaweb/api/cuentas/consolidada/{identificacion}`

Consulta todas las cuentas activas de un cliente (agregadas desde el backend cbs).

```powershell
curl http://localhost:8085/bancaweb/api/cuentas/consolidada/1234567890
```

**Respuesta (200 OK):**
```json
[
  {
    "numeroCuenta": "123456789",
    "tipo": "AHORROS",
    "saldo": 5050.50,
    "estado": "ACTIVA",
    "titulares": ["Juan Pérez"]
  },
  {
    "numeroCuenta": "123456790",
    "tipo": "CORRIENTE",
    "saldo": 10000.00,
    "estado": "ACTIVA",
    "titulares": ["Juan Pérez"]
  }
]
```

**Respuesta (204 No Content):** si no hay cuentas activas

---

### 2.8 Consultar movimientos por cuenta
**Ruta pública:** `GET /bancaweb/api/cuentas/movimientos/{numeroCuenta}`

Obtiene el historial de movimientos de una cuenta específica (proxy al backend cbs).

```powershell
curl "http://localhost:8085/bancaweb/api/cuentas/movimientos/123456789?fechaInicio=2025-11-01&fechaFin=2025-11-15"
```

**Query parameters (opcionales):**
- `fechaInicio`: `YYYY-MM-DD`
- `fechaFin`: `YYYY-MM-DD`

**Respuesta:** igual a la del endpoint 1.7 (movimientos del core)

---

## 3. Cajero Automático (CAJERO) — `/cajero`
Transacciones desde cajeros automáticos: depósitos, retiros y consulta de historial.

### 3.1 Depositar en cajero
**Ruta pública:** `POST /cajero/api/cajero/depositar`

Registra un depósito realizado desde un cajero automático.

```powershell
curl -X POST http://localhost:8085/cajero/api/cajero/depositar `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuenta": "123456789",
    "monto": 100.00,
    "idCajero": 1,
    "descripcion": "Depósito en cajero",
    "referencia": "ATM-001"
  }'
```

**Respuesta (201 Created):**
```json
{
  "idTransaccion": 1,
  "numeroCuenta": "123456789",
  "idCajero": 1,
  "tipo": "DEPOSITO",
  "monto": 100.00,
  "descripcion": "Depósito en cajero",
  "referencia": "ATM-001",
  "fecha": "2025-11-15T20:45:00",
  "estado": "PENDIENTE"
}
```

---

### 3.2 Retirar en cajero
**Ruta pública:** `POST /cajero/api/cajero/retirar`

Registra un retiro realizado desde un cajero automático.

```powershell
curl -X POST http://localhost:8085/cajero/api/cajero/retirar `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuenta": "123456789",
    "monto": 50.00,
    "idCajero": 1,
    "descripcion": "Retiro en cajero",
    "referencia": "ATM-002"
  }'
```

**Respuesta:** similar a 3.1

---

### 3.3 Listar transacciones de cajero
**Ruta pública:** `GET /cajero/api/cajero/transacciones?numeroCuenta={numeroCuenta}`

Obtiene el historial de transacciones de una cuenta desde cajeros (ordenadas por fecha descendente).

```powershell
curl "http://localhost:8085/cajero/api/cajero/transacciones?numeroCuenta=123456789"
```

**Query parameter (obligatorio):**
- `numeroCuenta`: número de cuenta

**Respuesta (200 OK):**
```json
[
  {
    "idTransaccion": 2,
    "numeroCuenta": "123456789",
    "idCajero": 1,
    "tipo": "RETIRO",
    "monto": 50.00,
    "descripcion": "Retiro en cajero",
    "referencia": "ATM-002",
    "fecha": "2025-11-15T20:45:30",
    "estado": "PENDIENTE"
  },
  {
    "idTransaccion": 1,
    "numeroCuenta": "123456789",
    "idCajero": 1,
    "tipo": "DEPOSITO",
    "monto": 100.00,
    "descripcion": "Depósito en cajero",
    "referencia": "ATM-001",
    "fecha": "2025-11-15T20:45:00",
    "estado": "PENDIENTE"
  }
]
```

---

## Manejo de errores y fallbacks

### CircuitBreaker — Cuando un backend está caído

Si un backend no responde, el gateway activa el CircuitBreaker y devuelve un JSON de fallback (HTTP 503):

```powershell
curl http://localhost:8085/fallback/cbs
```

**Respuesta (503 Service Unavailable):**
```json
{
  "service": "cbs",
  "timestamp": "2025-11-15T20:50:00.123456Z",
  "message": "El servicio cbs no está disponible temporalmente."
}
```

Lo mismo aplica para `/fallback/bancaweb` y `/fallback/cajero`.

### Cabecera de correlación

Todas las requests/responses incluyen automáticamente la cabecera `X-Correlation-Id` para trazabilidad:

```powershell
curl http://localhost:8085/cbs/api/core/consultas/cliente/1234567890/validar -i
```

Output incluirá:
```
X-Correlation-Id: 550e8400-e29b-41d4-a716-446655440000
```

Si quieres pasar tu propio ID:
```powershell
curl http://localhost:8085/cbs/api/core/consultas/cliente/1234567890/validar `
  -H "X-Correlation-Id: my-custom-id-123"
```

---

## Pruebas rápidas en PowerShell

### 1. Verificar que el gateway está activo
```powershell
curl http://localhost:8085/actuator/health
```

### 2. Depositar en CBS
```powershell
curl -X POST http://localhost:8085/cbs/api/core/transacciones/depositar `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuenta":"123456789",
    "monto":100.0,
    "descripcion":"Test",
    "referencia":"REF-TEST",
    "idSucursalTx":1
  }'
```

### 3. Validar cliente en CBS
```powershell
curl http://localhost:8085/cbs/api/core/consultas/cliente/1234567890/validar
```

### 4. Registrar usuario en BANCAWEB
```powershell
curl -X POST http://localhost:8085/bancaweb/api/usuarios/registro `
  -H "Content-Type: application/json" `
  -d '{
    "nombreUsuario":"testuser",
    "clave":"Test@1234",
    "tipoIdentificacion":"CEDULA",
    "identificacion":"9999999999",
    "idSucursal":1
  }'
```

### 5. Depositar en CAJERO
```powershell
curl -X POST http://localhost:8085/cajero/api/cajero/depositar `
  -H "Content-Type: application/json" `
  -d '{
    "numeroCuenta":"123456789",
    "monto":50.0,
    "idCajero":1,
    "descripcion":"Test ATM",
    "referencia":"ATM-TEST"
  }'
```

---

## Estructura de configuración

Archivo: `src/main/resources/application.yml`

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: cbs-service
          uri: http://localhost:8080
          predicates:
            - Path=/cbs/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: cbsCircuitBreaker
                fallbackUri: forward:/fallback/cbs

        - id: bancaweb-service
          uri: http://localhost:8081
          predicates:
            - Path=/bancaweb/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: bancawebCircuitBreaker
                fallbackUri: forward:/fallback/bancaweb

        - id: cajero-service
          uri: http://localhost:8082
          predicates:
            - Path=/cajero/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: cajeroCircuitBreaker
                fallbackUri: forward:/fallback/cajero

      globalcors:
        add-to-simple-url-handler-mapping: true
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - PATCH
              - OPTIONS
            allowedHeaders: "*"

server:
  port: 8085

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

---

## Troubleshooting

| Problema | Causa | Solución |
| --- | --- | --- |
| `Connection refused` a `localhost:8085` | Gateway no está corriendo | Ejecuta `.\mvnw spring-boot:run` |
| `503 Service Unavailable` | Backend no disponible | Verifica que los 3 backends estén activos en sus puertos (8080, 8081, 8082) |
| `404 Not Found` | Ruta incorrecta o typo | Revisa la URL pública vs. ruta del backend (recuerda StripPrefix=1) |
| Request timeout | Servicio lento o muerto | Aumenta timeout en CircuitBreaker o reinicia los backends |
| CORS error en navegador | CORS no permitido | Gateway tiene CORS global abierto; verifica encabezados en request |

---

## Contacto y soporte

Para reportar bugs o sugerir mejoras, contacta al equipo de arquitectura.

**Última actualización:** 15 de noviembre de 2025
