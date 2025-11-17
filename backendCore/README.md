# ArcBank - Core Bancario Simplificado 🏦

## 📖 Descripción
Backend Core del sistema bancario ArcBank. Gestiona las operaciones principales de cuentas de ahorro mediante arquitectura por capas.

## 🎯 Alcance del Proyecto
Este **BACKEND CORE** es parte de una arquitectura distribuida más grande:
- ✅ **Implementado**: Operaciones del core bancario (depósito, retiro, transferencia, consultas)
- ❌ **No incluido**: Frontend Web, Frontend Cajero, Backend Web, Backend Cajero, API Gateway

## 🏗️ Arquitectura

### Arquitectura General del Sistema
```
┌─────────────┐       ┌──────────────┐
│ FRONTEND WEB│       │FRONTEND CAJERO│
└──────┬──────┘       └──────┬────────┘
       │                     │
┌──────▼──────┐       ┌──────▼─────────┐
│ BACKEND WEB │       │ BACKEND CAJERO │
└──────┬──────┘       └──────┬─────────┘
       │                     │
       └──────────┬──────────┘
                  │
          ┌───────▼────────┐
          │  BACKEND CORE  │ ◄── ESTE PROYECTO
          │   (Este API)   │
          └───────┬────────┘
                  │
          ┌───────▼────────┐
          │   PostgreSQL   │
          │   (BD CORE)    │
          └────────────────┘
```

### Arquitectura por Capas (Este Proyecto)
```
┌────────────────────────────────┐
│   Controller Layer (REST API)  │
├────────────────────────────────┤
│   Service Layer (Lógica)       │
├────────────────────────────────┤
│   Repository Layer (Datos)     │
├────────────────────────────────┤
│   Model Layer (Entidades JPA)  │
└────────────────────────────────┘
```

## 🚀 Tecnologías
- **Java**: 21
- **Spring Boot**: 3.5.7
- **Spring Data JPA**: Para acceso a datos
- **PostgreSQL**: Base de datos
- **Lombok**: Reducción de código boilerplate
- **Bean Validation**: Validación de datos
- **Maven**: Gestión de dependencias

## 📋 Requisitos Previos
- JDK 21 o superior
- PostgreSQL 14 o superior
- Maven 3.8+ (incluido wrapper)
- Git

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone <tu-repositorio>
cd cbs
```

### 2. Configurar PostgreSQL
```sql
-- Crear base de datos
CREATE DATABASE arcbank_cbs;

-- Ejecutar DDL principal (crear tablas)
-- Ver archivo: ddl-postgresql.sql

-- Cargar datos de prueba (opcional)
-- Ver archivo: test-data.sql
```

### 3. Configurar application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/arcbank_cbs
spring.datasource.username=postgres
spring.datasource.password=tu_password
```

### 4. Compilar el proyecto
```bash
./mvnw clean install
```

### 5. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

## 🔗 APIs Disponibles

### Transacciones (Para Backend Cajero)
- `POST /api/core/transacciones` - **Endpoint principal para Backend Cajero** (DEPOSITO/RETIRO)

### Transacciones Específicas
- `POST /api/core/transacciones/depositar` - Realizar depósito
- `POST /api/core/transacciones/retirar` - Realizar retiro
- `POST /api/core/transacciones/transferir` - Transferir entre cuentas

### Consultas
- `GET /api/core/consultas/posicion-consolidada/{identificacion}` - Posición consolidada del cliente
- `GET /api/core/consultas/movimientos/{numeroCuenta}` - Movimientos de una cuenta

**📄 Documentación completa**: Ver [API_DOCUMENTATION.md](API_DOCUMENTATION.md)  
**🔌 Integración con Backend Cajero**: Ver [INTEGRACION_BACKEND_CAJERO.md](INTEGRACION_BACKEND_CAJERO.md)

## 🧪 Pruebas

### Ejemplos con cURL
Ver archivo: [TEST_EXAMPLES.md](TEST_EXAMPLES.md)

