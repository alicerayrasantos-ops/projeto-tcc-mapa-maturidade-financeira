package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.QuestionarioRequest;
import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.service.PerfilOnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilOnboardingController {

    private final PerfilOnboardingService perfilService;

    public PerfilOnboardingController(PerfilOnboardingService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping("/questionario")
    public ResponseEntity<?> responderQuestionario(
            @RequestBody QuestionarioRequest respostas,
            Authentication authentication
    ) {
        try {
            String emailUsuario = authentication.getName();
            PerfilOnboarding perfil = perfilService.calcularEsalvar(emailUsuario, respostas);
            return ResponseEntity.ok(perfil);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
