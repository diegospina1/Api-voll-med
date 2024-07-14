package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.consulta.validaciones.cancelamiento.ValidadorCancelamiento;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errors.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    List<ValidadorDeConsultas> validadores;
    @Autowired
    List<ValidadorCancelamiento> validadoresCancelamiento;

    public DatosDetalleConsulta agendar(DatosAgendarConsulta datos){
        if(!pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el paciente no fue encontrado");
        }
        if(datos.idMedico()!=null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionDeIntegridad("este id para el medico no fue encontrado");
        }
        validadores.forEach(v-> v.validar(datos));
        var paciente = pacienteRepository.findById(datos.idPaciente()).get();
        var medico = seleccionarMedico(datos);
        if(medico==null){
            throw new ValidacionDeIntegridad("no existen medicos disponibles para este horario y especialidad");
        }
        var consulta = new Consulta(medico,paciente,datos.fecha());
        consultaRepository.save(consulta);
        return new DatosDetalleConsulta(consulta);
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if(datos.idMedico() != null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if(datos.especialidad() == null){
            throw new ValidacionDeIntegridad("Debe seleccionar una especialidad para el medico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(), datos.fecha());
    }

    public void cancelar(DatosCancelarConsulta datos, Long id) {
        if(!consultaRepository.existsById(id)){
            throw new ValidacionDeIntegridad("El id de la consulta no existe!");
        }
        validadoresCancelamiento.forEach(v -> v.validar(datos, id));

        var consulta = consultaRepository.getReferenceById(id);
        consulta.cancelar(datos.motivo());
    }
}
