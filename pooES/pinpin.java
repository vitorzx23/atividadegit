agiota-

import java.util.*;

enum Label {
    GIVE, 
    TAKE, 
    PLUS;
    
    @Override 
    
    public String toString (){
        return this.name().toLowerCase();
    }
}

class Operation {
    private static int nextOpId = 0;
    private int id;
    private String name;
    private Label label;
    private int value;

    public Operation( String name, Label label, int value ) {
        this.name = name;
        this.label = label;
        this.value = value;
        this.id = Operation.nextOpId++;
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public Label getLabel() {
        return this.label;
    }
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "id:" + this.id +" "+ this.label +":"+ this.name + " " + this.value; 
    }
}

class Client {
    private String name;
    private int limite;
    ArrayList<Operation> operations;

    public Client(String name, int limite) {
        this.name = name;
        this.limite = limite;
        this.operations = new ArrayList<Operation>();
    }

    public String getName() {
        return this.name;
    }
    
    public int getLimite() {
        return this.limite;
    }
    
    public ArrayList<Operation> getOperations() {
        return this.operations;
    }

    
    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

//nao sabia e vi o codigo do professor(mas entendi a lógica).
    public int getBalance() {
        int retorno = 0;
        for(Operation o : this.operations){
            if(o.getLabel() == Label.TAKE){
                retorno -= o.getValue();
            }else{
                retorno += o.getValue();
            }
        }
        return retorno;
    }

    @Override
    public String toString() {
        String  saida = this.name + " " + this.getBalance() + "/" + this.limite + "\n";
        for(Operation o : this.operations){
            saida += o + "\n";
        }
        return saida;
    }
}


class Agiota {
    private ArrayList<Client> aliveList;
    private ArrayList<Client> deathList;
    private ArrayList<Operation> aliveOper;
    private ArrayList<Operation> deathOper;

    public Agiota() {
        this.aliveList = new ArrayList<Client>();
        this.aliveOper = new ArrayList<Operation>();
        this.deathList = new ArrayList<Client>();
        this.deathOper = new ArrayList<Operation>();
    }

    private int searchClient(String name) {
        for(int i = 0 ; i < this.aliveList.size() ; i++){
            if(this.aliveList.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    
    private void pushOperation(Client client, String name, Label label, int value) {
        Operation o = new Operation(name, label, value);
        this.aliveOper.add(o);
        client.addOperation(o);
    }

    public Client getClient(String name) {
        if(this.searchClient(name) == -1){
            return null;
        }
        return this.aliveList.get(this.searchClient(name));
    }

    public void addClient(String name, int limite) {
        Client cliente = getClient(name);
        if(cliente != null){
            throw new RuntimeException("fail: cliente ja existe");
        }
        this.aliveList.add(new Client(name, limite));
        this.sortAliveList();
    }

    public void give(String name, int value) {
        Client cliente = getClient(name);
        if(cliente == null) throw new RuntimeException("fail: cliente nao existe");
        if(cliente.getBalance() + value > cliente.getLimite()) throw new RuntimeException("fail: limite excedido");
        this.pushOperation(cliente, name,Label.GIVE, value);
    }

    public void take(String name, int value) {
        Client cliente = getClient(name);
        if(cliente == null) throw new RuntimeException("fail: cliente nao existe");
        this.pushOperation(cliente, name, Label.TAKE, value);
    }

//nao sabia e vi o codigo do professor(mas entendi a lógica).
    public void kill(String name) {
        if(this.searchClient(name) == -1)return;
        this.deathList.add(this.aliveList.remove(this.searchClient(name)));
        for(int i = 0 ; i < this.aliveOper.size() ; i++){
            if(this.aliveOper.get(i).getName().equals(name)){
                this.deathOper.add(this.aliveOper.remove(i));
                i--;
            }
        }
    }

    public void plus() {
        for(Client cliente : this.aliveList){
            this.pushOperation(cliente, cliente.getName(), Label.PLUS,(int) Math.ceil( 0.1*cliente.getBalance() ) );
        }
        for (int i=0; i<this.aliveList.size(); i++) {
            Client client = this.aliveList.get(i);
            if ( client.getBalance() > client.getLimite() ) {
                this.kill( client.getName() );
                i--;
            }
        }
    }

    private void sortAliveList() {
        this.aliveList.sort( new Comparator<Client>() {
            public int compare(Client c1, Client c2) {
                return c1.getName().compareTo(c2.getName());            }
        });

    }
//nao sabia e vi o codigo do professor(mas entendi a lógica).
    @Override
    public String toString() {
         String ss = "";
        for ( Client client : this.aliveList ) {
            ss += ":) " + client.getName() + " " + client.getBalance() + "/" + client.getLimite() + "\n";
        }
        for ( Operation oper : this.aliveOper ) {
            ss += "+ " + oper + "\n";
        }
        for ( Client client : this.deathList ) {
            ss += ":( " + client.getName() + " " + client.getBalance() + "/" + client.getLimite() + "\n";
        }
        for ( Operation oper : this.deathOper ) {
            ss += "- " + oper + "\n";
        }
        return ss;
    }
}

public class Solver {
    public static void main(String[] arg) {
        Agiota agiota = new Agiota();

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");
        try{
            if      (args[0].equals("end"))     { break; }
            else if (args[0].equals("init"))    { agiota = new Agiota(); }
            else if (args[0].equals("show"))    { print(agiota); }
            else if (args[0].equals("showCli")) { print( agiota.getClient( args[1] ) ); }
            else if (args[0].equals("addCli"))  { agiota.addClient( args[1], (int) number(args[2]) ); }
            else if (args[0].equals("give"))    { agiota.give( args[1], (int) number(args[2]) ); }
            else if (args[0].equals("take"))    { agiota.take( args[1], (int) number(args[2]) ); }
            else if (args[0].equals("kill"))    { agiota.kill( args[1] ); }
            else if (args[0].equals("plus"))    { agiota.plus(); }
            else                                { println("fail: comando invalido"); }
            
        }catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
    }
    }

    private static Scanner scanner = new Scanner(System.in);
    private static String  input()                { return scanner.nextLine();        }
    private static double  number(String value)   { return Double.parseDouble(value); }
    public  static void    println(Object value)  { System.out.println(value);        }
    public  static void    print(Object value)    { System.out.print(value);          }
}


porquinho-

import java.util.*;
import java.util.stream.Collectors;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

enum Coin {

    C10(0.10, 1, "C10"),
    C25(0.25, 2, "C25"),
    C50(0.50, 3, "C50"),
    C100(1.00, 4, "C100");

    private double value;
    private int volume;
    private String label;

    private Coin(double value, int volume, String label) {
        this.value = value;
        this.volume = volume;
        this.label = label;
    }

    public double getValue() {
        return this.value;
    }

    public int getVolume() {
        return this.volume;
    }

    public String getLabel() {
        return this.label;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US));
        return df.format(this.value) + ":" + this.volume;
    }
}

class Item {

    private String label;
    private int volume;

    public Item(String label, int volume) {
        this.label = label;
        this.volume = volume;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return this.label + ":" + this.volume;
    }
}

class Pig {

