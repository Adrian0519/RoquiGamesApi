package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.Excepciones.CredencialesInvalidasException;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoDuplicadoException;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceppcionesController {

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarDuplicado(RecursoDuplicadoException ex) {

        Map.of("error duplicado ", ex.getMessage());
        Map<String, String> cuerpo = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);

    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String,String>> manejarNoEncontrado(RecursoNoEncontradoException ex){

        Map<String, String> cuerpo= Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesIncorrectas(BadCredentialsException ex) {

        Map<String, String> cuerpo = Map.of("error", "Email o contraseña incorrectos");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo);

    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {

        Map<String, String> cuerpo = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo);

    }

}
