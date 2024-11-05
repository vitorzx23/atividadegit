// 1. Fiz tudo e passou em todos os testes
// 2. Fiz sozinho.
// 3. Aprendi como funciona os construtores e metodos em java.
// 4. Levei pouco mais de uma hora.




import java.util.Scanner;

class Aluno {

    String nome;
    int matricula;
    String disciplina;
    float nota;

   
    void ler() {
        Scanner in = new Scanner(System.in);
        nome = in.nextLine();
        matricula = Integer.parseInt(in.nextLine());
        disciplina = in.nextLine();
        nota = Float.parseFloat(in.nextLine());
    }
    
    
    void mostrar() {
        System.out.println("Nome = " + nome);
        System.out.println("Matrícula = " + matricula);
        System.out.println("Disciplina = " + disciplina);
        System.out.println("Nota = " + nota);
    }
}

public class Impressao {
    public static void main(String[] args) {
        Aluno aluno = new Aluno(); 
        aluno.ler(); 
        aluno.mostrar(); 
    }
}