    private boolean broken;
    private ArrayList<Coin> coins;
    private ArrayList<Item> items;
    private int volumeMax;

    public Pig(int volumeMax) {
        this.volumeMax = volumeMax;
        this.coins = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public Coin createCoin(String valor) {
        switch (valor) {
            case "10":
                return Coin.C10;
            case "25":
                return Coin.C25;
            case "50":
                return Coin.C50;
            case "100":
                return Coin.C100;
            default:
                return null;
        }
    }

    public boolean addCoin(Coin coin) {
        if ((!broken)) {
            if ((getVolume() + coin.getVolume()  <= this.volumeMax)) {
                this.coins.add(coin);
                return true;
            } else {
                throw new RuntimeException("fail: the pig is full");
            }
        } else {
            throw new RuntimeException("fail: the pig is broken");
        }
    }

    public boolean addItem(Item item) {
        if ((!broken)) {
            if ((getVolume() + item.getVolume() <= this.volumeMax)) {
                this.items.add(item);
                return true;
            } else {
                throw new RuntimeException("fail: the pig is full");
            }
        } else {
            throw new RuntimeException("fail: the pig is broken");
        }
    }

    public boolean breakPig() {
        if (!broken)
            return broken = true;
        return false;
    }

    public ArrayList<String> extractCoins() {
        if (!broken) {
            throw new RuntimeException("fail: you must break the pig first" +"\n"+ "[]");
        }
        ArrayList<String> coins = new ArrayList<>();
        for (Coin x : this.coins) {
            coins.add(x.toString());
        }
        this.coins.clear();
        return coins;
    }

    public ArrayList<String> extractItems() {

        if (!broken) {
            throw new RuntimeException("fail: you must break the pig first" +"\n"+ "[]");
        }

        ArrayList<String> items = new ArrayList<>();
        for (Item x : this.items) {
            items.add(x.toString());
        }
        this.items.clear();
        return items;
    }

    public int getVolume() {
        if (broken)
            return 0;
        else {
            int x = 0;
            for (int i = 0; i < this.coins.size(); i++) {
                x += this.coins.get(i).getVolume();
            }
            for (int i = 0; i < this.items.size(); i++) {
                x += this.items.get(i).getVolume();
            }
            return x;
        }
    }

    public double getValue() {
        double x = 0;
        for (int i = 0; i < this.coins.size(); i++) {
            x += this.coins.get(i).getValue();
        }
        return x;
    }

    public int getVolumeMax() {
        return this.volumeMax;
    }

    public int getVolumeRestante() {
        int x = 0;
        x = this.getVolume() + this.volumeMax;
        return x;
    }

    private String toStringArrayCoin(ArrayList<Coin> list) {
        String saida = "";

        for (Coin i : list) {
            saida += i;

            if (!list.get(list.size() - 1).equals(i)) {
                saida += ", ";
            }
        }

        return saida;
    }

    private String toStringArrayItem(ArrayList<Item> list) {
        String saida = "";

        for (Item i : list) {
            saida += i;

            if (!list.get(list.size() - 1).equals(i)) {
                saida += ", ";
            }
        }

        return saida;
    }

    @Override
    public String toString() {
        String state = "intact";

        if (broken) {
            state = "broken";
        }

        return "state=" + state + " : " + "coins=[" + toStringArrayCoin(coins) + "] : items=["
                + toStringArrayItem(items) +
                "] : value=" + new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.US)).format(getValue()) +
                " : volume=" + getVolume() + "/" + volumeMax;
    }
}

