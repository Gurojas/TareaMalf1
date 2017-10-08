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
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        TareaMalf1 e = new TareaMalf1();
        //e.thompsonConcatenacion("a.(b.c)");
        e.thompsonUnion("a|b");
       
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
        int i = 0;
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

                nodo = new Nodo(v);
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

    }
    
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
