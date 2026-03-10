package com.forohubBurgos.forohub.domain.usuario;

import com.forohubBurgos.forohub.domain.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userDetails = usuarioRepository.findByCorreoElectronicoAndActivoTrue(username);
        if(userDetails == null) {
            throw new ValidacionException("Usuario no registrado o inactivo.");
        }
        return userDetails;
    }
}
