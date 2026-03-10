package com.forohubBurgos.forohub.domain.topico;

import com.forohubBurgos.forohub.domain.curso.DatosDetalleCurso;
import com.forohubBurgos.forohub.domain.usuario.DatosDetalleUsuario;

import java.time.LocalDateTime;

public record DatosListaTopico(
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        Status estado,
        DatosDetalleUsuario autor,
        DatosDetalleCurso curso) {

    public DatosListaTopico(Topico topico) {
        this(topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(), topico.getStatus(),
                new DatosDetalleUsuario(topico.getAutor()), new DatosDetalleCurso(topico.getCurso()));
    }
}
