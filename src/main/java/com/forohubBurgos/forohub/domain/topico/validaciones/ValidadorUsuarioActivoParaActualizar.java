package com.forohubBurgos.forohub.domain.topico.validaciones;

import com.forohubBurgos.forohub.domain.ValidacionException;
import com.forohubBurgos.forohub.domain.topico.DatosRegistroTopico;
import com.forohubBurgos.forohub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorUsuarioActivoParaActualizar implements ValidadorDeActualizacion {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validar(DatosRegistroTopico datos) {
        var usuario = usuarioRepository.getReferenceById(datos.idUsuario());
        if(!usuario.getActivo()) {
            throw new ValidacionException("No se puede actualizar tópico con usuario inactivo.");
        }
    }
}