### Ejemplo rápido (PowerShell)
```powershell
# Depositar
Invoke-RestMethod -Uri "http://localhost:8080/api/core/transacciones/depositar" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"numeroCuenta":"1234567890","monto":500.00,"descripcion":"Test"}'

# Consultar posición
Invoke-RestMethod -Uri "http://localhost:8080/api/core/consultas/posicion-consolidada/1234567890"
```

## 📊 Modelo de Datos

### Entidades Principales
- **EntidadBancaria**: Entidades bancarias del sistema
- **Cliente**: Clientes (personas o empresas)
- **CuentaAhorro**: Cuentas de ahorro
- **Transaccion**: Transacciones bancarias
- **Sucursal**: Sucursales bancarias
- **TipoCuentaAhorro**: Tipos de cuenta (PERSONAL, EMPRESARIAL)

Ver DDL completo en la documentación del proyecto.

## ✅ Funcionalidades Implementadas

### Operaciones Bancarias
- ✅ Depósitos en cuentas de ahorro
- ✅ Retiros de cuentas de ahorro
- ✅ Transferencias entre cuentas de la misma institución
- ✅ Validación de saldos disponibles
- ✅ Validación de estado de cuentas
- ✅ Transaccionalidad garantizada

### Consultas
- ✅ Posición consolidada del cliente
- ✅ Movimientos por cuenta
- ✅ Filtrado por fechas

### Validaciones
- ✅ Monto mayor a cero
- ✅ Cuenta debe existir y estar activa
- ✅ Saldo suficiente para retiros/transferencias
- ✅ No transferir a la misma cuenta

### Características Técnicas
- ✅ Manejo centralizado de excepciones
- ✅ Logs detallados de operaciones
- ✅ Arquitectura por capas
- ✅ DTOs para entrada/salida
- ✅ Transacciones ACID

## 🔒 Consideraciones de Seguridad

⚠️ **IMPORTANTE**: Este es un proyecto académico. Para producción se debe agregar:
- Autenticación y autorización (JWT, OAuth2)
- HTTPS/TLS
- Rate limiting
- Auditoría completa
- Encriptación de datos sensibles

## 📁 Estructura del Proyecto
```
cbs/
├── src/main/java/com/arcbank/cbs/
│   ├── controller/          # REST Controllers
│   ├── service/            # Lógica de negocio
│   ├── repository/         # Acceso a datos
│   ├── model/              # Entidades JPA
│   │   ├── admin/          # Entidades administrativas
│   │   ├── client/         # Entidades de clientes
│   │   └── passive/        # Productos pasivos
│   ├── dto/                # Data Transfer Objects
│   └── exception/          # Excepciones personalizadas
├── src/main/resources/
│   └── application.properties
├── API_DOCUMENTATION.md    # Documentación de APIs
├── TEST_EXAMPLES.md        # Ejemplos de pruebas
├── test-data.sql          # Datos de prueba
└── pom.xml                # Dependencias Maven
```

## 🐛 Solución de Problemas

### Error: "relation does not exist"
**Solución**: Verificar que `hibernate.globally_quoted_identifiers=true` esté en application.properties

### Error: Dependencias no encontradas
**Solución**: Ejecutar `./mvnw clean install`

### Error: No se puede conectar a PostgreSQL
**Solución**: Verificar que PostgreSQL esté corriendo y las credenciales sean correctas

## 📚 Recursos Adicionales
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## 👥 Equipo
Proyecto académico - Universidad

## 📝 Licencia
Proyecto educativo

## 🔮 Mejoras Futuras
- [ ] Implementar cálculo automático de intereses
- [ ] Validación de días hábiles usando tabla Feriado
- [ ] Límites de transacción por tipo de cuenta
- [ ] Endpoints para gestión de cuentas (crear, cerrar)
- [ ] Reversión de transacciones
- [ ] Documentación con Swagger/OpenAPI
- [ ] Tests unitarios y de integración
- [ ] Métricas y monitoreo
- [ ] Seguridad (JWT)

---

**¿Necesitas ayuda?** Revisa la documentación en [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
