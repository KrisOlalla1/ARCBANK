login 
postgres 17
-- DATABASE: db_login

-- Tabla Hija (Usuarios Web)
CREATE TABLE public."UsuarioSistema" (
    "IdUsuario" SERIAL PRIMARY KEY,
    "NombreUsuario" character varying(50) NOT NULL UNIQUE,
    "ClaveHash" character varying(255) NOT NULL,
    "IdSucursal" integer, -- Referencia lógica a db_sucursal
    "FechaCreacion" timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "UltimoAcceso" timestamp without time zone,
    "Estado" character varying(15) DEFAULT 'ACTIVO' NOT NULL,
    CONSTRAINT "UsuarioSistema_Estado_check" CHECK ("Estado" IN ('ACTIVO', 'INACTIVO', 'BLOQUEADO'))
);

-- Tabla Padre (Cajero Empleado)
CREATE TABLE public."Cajero" (
    "IdCajero" SERIAL PRIMARY KEY,
    "Usuario" character varying(50) NOT NULL UNIQUE,
    "Clave" character varying(100) NOT NULL,
    "NombreCompleto" character varying(150) NOT NULL,
    "IdSucursal" integer NOT NULL, -- Referencia lógica a db_sucursal
    "Estado" character varying(10) DEFAULT 'ACTIVO' NOT NULL,
    CONSTRAINT "Cajero_Estado_check" CHECK ("Estado" IN ('ACTIVO', 'INACTIVO'))
);

-- Tabla Hija de Cajero (Historial Login)
CREATE TABLE public."LoginCajero" (
    "IdLogin" SERIAL PRIMARY KEY,
    "IdCajero" integer NOT NULL, -- FK: Hijo apunta a Padre
    "FechaHora" timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "LoginCajero_IdCajero_fkey" FOREIGN KEY ("IdCajero") REFERENCES public."Cajero"("IdCajero")
);
