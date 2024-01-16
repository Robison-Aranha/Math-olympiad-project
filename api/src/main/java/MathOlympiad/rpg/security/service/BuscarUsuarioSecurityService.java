package MathOlympiad.rpg.security.service;

import MathOlympiad.rpg.domain.Usuario;
import MathOlympiad.rpg.repository.UsuarioRepository;
import MathOlympiad.rpg.security.domain.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BuscarUsuarioSecurityService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNome(nome).get();
        return new UsuarioSecurity(usuario);
    }

}
