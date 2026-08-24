package com.spring.security.app.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.spring.security.app.entity.Usuario;

public class CustomUserDetails implements UserDetails{

    /* ini inyeccion por constructor */
    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
 
    }
    /* fin inyeccion por constructor */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + usuario.getRole()
                )
        );
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    
    /* ini los genero codex */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    /* fin los genero codex */

}
