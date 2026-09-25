package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.ProgressoUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ModuloRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.PerfilOnboardingRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ProgressoUsuarioRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ModuloService {

    private final ModuloRepository moduloRepository;
    private final ProgressoUsuarioRepository progressoRepository;
    private final PerfilOnboardingRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public ModuloService(
            ModuloRepository moduloRepository,
            ProgressoUsuarioRepository progressoRepository,
            PerfilOnboardingRepository perfilRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.moduloRepository = moduloRepository;
        this.progressoRepository = progressoRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Modulo> listarTrilhaDoUsuario(String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        String perfil = perfilRepository.findByUsuario(usuario)
                .map(p -> p.getPerfilCalculado())
                .orElse(PerfilOnboardingService.PERFIL_INICIANTE); // se ainda não fez o questionário, usa o perfil mais básico

        List<Modulo> camada1 = moduloRepository.findByCamadaOrderByOrdemAsc(1);
        List<Modulo> camada2 = moduloRepository.findByCamadaOrderByOrdemAsc(2);
        List<Modulo> camada3 = moduloRepository.findByCamadaOrderByOrdemAsc(3);

        int limiteCamada1;
        int limiteCamada2;
        int limiteCamada3;

        switch (perfil) {
            case PerfilOnboardingService.PERFIL_INVESTIDOR:
                limiteCamada1 = 0;
                limiteCamada2 = 1;
                limiteCamada3 = 5;
                break;
            case PerfilOnboardingService.PERFIL_EM_DESENVOLVIMENTO:
                limiteCamada1 = 1;
                limiteCamada2 = 2;
                limiteCamada3 = 3;
                break;
            default: // PERFIL_INICIANTE
                limiteCamada1 = 3;
                limiteCamada2 = 2;
                limiteCamada3 = 1;
        }

        return Stream.of(
                camada1.stream().limit(limiteCamada1),
                camada2.stream().limit(limiteCamada2),
                camada3.stream().limit(limiteCamada3)
        ).flatMap(s -> s).collect(Collectors.toList());
    }

    public void marcarComoConcluido(String emailUsuario, Long moduloId) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado."));

        ProgressoUsuario progresso = progressoRepository.findByUsuarioAndModulo(usuario, modulo)
                .orElse(new ProgressoUsuario(usuario, modulo, false));

        progresso.setConcluido(true);
        progressoRepository.save(progresso);
    }

    public double calcularPercentualConcluido(String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        List<Modulo> trilha = listarTrilhaDoUsuario(emailUsuario);
        List<ProgressoUsuario> progressos = progressoRepository.findByUsuario(usuario);

        if (trilha.isEmpty()) {
            return 0.0;
        }

        long concluidos = progressos.stream()
                .filter(ProgressoUsuario::isConcluido)
                .count();

        return (concluidos * 100.0) / trilha.size();
    }
}
