package com.tsystems.nazukin.logiweb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by 1 on 17.03.2016.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    // регистрируем нашу реализацию UserDetailsService
    // а также PasswordEncoder для приведения пароля в формат SHA1
    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // указываем правила запросов
                // по которым будет определятся доступ к ресурсам и остальным данным
        http.authorizeRequests()
                .antMatchers("/resources/**", "/login", "/registration").permitAll()
                .antMatchers("/manager/**").access("hasRole('MANAGER')")
                .antMatchers("/driver/**").access("hasRole('DRIVER')")
                .anyRequest().permitAll()
                .and()

            .formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                // указываем URL при неудачном логине
                .failureUrl("/login?type=notRegistr")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("j_login")
                .passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll()
                .and()

            .logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutUrl("/logout")
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login")
                // делаем не валидной текущую сессию
                .invalidateHttpSession(true)
                .and()

            .exceptionHandling()
                .accessDeniedPage("/home");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
