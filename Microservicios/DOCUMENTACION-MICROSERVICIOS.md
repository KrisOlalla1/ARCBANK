# Microservicios ARCBANK

🏦 Microservicios ARCBANK

Esta rama contiene los microservicios que conforman el Core Bancario ARCBANK, resultado de la migración de un sistema monolítico en capas hacia una arquitectura basada en microservicios.

🎯 Objetivo de la Arquitectura

El propósito de esta arquitectura es permitir que diferentes equipos y desarrolladores trabajen de forma independiente, escalable y desacoplada, garantizando que cada funcionalidad crítica del banco evolucione sin afectar a los demás componentes.

Cada microservicio encapsula un dominio de negocio específico, expone su propia API y administra su propia base de datos, eliminando dependencias directas entre módulos y favoreciendo el diseño orientado a DDD (Domain-Driven Design).

🧱 Microservicios del Core Bancario

A continuación, se listan los microservicios implementados en ARCBANK:

Microservicio	Descripción	Base de Datos	Dominio funcional
Micro Cuentas	Gestión de cuentas de ahorro, tipos de cuenta y saldos	db_cuentas	Productos financieros
Micro Clientes	Administración de personas, empresas y representantes	db_clientes	Identidad del cliente
Micro Sucursal	Entidades bancarias, sucursales, ubicaciones y feriados	db_sucursal	Infraestructura operativa
Micro Transacciones	Procesamiento de depósitos, retiros y movimientos internos	db_transacciones	Operaciones financieras internas
Micro Login	Autenticación de usuarios del banco y cajeros	db_login	Seguridad y control de acceso
Micro Transacción Interbancaria	Gestión de transferencias entre bancos mediante el switch HERMES	db_interbancaria	Integración interbancaria
🏗️ Características clave de la arquitectura

✔️ Autonomía: cada microservicio tiene su propia lógica, API y base de datos
✔️ Desacoplamiento: los servicios no comparten tablas ni acceso directo a datos
✔️ Comunicación vía APIs: la interacción entre microservicios se realiza a través de endpoints REST
✔️ API Gateway: única puerta de entrada para los canales Web y Cajero
✔️ Escalabilidad: los servicios pueden desplegarse, versionarse y escalarse de forma independiente
✔️ Alta Cohesión: cada micro está alineado a un dominio del banco definido con DDD
✔️ Integraciones externas: el micro de transacciones interbancarias se comunica con HERMES para operaciones entre bancos

🌐 Canales conectados

El Core Bancario se consume desde dos frontends:

Front Web → permite operaciones completas del cliente (incluye transferencias interbancarias)

Front Cajero → enfocado en depósitos y retiros internos (no realiza transferencias entre bancos)

Ambos canales acceden únicamente a través del API Gateway, evitando comunicación directa con los microservicios.

🚀 Estado del Proyecto

Los microservicios se encuentran en desarrollo activo. Cada módulo será publicado individualmente en su respectiva carpeta, junto con su documentación técnica, endpoints y scripts SQL.

📌 Conclusión

La transformación del Core Bancario ARCBANK hacia microservicios permite:

Mejorar la mantenibilidad del sistema

Aumentar la tolerancia a fallos

Facilitar el despliegue independiente

Habilitar la integración con otros bancos e infraestructuras financieras

Este repositorio constituye la base de una plataforma bancaria moderna, modular y preparada para escalar.