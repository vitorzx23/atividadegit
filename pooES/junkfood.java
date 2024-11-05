// 1. Fiz tudo e passou em todos os testes.
// 2. Fiz sozinho.
// 3. Aprendi a questao como um todo.
// 4. Levei  mais ou menos 3 horas.import java.util.*;


import java.text.DecimalFormat;

class Slot {
    private String name;
    private float price;
    private int quantity;

    public Slot(String name, float price, int quantity) {
        this.setName(name);
        this.setPrice(price);
        this.setQuantity(quantity);
    }

    public String getName() {
        return this.name;
    }

    public float getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return String.format("%8s", this.name) + " : " +
                this.quantity + " U : " +
                Main.decForm.format(this.price) + " RS";
    }
}

class VendingMachine {
    private ArrayList<Slot> slots;
    private float profit;
    private float cash;
    private int capacity;

    public VendingMachine(int capacity) {
        this.slots = new ArrayList<>();

        for (int i = 0; i < capacity; i++) {
            this.slots.add(new Slot("empty", 0f, 0));
        }
    }

    private boolean indiceInvalido(int ind) {
        if (ind < 0 || ind >= this.slots.size()) {
            return false;
        }
        return true;
    }

    public Slot getSlot(int ind) {
        if (!this.indiceInvalido(ind)) {
            return null;
        }
        return this.slots.get(ind);
    }

    public void setSlot(int ind, Slot slot) {
        if (this.indiceInvalido(ind)) {
            this.slots.set(ind, slot);
        } else {
            System.out.println("fail: indice nao existe");
        }
    }

    public void clearSlot(int ind) {
        if (this.indiceInvalido(ind)) {
            this.slots.remove(ind);
            this.slots.add(new Slot("empty", 0f, 0));
        } else {
            System.out.println("fail: indice nao existe");
        }
    }

    public void insertCash(float cash) {
        this.cash += cash;
    }

    public float withdrawCash() {
        Main.println("voce recebeu " + Main.decForm.format(this.cash) + " RS");
        float withdrawnCash = this.cash;
        this.cash = 0f;
        return withdrawnCash;

    }

    public float getCash() {
        return this.cash;
    }

    public float getProfit() {
        return this.profit;
    }

    public void buyItem(int ind) {
        if (this.indiceInvalido(ind)) {
            if (!this.getSlot(ind).getName().equals("empty")) {
                if (this.cash >= this.getSlot(ind).getPrice()) {
                    if (this.getSlot(ind).getQuantity() > 0) {
                        this.cash -= this.getSlot(ind).getPrice();
                        this.slots.get(ind).setQuantity(this.slots.get(ind).getQuantity() - 1);
                        this.profit += this.slots.get(ind).getPrice();
                        System.out.println("voce comprou um " + this.slots.get(ind).getName());
                    } else {
                        System.out.println("fail: espiral sem produtos");
                    }
                } else {
                    System.out.println("fail: saldo insuficiente");
                }
            }
        } else {
            System.out.println("fail: indice nao existe");
        }
    }

    public String toString() {
        String s = "saldo: " + Main.decForm.format(this.cash) + "\n";
        for (int i = 0; i < this.slots.size(); i++) {
            Slot slot = this.getSlot(i);
            s += i + " [" + slot + "]\n";
        }
        return s;
    }
}

class Main {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine(0);

        while (true) {
            String linha = input();
            String[] palavras = linha.split(" ");
            println("$" + linha);

            if (palavras[0].equals("end")) {
                break;
            } else if (palavras[0].equals("init")) {
                machine = new VendingMachine(Integer.parseInt(palavras[1]));
            } else if (palavras[0].equals("dinheiro")) {
                machine.insertCash(Float.parseFloat(palavras[1]));
            } else if (palavras[0].equals("troco")) {
                machine.withdrawCash();
            } else if (palavras[0].equals("comprar")) {
                machine.buyItem(Integer.parseInt(palavras[1]));
            } else if (palavras[0].equals("show")) {
                print(machine);
            } else if (palavras[0].equals("set")) {
                machine.setSlot(Integer.parseInt(palavras[1]),
                        new Slot(palavras[2], Float.parseFloat(palavras[4]), Integer.parseInt(palavras[3])));
            } else if (palavras[0].equals("limpar")) {
                machine.clearSlot(Integer.parseInt(palavras[1]));
            } else if (palavras[0].equals("apurado")) {
                println("apurado total: " + decForm.format(machine.getProfit()));
            } else {
                println("comando inválido!");
            }
        }
    }

    public static Scanner scan = new Scanner(System.in);

    public static String input() {
        return scan.nextLine();
    }

    public static void println(Object str) {
        System.out.println(str);
    }

    public static void print(Object str) {
        System.out.print(str);
    }

    public static DecimalFormat decForm = new DecimalFormat("0.00");
}
