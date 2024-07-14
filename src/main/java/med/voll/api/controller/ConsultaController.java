package med.voll.api.controller;



import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.*;
import med.voll.api.infra.errors.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultaService service;

    @PostMapping
    @Transactional
    public ResponseEntity agendarCita(@RequestBody @Valid DatosAgendarConsulta datosAgendarConsulta) throws ValidacionDeIntegridad {

        var response = service.agendar(datosAgendarConsulta);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity cancelarCita(@RequestBody @Valid DatosCancelarConsulta datosCancelarCita, @PathVariable Long id){

        service.cancelar(datosCancelarCita, id);
        return ResponseEntity.noContent().build();
    }
}
