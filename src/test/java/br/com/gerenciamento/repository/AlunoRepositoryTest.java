package br.com.gerenciamento.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno criarAluno(String nome, String matricula, Curso curso, Status status, Turno turno) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(curso);
        aluno.setStatus(status);
        aluno.setTurno(turno);
        return alunoRepository.save(aluno);
    }

    @Test
    public void testSalvarERecuperarAluno() {
        Aluno aluno = criarAluno("João Silva", "190", Curso.ADMINISTRACAO, Status.ATIVO, Turno.MATUTINO);

        Aluno encontrado = alunoRepository.findById(aluno.getId()).orElse(null);

        assertEquals("João Silva", encontrado.getNome());
        assertEquals(Status.ATIVO, encontrado.getStatus());
        alunoRepository.deleteAll();
    }

    @Test
    public void testFindByStatusAtivo() {
        criarAluno("João Carlos", "450", Curso.DIREITO, Status.ATIVO, Turno.NOTURNO);
        criarAluno("José Pedro", "788", Curso.BIOMEDICINA, Status.INATIVO, Turno.MATUTINO);

        List<Aluno> ativos = alunoRepository.findByStatusAtivo();

        assertEquals(1, ativos.size());
        assertEquals("João Carlos", ativos.get(0).getNome());
        assertEquals(Status.ATIVO, ativos.get(0).getStatus());
        alunoRepository.deleteAll();
    }

    @Test
    public void testFindByStatusInativo() {
        criarAluno("Marcelo Maia", "100", Curso.CONTABILIDADE, Status.INATIVO, Turno.MATUTINO);
        criarAluno("Carlos Alberto", "202", Curso.CONTABILIDADE, Status.ATIVO, Turno.NOTURNO);

        List<Aluno> inativos = alunoRepository.findByStatusInativo();

        assertEquals(1, inativos.size());
        assertEquals("Marcelo Maia", inativos.get(0).getNome());
        assertEquals(Status.INATIVO, inativos.get(0).getStatus());
        alunoRepository.deleteAll();
    }

    @Test
    public void testFindByNomeContainingIgnoreCase() {
        criarAluno("Bruna Clara", "120", Curso.ADMINISTRACAO, Status.ATIVO, Turno.NOTURNO);
        criarAluno("Bruno Ferreira", "498", Curso.DIREITO, Status.ATIVO, Turno.MATUTINO);

        List<Aluno> encontrados = alunoRepository.findByNomeContainingIgnoreCase("bruna");

        assertEquals(1, encontrados.size());
        assertEquals("Bruna Clara", encontrados.get(0).getNome());
        alunoRepository.deleteAll();
    }
}