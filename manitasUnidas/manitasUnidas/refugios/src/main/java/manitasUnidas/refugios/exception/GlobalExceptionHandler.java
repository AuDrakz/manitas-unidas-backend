package manitasUnidas.refugios.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger manual explícito para el manejador de excepciones
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        log.warn("Errores de validación: {}", errores);

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> manejarNoEncontrado(
            ResourceNotFoundException ex) {

        log.warn("Recurso no encontrado: {}", ex.getMessage());

        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrorGeneral(Exception ex) {

        log.error("Error inesperado", ex);

        return ResponseEntity.status(500)
                .body("Error interno del servidor");
    }
}