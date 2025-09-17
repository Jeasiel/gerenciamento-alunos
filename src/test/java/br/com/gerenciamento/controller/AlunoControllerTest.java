package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.service.ServiceAluno;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoControllerTest {

    @Autowired
    private AlunoController alunoController;

    @Autowired
    private ServiceAluno serviceAluno;

    private Aluno criarAluno(String nome, String matricula, Curso curso, Status status, Turno turno) throws Exception{
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(curso);
        aluno.setStatus(status);
        aluno.setTurno(turno);
        BindingResult br = new BeanPropertyBindingResult(aluno, nome);
        alunoController.inserirAluno(aluno, br);
        return aluno;
    }

    @Before
    public void limparBanco() {
        for (Aluno aluno : serviceAluno.findAll()) {
            serviceAluno.deleteById(aluno.getId());
        }
    }

    @Test
    public void deveSalvarAlunoPeloController() throws Exception {
        Aluno aluno = criarAluno("João Silva", "321", Curso.CONTABILIDADE, Status.ATIVO, Turno.NOTURNO);

        List<Aluno> alunos = serviceAluno.findAll();
        Assert.assertEquals(1, alunos.size());
        Assert.assertEquals(aluno.getNome(), alunos.get(0).getNome());
    }

    @Test
    public void deveEditarAlunoPeloController() throws Exception {
        Aluno aluno = criarAluno("Maria Souza", "834", Curso.INFORMATICA, Status.ATIVO, Turno.MATUTINO);
        aluno.setNome("Maria Sousa");
        alunoController.editar(aluno);

        Aluno atualizado = serviceAluno.getById(aluno.getId());
        Assert.assertEquals("Maria Sousa", atualizado.getNome());
    }

    @Test
    public void deveRemoverAlunoPeloController() throws Exception {
        Aluno aluno = criarAluno("Carlos Pereira", "176", Curso.ADMINISTRACAO, Status.INATIVO, Turno.NOTURNO);
        alunoController.removerAluno(aluno.getId());

        Assert.assertTrue(serviceAluno.findAll().isEmpty());
    }

    @Test
    public void deveListarSomenteAlunosAtivos() throws Exception {
        criarAluno("Ana Francisca", "234", Curso.CONTABILIDADE, Status.ATIVO, Turno.NOTURNO);
        criarAluno("Bruno Mateus", "255", Curso.DIREITO, Status.INATIVO, Turno.MATUTINO);
        //teste do controller no criarAluno
        List<Aluno> ativos = serviceAluno.findByStatusAtivo();

        Assert.assertEquals(1, ativos.size());
        Assert.assertEquals("Ana Francisca", ativos.get(0).getNome());
    }
}
