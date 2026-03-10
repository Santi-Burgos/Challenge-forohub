package com.forohubBurgos.forohub.domain.curso;

public record DatosDetalleCurso(
        String nombre,
        Categoria categoria) {

    public DatosDetalleCurso(Curso curso) {
        this(curso.getNombre(), curso.getCategoria());
    }
}
