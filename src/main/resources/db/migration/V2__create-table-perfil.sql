CREATE TABLE perfil (
    id bigint not null auto_increment,
    nombre varchar(50) not null,
    activo tinyint not null default 1,

    primary key(id)
);