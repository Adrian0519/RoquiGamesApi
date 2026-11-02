package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;


@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generarToken(Usuarios usuarios) {
        // Decodifica la clave secreta que está codificada en Base64.
        // Después, la convierte en una clave criptográfica que se utilizará para firmar los tokens JWT.
        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + expiration);

        // Inicia la construcción de un nuevo token JWT.
        return Jwts.builder()
                // Establece el ID del usuario como identificador principal del token.
                .subject(usuarios.getId_usuario().toString())
                // Registra la fecha y hora en la que se genera el token.
                .issuedAt(fechaActual)
                // Establece la fecha y hora en la que el token dejará de ser válido.
                .expiration(fechaExpiracion)
                // Firma el token utilizando nuestra clave secreta.
                .signWith(secretKey)
                // Construye el JWT y lo devuelve como una cadena de texto.
                .compact();
    }

    public Integer extraerUsuarioId(String token){
        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

        String idUsuario= Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return Integer.valueOf(idUsuario);
    }
}
