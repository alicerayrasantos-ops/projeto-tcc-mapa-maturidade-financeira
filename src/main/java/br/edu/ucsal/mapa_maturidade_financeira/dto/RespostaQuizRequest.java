package br.edu.ucsal.mapa_maturidade_financeira.dto;

public class RespostaQuizRequest {

    private Long perguntaId;
    private String alternativaEscolhida; // "A", "B", "C" ou "D"

    public Long getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }

    public String getAlternativaEscolhida() {
        return alternativaEscolhida;
    }

    public void setAlternativaEscolhida(String alternativaEscolhida) {
        this.alternativaEscolhida = alternativaEscolhida;
    }
}
