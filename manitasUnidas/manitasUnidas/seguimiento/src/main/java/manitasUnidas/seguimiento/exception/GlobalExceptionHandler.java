package manitasUnidas.seguimiento.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones del microservicio.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación Jakarta Validation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    manejarValidaciones(MethodArgumentNotValidException ex) {

        log.warn("Error de validación detectado");

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        return ResponseEntity.badRequest().body(errores);
    }

    /**
     * Maneja recursos inexistentes.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String>
    manejarNoEncontrado(ResourceNotFoundException ex) {

        log.warn("Recurso no encontrado: {}", ex.getMessage());

        return ResponseEntity
                .status(404)
                .body(ex.getMessage());
    }

    /**
     * Maneja errores inesperados.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String>
    manejarErrorGeneral(Exception ex) {

        log.error("Error interno: ", ex);

        return ResponseEntity
                .internalServerError()
                .body("Ha ocurrido un error interno en el servidor.");
    }
}