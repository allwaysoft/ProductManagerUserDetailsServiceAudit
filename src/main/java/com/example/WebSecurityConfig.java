package com.example;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomLoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomLoginSuccessHandler successHandler;

    @Autowired

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Filter filter = new KaptchaAuthenticationFilter("/login", "/login?error");
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/captcha_image**").permitAll()
                .antMatchers("/header").permitAll()
                .antMatchers("/footer").permitAll()
                .antMatchers("/sidebar").permitAll()
                .antMatchers("/index2").permitAll()
                .antMatchers("/index3").permitAll()
                .antMatchers("/pages/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/common/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/verify").permitAll()
                .antMatchers("/home").authenticated()
                .antMatchers("/user/info").authenticated()
                .antMatchers("/change/password").authenticated()
                .antMatchers("/new/password").authenticated()
                .antMatchers("/").authenticated()
                .anyRequest()
                .access("@rbacService.hasPermission(request , authentication)")
                .and()
                .formLogin().loginPage("/login")
                .permitAll()
                .failureHandler(loginFailureHandler)
                .successHandler(successHandler)
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");
        http
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/")
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
