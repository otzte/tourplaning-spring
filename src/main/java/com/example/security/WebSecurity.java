package com.example.security;

import com.example.ws.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity //configure entry points, this is a web security configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;  //injected via @EnableWebSecurity
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;//injected via @EnableWebSecurity
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL)
                .permitAll()//alle Post requests mit signUpURL akzeptieren alle anderen Requests ablehnen
                .anyRequest().authenticated().and()
                .addFilter(getAuthenticationFilter())//use authenticationFilter Implementation
                .addFilter(new AuthorizationFilter(authenticationManager()))//Authorizationfilter(uses JWT in header)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // authenticationManager() comes from  WebsecurityConfigurerAdapter and returns AuthentificationManager
        //has a request authority to do so?
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //                        User.class
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder); // needs password encoder

    }

    public AuthenticationFilter getAuthenticationFilter() throws Exception{
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
}
