//package com.example.tamna.config.auth;
//
//import com.example.tamna.model.User;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@Component
//public class CustomUserDetails implements UserDetails {
//    private List<GrantedAuthority> authorities;
//    private User user;
//    private String userId;
//
//    public CustomUserDetails(User user) {
//        this.user = user;
//        this.authorities = authorities;
//        this.userId = userId;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        System.out.println(user.getUserId());
//        System.out.println(user.getRoles());
//        if (user.getRoles().equals("MANAGER")){
//            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
//        }else{
//            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        }
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return userId;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
//}