public class Solver {
    public static void main(String[] arg) {
        Pig pig = new Pig(5);

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");
            try {
                if (args[0].equals("end")) {
                    break;
                } else if (args[0].equals("init")) {
                    pig = new Pig((int) number(args[1]));
                } else if (args[0].equals("show")) {
                    println(pig);
                } else if (args[0].equals("addCoin")) {
                    pig.addCoin(pig.createCoin(args[1]));
                } else if (args[0].equals("addItem")) {
                    pig.addItem(new Item(args[1], (int) number(args[2])));
                } else if (args[0].equals("break")) {
                    pig.breakPig();
                } else if (args[0].equals("extractCoins")) {
                    println("[" + String.join(", ", pig.extractCoins()) + "]");
                } else if (args[0].equals("extractItems")) {
                    println("[" + String.join(", ", pig.extractItems()) + "]");
                } else {
                    println("fail: comando invalido");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
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

    public static void print(Object value) {
        System.out.print(value);
    }
}

shapes-

import java.util.*;
import java.text.DecimalFormat;

class Point2D {
    public double x;
    public double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return "(" + df.format(this.x) + ", " + df.format(this.y) + ")";
    }
}

abstract class Shape {
    private String name;

    public Shape(String name) {
        this.name = name;
    }

    public boolean inside(Point2D p) {
        return false;
    };

    public abstract double getArea();

    public double getPerimeter() {
        return 0.0;
    };

    public final String getInfo() {
        DecimalFormat df = new DecimalFormat("0.00");
        return this.name + ": A=" + df.format(this.getArea()) + " P=" + df.format(this.getPerimeter());

    }

    @Override
    public String toString() {
        return this.name + ": ";
    }
}

class Circle extends Shape {

    public Point2D center;
    public double radius;

    public Circle(Point2D center, double radius) {
        super("Circ");  
        this.center = center;
        this.radius = radius;
    }
    
    @Override
    public boolean inside(Point2D p) {
        return false;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * this.radius;
    }

    @Override
    public double getArea() {
        return Math.PI * this.radius * this.radius;
    }

    @Override
    public String toString() {
        DecimalFormat d = new DecimalFormat("0.00");
        return super.toString() + "C=" + this.center + ", R=" + d.format(this.radius);

    }
}

class Rectangle extends Shape {

    public Point2D p1;
    public Point2D p2;

    public Rectangle(Point2D p1, Point2D p2) {
        super("Rect");

        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public double getArea() {
        double b = Math.abs(this.p1.x - this.p2.x);
        double h = Math.abs(this.p1.y - this.p2.y);
        return b * h;
    }

    @Override
    public double getPerimeter() {
        double b = Math.abs(this.p1.x - this.p2.x);
        double h = Math.abs(this.p1.y - this.p2.y);
        return 2 * (b + h);
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return super.toString() + "P1=" + this.p1 + " P2=" + this.p2;
    }
}

public class Solver {
    public static void main(String[] arg) {

        ArrayList<Shape> shapes = new ArrayList<Shape>();

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");

            if (args[0].equals("end")) {
                break;
            } else if (args[0].equals("circle")) {
                shapes.add(new Circle(new Point2D(number(args[1]), number(args[2])), number(args[3])));
            } else if (args[0].equals("rect")) {
                shapes.add(new Rectangle(new Point2D(number(args[1]), number(args[2])),
                        new Point2D(number(args[3]), number(args[4]))));
            } else if (args[0].equals("show")) {
                showAll(shapes);
            } else if (args[0].equals("info")) {
                infoAll(shapes);
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

    public static void print(Object value) {
        System.out.print(value);
    }

    public static void showAll(ArrayList<Shape> shapes) {
        for (Shape s : shapes)
            println(s);
    }

    public static void infoAll(ArrayList<Shape> shapes) {
        for (Shape s : shapes)
            println(s.getInfo());
    }

}

cadastro-

cria uma pasta.

import java.util.*;
public class CheckingAccount extends Account {
    protected double monthlyFee;

    public CheckingAccount(String clientId) {
        super(clientId, "CC");
        this.monthlyFee = 20.0;
    }

    @Override
    public void updateMonthly() {
        this.balance -= this.monthlyFee;
    }
}

cria outra

import java.util.*;
public class SavingsAccount extends Account {
    protected double monthlyInteres;

    public SavingsAccount(String clientId) {
        super(clientId, "CP");
        this.monthlyInteres = 0.01;
    }

    @Override
    public void updateMonthly() {
        this.balance += this.balance * this.monthlyInteres;
    }
}

import java.util.*;
import java.text.DecimalFormat;

abstract class Account {
    protected double balance;
    private static int nextAccountId = 0;
    protected int accId;
    protected String clientId;
    protected String typeId;

    public Account(String clientId, String typeId) {
        this.clientId = clientId;
        this.typeId = typeId;
        this.balance = 0.00;
        this.accId = nextAccountId++;
    }

    public void deposit(double value) {
        this.balance += value;
    }

    public void withdraw(double value) {
        if (value <= this.balance)
            this.balance -= value;
        else{
        throw new RuntimeException("fail: saldo insuficiente");
        }
    }

    public void transfer(Account other, double value) {
        this.withdraw(value);
        other.deposit(value);
    }

    public double getBalance() {
        return this.balance;
    }

    public int getAccId() {
        return this.accId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public abstract void updateMonthly();

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return this.accId + ":" + this.clientId + ":" + df.format(this.balance) + ":" + this.typeId + "\n";
    }
}


class Client {
    private String clientId;
    private ArrayList<Account> accounts;

    public Client(String clientId) {
        this.clientId = clientId;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account acc) {
        this.accounts.add(acc);
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return this.clientId + " [" + this.accounts.get(0).getAccId() + ", " + this.accounts.get(1).getAccId() + "]\n";
    }
}

class Agency {
    private Map<Integer, Account> accounts;
    private Map<String, Client> clients;

    public Agency() {
        this.accounts = new HashMap<Integer, Account>();
        this.clients = new LinkedHashMap<String, Client>();
    }

    
    public void addClient(String clientId) {
        Client cliente = new Client(clientId);
        this.clients.put(clientId, cliente);  

        Account cc = new CheckingAccount(clientId);
        this.accounts.put(cc.getAccId(), cc);
        cliente.addAccount(cc);

        Account cp = new SavingsAccount(clientId);
        this.accounts.put(cp.getAccId(), cp);
        cliente.addAccount(cp);
    }

    
    public void deposit(int accId, double value) {
        this.getAccount(accId).deposit(value);
    }

   
    public void withdraw(int accId, double value) {
        this.getAccount(accId).withdraw(value);
    }

    
    public void transfer(int fromAccId, int toAccId, double value) {
        this.getAccount(fromAccId).transfer(this.getAccount(toAccId), value);
    }

    
    public void updateMonthly() {
       for(Account conta : this.accounts.values()){
            conta.updateMonthly();
       }
    }

    private Account getAccount(int accountId) {
        if(!this.accounts.containsKey(accountId))throw new RuntimeException("fail: conta nao encontrada");

        return this.accounts.get(accountId);
    }

    @Override
    public String toString() {
        String s = "- Clients\n";
        for (Client client : this.clients.values()) {
            s += client;
        }
        s += "- Accounts\n";
        for (Account acc : this.accounts.values()) {
            s += acc;
        }
        return s;
    }
}

public class Solver {
    public static void main(String[] arg) {
        Agency agency = new Agency();

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");

            try {
                if (args[0].equals("end")) {
                    break;
                } else if (args[0].equals("show")) {
                    print(agency);
                } else if (args[0].equals("addCli")) {
                    agency.addClient(args[1]);
                } else if (args[0].equals("deposito")) {
                    agency.deposit((int) number(args[1]), number(args[2]));
                } else if (args[0].equals("saque")) {
                    agency.withdraw((int) number(args[1]), number(args[2]));
                } else if (args[0].equals("transf")) {
                    agency.transfer((int) number(args[1]), (int) number(args[2]), number(args[3]));
                } else if (args[0].equals("update")) {
                    agency.updateMonthly();
                } else {
                    println("fail: comando invalido");
                }
            } catch (RuntimeException e) {
                println(e.getMessage());
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

    public static void print(Object value) {
        System.out.print(value);
    }
}

cofre-

import java.text.DecimalFormat;
import java.util.*;

class Valuable {
    private String label;
    private double value;
    private int volume;

    public Valuable(String label, double value, int volume) {
        this.label = label;
        this.value = value;
        this.volume = volume;
    }

    public String getLabel() {
        return this.label;
    }

    public double getValue() {
        return this.value;
    }

    public int getVolume() {
        return this.volume;
    }

    @Override
    public String toString() {
        DecimalFormat d = new DecimalFormat("0.00");
        return this.label + ":" + d.format(this.value) + ":" + this.volume;
    }
}

class Item extends Valuable {

    public Item(String label, double value, int volume) {
        super(label, value, volume);
    }
}

class Coin extends Valuable {
    public static Coin C10 = new Coin(0.10, 1, "M10");
    public static Coin C25 = new Coin(0.25, 2, "M25");
    public static Coin C50 = new Coin(0.50, 3, "M50");
    public static Coin C100 = new Coin(1.00, 4, "M100");

    private Coin(double value, int volume, String label) {
        super(label, value, volume);
    }
}

class Pig {

    private boolean broken;
    private List<Valuable> valuables;
    private int volumeMax;

    public Pig(int volumeMax) {
        this.broken = false;
        this.valuables = new ArrayList<Valuable>();
        this.volumeMax = volumeMax;
    }

    public Coin createCoin(String valor) {
        switch (valor) {
            case "10":
                return Coin.C10;
            case "25":
                return Coin.C25;
            case "50":
                return Coin.C50;
            case "100":
                return Coin.C100;
            default:
                return null;
        }
    }

    public void addValuable(Valuable valuable) {
        if (this.broken) {
            throw new RuntimeException("fail: the pig is broken");
        }

        if (valuable.getVolume() > this.getVolumeRestante()) {
            throw new RuntimeException("fail: the pig is full");
        }

        this.valuables.add(valuable);
    }

    public void breakPig() {
        if (this.broken) {
            throw new RuntimeException("fail: the pig is broken");
        }

        this.broken = true;
    }

    public ArrayList<String> extractCoins() {
        if (!this.broken) {
            throw new RuntimeException("fail: you must break the pig first");
        }

        ArrayList<String> labels = new ArrayList<String>();

        for (Valuable v : this.valuables) {
            if (v instanceof Coin) {
                labels.add(v.toString());
            }
        }

        for (int i = 0; i < this.valuables.size(); i++) {
            Valuable v = this.valuables.get(i);
            if (v instanceof Coin) {
                this.valuables.remove(i);
                i--;
            }
        }

        return labels;
    }

    public ArrayList<String> extractItems() {
        if (!this.broken) {
            throw new RuntimeException("fail: you must break the pig first");
        }

        ArrayList<String> labels = new ArrayList<String>();

        for (Valuable v : this.valuables) {
            if (v instanceof Item) {
                labels.add(v.toString());
            }
        }

        for (int i = 0; i < this.valuables.size(); i++) {
            Valuable v = this.valuables.get(i);
            if (v instanceof Item) {
                this.valuables.remove(i);
                i--;
            }
        }

        return labels;
    }

    @Override
    public String toString() {
        DecimalFormat d = new DecimalFormat("0.00");
        String s = "";
        s += this.valuables + " : ";
        s += d.format(this.getValue()) + "$ : ";
        s += this.getVolume() + "/" + this.getVolumeMax() + " : ";
        s += ((this.broken) ? "broken" : "intact");
        return s;
    }

    public int getVolume() {
        if (this.isBroken()) {
            return 0;
        }

        int volume = 0;

        for (Valuable v : this.valuables) {
            volume += v.getVolume();
        }
        return volume;
    }

    public double getValue() {
        double value = 0;
        for (Valuable v : this.valuables) {
            value += v.getValue();
        }
        return value;
    }

    public int getVolumeMax() {
        return this.volumeMax;
    }

    public int getVolumeRestante() {
        return this.getVolumeMax() - this.getVolume();
    }

    public boolean isBroken() {
        return this.broken;
    }
}

public class Solver {
    public static void main(String[] arg) {
        Pig pig = new Pig(5);

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");

            try {
                if      (args[0].equals("end"))          { break; }
                else if (args[0].equals("init"))         { pig = new Pig( (int) number(args[1]) ); }
                else if (args[0].equals("show"))         { println(pig); }
                else if (args[0].equals("addCoin"))      { pig.addValuable( pig.createCoin( args[1] ) ); }
                else if (args[0].equals("addItem"))      { pig.addValuable( new Item( args[1], number(args[2]), (int) number(args[3]) ) ); }
                else if (args[0].equals("break"))        { pig.breakPig(); }
                else if (args[0].equals("extractCoins")) { println("[" + String.join(", ", pig.extractCoins()) + "]"); }
                else if (args[0].equals("extractItems")) { println("[" + String.join(", ", pig.extractItems()) + "]"); }
                else                                     { println("fail: comando invalido"); }
            } catch (RuntimeException e) {
                println(e.getMessage());
            }
        }
    }

    private static Scanner scanner = new Scanner(System.in);
    private static String  input()                { return scanner.nextLine();        }
    private static double  number(String value)   { return Double.parseDouble(value); }
    public  static void    println(Object value)  { System.out.println(value);        }
    public  static void    print(Object value)    { System.out.print(value);          }
}


salario-

pasta

import java.util.*;

abstract class Funcionario{
    protected String nome;
    protected int bonus;
    protected int diarias;
    protected int maxDiarias;
    
    public Funcionario(String nome){
        this.nome = nome;
    }
    
    public void setBonus(int bonus){
     this.bonus = bonus;   
    }

    public String getNome(){
        return this.nome;
    }
    
    public void addDiaria(){
        if(diarias < maxDiarias){
            diarias++;
            return;
        }
        //eu continuo usando o runtimeexception pq nao precisa colocar o throw new exception no metodo mas compreendo a maneira correta q o senhor ensinou em aula.
        throw new RuntimeException("fail: limite de diarias atingido");
    }
    
    
    public abstract int getSalario();
    public abstract String toString();
    
} 

pasta

import java.util.*;

class Professor extends Funcionario{
    protected String classe;
    
    
    public Professor(String nome,String classe){
        super(nome);
        this.classe = classe;
        this.maxDiarias = 2;
    }
    
    @Override
    public int getSalario(){
        
        int adicional = 100 * diarias + bonus;
        
        if(this.classe.equals("A")){
            return 3000 + adicional;
        }else if(this.classe.equals("B")){
            return 5000 + adicional;
        }else if(this.classe.equals("C")){
            return 7000 + adicional;
        }else if(this.classe.equals("D")){
            return 9000 + adicional;
        }else if(this.classe.equals("E")){
            return 11000 + adicional;
        }
        return -1;
    }

    public String getClasse(){
        return this.classe;
    }
    
    @Override 
    public String toString(){
        return "prof:" + nome + ":" + classe + ":" +  getSalario();
    }
    
    
}

pasta

import java.util.*;

class STA extends Funcionario{
    protected int nivel;
    
    public STA(String nome, int nivel){
        super(nome);
        this.nivel = nivel;
        maxDiarias = 1;
    }
    
    @Override
    public int getSalario(){
        return 3000 + 300 * nivel + bonus + diarias * 100; 
    }
    
    public int getNivel(){
        return this.nivel;
    }
    
    public String toString(){
        return "sta:" + nome + ":" + nivel + ":" + getSalario();
    }
    
}

pasta

import java.util.*;

class Terceirizado extends Funcionario{
    protected int horas;
    protected boolean salubre = false;
    
    public Terceirizado(String nome, int horas, String salubre){
        super(nome);
        this.horas = horas;
        
        if(salubre.equals("sim"))this.salubre = true;
        else{this.salubre = false;}
    }
    
    @Override
    public int getSalario(){
        int salario = 4 * horas;
        
        if(salubre) salario += 500;
        
        return salario + bonus + diarias * 100;
    }
    
    public void addDiaria(){
        throw new RuntimeException ("fail: terc nao pode receber diaria");
    }
    
    public int getHoras(){
        return horas;
    }
    
    public String getSalubre(){
        if(this.salubre) return "sim";
        else {return "nao";}
    }
    
    public String toString(){
        return "ter:" + nome + ":" + horas + ":" + getSalubre() + ":" + getSalario();
    }
}

pasta

import java.util.*;


class UFC {
    private Map<String, Funcionario> funcionarios = new TreeMap<>();
    
    public Funcionario getFuncionario(String nome){
        return funcionarios.get(nome);
    }
    
    public void addFuncionario(Funcionario funcionario){
        funcionarios.put(funcionario.getNome(), funcionario);
    }
    
    public void rmFuncionario(String nome){
        funcionarios.remove(nome);
    }
    
    public void setBonus(int bonus){
        //bonusp = bonus pessoal.
        int bonusp = bonus / funcionarios.size();
        
        for(Funcionario funcionario : funcionarios.values()){
            funcionario.setBonus(bonusp);
        }
    }
    
    @Override
    public String toString(){
        StringBuilder saida = new StringBuilder();
        int i = 0;
        for(Funcionario funcionario : funcionarios.values()){
            saida.append(funcionario.toString()).append("\n");
        }
        //o trim retira o \n do ultimo funcionario.
        return saida.toString().trim();
    }
}

pasta 

import java.util.*;



public class Solver{
    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        UFC ufc = new UFC();
        
        while (true){
            try {
                String line = scanner.nextLine();
                System.out.println("$" + line);
                String ui[] = line.split(" ");
                
                if(ui[0].equals("end")){break;}
                
                else if(ui[0].equals("addProf")){
                    ufc.addFuncionario(new Professor(ui[1], ui[2]));
                }
                else if(ui[0].equals("addSta")){
                    ufc.addFuncionario(new STA(ui[1], Integer.parseInt (ui[2])));
                }
                else if(ui[0].equals("addTer")){
                    ufc.addFuncionario(new Terceirizado(ui[1],Integer.parseInt (ui[2]), ui[3]));
                }
                else if(ui[0].equals("rm")){
                    ufc.rmFuncionario(ui[1]);
                }
                else if(ui[0].equals("showAll")){
                    System.out.println(ufc);
                }
                else if(ui[0].equals("show")){
                    System.out.println(ufc.getFuncionario(ui[1]));
                }
                else if(ui[0].equals("addDiaria")){
                    ufc.getFuncionario(ui[1]).addDiaria();
                }
                else if(ui[0].equals("setBonus")){
                    ufc.setBonus(Integer.parseInt(ui[1]));
                }else{System.out.println("fail: comando invalido");}
                
            } catch(RuntimeException e) {
                System.out.println(e.getMessage());
            }
            
        }
    }
}

autenticaveis-

import java.util.*;

abstract class Funcionario {
    protected String nome;
    protected int bonus;
    protected int diarias;
    protected int maxDiarias;

    public Funcionario(String nome) {
        this.nome = nome;
        this.bonus = 0;
        this.diarias = 0;
        this.maxDiarias = 0;
    }

    public String getNome() {
        return this.nome;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
        // this.bonus += bonus;
    }

    // se atingiu o máximo, lance uma MsgException
    // se não atingiu o máximo, adicione mais uma diária
    public void addDiaria() {
        if (this.diarias >= this.maxDiarias) {
            throw new RuntimeException("fail: limite de diarias atingido");
        }

        this.diarias++;
    }

    // retorna bonus + diarias * 100
    public int getSalario() {
        return this.bonus + this.diarias * 100;
    }
}

interface Autenticavel {
    public void logar(String senha);

    public void deslogar();

    public String getNome();

    public void setSenha(String senha);

    public String getSenha();

    public void setLogado(boolean logado);

    public boolean getLogado();
}

class FuncionarioAutenticavel extends Funcionario implements Autenticavel {
    private String senha;
    private boolean logado;

    public FuncionarioAutenticavel(String nome) {
        super(nome);

        this.senha = "";
        this.logado = false;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    public boolean getLogado() {
        return this.logado;
    }

    public void logar(String senha) {
        if (!this.senha.equals(senha)) {
            throw new RuntimeException("fail: senha invalida");
        }

        this.setLogado(true);
    }

    public void deslogar() {
        this.setLogado(false);
    }
}

class Professor extends FuncionarioAutenticavel {
    protected String classe;

    // inicializa classe e muda maxDiarias para 2
    public Professor(String nome, String classe) {
        super(nome);
        this.classe = classe;
        this.maxDiarias = 2;
    }

    public String getClasse() {
        return this.classe;
    }

    public int getValorClasse() {
        switch (this.getClasse()) {
            case "A":
                return 3000;
            case "B":
                return 5000;
            case "C":
                return 7000;
            case "D":
                return 9000;
            case "E":
                return 11000;
            default:
                return 0;
        }
    }

    @Override
    public int getSalario() {
        return super.getSalario() + getValorClasse();
    }

    @Override
    public String toString() {
        return "prof:" + this.getNome() + ":" + this.getClasse() + ":" + this.getSalario();
    }
}

class STA extends FuncionarioAutenticavel {
    protected int nivel;

    // inicializa nivel e muda maxDiarias para 1
    public STA(String nome, int nivel) {
        super(nome);
        this.nivel = nivel;
        this.maxDiarias = 1;
    }

    public int getNivel() {
        return this.nivel;
    }

    // lógica do salário do sta
    // usa o super.getSalario() para pegar bonus e diarias
    @Override
    public int getSalario() {
        return super.getSalario() + 3000 + 300 * this.getNivel();
    }

    @Override
    public String toString() {
        return "sta:" + this.getNome() + ":" + this.getNivel() + ":" + this.getSalario();
    }
}

class Terceirizado extends Funcionario {
    protected int horas;
    protected boolean isSalubre;

    public Terceirizado(String nome, int horas, String isSalubre) {
        super(nome);
        this.horas = horas;
        this.isSalubre = isSalubre.equals("sim") ? true : false;
    }

    public int getHoras() {
        return this.horas;
    }

    public String getIsSalubre() {
        return this.isSalubre ? "sim" : "nao";
    }

    // lance uma MsgException com um texto diferente
    @Override
    public void addDiaria() {
        throw new RuntimeException("fail: terc nao pode receber diaria");
    }

    // lógica do salário do terceirizado
    // usa o super.getSalario() para pegar bonus e diarias
    @Override
    public int getSalario() {
        return super.getSalario() + 4 * this.getHoras() + (this.isSalubre ? 500 : 0);
    }

    @Override
    public String toString() {
        return "ter:" + this.getNome() + ":" + this.getHoras() + ":" + this.getIsSalubre() + ":" + this.getSalario();
    }
}

class Aluno implements Autenticavel {
    private String nome;
    private String curso;
    private int bolsa;

    private String senha;
    private boolean logado;

    public Aluno(String nome, String curso, int bolsa) {
        this.nome = nome;
        this.curso = curso;
        this.bolsa = bolsa;
        this.senha = "";
        this.logado = false;
    }

    public String getNome() {
        return this.nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return this.senha;
    }

    public int getBolsa() {
        return this.bolsa;
    }

    public String getCurso() {
        return this.curso;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    public boolean getLogado() {
        return this.logado;
    }

    public void logar(String senha) {
        if (!this.senha.equals(senha)) {
            throw new RuntimeException("fail: senha invalida");
        }
        this.setLogado(true);
    }

    public void deslogar() {
        this.setLogado(false);
    }

    @Override
    public String toString() {
        return "alu:" + this.getNome() + ":" + this.getCurso() + ":" + this.getBolsa();
    }
}

class UFC {
    private Map<String, Funcionario> funcionarios;
    private Map<String, Aluno> alunos;

    public UFC() {
        this.funcionarios = new TreeMap<String, Funcionario>();
        this.alunos = new TreeMap<String, Aluno>();
    }

    @Override
    public String toString() {
        String s = "";
        for (Funcionario f : this.funcionarios.values()) {
            s += f + "\n";
        }
        for (Aluno a : this.alunos.values()) {
            s += a + "\n";
        }
        return s;
    }

    public Funcionario getFuncionario(String nome) {
        return this.funcionarios.get(nome);
    }

    public void addFuncionario(Funcionario funcionario) {
        this.funcionarios.put(funcionario.getNome(), funcionario);
    }

    public void rmFuncionario(String nome) {
        this.funcionarios.remove(nome);
    }

    // reparte o bonus para todos os funcionarios
    public void setBonus(int bonus) {
        if (this.funcionarios.size() == 0) {
            throw new RuntimeException("fail: sem funcionarios");
        }

        int eachBonus = bonus / this.funcionarios.size();
        for (Funcionario f : this.funcionarios.values()) {
            f.setBonus(eachBonus);
        }
    }

    public Aluno getAluno(String nome) {
        return this.alunos.get(nome);
    }

    public void addAluno(Aluno aluno) {
        this.alunos.put(aluno.getNome(), aluno);
    }

    public void rmAluno(String nome) {
        this.alunos.remove(nome);
    }
}

class Sistema {
    private UFC ufc;
    private Map<String, Autenticavel> usuarios;

    public Sistema() {
        this.usuarios = new TreeMap<String, Autenticavel>();
        this.ufc = new UFC();
    }

    public UFC getUFC() {
        return this.ufc;
    }

    public Autenticavel getUsuario(String nome) {
        return this.usuarios.get(nome);
    }

    public void addUsuario(String nome, String senha) {
        Autenticavel user = this.getUsuario(nome);

    if (user != null)
        throw new RuntimeException("fail: usuario " + nome + " ja cadastrado");

    Funcionario funcionario = this.getUFC().getFuncionario(nome);

    if (funcionario == null)
        throw new RuntimeException("fail: " + nome + " nao encontrado");

    if (!(funcionario instanceof Autenticavel))
        throw new RuntimeException("fail: " + nome + " nao pode ser cadastrado no sistema");

    user = (Autenticavel) funcionario;
    user.setSenha(senha);
    this.usuarios.put(nome, user);
    }

    public void rmUsuario(String nome) {
        this.usuarios.remove(nome);
    }

    public void logar(String nome, String senha) {
        Autenticavel user = this.getUsuario(nome);
        if (user == null)
            throw new RuntimeException("fail: usuario nao encontrado");

        user.logar(senha);
    }

    public void deslogar(String nome) {
        Autenticavel user = this.getUsuario(nome);
        if (user == null)
            throw new RuntimeException("fail: usuario nao encontrado");

        user.deslogar();
    }

    public void deslogarTodos() {
        for (Autenticavel user : this.usuarios.values()) {
            user.deslogar();
        }
    }

    public void showAllUsers() {
        for (Autenticavel user : this.usuarios.values()) {
            if (user instanceof Funcionario) {
                Funcionario funcionario = (Funcionario) user;
                if (funcionario instanceof Professor) {
                    Professor professor = (Professor) funcionario;
                    System.out.println("prof:" + professor.getNome() + ":" + professor.getClasse() + ":" +
                            professor.getSalario() + ":" + professor.getSenha() + ":" +
                            (user.getLogado() ? "online" : "offline"));
                } else if (funcionario instanceof STA) {
                    STA sta = (STA) funcionario;
                    System.out.println("sta:" + sta.getNome() + ":" + sta.getNivel() + ":" +
                            sta.getSalario() + ":" + sta.getSenha() + ":" +
                            (user.getLogado() ? "online" : "offline"));
                } else {
                    System.out.println(funcionario.getClass().getSimpleName().toLowerCase() + ":" +
                            funcionario.getNome() + ":" + funcionario.getSalario() + ":" +
                            ((FuncionarioAutenticavel) funcionario).getSenha() + ":" +
                            (user.getLogado() ? "online" : "offline"));
                }
            } else if (user instanceof Aluno) {
                Aluno aluno = (Aluno) user;
                System.out.println("alu:" + aluno.getNome() + ":" + aluno.getCurso() + ":" +
                        aluno.getBolsa() + ":" + aluno.getSenha() + ":" +
                        (user.getLogado() ? "online" : "offline"));
            }
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (Autenticavel user : this.usuarios.values()) {
            s += user + ":" + user.getSenha() + ":" + user.getLogado() + "\n";
        }
        return s;
    }
}

public class Solver {
    public static void main(String[] arg) {
        Sistema sis = new Sistema();

        while (true) {

            try {
                String line = input();
                println("$" + line);
                String[] args = line.split(" ");

                if (args[0].equals("end")) {
                    break;
                } else if (args[0].equals("addProf")) {
                    sis.getUFC().addFuncionario(new Professor(args[1], args[2]));
                } else if (args[0].equals("addAlu")) {
                    sis.getUFC().addAluno(new Aluno(args[1], args[2], (int) number(args[3])));
                } else if (args[0].equals("rmAlu")) {
                    sis.getUFC().rmAluno(args[1]);
                } else if (args[0].equals("showAlu")) {
                    println(sis.getUFC().getAluno(args[1]));
                } else if (args[0].equals("addSta")) {
                    sis.getUFC().addFuncionario(new STA(args[1], (int) number(args[2])));
                } else if (args[0].equals("addTer")) {
                    sis.getUFC().addFuncionario(new Terceirizado(args[1], (int) number(args[2]), args[3]));
                } else if (args[0].equals("addUser")) {
                    sis.addUsuario(args[1], args[2]);
                } else if (args[0].equals("rm")) {
                    sis.getUFC().rmFuncionario(args[1]);
                } else if (args[0].equals("rmUser")) {
                    sis.rmUsuario(args[1]);
                } else if (args[0].equals("showAll")) {
                    print(sis.getUFC());
                } else if (args[0].equals("showAllUsers")) {
                    sis.showAllUsers();
                } else if (args[0].equals("show")) {
                    println(sis.getUFC().getFuncionario(args[1]));
                } else if (args[0].equals("addDiaria")) {
                    sis.getUFC().getFuncionario(args[1]).addDiaria();
                } else if (args[0].equals("setBonus")) {
                    sis.getUFC().setBonus((int) number(args[1]));
                } else {
                    println("fail: comando invalido");
                }
            } catch (RuntimeException me) {
                println(me.getMessage());

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

    public static void print(Object value) {
        System.out.print(value);
    }
}

trem-

import java.util.*;

enum Direcao {
    IN,
    OUT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

class Passageiro {
    String id;

    public Passageiro(String id) {
        this.id = id;
    }

    public String getNome() {
        return this.id;
    }

    public void setNome(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id;
    }

}

class Vagao {
    private LinkedList<Passageiro> cadeiras;
    private int capacidade;

    public Vagao(int capacidade) {
        this.cadeiras = new LinkedList<Passageiro>();
        this.capacidade = capacidade;

        for (int i = 0; i < this.capacidade; i++) {
            cadeiras.add(null);
        }

    }

    public void embarcar(Passageiro pass) {
        int tamanho = cadeiras.size();

        for (int i = 0; i < tamanho; i++) {
            if (cadeiras.get(i) == null) {
                cadeiras.set(i, pass);
                break;
            }
        }

    }

    public void desembarcar(String id) {
        int tamanho = cadeiras.size();
        for (int i = 0; i < tamanho; i++) {
            if (cadeiras.get(i) != null) {
                if (cadeiras.get(i).getNome().equals(id)) {
                    cadeiras.set(i, null);
                    break;
                }
            }
        }
    }

    public Passageiro getPassageiro(String idpass) {

        for (Passageiro passageiro : cadeiras) {
            if (passageiro != null) {
                if (passageiro.getNome().equals(idpass)) {
                    return passageiro;
                }
            }
        }
        return null;
    }

    public boolean exists(String idPass) {
        for (Passageiro pass : cadeiras) {
            if (pass != null) {
                if (pass.getNome().equals(idPass)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFull() {
        for (Passageiro passageiro : cadeiras) {
            if (passageiro == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String saida = "[ ";

        for (Passageiro pass : cadeiras) {
            saida += ((pass != null) ? pass : "-") + " ";
        }

        return saida + "]";
    }

}

class Trem {
    private int maxVagoes;
    private ArrayList<Vagao> vagoes;
    private static Registro registro = new Registro();

    public Trem(int maxVagoes) {
        this.maxVagoes = maxVagoes;
        this.vagoes = new ArrayList<>();
    }

    public void addVagao(Vagao vagao) {
        if (vagoes.size() == this.maxVagoes) {
            System.out.println("fail: limite de vagões atingido");
            return;
        }
        vagoes.add(vagao);
    }

    public void embarcar(Passageiro pass) {
        registro.cadastrar(pass);
        boolean verificador = true;

        for (Vagao vagao : vagoes) {
            if (vagao.exists(pass.getNome())) {
                System.out.println("fail: " + pass.getNome() + " já está no trem");
                return;
            }
        }

        for (Vagao vagao : vagoes) {
            if (!vagao.isFull()) {
                Movimento mov = new Movimento(pass, Direcao.IN);
                registro.movimentar(mov);
                vagao.embarcar(pass);
                verificador = false;
                break;
            }
        }

        if (verificador == true) {
            System.out.println("fail: trem lotado");
            return;
        }

    }

    public void desembarcar(String nome) {
        Passageiro pass = getPassageiro(nome);

        boolean verificador = false;
        int i = 0;

        for (Vagao vagao : vagoes) {
            i++;
            if (vagao.exists(nome)) {
                verificador = true;
            }

            if (i == vagoes.size() && verificador == false) {
                System.out.println("fail: " + nome + " nao esta no trem");
                return;
            }

            vagao.desembarcar(nome);
        }

        Movimento mov = new Movimento(pass, Direcao.OUT);
        registro.movimentar(mov);
    }

    public Passageiro getPassageiro(String idpass) {
        for (Vagao vagao : vagoes) {
            if (vagao.getPassageiro(idpass) != null) {
                return vagao.getPassageiro(idpass);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String saida = "Trem ";

        for (Vagao vagao : vagoes) {
            saida += vagao;
        }
        return saida;
    }

}

class Movimento {
    private Passageiro pass;
    private Direcao dir;

    Movimento(Passageiro pass, Direcao dir) {

        this.pass = pass;
        this.dir = dir;

    }

    public String toString() {

        return this.pass + " " + this.dir;

    }

    public String getNome() {

        return this.pass.getNome();

    }
}

class Registro {
    private static ArrayList<Passageiro> cadastro;
    private static ArrayList<Movimento> movimentacao;

    public Registro() {
        this.cadastro = new ArrayList<Passageiro>();
        this.movimentacao = new ArrayList<Movimento>();
    }

    public void cadastrar(Passageiro pass) {
        for (Passageiro passageiro : cadastro) {
            if (passageiro.getNome().equals(pass.getNome())) {
                return;
            }
        }
        cadastro.add(pass);
    }

    public void movimentar(Movimento mov) {
        movimentacao.add(mov);
    }

    public String showCadastro() {
        Collections.sort(cadastro, Comparator.comparing(Passageiro::getNome));
    String saida = "";
    for (int i = 0; i < cadastro.size(); i++) {
        saida += cadastro.get(i);
        if (i < cadastro.size() - 1) {
            saida += "\n";
        }
    }
    return saida;
    }

    public String showMovimentacao() {
        String saida = "";
    for (int i = 0; i < movimentacao.size(); i++) {
        saida += movimentacao.get(i);
        if (i < movimentacao.size() - 1) {
            saida += "\n";
        }
    }
    return saida;
    }

}

public class Solver {
    public static void main(String[] arg) {
        Scanner scanner = new Scanner(System.in);
        Trem trem = new Trem(1);
        Registro registro = new Registro();

        while (true) {
            try {
                String line = scanner.nextLine();
                System.out.println("$" + line);
                String[] ui = line.split(" ");

                if (ui[0].equals("end")) {
                    break;
                } else if (ui[0].equals("init")) {
                    trem = new Trem(Integer.parseInt(ui[1]));

                } else if (ui[0].equals("nwvag")) {
                    trem.addVagao(new Vagao(Integer.parseInt(ui[1])));

                } else if (ui[0].equals("la")) {
                    System.out.println(trem);

                } else if (ui[0].equals("entrar")) {
                    trem.embarcar(new Passageiro(ui[1]));

                } else if (ui[0].equals("sair")) {
                    trem.desembarcar(ui[1]);

                } else if (ui[0].equals("show")) {
                    System.out.println(trem);

                } else if (ui[0].equals("movimentacao")) {
                    System.out.println(registro.showMovimentacao());

                } else if (ui[0].equals("cadastro")) {
                    System.out.println(registro.showCadastro());

                } else {
                    System.out.println("fail: comando invalido");
                }

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        }
    }
}



carga-




import java.util.*;

enum Direcao {
    IN,
    OUT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

class Pass {
    protected String id;

    public Pass(String id) {
        setNome(id);
    }

    public String getNome() {
        return this.id;
    }

    public void setNome(String id) {
        this.id = id;
    }

}

class Passageiro extends Pass {

    public Passageiro(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return this.id;
    }

}

class Vagao {
    private LinkedList<Passageiro> cadeiras;
    private int capacidade;

    public Vagao(int capacidade) {
        this.cadeiras = new LinkedList<Passageiro>();
        this.capacidade = capacidade;

        for (int i = 0; i < this.capacidade; i++) {
            cadeiras.add(null);
        }

    }

    public void embarcar(Passageiro pass) {
        int tamanho = cadeiras.size();

        for (int i = 0; i < tamanho; i++) {
            if (cadeiras.get(i) == null) {
                cadeiras.set(i, pass);
                break;
            }
        }

    }

    public void desembarcar(String id) {
        int tamanho = cadeiras.size();
        for (int i = 0; i < tamanho; i++) {
            if (cadeiras.get(i) != null) {
                if (cadeiras.get(i).getNome().equals(id)) {
                    cadeiras.set(i, null);
                    break;
                }
            }
        }
    }

    public Passageiro getPassageiro(String idpass) {

        for (Passageiro passageiro : cadeiras) {
            if (passageiro != null) {
                if (passageiro.getNome().equals(idpass)) {
                    return passageiro;
                }
            }
        }
        return null;
    }

    public boolean exists(String idPass) {
        for (Passageiro pass : cadeiras) {
            if (pass != null) {
                if (pass.getNome().equals(idPass)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFull() {
        for (Passageiro passageiro : cadeiras) {
            if (passageiro == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String saida = "[ ";

        for (Passageiro pass : cadeiras) {
            saida += ((pass != null) ? pass : "-") + " ";
        }

        return saida + "]";
    }

}

class Carga extends Pass {
    private float peso;

    public Carga(String id, float peso) {
        super(id);
        setPeso(peso);
    }

    public float getPeso() {
        return this.peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return getNome() + ":" + getPeso();
    }
}

class VagaoC {
    private float capacidade;
    private LinkedList<Carga> items;

    public VagaoC(float capacidade) {
        this.capacidade = capacidade;
        this.items = new LinkedList<>();
       
    }

    public float capacidadeRestante() {
        float peso = 0;

        for (Carga carga : items) {
            peso += carga.getPeso();
        }
        return this.capacidade - peso;
    }

    public void addItem(Carga carga) {
       float peso = capacidadeRestante() - carga.getPeso();

        if (peso <= 0) {
            throw new RuntimeException("fail: trem lotado");
        }
        
       this.items.add(carga);
    }

    public void removeItem(int i) {
        items.remove(i);
    }

    public LinkedList<Carga> getItems() {
        return this.items;
    }

    @Override
    public String toString() {
        String saida = "( ";
        
        for (Carga carga : items) {
            saida += carga + " ";
        }

        return saida + "_" + capacidadeRestante() + " )";
    }

}

class Trem {
    private int maxVagoes;
    private LinkedList<Vagao> vagoes;
    private LinkedList<VagaoC> vagoesDeCarga;
    private LinkedList<Object> ordem;
    private static Registro registro = new Registro();

    public Trem(int maxVagoes) {
        this.maxVagoes = maxVagoes;
        this.vagoes = new LinkedList<>();
        this.vagoesDeCarga = new LinkedList<>();
        this.ordem = new LinkedList<>();
    }

    public void addVagao(Vagao vagao) {
        if (vagoes.size() + vagoesDeCarga.size() >= this.maxVagoes) {
            throw new RuntimeException("fail: limite de vagões atingido");
        }
        vagoes.add(vagao);
        ordem.add(vagao);
    }

    public void addVagaoC(VagaoC vagaoC) {
        if (vagoes.size() + vagoesDeCarga.size() >= this.maxVagoes) {
            throw new RuntimeException("fail: limite de vagões atingido");
        }
        vagoesDeCarga.add(vagaoC);
        ordem.add(vagaoC);

    }

    public void addItems(Carga carga) {
        for (VagaoC vagaoc : vagoesDeCarga) {
            vagaoc.addItem(carga);
            break; 
        }
    }

    public void embarcar(Passageiro pass) {
        registro.cadastrar(pass);
        boolean verificador = true;

        for (Vagao vagao : vagoes) {
            if (vagao.exists(pass.getNome())) {
                System.out.println("fail: " + pass.getNome() + " já está no trem");
                return;
            }
        }

        for (Vagao vagao : vagoes) {
            if (!vagao.isFull()) {
                Movimento mov = new Movimento(pass, Direcao.IN);
                registro.movimentar(mov);
                vagao.embarcar(pass);
                verificador = false;
                break;
            }
        }

        if (verificador == true) {
            System.out.println("fail: trem lotado");
            return;
        }

    }

    public void desembarcar(String nome) {
        Passageiro pass = getPassageiro(nome);

        boolean verificador = false;
        int i = 0;

        for (VagaoC object : vagoesDeCarga) {
            for (Carga x : object.getItems()) {
                if(x.getNome().equals(nome)){
                    object.removeItem(object.getItems().indexOf(x));
                    return;
                }
            }
        }


        for (Vagao vagao : vagoes) {
            i++;
            if (vagao.exists(nome)) {
                verificador = true;
            }

            if (i == vagoes.size() && verificador == false) {
                System.out.println("fail: " + nome + " nao esta no trem");
                return;
            }

            vagao.desembarcar(nome);
        }

        Movimento mov = new Movimento(pass, Direcao.OUT);
        registro.movimentar(mov);
    }

    public Passageiro getPassageiro(String idpass) {
        for (Vagao vagao : vagoes) {
            if (vagao.getPassageiro(idpass) != null) {
                return vagao.getPassageiro(idpass);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder saida = new StringBuilder("Trem ");

        for (Object objeto : ordem) {
            saida.append(objeto);
        }
        
        return saida.toString();
    }

}

class Movimento {
    private Passageiro pass;
    private Direcao dir;

    Movimento(Passageiro pass, Direcao dir) {

        this.pass = pass;
        this.dir = dir;

    }

    public String toString() {

        return this.pass + " " + this.dir;

    }

    public String getNome() {

        return this.pass.getNome();

    }
}

class Registro {
    private static ArrayList<Passageiro> cadastro;
    private static ArrayList<Movimento> movimentacao;

    public Registro() {
        this.cadastro = new ArrayList<Passageiro>();
        this.movimentacao = new ArrayList<Movimento>();
    }

    public void cadastrar(Passageiro pass) {
        for (Passageiro passageiro : cadastro) {
            if (passageiro.getNome().equals(pass.getNome())) {
                return;
            }
        }
        cadastro.add(pass);
    }

    public void movimentar(Movimento mov) {
        movimentacao.add(mov);
    }

    public String showCadastro() {
        Collections.sort(cadastro, Comparator.comparing(Passageiro::getNome));
        String saida = "";
        for (int i = 0; i < cadastro.size(); i++) {
            saida += cadastro.get(i);
            if (i < cadastro.size() - 1) {
                saida += "\n";
            }
        }
        return saida;
    }

    public String showMovimentacao() {
        String saida = "";
        for (int i = 0; i < movimentacao.size(); i++) {
            saida += movimentacao.get(i);
            if (i < movimentacao.size() - 1) {
                saida += "\n";
            }
        }
        return saida;
    }
}

public class Solver {
    public static void main(String[] arg) {
        Scanner scanner = new Scanner(System.in);
        Trem trem = new Trem(1);
        Registro registro = new Registro();

        while (true) {
            try {
                String line = scanner.nextLine();
                System.out.println("$" + line);
                String[] ui = line.split(" ");

                if (ui[0].equals("end")) {
                    break;
                } else if (ui[0].equals("init")) {
                    trem = new Trem(Integer.parseInt(ui[1]));

                } else if (ui[0].equals("nwvc")) {
                    trem.addVagaoC(new VagaoC(Float.parseFloat(ui[1])));

                } else if (ui[0].equals("nwvp")) {
                    trem.addVagao(new Vagao(Integer.parseInt(ui[1])));

                } else if (ui[0].equals("la")) {
                    System.out.println(trem);

                } else if (ui[0].equals("addp")) {
                    trem.embarcar(new Passageiro(ui[1]));

                } else if (ui[0].equals("addc")) {
                    trem.addItems(new Carga(ui[1], Float.parseFloat(ui[2])));

                } else if (ui[0].equals("sair")) {
                    trem.desembarcar(ui[1]);

                } else if (ui[0].equals("show")) {
                    System.out.println(trem);

                } else if (ui[0].equals("movimentacao")) {
                    System.out.println(registro.showMovimentacao());

                } else if (ui[0].equals("cadastro")) {
                    System.out.println(registro.showCadastro());

                } else {
                    System.out.println("fail: comando invalido");
                }

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
