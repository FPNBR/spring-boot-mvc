package br.com.fpnbr.springbootmvc.security;

import br.com.fpnbr.springbootmvc.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable() // Desativa as configurações padrões de memória
                .authorizeHttpRequests() // Permitir/Restringir acessos
                .requestMatchers(HttpMethod.GET, "/", "/static/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/cadastro-pessoa").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().permitAll() // Permite qualquer usuário
                .loginPage("/login")
                .defaultSuccessUrl("/cadastro-pessoa")
                .failureUrl("/login?error=true")
                .and().logout().logoutSuccessUrl("/login") // Mapeia a URL de Logout e invalida o usuário autenticado
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailServiceImpl)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .build();
    }
}
