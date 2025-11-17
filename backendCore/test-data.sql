-- =====================================================
-- DATOS DE PRUEBA PARA EL CORE BANCARIO
-- =====================================================

-- NOTA: Este script asume que ya ejecutaste el DDL principal

-- ========= ENTIDAD BANCARIA =========
INSERT INTO "EntidadBancaria" ("Nombre", "Ruc", "Estado") 
VALUES ('ArcBank Ecuador', '1234567890001', 'ACTIVA');

-- ========= UBICACIÓN GEOGRÁFICA =========
-- País
INSERT INTO "UbicacionGeografica" ("Nombre", "Tipo", "IdUbicacionPadre") 
VALUES ('Ecuador', 'PAIS', NULL);

-- Provincia
INSERT INTO "UbicacionGeografica" ("Nombre", "Tipo", "IdUbicacionPadre") 
VALUES ('Pichincha', 'PROVINCIA', 1);

-- Cantón
INSERT INTO "UbicacionGeografica" ("Nombre", "Tipo", "IdUbicacionPadre") 
VALUES ('Quito', 'CANTON', 2);

-- Parroquia
INSERT INTO "UbicacionGeografica" ("Nombre", "Tipo", "IdUbicacionPadre") 
VALUES ('La Mariscal', 'PARROQUIA', 3);

-- ========= SUCURSALES =========
INSERT INTO "Sucursal" ("IdEntidad", "CodigoUnico", "Nombre", "Direccion", "Telefono", "Latitud", "Longitud", "IdUbicacion", "Estado", "FechaApertura") 
VALUES 
(1, 'SUC-001', 'Matriz Quito', 'Av. Amazonas N21-41', '02-2500100', -0.180653, -78.479226, 4, 'ACTIVA', '2020-01-15'),
(1, 'SUC-002', 'Sucursal Norte', 'Av. De los Shyris N35-98', '02-2500200', -0.150000, -78.480000, 4, 'ACTIVA', '2021-03-20');

-- ========= FERIADOS =========
INSERT INTO "Feriado" ("Fecha", "Descripcion", "EsNacional", "IdUbicacion") 
VALUES 
('2025-01-01', 'Año Nuevo', TRUE, NULL),
('2025-12-25', 'Navidad', TRUE, NULL),
('2025-08-10', 'Primer Grito de Independencia', TRUE, NULL);

-- ========= TIPOS DE CUENTA =========
INSERT INTO "TipoCuentaAhorro" ("Nombre", "Descripcion", "TasaInteresMaxima", "Amortizacion", "Activo") 
VALUES 
('PERSONAL', 'Cuenta de ahorro para personas naturales', 3.50, 'MENSUAL', TRUE),
('EMPRESARIAL', 'Cuenta de ahorro para empresas', 2.75, 'MENSUAL', TRUE);

-- ========= TASAS DE INTERÉS HISTORIAL =========
INSERT INTO "TasaInteresHistorial" ("IdTipoCuenta", "TasaInteresAnual", "FechaInicioVigencia", "FechaFinVigencia", "Observaciones") 
VALUES 
(1, 3.50, '2025-01-01', NULL, 'Tasa vigente para cuentas personales'),
(2, 2.75, '2025-01-01', NULL, 'Tasa vigente para cuentas empresariales');

-- ========= CLIENTES PERSONA =========
-- Cliente 1: Juan Pérez
INSERT INTO "Cliente" ("TipoCliente", "TipoIdentificacion", "Identificacion", "FechaRegistro", "Estado") 
VALUES ('P', 'CEDULA', '1234567890', '2024-01-10', 'ACTIVO');

INSERT INTO "Persona" ("IdCliente", "Nombres", "Apellidos", "Identificacion", "FechaNacimiento", "DireccionPrincipal") 
VALUES (1, 'Juan Carlos', 'Pérez García', '1234567890', '1990-05-15', 'Av. 6 de Diciembre N35-123');

