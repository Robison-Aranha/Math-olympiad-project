package MathOlympiad.quiz.security.service;

import MathOlympiad.quiz.domain.Usuario;
import MathOlympiad.quiz.repository.UsuarioRepository;
import MathOlympiad.quiz.security.domain.UsuarioSecurity;
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
