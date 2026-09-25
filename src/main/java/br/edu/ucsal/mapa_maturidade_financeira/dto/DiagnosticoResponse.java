package br.edu.ucsal.mapa_maturidade_financeira.dto;

import java.time.LocalDateTime;

public class DiagnosticoResponse {

    private String texto;
    private LocalDateTime dataGeracao;

    public DiagnosticoResponse(String texto, LocalDateTime dataGeracao) {
        this.texto = texto;
        this.dataGeracao = dataGeracao;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }
}
