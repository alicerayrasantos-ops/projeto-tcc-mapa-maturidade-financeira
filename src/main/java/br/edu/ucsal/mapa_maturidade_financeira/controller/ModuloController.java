package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.ConcluirModuloRequest;
import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.service.ModuloService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
    }

    @GetMapping("/trilha")
    public ResponseEntity<?> listarTrilha(Authentication authentication) {
        try {
            List<Modulo> trilha = moduloService.listarTrilhaDoUsuario(authentication.getName());
            return ResponseEntity.ok(trilha);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/concluir")
    public ResponseEntity<?> concluirModulo(
            @RequestBody ConcluirModuloRequest dados,
            Authentication authentication
    ) {
        try {
            moduloService.marcarComoConcluido(authentication.getName(), dados.getModuloId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/progresso")
    public ResponseEntity<?> obterProgresso(Authentication authentication) {
        try {
            double percentual = moduloService.calcularPercentualConcluido(authentication.getName());
            return ResponseEntity.ok(Map.of("percentualConcluido", percentual));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
