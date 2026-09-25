package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.RespostaUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespostaUsuarioRepository extends JpaRepository<RespostaUsuario, Long> {

    List<RespostaUsuario> findByUsuario(Usuario usuario);
}
