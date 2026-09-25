package br.edu.ucsal.mapa_maturidade_financeira.dto;

public class QuestionarioRequest {

    private String respostaOrcamento;
    private String respostaReservaEmergencia;
    private String respostaDivida;
    private String respostaConhecimentoInvestimento;
    private String respostaExperienciaInvestimento;

    public String getRespostaOrcamento() {
        return respostaOrcamento;
    }

    public void setRespostaOrcamento(String respostaOrcamento) {
        this.respostaOrcamento = respostaOrcamento;
    }

    public String getRespostaReservaEmergencia() {
        return respostaReservaEmergencia;
    }

    public void setRespostaReservaEmergencia(String respostaReservaEmergencia) {
        this.respostaReservaEmergencia = respostaReservaEmergencia;
    }

    public String getRespostaDivida() {
        return respostaDivida;
    }

    public void setRespostaDivida(String respostaDivida) {
        this.respostaDivida = respostaDivida;
    }

    public String getRespostaConhecimentoInvestimento() {
        return respostaConhecimentoInvestimento;
    }

    public void setRespostaConhecimentoInvestimento(String respostaConhecimentoInvestimento) {
        this.respostaConhecimentoInvestimento = respostaConhecimentoInvestimento;
    }

    public String getRespostaExperienciaInvestimento() {
        return respostaExperienciaInvestimento;
    }

    public void setRespostaExperienciaInvestimento(String respostaExperienciaInvestimento) {
        this.respostaExperienciaInvestimento = respostaExperienciaInvestimento;
    }
}