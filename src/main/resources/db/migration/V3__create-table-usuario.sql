CREATE TABLE usuario (
    id bigint not null auto_increment,
    nombre varchar(100) not null,
    correo_electronico varchar(100) not null unique,
    contrasena varchar(255) not null unique,
    activo tinyint not null default 1,

    primary key(id)
);