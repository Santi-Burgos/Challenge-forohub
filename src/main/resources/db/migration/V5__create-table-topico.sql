CREATE TABLE topico (
    id bigint not null auto_increment,
    titulo varchar(100) not null,
    mensaje text not null,
    fecha_creacion datetime not null,
    status varchar(100) not null,
    usuario_id bigint not null,
    curso_id bigint not null,
    activo tinyint not null default 1,

    primary key(id),
    CONSTRAINT fk_topico_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_topico_curso FOREIGN KEY (curso_id) REFERENCES curso(id)
);