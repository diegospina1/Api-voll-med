package med.voll.api.domain.consulta.validaciones.cancelamiento;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorCancelamiento{

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public void validar(DatosCancelarConsulta datos, Long id) {
        var consulta = consultaRepository.tomarConsultaPorId(id);
        var ahora = LocalDateTime.now();
        var diferenciaEnHoras = Duration.between(ahora, consulta.getFecha()).toHours();

        if(diferenciaEnHoras < 24){
            throw new ValidationException("Las consultas deben ser canceladas con 24 horas o mas de anticipacion");
        }
    }
}
