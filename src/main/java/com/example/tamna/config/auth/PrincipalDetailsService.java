package com.example.tamna.config.auth;

import com.example.tamna.model.User;
import com.example.tamna.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자를 자동 생성해주는 어노테이션
public class PrincipalDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public PrincipalDetailsService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{

        System.out.println("PrincipalDetailsService: 진입");
        User user = userService.getUserData(userId);

        return new PrincipalDetails(user);
    }

}
