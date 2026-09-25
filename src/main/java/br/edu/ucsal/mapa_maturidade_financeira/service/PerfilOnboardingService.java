package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.dto.QuestionarioRequest;
import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.PerfilOnboardingRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class PerfilOnboardingService {

    public static final String PERFIL_INICIANTE = "INICIANTE";
    public static final String PERFIL_EM_DESENVOLVIMENTO = "EM_DESENVOLVIMENTO";
    public static final String PERFIL_INVESTIDOR = "INVESTIDOR";

    private final PerfilOnboardingRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilOnboardingService(PerfilOnboardingRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public PerfilOnboarding calcularEsalvar(String emailUsuario, QuestionarioRequest respostas) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        int scoreOrcamento = converterParaPontuacao(respostas.getRespostaOrcamento());
        int scoreReserva = converterParaPontuacao(respostas.getRespostaReservaEmergencia());
        int scoreDivida = converterParaPontuacao(respostas.getRespostaDivida());
        int scoreConhecimento = converterParaPontuacao(respostas.getRespostaConhecimentoInvestimento());
        int scoreExperiencia = converterParaPontuacao(respostas.getRespostaExperienciaInvestimento());

        int pontuacaoTotal = scoreOrcamento + scoreReserva + scoreDivida + scoreConhecimento + scoreExperiencia;
        int pontuacaoBaseFinanceira = scoreOrcamento + scoreReserva + scoreDivida;

        String perfilCalculado = classificarPerfil(respostas.getRespostaExperienciaInvestimento(), pontuacaoBaseFinanceira);

        PerfilOnboarding perfil = perfilRepository.findByUsuario(usuario)
                .orElse(new PerfilOnboarding());

        perfil.setUsuario(usuario);
        perfil.setPontuacaoTotal(pontuacaoTotal);
        perfil.setPerfilCalculado(perfilCalculado);

        return perfilRepository.save(perfil);
    }

    /**
     * Regra de classificação:
     * 1. Experiência prática real com investimentos (C ou D) tem prioridade:
     *    quem já investe de fato vai direto para "Investidor".
     * 2. Sem experiência prática relevante, olhamos a base financeira
     *    (orçamento + reserva + dívida). Base fraca (0 a 3 de 9) indica
     *    que a pessoa ainda não tem os fundamentos, então vai para "Iniciante".
     * 3. Caso intermediário: tem alguma base, mas ainda não investe -> "Em desenvolvimento".
     */
    private String classificarPerfil(String respostaExperiencia, int pontuacaoBaseFinanceira) {

        boolean temExperienciaPratica = "C".equalsIgnoreCase(respostaExperiencia)
                || "D".equalsIgnoreCase(respostaExperiencia);

        if (temExperienciaPratica) {
            return PERFIL_INVESTIDOR;
        }

        if (pontuacaoBaseFinanceira <= 3) {
            return PERFIL_INICIANTE;
        }

        return PERFIL_EM_DESENVOLVIMENTO;
    }

    private int converterParaPontuacao(String alternativa) {
        if (alternativa == null) {
            return 0;
        }
        switch (alternativa.toUpperCase()) {
            case "A": return 0;
            case "B": return 1;
            case "C": return 2;
            case "D": return 3;
            default: return 0;
        }
    }
}