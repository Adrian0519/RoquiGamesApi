package Api.RoquiGames.RoquiGames.Configuracion;

import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import Api.RoquiGames.RoquiGames.Repositorios.UsuariosRepository;
import Api.RoquiGames.RoquiGames.Servicios.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuariosRepository usuariosRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuariosRepository usuariosRepository) {

        this.jwtService = jwtService;
        this.usuariosRepository = usuariosRepository;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Obtenemos la cabecera Authorization de la petición HTTP, donde el cliente envía el token JWT.
        String authHeader = request.getHeader("Authorization");

        // Comprobamos que la cabecera existe y que contiene un token Bearer.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el JWT eliminando el prefijo "Bearer " de la cabecera.
        String token = authHeader.substring(7);
        Integer usuarioId;

        try {
            usuarioId = jwtService.extraerUsuarioId(token);
        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token JWT no valido o caducado"
            );

            return;

        }

        Usuarios usuario = usuariosRepository.findById(usuarioId).orElse(null);

        if (usuario != null && usuario.isEnabled()
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }

        filterChain.doFilter(request, response);

    }

}
