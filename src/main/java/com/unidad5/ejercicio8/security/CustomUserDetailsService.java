package com.unidad5.ejercicio8.security;

import com.unidad5.ejercicio8.exception.ConflictException;
import com.unidad5.ejercicio8.model.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // Usuario Admin por defecto para probar
        usuarios.put("admin", new Usuario("admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN"));
        usuarios.put("biblio", new Usuario("biblio", passwordEncoder.encode("biblio123"), "ROLE_BIBLIOTECARIO"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!usuarios.containsKey(username)) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return usuarios.get(username);
    }

    public void registrarUsuario(String username, String rawPassword) {
        if (usuarios.containsKey(username)) {
            throw new ConflictException("El usuario ya existe");
        }
        usuarios.put(username, new Usuario(username, passwordEncoder.encode(rawPassword), "ROLE_LECTOR"));
    }
}