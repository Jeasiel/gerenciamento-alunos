package br.com.gerenciamento.service;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    private Aluno criarAluno(String nome, String matricula, Curso curso, Status status, Turno turno) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(curso);
        aluno.setStatus(status);
        aluno.setTurno(turno);
        this.serviceAluno.save(aluno);
        return aluno;
    }

    @BeforeEach
    public void limpar(){
        for (Aluno aluno : this.serviceAluno.findAll()) {
            this.serviceAluno.deleteById(aluno.getId());
        }
    }

    @Test
    public void getById() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        this.serviceAluno.save(aluno);

        Aluno alunoRetorno = this.serviceAluno.getById(1L);
        Assert.assertTrue(alunoRetorno.getNome().equals("Vinicius"));
    }

    @Test
    public void salvarSemNome() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        Assert.assertThrows(ConstraintViolationException.class, () -> {
                this.serviceAluno.save(aluno);});
    }

    @Test
    public void recuperarAlunoInexistente(){
        Aluno aluno = criarAluno("José Alves", "300", Curso.BIOMEDICINA, Status.ATIVO, Turno.MATUTINO);

        Assert.assertNotNull(this.serviceAluno.getById(aluno.getId()));
        this.serviceAluno.deleteById(aluno.getId());
        Assert.assertThrows(NoSuchElementException.class ,() -> {this.serviceAluno.getById(aluno.getId());});
    }

    @Test
    public void deveBuscarAlunoPorNomeIgnorandoCase() {
        criarAluno("Ana Maria", "777", Curso.CONTABILIDADE, Status.ATIVO, Turno.NOTURNO);
        criarAluno("Bruna Seixas", "666", Curso.DIREITO, Status.INATIVO, Turno.MATUTINO);

        Assert.assertEquals(1, this.serviceAluno.findByNomeContainingIgnoreCase("ana").size());
        Assert.assertEquals("Ana Maria", this.serviceAluno.findByNomeContainingIgnoreCase("ana").get(0).getNome());
    }

}