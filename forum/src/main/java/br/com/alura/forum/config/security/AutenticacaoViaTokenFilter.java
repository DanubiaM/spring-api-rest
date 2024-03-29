package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
* OncePerRequestFilter é como se fosse uma gateway, a requisição  irá passar primeiro nela para ir para o controller.
*
* */
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {


    //Não é possível realizar @Autorized nesta classe, portanto devera gerar um construtor
    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;


    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);
        boolean valido = tokenService.isTokenValido(token);

        if(valido){
            autenticarCliente(token);
        }

        //Filter Chain é o reponsável por chamar o próximo filtro.
        filterChain.doFilter(request, response);



    }

    private void autenticarCliente(String token) {
        Long idUsuario = tokenService.getIdUsuario(token);

        Usuario  usuario = usuarioRepository.findById(idUsuario).get();

        UsernamePasswordAuthenticationToken  authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    //Function para recuperar e verificar se o token é valido, caso valido é retornado.
    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7, token.length());
    }
}
