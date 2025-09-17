package br.com.gerenciamento.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuario(String email, String user, String senha) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setUser(user);
        usuario.setSenha(senha);
        return usuarioRepository.save(usuario);
    }

    @Test
    public void testSalvarERecuperarUsuario() {
        Usuario usuario = criarUsuario("teste@gmail.com", "joao", "123");

        Usuario encontrado = usuarioRepository.findById(usuario.getId()).orElse(null);

        assertEquals("teste@gmail.com", encontrado.getEmail());
        assertEquals("joao", encontrado.getUser());
        assertEquals("123", encontrado.getSenha());
        usuarioRepository.deleteAll();
    }

    @Test
    public void testFindByEmail() {
        criarUsuario("santos@gmail.com", "santos", "abc123");

        Usuario encontrado = usuarioRepository.findByEmail("santos@gmail.com");

        assertEquals("santos@gmail.com", encontrado.getEmail());
        assertEquals("santos", encontrado.getUser());
        usuarioRepository.deleteAll();
    }

    @Test
    public void testBuscarLoginValido() {
        criarUsuario("arsene@gmail.com", "arsene", "arsene1");

        Usuario encontrado = usuarioRepository.buscarLogin("arsene", "arsene1");

        assertEquals("arsene", encontrado.getUser());
        assertEquals("arsene1", encontrado.getSenha());
        usuarioRepository.deleteAll();
    }

    @Test
    public void testBuscarLoginInvalido() {
        criarUsuario("ana@gmail.com", "ana", "hamburguer");

        Usuario encontrado = usuarioRepository.buscarLogin("ana", "pastel");

        assertEquals(null, encontrado);
        usuarioRepository.deleteAll();
    }
}