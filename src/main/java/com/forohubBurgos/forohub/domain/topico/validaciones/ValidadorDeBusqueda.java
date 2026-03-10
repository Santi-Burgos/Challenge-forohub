package com.forohubBurgos.forohub.domain.topico.validaciones;

import java.util.Optional;

public interface ValidadorDeBusqueda {
    void validar(Optional<String> nombreCurso, Optional<Integer> anioCreacion);
}
