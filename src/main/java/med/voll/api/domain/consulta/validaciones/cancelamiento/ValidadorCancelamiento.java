package med.voll.api.domain.consulta.validaciones.cancelamiento;

import med.voll.api.domain.consulta.DatosCancelarConsulta;

public interface ValidadorCancelamiento {

    public void validar(DatosCancelarConsulta datosCancelarConsulta, Long id);
}
