//passei muito tempo resolvendo um erro da primeira questão devido a nervosismo e etc, o q me deixou sem tempo pra fazer essa e só coloquei alguns metodos e atributos que faltavam. Peço perdão.

import java.text.DecimalFormat;
import java.util.*;

class Lead {
    private float thickness;
    private String hardness;
    private int size;

    public Lead(float thickness, String hardness, int size) {
        this.thickness = thickness;
        this.hardness = hardness;
        this.size = size;
    }

    // retorna o gasto em milímetros para uma página escrita, de acordo com sua
    // dureza
    public int usagePerSheet() {
        if (hardness.equals("HB"))
            return 1;
        else if (hardness.equals("2B"))
            return 2;
        else if (hardness.equals("4B"))
            return 4;
        else if (hardness.equals("6B"))
            return 6;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getHardness() {
        return this.hardness;
    }

    public int getSize() {
        return this.size;
    }

    public float getThickness() {
        return this.thickness;
    }

    public String toString() {
        DecimalFormat form = new DecimalFormat("0.0");
        return form.format(thickness) + ":" + hardness + ":" + size;
    }
}

class Pencil {

    private float thickness;
    private Lead tip;
    private LinkedList<Lead> barrel;

    public Pencil(float thickness) {
        this.thickness = thickness;
        this.barrel = new LinkedList<>();
    }

    // se o calibre for compatível, insere um lead no final do tambor
    public boolean insert(Lead lead) {
        if (this.thickness != lead.getThickness()) {
            System.out.println("fail: calibre incompatível");
            return false;
        }
        this.barrel.add(lead);
        return true;
    }

    // remove e retorna o lead da ponta
    public Lead remove() {
        if (this.tip == null) {
            System.out.println("fail: nao existe grafite no bico");
            return null;
        }
        Lead backup = this.tip;
        this.tip = null;
        return backup;

    }

    // se a ponta estiver vazia, puxa o primeiro lead do tambor para a ponta
    public boolean pull() {
        if (this.tip != null) {
            System.out.println("fail: ja existe grafite no bico");
            return false;
        }
        if (this.barrel.size() == 0) {
            System.out.println("fail: nao existe grafite no barril");
            return false;
        }
        this.tip = this.barrel.remove(0);
        return true;
    
    }


    public void writePage() {

        if(this.tip == null){
            System.out.println("fail: nao existe grafite no bico");
            return;
        }
        if(this.tip.getSize() == 10){
            System.out.println("fail: tamanho insuficiente");
            return;
        }
        int finalSize = this.tip.getSize() - this.tip.usagePerSheet();
        if(finalSize >= 10){
            this.tip.setSize(finalSize);
        }else{
            this.tip.setSize(10);
            System.out.println("fail: folha incompleta");
        }

}

    public String toString() {
        String saida = "calibre: " + thickness + ", bico: ";
        if(this.tip != null){
            saida += "[" + this.tip + "]";
        }else{
            saida += "[]";
        }
        saida += ", tambor: {";
        for(Lead x : barrel){
            saida += "[" + x + "]";
        }
        return saida + "}";

    }
}

public class App {
    public static void main(String[] arg) {
        Pencil lapiseira = new Pencil(0.5f);

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");

            if (args[0].equals("end")) {
                break;

            } else if (args[0].equals("show")) {
                println(lapiseira);

            } else if (args[0].equals("init")) {
                lapiseira = new Pencil((float) number(args[1]));

            } else if (args[0].equals("remove")) {
                lapiseira.remove();

            }else if (args[0].equals("write")) {
                lapiseira.writePage();

            } else if (args[0].equals("pull")) {
                lapiseira.pull();

            } else {
                println("fail: comando invalido");
            }
        }
    }

    private static Scanner scanner = new Scanner(System.in);

    private static String input() {
        return scanner.nextLine();
    }

    private static double number(String value) {
        return Double.parseDouble(value);
    }

    public static void println(Object value) {
        System.out.println(value);
    }
}
