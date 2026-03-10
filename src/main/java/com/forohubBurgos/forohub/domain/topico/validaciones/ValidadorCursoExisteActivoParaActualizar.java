package com.forohubBurgos.forohub.domain.topico.validaciones;

import com.forohubBurgos.forohub.domain.ValidacionException;
import com.forohubBurgos.forohub.domain.curso.Curso;
import com.forohubBurgos.forohub.domain.curso.CursoRepository;
import com.forohubBurgos.forohub.domain.topico.DatosRegistroTopico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidadorCursoExisteActivoParaActualizar implements ValidadorDeActualizacion {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void validar(DatosRegistroTopico datos) {
        List<Curso> cursos = cursoRepository.findByActivoAndNombre(true, datos.nombreCurso());
        if(cursos.isEmpty()) {
            throw new ValidacionException("No se puede actualizar tópico con curso no registrado o inactivo.");
        }
    }
}
