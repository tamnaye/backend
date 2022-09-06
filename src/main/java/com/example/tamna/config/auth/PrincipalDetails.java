package com.example.tamna.config.auth;

import com.example.tamna.model.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Security에서 사용자의 정보를 담는 인터페이스
@Data
@RequiredArgsConstructor
@Component
public class PrincipalDetails implements UserDetails {

    private List<GrantedAuthority> authorities;
    private UserDto userDto;

    public PrincipalDetails(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        authorities = new ArrayList<>();
        System.out.println(userDto.getRoles());
        if (userDto.getRoles().equals("USER")){
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }else{
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return userDto.getUserId();
    }

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
        return false;
    }
}
