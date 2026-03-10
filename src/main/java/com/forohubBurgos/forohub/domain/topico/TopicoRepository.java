package com.forohubBurgos.forohub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Long countByTituloAndMensaje(String titulo, String mensaje);

    Page<Topico> findAllByActivoTrue(Pageable paginacion);

    @Query("SELECT t FROM Topico t WHERE t.curso.nombre = :nombreCurso AND t.activo = TRUE")
    Page<Topico> buscarPorNombreCurso(String nombreCurso, Pageable paginacion);

    Page<Topico> findAllByFechaCreacionBetweenAndActivoTrue(LocalDateTime inicio, LocalDateTime fin, Pageable paginacion);

    @Query("""
            SELECT t FROM Topico t
            WHERE t.fechaCreacion BETWEEN :inicio AND :fin
                        AND t.curso.nombre = :nombreCurso
                        AND t.activo = TRUE
            """)
    Page<Topico> buscarPorNombreYAnioCreacionCurso(LocalDateTime inicio, LocalDateTime fin, String nombreCurso, Pageable paginacion);

    Long countByTituloAndMensajeAndIdNot(String titulo, String mensaje, Long id);
}
