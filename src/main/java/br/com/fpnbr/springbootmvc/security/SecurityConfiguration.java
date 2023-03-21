package br.com.fpnbr.springbootmvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable() // Desativa as configurações padrões de memória
                .authorizeHttpRequests() // Permitir/Restringir acessos
                .requestMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a página inicial
                .anyRequest().authenticated()
                .and().formLogin().permitAll() // Permite qualquer usuário
                .and().logout() // Mapeia a URL de Logout e invalida o usuário autenticado
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/static/**");
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() { // Autenticação do usuário com o banco de dados ou em memória
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
