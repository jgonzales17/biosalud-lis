/*
=============================================================================
 SCRIPT MAESTRO DE CREACIÓN: BioSaludDB
=============================================================================
 Propósito:
 1. Conectarse a 'master'.
 2. Crear la BD 'BioSaludDB' si no existe.
 3. Usar 'BioSaludDB' y ELIMINAR todas las tablas antiguas (en orden).
 4. CREAR todas las tablas nuevas (estructura).
 5. AÑADIR todas las llaves foráneas (relaciones) y constraints.
=============================================================================
*/

-- 1. CONECTARSE A MASTER Y CREAR LA BD
USE master;
GO

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'BioSaludDB')
BEGIN
    CREATE DATABASE BioSaludDB;
    PRINT 'Base de datos BioSaludDB CREADA.';
END
ELSE
BEGIN
    PRINT 'Base de datos BioSaludDB ya existe.';
END
GO

-- 2. USAR LA BD Y ELIMINAR TABLAS ANTIGUAS
USE BioSaludDB;
GO

PRINT 'Eliminando tablas existentes en orden de dependencia...';
-- Hijos (dependen de otras tablas)
DROP TABLE IF EXISTS detalle_factura;
DROP TABLE IF EXISTS resultado_laboratorio;
DROP TABLE IF EXISTS toma_muestra;
-- Padres (son referenciados)
DROP TABLE IF EXISTS orden_laboratorio;
DROP TABLE IF EXISTS factura;
DROP TABLE IF EXISTS paciente;
DROP TABLE IF EXISTS medico;
DROP TABLE IF EXISTS tecnico_laboratorio;
DROP TABLE IF EXISTS usuario;
GO

-- 3. CREAR LA ESTRUCTURA DE TABLAS (de tu log de Hibernate)
PRINT 'Creando estructura de tablas...';

create table detalle_factura (
    id_detalle int identity not null, 
    cantidad int, 
    descripcion_servicio varchar(255), 
    precio_unitario float(53), 
    id_factura int, 
    primary key (id_detalle)
);

create table factura (
    id_factura int identity not null, 
    fecha_emision datetime2(6), 
    metodo_pago varchar(255), 
    monto_total float(53), 
    id_paciente int, 
    primary key (id_factura)
);

create table medico (
    id_medico int identity not null, 
    apellidos varchar(255), 
    cmp varchar(255) not null, 
    especialidad varchar(255), 
    nombres varchar(255), 
    telefono varchar(255), 
    primary key (id_medico)
);

create table orden_laboratorio (
    id_orden int identity not null, 
    entregado bit, 
    fecha_orden datetime2(6), 
    observaciones varchar(255), 
    tipo_examen varchar(255), 
    id_medico int, 
    id_paciente int, 
    primary key (id_orden)
);

create table paciente (
    id_paciente int identity not null, 
    apellidos varchar(255), 
    dni varchar(255) not null, 
    email varchar(255), 
    fecha_nacimiento datetime2(6), 
    nombres varchar(255), 
    telefono varchar(255), 
    primary key (id_paciente)
);

create table resultado_laboratorio (
    id_resultado int identity not null, 
    conclusiones varchar(255), 
    descripcion varchar(255), 
    fecha_resultado datetime2(6), 
    validado bit, 
    valores varchar(255), 
    id_orden int, 
    primary key (id_resultado)
);

create table tecnico_laboratorio (
    id_tecnico int identity not null, 
    apellidos varchar(255), 
    especialidad varchar(255), 
    nombres varchar(255), 
    telefono varchar(255), 
    primary key (id_tecnico)
);

create table toma_muestra (
    id_toma int identity not null, 
    fecha_hora datetime2(6), 
    tipo_muestra varchar(255), 
    id_orden int, 
    id_tecnico int, 
    primary key (id_toma)
);

create table usuario (
    id_usuario int identity not null, 
    contrasena varchar(255) not null, 
    nombre_usuario varchar(255) not null, 
    rol varchar(255), 
    primary key (id_usuario)
);
GO

-- 4. AÑADIR CONSTRAINTS (ÚNICOS, FOREIGN KEYS, ETC.)
PRINT 'Añadiendo llaves foráneas y constraints...';

alter table medico add constraint UK_medico_cmp unique (cmp);
alter table paciente add constraint UK_paciente_dni unique (dni);
alter table usuario add constraint UK_usuario_nombre unique (nombre_usuario);

-- Constraint especial para toma_muestra (solo 1 toma por orden)
alter table toma_muestra add constraint UK_toma_muestra_orden unique (id_orden);

alter table detalle_factura add constraint FK_detalle_factura 
    foreign key (id_factura) references factura;

alter table factura add constraint FK_factura_paciente 
    foreign key (id_paciente) references paciente;

alter table orden_laboratorio add constraint FK_orden_medico 
    foreign key (id_medico) references medico;

alter table orden_laboratorio add constraint FK_orden_paciente 
    foreign key (id_paciente) references paciente;

alter table resultado_laboratorio add constraint FK_resultado_orden 
    foreign key (id_orden) references orden_laboratorio;

alter table toma_muestra add constraint FK_toma_orden 
    foreign key (id_orden) references orden_laboratorio;

alter table toma_muestra add constraint FK_toma_tecnico 
    foreign key (id_tecnico) references tecnico_laboratorio;
GO

PRINT '¡ÉXITO! Base de datos BioSaludDB creada y lista.';