// 1. Fiz tudo e passou em todos os testes
// 2. Fiz sozinho.
// 3. Aprendi essa outra maneira de utilizar o for (linha 21).
// 4. Levei pouco mais de uma hora(fiz na sala juntamente com o professor).



import java.util.Scanner;

class Aluno {
    String nome;
    float[] notas;

    Aluno(String nome, float[] notas) {
        this.nome = nome;
        this.notas = notas;
    }

    float calculaMedia() {
        float soma = 0;
        for (float nota : notas) {
            soma += nota;
        }
        return soma / notas.length;
    }
}

public class media {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nome = scanner.nextLine();

        float[] notas = new float[3];
        for (int i = 0; i < 3; i++) {
            notas[i] = Float.parseFloat(scanner.nextLine());
        }

        Aluno aluno = new Aluno(nome, notas);

        
        float media = aluno.calculaMedia();
        System.out.printf("%.2f%n", media);

     
    }
}
