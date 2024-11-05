// 1. Fiz tudo e passou em todos os testes.
// 2. Fiz sozinho.
// 3. Aprendi a questao como um todo.
// 4. Levei mais ou menos 3 ou 4 horas (não seguidas).




import java.util.*;

class Client{
    private String fone;
    private String id;
    
    public Client (String id, String fone){
        this.id = id;
        this.fone = fone;
    }
    
    public String getFone (){
        return this.fone;
    }
    public void setFone (String fone){
        this.fone = fone;
    }
    
    public String getId (){
        return this.id;
    }
    public void setId (String id){
        this.id = id;
    }
    
    public String toString (){
      return id + ":" + fone;  
    }
    
}

class Sala{
    private ArrayList<Client> cadeiras;
    
    public Sala (int capacidade){
        this.cadeiras = new ArrayList<>();
        
        for(int i = 0 ; i < capacidade ; i++){
            this.cadeiras.add(null);
        }
      
    }
    
    private int procurar (String nome){
       for(int i = 0 ; i < this.cadeiras.size(); i++){
           if(this.cadeiras.get(i) != null){
               if(this.cadeiras.get(i).getId().equals(nome)){
                   return i;
               }
           }
       
        }
        
       return -1;
    }
    
    private boolean verificarIndice (int indice){
        if(indice >= 0 && indice <= this.cadeiras.size()){
            return true;
        }
        return false;
    }
    
    public boolean reservar (String id, String fone, int ind){
        if(verificarIndice(ind)){
            if(this.cadeiras.get(ind) != null){
                System.out.println("fail: cadeira ja esta ocupada");
                return false;
            }
            
          if(procurar(id) != -1){
             System.out.println("fail: cliente ja esta no cinema"); 
             return false;
          }
          Client cliente = new Client(id, fone);
          this.cadeiras.set(ind, cliente);
          return true;
        }
        System.out.println("fail: cadeira nao existe");
        return false;
    }
    
    public void cancelar (String id){
        if(procurar(id) != -1){
            this.cadeiras.set(procurar(id), null);
        }else {
        System.out.println("fail: cliente nao esta no cinema");
        }
    }
    
    public ArrayList<Client> getCadeiras (){
        return this.cadeiras;
    }
    
    public String toString(){
        String saida = "[";
    for (Client cliente : cadeiras) {
        if (cliente == null)
            saida += "-";
        else
            saida += cliente;
        saida += " "; 
    }
    saida = saida.trim(); 
    return saida + "]";
    }
    
}

class Main {
    public static void main(String args[]) {
        Sala sala = new Sala(0);

        while (true) {
            String line = in.nextLine();
            String[] argsL = line.split(" ");
            System.out.println('$' + line);

            switch (argsL[0]) {

                case "end":
                    return;

                case "init":
                    sala = new Sala(getInt(argsL[1]));
                    break;

                case "show":
                    System.out.println(sala);
                    break;

                case "reservar":
                    sala.reservar(argsL[1], argsL[2], getInt(argsL[3]));
                    break;

                case "cancelar":
                    sala.cancelar(argsL[1]);
                    break;

                default:
                    System.out.println("fail: comando invalido");
                    break;
            }
        }
    }

    static Scanner in = new Scanner(System.in);

    static int getInt(String pos) {
        return Integer.parseInt(pos);
    }
}
