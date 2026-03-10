package com.forohubBurgos.forohub.domain.topico;

import com.forohubBurgos.forohub.domain.ValidacionException;
import com.forohubBurgos.forohub.domain.curso.CursoRepository;
import com.forohubBurgos.forohub.domain.topico.validaciones.ValidadorDeActualizacion;
import com.forohubBurgos.forohub.domain.topico.validaciones.ValidadorDeBusqueda;
import com.forohubBurgos.forohub.domain.topico.validaciones.ValidadorDeRegistro;
import com.forohubBurgos.forohub.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private List<ValidadorDeRegistro> validadores;

    @Autowired
    private List<ValidadorDeBusqueda> validadoresBusqueda;

    @Autowired
    private List<ValidadorDeActualizacion> validadoresActualizacion;

    public DatosDetalleTopico registrar(@Valid DatosRegistroTopico datos) {
        if(!usuarioRepository.existsById(datos.idUsuario())) {
            throw new ValidacionException("No existe usuario con el id indicado.");
        }

        // validaciones
        // 1 usuario activo
        // 2 existe el curso y es activo
        // 3 duplicidad de tópicos (título+mensaje únicos)
        validadores.forEach(v -> v.validar(datos));

        var autor = usuarioRepository.findById(datos.idUsuario()).get();
        var curso = cursoRepository.findByNombre(datos.nombreCurso()).get(0);
        var nuevoTopico = new Topico(datos, autor, curso);
        topicoRepository.save(nuevoTopico);

        return new DatosDetalleTopico(nuevoTopico.getId(), nuevoTopico.getTitulo(), nuevoTopico.getMensaje(), nuevoTopico.getFechaCreacion());
    }

    public Page<DatosListaTopico> listar(Pageable paginacion) {
        return topicoRepository.findAllByActivoTrue(paginacion)
                .map(DatosListaTopico::new);
    }

    public Page<DatosListaTopico> buscar(Optional<String> nombreCurso, Optional<Integer> anioCreacion, Pageable paginacion) {
        // validaciones
        validadoresBusqueda.forEach(v -> v.validar(nombreCurso, anioCreacion));

        if(nombreCurso.isPresent() && anioCreacion.isPresent()) {
            LocalDate fecha = LocalDate.of(anioCreacion.get(), 1, 1);
            LocalDateTime primerDiaDelAnio = fecha.atStartOfDay();
            LocalDateTime ultimoDiaDelAnio = fecha.plusYears(1).atStartOfDay();
            return topicoRepository.buscarPorNombreYAnioCreacionCurso(primerDiaDelAnio, ultimoDiaDelAnio, nombreCurso.get(), paginacion)
                    .map(DatosListaTopico::new);
        }
        if(nombreCurso.isPresent() && anioCreacion.isEmpty()) {
            return topicoRepository.buscarPorNombreCurso(nombreCurso.get(), paginacion)
                .map(DatosListaTopico::new);
        }
        if(nombreCurso.isEmpty() && anioCreacion.isPresent()) {
            LocalDate fecha = LocalDate.of(anioCreacion.get(), 1, 1);
            LocalDateTime primerDiaDelAnio = fecha.atStartOfDay();
            LocalDateTime ultimoDiaDelAnio = fecha.plusYears(1).atStartOfDay();
            return topicoRepository.findAllByFechaCreacionBetweenAndActivoTrue(primerDiaDelAnio, ultimoDiaDelAnio, paginacion)
                    .map(DatosListaTopico::new);
        }
        return listar(paginacion);
    }

    public Topico obtener(Long id) {
        if(!topicoRepository.existsById(id)) {
            throw new ValidacionException("No existe topico con el id indicado.");
        }
        return topicoRepository.getReferenceById(id);
    }

    public DatosDetalleTopico actualizar(Long id, @Valid DatosRegistroTopico datos) {
        if(!topicoRepository.existsById(id)) {
            throw new ValidacionException("No existe tópico con el id indicado.");
        }
        if(!usuarioRepository.existsById(datos.idUsuario())) {
            throw new ValidacionException("No existe usuario con el id indicado.");
        }
        // validaciones
        // 1 usuario activo
        // 2 existe el curso y es activo
        validadoresActualizacion.forEach(v -> v.validar(datos));

        // 3 duplicidad de tópicos para actualización (título+mensaje únicos)
        var cuantos = topicoRepository.countByTituloAndMensajeAndIdNot(datos.titulo(), datos.mensaje(), id);
        if(cuantos > 0) {
            throw new ValidacionException("No se puede actualizar tópico duplicado, debe cambiar título o mensaje.");
        }

        var topicoParaActualizar = topicoRepository.getReferenceById(id);
        var autor = usuarioRepository.findById(datos.idUsuario()).get();
        var curso = cursoRepository.findByNombre(datos.nombreCurso()).get(0);
        topicoParaActualizar.actualizar(datos, autor, curso);

        return new DatosDetalleTopico(topicoParaActualizar.getId(), topicoParaActualizar.getTitulo(), topicoParaActualizar.getMensaje(), topicoParaActualizar.getFechaCreacion());
    }

    public void eliminar(Long id) {
        if(!topicoRepository.existsById(id)) {
            throw new ValidacionException("No existe tópico con el id indicado.");
        }
        topicoRepository.deleteById(id);
    }
}
