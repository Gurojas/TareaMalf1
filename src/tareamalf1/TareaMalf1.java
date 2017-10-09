/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareamalf1;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class TareaMalf1 {
    
    
    public TareaMalf1(){
        Digraph g = new Digraph();
        // Caso (a.b)*
        /*
        Nodo nodo0 = new Nodo(0);
        nodo0.addTransicion(0, 'a', 0);
        nodo0.addTransicion(0, 'b', 0);
        nodo0.addTransicion(0, '_', 1);
        nodo0.addTransicion(0, '_', 5);
        
        Nodo nodo1 = new Nodo(1);
        nodo1.addTransicion(1, 'a', 2);
        
        Nodo nodo2 = new Nodo(2);
        nodo2.addTransicion(2, '_', 3);
        
        Nodo nodo3 = new Nodo(3);
        nodo3.addTransicion(3,'b',4);
        
        Nodo nodo4 = new Nodo(4);
        nodo4.addTransicion(4, '_', 1);
        nodo4.addTransicion(4, '_', 5);
        
        Nodo nodo5 = new Nodo(5);
        
        g.addNodo(nodo0);
        g.addNodo(nodo1);
        g.addNodo(nodo2);
        g.addNodo(nodo3);
        g.addNodo(nodo4);
        g.addNodo(nodo5);
        */
        
        // Caso a.b
        Nodo nodo0 = new Nodo(0);
        nodo0.addTransicion(0, 'a', 0);
        nodo0.addTransicion(0, 'b', 0);
        nodo0.addTransicion(0, 'a', 1);
        
        Nodo nodo1 = new Nodo(1);
        nodo1.addTransicion(1, '_', 2);
        
        Nodo nodo2 = new Nodo(2);
        nodo2.addTransicion(2, 'b', 3);
        
        Nodo nodo3 = new Nodo(3);
        
        g.addNodo(nodo0);
        g.addNodo(nodo1);
        g.addNodo(nodo2);
        g.addNodo(nodo3);
        
     
        
        AFNDToAFD(g);

        
        
        
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        TareaMalf1 e = new TareaMalf1();

        
        
        //e.thompsonConcatenacion("a.(b.c)");
        //e.thompsonUnion("a|b");
        
       
    }
    
    private String codigoEstado(ArrayList<Integer> nodos){
        String cod = "";
        int n = nodos.size();
        for (int i = 0; i < n; i++) {
            int num = nodos.get(i);
            cod = cod+String.valueOf(num);
        }
        return cod;
    }
    
    public void AFNDToAFD(Digraph afnd){
        char alfabeto[] = {'a','b'}; //este alfabeto es de prueba....pueden venir mas elementos
              
        // estados a los que puedo llegar desde el 0 con el caracter vacio
        ArrayList<Integer> estadoInicial = clausuraVacio(afnd, 0);
        
        ArrayList<String> codEstados = new ArrayList<>();
        String cod = codigoEstado(estadoInicial);
        codEstados.add(cod);
        
        Digraph afd = new Digraph();
        
        ArrayList<ArrayList<Integer>> conjuntoEstados = new ArrayList<>();
        conjuntoEstados.add(estadoInicial);
        
        int contNodos = 0;
        Nodo estado = new Nodo(contNodos);
        contNodos++;
        afd.addNodo(estado);
        
        // Lista que guardara el conjunto de estados nuevos
        ArrayList<Integer> nuevoEstado = new ArrayList<>();
        
        // recorro los nodos del afd
        for (int i = 0; i < conjuntoEstados.size(); i++) {
            estadoInicial = conjuntoEstados.get(i);
            // recorro el alfabeto
            for (int j = 0; j < alfabeto.length; j++) {
                nuevoEstado = new ArrayList<>();
                char c = alfabeto[j];
                int n = estadoInicial.size();
                ArrayList<Integer> estadosClausura = new ArrayList<>();
                for (int k = 0; k < n; k++) {
                    int s = estadoInicial.get(k);
                    estadosClausura = clausura(afnd, s, c);
                    // Compruebo que los elementos que agrege al nuevo estado no se encunetren repetidos
                    for (int l = 0; l < estadosClausura.size(); l++) {
                        int num = estadosClausura.get(l);
                        if (!nuevoEstado.contains(num)){
                            nuevoEstado.add(num);
                        }
                    }
                }
                
                // veo la clausura de vacio con los elementos del nuevo estado
                for (int k = 0; k < nuevoEstado.size(); k++) {
                    int num = nuevoEstado.get(k);
                    estadosClausura = clausuraVacio(afnd, num);
                    for (int l = 0; l < estadosClausura.size(); l++) {
                        int e = estadosClausura.get(l);
                        if (!nuevoEstado.contains(e)){
                            nuevoEstado.add(e);
                        }
                    }
                }
                
                cod = codigoEstado(nuevoEstado);
                // Compruebo que el nuevo estado que quiero agregar al afd no este repetido
                          
                if (!codEstados.contains(cod)){
                    codEstados.add(cod);
                    Nodo nuevoNodo = new Nodo(contNodos);
                    contNodos++;
                    int from = i;
                    int to = i+1;
                    estado = afd.adj.get(i);
                    estado.addTransicion(from, c, to);   
                    afd.addNodo(nuevoNodo);
                    conjuntoEstados.add(nuevoEstado);
                }
                else{
                    int from = i;
                    int to = codEstados.indexOf(cod);
                    estado.addTransicion(from, c, to);          
                }
                     
            }
        }

        mostrarGrafo(afd);
        
        
    }
    
    private ArrayList<Integer> clausura(Digraph g, int s, char c){
        ArrayList<Integer> estados = new ArrayList<>();
        Nodo nodo = g.adj.get(s);
        int numTransiciones = nodo.numTransiciones();
        for (int i = 0; i < numTransiciones; i++) {
            Transicion t = nodo.getTransicion(i);
            char trance = t.getTrance();
            if (trance == c){
                int to = t.getTo();
                estados.add(to);
            }
        }
        return estados;
    }
    private ArrayList<Integer> clausuraVacio(Digraph g, int s){
        char vacio = '_';
        ArrayList<Integer> nodos = new ArrayList<>();
        nodos.add(s);
        for (int i = 0; i < nodos.size(); i++) {
            s = nodos.get(i);
            Nodo nodo = g.adj.get(s);
            int n = nodo.numTransiciones();
            for (int j = 0; j < n; j++) {
                Transicion t = nodo.getTransicion(j);
                char trans = t.getTrance();
                if (vacio == trans){
                    // me aseguro que no hayan elementos repetidos
                    int to = t.getTo();
                    if (!nodos.contains(to)){
                        nodos.add(to);
                    }                    
                }
            }
        }
        return nodos;     
    }
    
    public void ErToAFND(){
        Digraph g = new Digraph();
        String expresion = "a.b.c";
        String cadenas[] = separarCadenas(expresion);
        
        String expr = cadenas[2];
        
        if (expr.equals(".")){
            thompsonConcatenacion(g, 0, expresion);
        }
        else if (expr.equals("|")){
            
        }
        mostrarGrafo(g);
        
        
    }
    
    public void mostrarGrafo(Digraph g){
        int n = g.adj.size();
        for (int i = 0; i < n; i++) {
            Nodo nodo = g.adj.get(i);
            nodo.info();
        }
    }
    
    public String[] separarCadenas(String expresion){
        String cadenas[] = new String[3];
        int n = expresion.length();
        char caracteres[] = expresion.toCharArray();
        int index = -1;
        String expr = "";
        for (int i = 0; i < n; i++) {
            if (caracteres[i] == '|' || caracteres[i] == '.'){
                index = i;
                expr = String.valueOf(caracteres[i]);
                break;
            }
        }
        String exp1 = expresion.substring(0,index);
        String exp2 = expresion.substring(index+1);
        
        cadenas[0] = exp1;
        cadenas[1] = exp2;
        cadenas[2] = expr;
        
        return cadenas;

    }
    
    public void thompsonConcatenacion(Digraph g, int v, String expresion){
        ArrayList<String> subsER = new ArrayList<>();
        String cadenas[] = separarCadenas(expresion);
        subsER.add(0,cadenas[0]);
        subsER.add(1,cadenas[1]);
        int s = v;
        while(!subsER.isEmpty()){
            String th = subsER.get(0);
            subsER.remove(0);
            int n = th.length();
            if (n == 1){
                char c = th.charAt(0);
                s = thompsonBase(g, s, c);
            }
            else{
                cadenas = separarCadenas(th);
                String op = cadenas[2];
                if (op.equals(".")){
                    thompsonConcatenacion(g, s, th);
                }
                else if (op.equals("|")){
                
                }
            }
        }
    }
    
    public void thopsonUnion(Digraph g, int v, String expresion){
        
    }
    
    public int thompsonBase(Digraph g, int v, char c){
        Nodo from = new Nodo(v);
        int w = v + 1;
        Nodo to = new Nodo(w);
        from.addTransicion(v, c, w);
        g.addNodo(from);
        g.addNodo(to);
        
        return w;
    }
    

        
    public void thompsonConcatenacion(String expresion){
        Digraph g = new Digraph();
        ArrayList<String> subsER = new ArrayList<>();
        String cadenas[] = separarCadenas(expresion);
        subsER.add(0,cadenas[0]);
        subsER.add(1,cadenas[1]);
        
        int i = 0; // Contador de los nodos del automata
        
        while (!subsER.isEmpty()){
            String reg = subsER.get(0);
            subsER.remove(0);
            int n = reg.length();
            // Thomspon(una letra)
            if (n == 1){               
                int v = i;
                i = i + 1;
                char c = reg.charAt(0);
                
                int w = i;
                i = i + 1;
                Nodo nodo = new Nodo(v);
                nodo.addTransicion(v, c, w);
                g.addNodo(nodo);
                
                // si la lista no es vacio agrego transicion con vacio
                if (!subsER.isEmpty()){
                    nodo = new Nodo(w);
                    nodo.addTransicion(w, '_', w+1);
                    g.addNodo(nodo);
                }
            }
            else{
                cadenas = separarCadenas(reg);
                subsER.add(0,cadenas[0]);
                subsER.add(1,cadenas[1]);
                 
            }
        }
        int num = g.adj.size();
        for (int j = 0; j < num; j++) {
            Nodo nodo = g.adj.get(j);
            nodo.info();
            
        }
    }
    
    public void thompsonUnion(String expresion){
        Digraph g = new Digraph();
        ArrayList<String> subsER = new ArrayList<>();
        
        int index = expresion.indexOf("|");
        String exp1 = expresion.substring(0, index);
        String exp2 = expresion.substring(index+1);
        
        subsER.add(0, exp1);
        subsER.add(1, exp2);
        
        int endEtapa = -1;
        int v = 0;
        int w = 0;
        int s = 0;
        
        ArrayList<Transicion> transFinales = new ArrayList<>();
        
        Nodo nodo = new Nodo(s);
        g.addNodo(nodo);
        
        while(!subsER.isEmpty()){
            String reg = subsER.get(0);
            subsER.remove(0);
            
            v++;
            
            nodo.addTransicion(s,'_', v);
            
            int n = reg.length();
            if (n == 1){
                char c = reg.charAt(0);
                nodo = new Nodo(v);
                w = v + 1;
                nodo.addTransicion(v, c, w);
                g.addNodo(nodo);
                v++;

                nodo = new Nodo(w);
                g.addNodo(nodo);
                Transicion t = new Transicion(w,'_',endEtapa);

                transFinales.add(t);
                
            }
            else{
                endEtapa--;    
            }
        }
        
        int n = transFinales.size();
        for (int j = n-1; j >= 0; j--) {
            Transicion t = transFinales.get(j);
            int from = t.getFrom();
            int to = v+1;
            nodo = g.adj.get(from);
            nodo.addTransicion(from,'_',to); 
        }
        
        int num = g.adj.size();
        for (int j = 0; j < num; j++) {
            nodo = g.adj.get(j);
            nodo.info();
        }

    }
    /*
    private String[] separarCadenas(String expresion){
        String cadenas[] = new String[2];
        String exp1;
        String exp2;
        int index;
        
        // primer caso (E1).(E2)
        index = expresion.indexOf(").(");
        if (index != -1){
            exp1 = expresion.substring(1,index);
            exp2 = expresion.substring(index + 3, expresion.length() - 1);
        }
        else{
            // segundo caso E1.(E2)
            index = expresion.indexOf(".(");      
            if(index != -1){
                exp1 = expresion.substring(0,index);
                exp2 = expresion.substring(index+2,expresion.length() - 1);
            }
            else{
                // tercer caso (E1).E2
                index = expresion.indexOf(").");
                if(index != -1){
                    exp1 = expresion.substring(1,index);
                    exp2 = expresion.substring(index+2,expresion.length());
                }
                else{
                    // cuarto caso E1.E2
                    index = expresion.indexOf(".");
                    exp1 = expresion.substring(0, index);
                    exp2 = expresion.substring(index+1);
                }
            }
        }
        cadenas[0] = exp1;
        cadenas[1] = exp2;
        return cadenas;
        
    }
    */
    
    
    public class Nodo{
        private int id;
        private ArrayList<Transicion> trans;
        
        public Nodo(int id){
            this.id = id;
            this.trans = new ArrayList<>();
        }
        
        public void addTransicion(int v, char c, int w){
            this.trans.add(new Transicion(v,c,w));
        }
        
        public int numTransiciones(){
            return this.trans.size();
        }
        
        public Transicion getTransicion(int t){
            return this.trans.get(t);
        }
        
        public void info(){
            int n = trans.size();
            for (int i = 0; i < n; i++) {
                Transicion t = trans.get(i);
                int from = t.getFrom();
                char c = t.getTrance();
                int to = t.getTo();
                System.out.println("("+from+","+c+","+to+")");
            }
        }
    }
    
    public class Digraph {

        private final int V;           // number of vertices in this digraph
        private int E;                 // number of edges in this digraph
        private ArrayList<Nodo> adj;    // adj[v] = adjacency list for vertex v
        private int[] indegree;        // indegree[v] = indegree of vertex v

        /**
         * Initializes an empty digraph with <em>V</em> vertices.
         *
         * @param  V the number of vertices
         * @throws IllegalArgumentException if {@code V < 0}
         */
        public Digraph() {

            adj =  new ArrayList<>();
            this.V = 0;

        }




        /**
         * Returns the number of vertices in this digraph.
         *
         * @return the number of vertices in this digraph
         */
        public int V() {
            return V;
        }

        /**
         * Returns the number of edges in this digraph.
         *
         * @return the number of edges in this digraph
         */
        public int E() {
            return E;
        }


        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
        }

        /**
         * Adds the directed edge v→w to this digraph.
         *
         * @param  v the tail vertex
         * @param  w the head vertex
         * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
         */
        public void addNodo(Nodo nodo) {
            adj.add(nodo);
            E++;
        }




        /**
         * Returns the number of directed edges incident to vertex {@code v}.
         * This is known as the <em>indegree</em> of vertex {@code v}.
         *
         * @param  v the vertex
         * @return the indegree of vertex {@code v}               
         * @throws IllegalArgumentException unless {@code 0 <= v < V}
         */
        public int indegree(int v) {
            validateVertex(v);
            return indegree[v];
        }





    }
    
    public class Transicion{
        
        private char trance;
        private int from;
        private int to;
        
        public Transicion(int from, char trance, int to){
            this.from = from;
            this.trance = trance;
            this.to = to;
        }

        public char getTrance() {
            return trance;
        }

        public void setTrance(char trance) {
            this.trance = trance;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

    }
    
    
}
