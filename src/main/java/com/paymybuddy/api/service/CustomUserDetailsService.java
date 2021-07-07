//package com.paymybuddy.api.service;
//
//import com.paymybuddy.api.model.User;
//import com.paymybuddy.api.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email);
//        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
//                user.getPassword(), Arrays.asList(authority));
//        return userDetails;
//    }
//
//}