-- Cliente 2: María López
INSERT INTO "Cliente" ("TipoCliente", "TipoIdentificacion", "Identificacion", "FechaRegistro", "Estado") 
VALUES ('P', 'CEDULA', '0987654321', '2024-02-15', 'ACTIVO');

INSERT INTO "Persona" ("IdCliente", "Nombres", "Apellidos", "Identificacion", "FechaNacimiento", "DireccionPrincipal") 
VALUES (2, 'María Fernanda', 'López Rodríguez', '0987654321', '1985-08-20', 'Av. Naciones Unidas E10-45');

-- Cliente 3: Pedro Gómez (Será representante de empresa)
INSERT INTO "Cliente" ("TipoCliente", "TipoIdentificacion", "Identificacion", "FechaRegistro", "Estado") 
VALUES ('P', 'CEDULA', '1122334455', '2024-03-01', 'ACTIVO');

INSERT INTO "Persona" ("IdCliente", "Nombres", "Apellidos", "Identificacion", "FechaNacimiento", "DireccionPrincipal") 
VALUES (3, 'Pedro Antonio', 'Gómez Mora', '1122334455', '1980-12-10', 'Av. República E7-123');

-- ========= CLIENTES EMPRESA =========
-- Cliente 4: TechCorp S.A.
INSERT INTO "Cliente" ("TipoCliente", "TipoIdentificacion", "Identificacion", "FechaRegistro", "Estado") 
VALUES ('E', 'RUC', '1790123456001', '2024-04-01', 'ACTIVO');

INSERT INTO "Empresa" ("IdCliente", "RazonSocial", "Ruc", "FechaConstitucion", "DireccionPrincipal") 
VALUES (4, 'TechCorp S.A.', '1790123456001', '2010-06-15', 'Av. República del Salvador N34-567');

-- Representante de la empresa
INSERT INTO "EmpresaRepresentante" ("IdEmpresa", "IdRepresentante", "Rol", "FechaDesignacion", "FechaFinDesignacion", "Estado") 
VALUES (4, 3, 'Representante Legal', '2024-04-01', NULL, 'ACTIVO');

-- ========= CUENTAS DE AHORRO =========
-- Cuenta 1: Juan Pérez - Personal
INSERT INTO "CuentaAhorro" ("NumeroCuenta", "IdCliente", "IdTipoCuenta", "IdSucursalApertura", "SaldoActual", "SaldoDisponible", "FechaApertura", "Estado") 
VALUES ('1234567890', 1, 1, 1, 1000.00, 1000.00, '2024-01-15', 'ACTIVA');

-- Cuenta 2: María López - Personal
INSERT INTO "CuentaAhorro" ("NumeroCuenta", "IdCliente", "IdTipoCuenta", "IdSucursalApertura", "SaldoActual", "SaldoDisponible", "FechaApertura", "Estado") 
VALUES ('0987654321', 2, 1, 2, 2500.00, 2500.00, '2024-02-20', 'ACTIVA');

-- Cuenta 3: Juan Pérez - Segunda cuenta personal
INSERT INTO "CuentaAhorro" ("NumeroCuenta", "IdCliente", "IdTipoCuenta", "IdSucursalApertura", "SaldoActual", "SaldoDisponible", "FechaApertura", "Estado") 
VALUES ('1234567891', 1, 1, 1, 500.00, 500.00, '2024-06-10', 'ACTIVA');

-- Cuenta 4: TechCorp S.A. - Empresarial
INSERT INTO "CuentaAhorro" ("NumeroCuenta", "IdCliente", "IdTipoCuenta", "IdSucursalApertura", "SaldoActual", "SaldoDisponible", "FechaApertura", "Estado") 
VALUES ('1000000001', 4, 2, 1, 50000.00, 50000.00, '2024-04-05', 'ACTIVA');

-- Cuenta 5: Pedro Gómez - Personal
INSERT INTO "CuentaAhorro" ("NumeroCuenta", "IdCliente", "IdTipoCuenta", "IdSucursalApertura", "SaldoActual", "SaldoDisponible", "FechaApertura", "Estado") 
VALUES ('1122334455', 3, 1, 2, 3000.00, 3000.00, '2024-03-15', 'ACTIVA');

