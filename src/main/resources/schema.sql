


CREATE TABLE categoria_comercio (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    nombre VARCHAR(200) NOT NULL
);

CREATE TABLE categoria_socio (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    nombre VARCHAR(200) NOT NULL
);

CREATE TABLE comercio (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    nombre VARCHAR(200) NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    cuit VARCHAR(12) ,
    direccion VARCHAR(200) NOT NULL,
    altura INT,
    piso VARCHAR(10),
    departamento VARCHAR(10),
    barrio_id VARCHAR(50),
    localidad_id VARCHAR(50),
    provincia_id VARCHAR(50),
    categoria_comercio_id BIGINT NOT NULL
);

CREATE TABLE localidad (
    id VARCHAR(50) PRIMARY KEY,
    version INT,
    nombre VARCHAR(50) NOT NULL,
    provincia_id VARCHAR(50) NOT NULL
);

CREATE TABLE promocion (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    nombre VARCHAR(200) NOT NULL
);

CREATE TABLE promocion_comercio (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    comercio_id BIGINT NOT NULL,
    promocion_id BIGINT NOT NULL,
    start_date DATE NULL,
    end_date DATE NULL,
    regla_recurrente VARCHAR(MAX) NULL
);

CREATE TABLE provincia (
    id VARCHAR(50) PRIMARY KEY,
    version INT,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE socio (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    tipo_socio VARCHAR(50) NOT NULL,
    numero_socio VARCHAR(50) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(50) NOT NULL,
    activo BIT NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(50) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    altura INT,
    piso VARCHAR(50),
    departamento VARCHAR(50),
    barrio_id VARCHAR(50),
    localidad_id VARCHAR(50),
    provincia_id VARCHAR(50),
    categoria_socio_id BIGINT
);

CREATE TABLE sucursal (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    comercio_id BIGINT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    abierta BIT NOT NULL,

    direccion VARCHAR(200) NOT NULL,
    altura INT,
    piso VARCHAR(50),
    departamento VARCHAR(50),
    barrio_id VARCHAR(50),
    localidad_id VARCHAR(50),
    provincia_id VARCHAR(50)
);

CREATE TABLE tag_comercio (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    comercio_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL    --UNIQUE comercio_id, tag
);

CREATE TABLE tag_socio (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    socio_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL    --UNIQUE socio_id, tag
);

CREATE TABLE venta (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL ,
    sucursal_id BIGINT NOT NULL,
    socio_id BIGINT NOT NULL,
    promocion_id BIGINT NOT NULL,
    importe DECIMAL(18,2) NOT NULL
);



CREATE TABLE "users" (
    id BIGINT IDENTITY PRIMARY KEY,
    version INT,
    email VARCHAR(200) UNIQUE,
    password_hash VARCHAR(200),
    first_name VARCHAR(200),
    last_name VARCHAR(200),
    role VARCHAR(200),
    locked BIT,
    sucursal BIGINT NULL
);