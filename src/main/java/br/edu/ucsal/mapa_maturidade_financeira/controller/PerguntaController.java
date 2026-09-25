package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.RespostaQuizRequest;
import br.edu.ucsal.mapa_maturidade_financeira.model.Pergunta;
import br.edu.ucsal.mapa_maturidade_financeira.service.PerguntaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class PerguntaController {

    private final PerguntaService perguntaService;

    public PerguntaController(PerguntaService perguntaService) {
        this.perguntaService = perguntaService;
    }

    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<?> listarPerguntas(@PathVariable Long moduloId) {
        try {
            List<Pergunta> perguntas = perguntaService.listarPerguntasDoModulo(moduloId);
            return ResponseEntity.ok(perguntas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/responder")
    public ResponseEntity<?> responder(@RequestBody RespostaQuizRequest dados, Authentication authentication) {
        try {
            return ResponseEntity.ok(
                    perguntaService.corrigirResposta(authentication.getName(), dados.getPerguntaId(), dados.getAlternativaEscolhida())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