-- ========= TRANSACCIONES INICIALES =========
-- Depósito inicial cuenta Juan Pérez
INSERT INTO "Transaccion" ("IdCuenta", "Tipo", "Monto", "Balance", "FechaHora", "Descripcion", "Referencia", "IdSucursalTx", "Estado") 
VALUES (1, 'DEPOSITO', 1000.00, 1000.00, '2024-01-15 10:00:00', 'Depósito inicial', 'DEP-INICIAL-001', 1, 'COMPLETADA');

-- Depósito inicial cuenta María López
INSERT INTO "Transaccion" ("IdCuenta", "Tipo", "Monto", "Balance", "FechaHora", "Descripcion", "Referencia", "IdSucursalTx", "Estado") 
VALUES (2, 'DEPOSITO', 2500.00, 2500.00, '2024-02-20 11:30:00', 'Depósito inicial', 'DEP-INICIAL-002', 2, 'COMPLETADA');

-- Depósito inicial cuenta Juan Pérez #2
INSERT INTO "Transaccion" ("IdCuenta", "Tipo", "Monto", "Balance", "FechaHora", "Descripcion", "Referencia", "IdSucursalTx", "Estado") 
VALUES (3, 'DEPOSITO', 500.00, 500.00, '2024-06-10 09:15:00', 'Depósito inicial', 'DEP-INICIAL-003', 1, 'COMPLETADA');

-- Depósito inicial cuenta TechCorp
INSERT INTO "Transaccion" ("IdCuenta", "Tipo", "Monto", "Balance", "FechaHora", "Descripcion", "Referencia", "IdSucursalTx", "Estado") 
VALUES (4, 'DEPOSITO', 50000.00, 50000.00, '2024-04-05 14:00:00', 'Depósito inicial empresarial', 'DEP-INICIAL-004', 1, 'COMPLETADA');

-- Depósito inicial cuenta Pedro Gómez
INSERT INTO "Transaccion" ("IdCuenta", "Tipo", "Monto", "Balance", "FechaHora", "Descripcion", "Referencia", "IdSucursalTx", "Estado") 
VALUES (5, 'DEPOSITO', 3000.00, 3000.00, '2024-03-15 16:30:00', 'Depósito inicial', 'DEP-INICIAL-005', 2, 'COMPLETADA');

-- ========= VERIFICACIÓN =========
-- Ver resumen de clientes y cuentas
SELECT 
    c."TipoCliente",
    CASE 
        WHEN c."TipoCliente" = 'P' THEN (p."Nombres" || ' ' || p."Apellidos")
        ELSE e."RazonSocial"
    END AS "Cliente",
    c."Identificacion",
    ca."NumeroCuenta",
    ca."SaldoActual",
    ca."Estado"
FROM "Cliente" c
LEFT JOIN "Persona" p ON c."IdCliente" = p."IdCliente"
LEFT JOIN "Empresa" e ON c."IdCliente" = e."IdCliente"
LEFT JOIN "CuentaAhorro" ca ON c."IdCliente" = ca."IdCliente"
ORDER BY c."IdCliente";

-- =====================================================
-- RESUMEN DE DATOS CREADOS
-- =====================================================
-- Cuentas para pruebas:
-- 1234567890 - Juan Pérez (Saldo: $1000.00)
-- 0987654321 - María López (Saldo: $2500.00)
-- 1234567891 - Juan Pérez #2 (Saldo: $500.00)
-- 1000000001 - TechCorp S.A. (Saldo: $50000.00)
-- 1122334455 - Pedro Gómez (Saldo: $3000.00)

-- Identificaciones de clientes:
-- 1234567890 - Juan Pérez (2 cuentas)
-- 0987654321 - María López (1 cuenta)
-- 1122334455 - Pedro Gómez (1 cuenta)
-- 1790123456001 - TechCorp S.A. (1 cuenta)
