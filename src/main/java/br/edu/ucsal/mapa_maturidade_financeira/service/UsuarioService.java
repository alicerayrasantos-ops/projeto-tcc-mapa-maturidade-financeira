package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.config.JwtUtil;
import br.edu.ucsal.mapa_maturidade_financeira.dto.CadastroRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginResponse;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Usuario cadastrar(CadastroRequest dados) {

        if (usuarioRepository.existsByEmail(dados.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        int idade = Period.between(dados.getDataNascimento(), LocalDate.now()).getYears();
        if (idade < 18) {
            throw new IllegalArgumentException("É necessário ter 18 anos ou mais para se cadastrar.");
        }

        String senhaCriptografada = passwordEncoder.encode(dados.getSenha());

        Usuario novoUsuario = new Usuario(
                dados.getNome(),
                dados.getEmail(),
                senhaCriptografada,
                dados.getDataNascimento()
        );

        return usuarioRepository.save(novoUsuario);
    }

    public LoginResponse login(LoginRequest dados) {

        Usuario usuario = usuarioRepository.findByEmail(dados.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));

        boolean senhaCorreta = passwordEncoder.matches(dados.getSenha(), usuario.getSenha());
        if (!senhaCorreta) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());

        return new LoginResponse(token, usuario.getNome(), usuario.getEmail());
    }
}
