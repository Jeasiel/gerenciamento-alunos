package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    private Usuario criarUsuario(String email, String user, String senha) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setUser(user);
        usuario.setSenha(senha);
        this.serviceUsuario.salvarUsuario(usuario);
        return usuario;
    }

    @Test
    public void deveSalvarUsuarioComSenhaCriptografada() throws Exception {
        Usuario usuario = criarUsuario("teste@hotmail.com", "joao", "123456");
        
        Assert.assertNotNull(this.serviceUsuario.loginUser(usuario.getUser(), usuario.getSenha()));
        Assert.assertNotEquals("123456", this.serviceUsuario.loginUser(usuario.getUser(), usuario.getSenha()).getSenha());
    }

    @Test
    public void naoDevePermitirEmailDuplicado() throws Exception {
        criarUsuario("maria@hotmail.com", "maria", "stopped");
        Usuario duplicado = new Usuario();
        duplicado.setEmail("maria@hotmail.com");
        duplicado.setUser("mariano");
        duplicado.setSenha("cantstop");

        Assert.assertThrows(EmailExistsException.class, () -> {
            this.serviceUsuario.salvarUsuario(duplicado);
        });
    }

    @Test
    public void deveFazerLoginValido() throws Exception {
        Usuario usuario = criarUsuario("carlos@hotmail.com", "carlos", "senha123");
        Usuario login = this.serviceUsuario.loginUser("carlos", usuario.getSenha());

        Assert.assertNotNull(login);
        Assert.assertEquals("carlos", login.getUser());
    }

    @Test
    public void deveRetornarNullQuandoLoginInvalido() throws Exception {
        criarUsuario("ana@hotmail.com", "ana", "senhaValida");
        Usuario login = this.serviceUsuario.loginUser("ana", "senhaErrada");

        Assert.assertNull(login);
    }
}
