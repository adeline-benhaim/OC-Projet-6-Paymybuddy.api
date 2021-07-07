//package com.paymybuddy.api.configuration;
//
//import com.paymybuddy.api.service.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/user/**", "/transaction/**", "/bankAccount/**", "/connection/**", "/transfer/**")
//                .hasAnyAuthority("ADMIN","USER").
//                .authenticated()
//                .antMatchers("/user/create").permitAll()
//                .anyRequest().denyAll()
//                .and().csrf().disable().cors()
//                .and().exceptionHandling()
//        ;
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//    }
//}