package Api.RoquiGames.RoquiGames.Configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityFilterChainConfig {

    @Bean
    public SecurityFilterChain securityFilterChainConfig(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/api/login",
                        "/api/usuarios/registro",
                        "/api/videojuegos/**",
                        "/api/plataformas/**",
                        "/api/generos/**",
                        "/api/valoraciones/**"
                ).permitAll().anyRequest().authenticated());
        return httpSecurity.build();

    }
}
