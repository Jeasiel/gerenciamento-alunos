package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void limparBanco() throws Exception {
        for (Usuario u : usuarioRepository.findAll()) {
            usuarioRepository.deleteById(u.getId());
        }
    }

    private Usuario criarUsuario(String email, String user, String senha) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setUser(user);
        usuario.setSenha(senha);
        usuarioController.cadastrar(usuario);
        return usuario;
    }

    @Test
    public void deveCadastrarUsuario() throws Exception {
        Usuario usuario = criarUsuario("teste@hotmail.com", "joao", "424534");
        List<Usuario> usuarios = usuarioRepository.findAll();
        Assert.assertEquals(1, usuarios.size());
        Assert.assertEquals(usuario.getUser(), usuarios.get(0).getUser());
    }

    @Test
    public void deveFazerLoginValido() throws Exception {
        Usuario usuario = criarUsuario("maria@hotmail.com", "maria", "132414");
        MockHttpSession session = new MockHttpSession();
        BindingResult br = new BeanPropertyBindingResult(usuario, "usuario");
        usuario.setSenha("senha123");
        usuarioController.login(usuario, br, session);
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        Assert.assertNotNull(logado);
        Assert.assertEquals("maria", logado.getUser());
    }

    @Test
    public void deveFalharLoginComSenhaIncorreta() throws Exception {
        criarUsuario("carlos@hotmail.com", "carlos", "senha123");
        Usuario usuarioLogin = new Usuario();
        usuarioLogin.setUser("carlos");
        usuarioLogin.setSenha("senhaErrada");
        MockHttpSession session = new MockHttpSession();
        BindingResult br = new BeanPropertyBindingResult(usuarioLogin, "usuario");
        usuarioController.login(usuarioLogin, br, session);

        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
        Assert.assertNull(logado);
    }

    @Test
    public void deveFazerLogout() throws Exception {
        Usuario usuario = criarUsuario("ana@hotmail.com", "ana", "senha123");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogado", usuario);
        usuarioController.logout(session);
        Assert.assertNull(session.getAttribute("usuarioLogado"));
    }
}
