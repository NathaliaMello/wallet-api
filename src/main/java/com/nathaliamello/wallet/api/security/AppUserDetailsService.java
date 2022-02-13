package com.nathaliamello.wallet.api.security;

import com.nathaliamello.wallet.api.model.Usuario;
import com.nathaliamello.wallet.api.repository.Usuariorepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.*;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private Usuariorepository usuariorepository;

    @Autowired
    public AppUserDetailsService(Usuariorepository usuariorepository) {
        this.usuariorepository = usuariorepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuariorepository.findByEmail(email);
        Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
        return new User(email, usuario.getSenha(), getPermissoes(usuario));
    }

    private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        usuario.getPermissoes().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase(Locale.ROOT))));
        return authorities;
    }
}
