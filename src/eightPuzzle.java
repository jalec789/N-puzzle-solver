
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;


public class eightPuzzle {

    //I think this is 31 for an 8 puzzle, change this if n changes
    final static int DIAMETER = 31;
    //N is the dimensions of the nxn matrix
    final static int N = 3;


    public static void aStarMisplacedTile(ArrayList<Integer> intial, ArrayList<Integer> solution, int n){
        //int depth = 0;
        int LIMIT = DIAMETER;
        //Assign the initial state to the root node
        //We'll track tree nodes which will contain a board states in each node
        ArrayList<Integer> initState = new ArrayList<>(intial);
        Node root = new Node(null, initState, misplacedTileCount(initState, solution, n));

        PriorityQueue<Node> pq = new PriorityQueue<>();

        //Priority Queue Test Demo
//        Node a = new Node(null, initState, 5);
//        Node b = new Node(null, initState, 7);
//        Node c = new Node(null, initState, 4);
//        pq.add(a);
//        pq.add(b);
//        pq.add(c);
//        while (!pq.isEmpty()){
//            System.out.println(pq.remove().getWeight());
//        }

        Node traverse = root;
        pq.add(traverse);

        while(!pq.isEmpty()){
            pq.remove();//pop traverse, marks as visited
            if(traverse.state.equals(solution)){
                break; //found solution so return solved Node
            }
            if(traverse.depth < LIMIT){
                misplacedTileExpandNode(traverse,solution,n);
                for(int i = 0; i < traverse.children.size(); i++){
                    pq.add(traverse.children.get(i));
                    traverse.children.get(i).depth = traverse.depth + 1;
//                    System.out.println(traverse.children.get(i).getWeight());
                }
//                System.out.println();
            }
            traverse = pq.peek();
//            System.out.println("current front:" + pq.peek().getWeight());
//            printState(pq.peek().state, n);
        }

        //show solution...
//        if(pq.isEmpty()){
//            System.out.println("No Solution is possible.");
//        }
//        else{
//            while(traverse != null){
//                printState(traverse.state,n);
//                System.out.println();
//                traverse = traverse.parent;
//            }
//        }
    }

    public static int misplacedTileCount(ArrayList<Integer> state, ArrayList<Integer> solution, int n){
        int count = 0;
        int tot = n*n;
        for(int i = 0; i < tot; i++){
            //System.out.println(state.get(i) + " " + solution.get(i));
            if(state.get(i) != solution.get(i)){
                count++;
            }
        }
        //System.out.println(count);
        return count;
    }

    //same thing as expand node except we now calculate the weight
    public static void misplacedTileExpandNode(Node node, ArrayList<Integer> solution, int n){

        //This save each swap state
        ArrayList<Integer> left = expandLeft(node.state,n);
        ArrayList<Integer> right = expandRight(node.state,n);
        ArrayList<Integer> top = expandTop(node.state,n);
        ArrayList<Integer> bottom = expandBottom(node.state,n);
        //but not all may be valid swaps in the puzzle so
        //we'll do a check and add valid swaps to the list of child nodes

        //list of expansions
        node.children = new ArrayList<>();
        //swapped left number and add to list if valid
        if(left != null){
            if(node.parent == null) {
                node.children.add(new Node(node, left, misplacedTileCount(left,solution,n)));
            }
            else{
                if(!node.parent.state.equals(left)){//check for repeated states
                    node.children.add(new Node(node, left, misplacedTileCount(left,solution,n)));
                }
            }
        }
        //swapped right number and add to list if valid
        if(right != null){
            if(node.parent == null) {
                node.children.add(new Node(node, right, misplacedTileCount(right,solution,n)));
            }
            else{
                if(!node.parent.state.equals(right)){//check for repeated states
                    node.children.add(new Node(node, right, misplacedTileCount(right,solution,n)));
                }
            }
        }
        //swapped top number and add to list if valid
        if(top != null){
            if(node.parent == null) {
                node.children.add(new Node(node, top, misplacedTileCount(top,solution,n)));
            }
            else{
                if(!node.parent.state.equals(top)){//check for repeated states
                    node.children.add(new Node(node, top, misplacedTileCount(top,solution,n)));
                }
            }
        }
        //swapped bottom number and add to list if valid
        if(bottom != null){
            if(node.parent == null) {
                node.children.add(new Node(node, bottom, misplacedTileCount(bottom,solution,n)));
            }
            else{
                if(!node.parent.state.equals(bottom)){//check for repeated states
                    node.children.add(new Node(node, bottom, misplacedTileCount(bottom,solution,n)));
                }
            }
        }
    }





    //This function does the BFS traversal
    //n: is the size of the matrix, nxn
    //initial: initial state
    //solution: solution state
    public static void uniformCostSearch(ArrayList<Integer> intial, ArrayList<Integer> solution, int n){
        int depth = 0;
        int LIMIT = DIAMETER;

        //Assign the initial state to the root node
        //We'll track tree nodes which will contain a board states in each node
        ArrayList<Integer> initState = new ArrayList<>(intial);
        Node root = new Node(null, initState);

        //For BFS traversal we'll have a parents list and a childs list
        //if no parent node has the solution state then we will pop the parents
        //and the childs will become new parents list
        //then those parents will expand a new childs list until a solution is found
        //in the parents list
        ArrayList<Node> parents = new ArrayList<Node>();

        parents.add(root);

        //count will keep track of node created. ONLY for this type of search.
        //we start with 1 nodes for 1 parent and the children will be made
        int count = 1;

        //first iteration outside loop
        Node traverse = parents.get(0);
        expandNode(parents.get(0), n);
        ArrayList<Node> childs = new ArrayList<>();

        //...still part of first iteration, creating children
        for(int j = 0; j < traverse.children.size(); j++){
            childs.add(traverse.children.get(j));
            //each child made is a new node added, so count+1
            count++;
        }

        //Okay now this is the loop we will do BFS if the root node isn't already the solution
        while(!traverse.state.equals(solution)){
            depth++;//tracks the depth of the tree
            parents = childs;
            childs = new ArrayList<>();
            for(int i = 0; i < parents.size(); i++){    //for each parent check with solution or else expand
                traverse = parents.get(i);
                //System.out.println();//DEBUG
                //printState(traverse.state,n);//DEBUG
                if(traverse.state.equals(solution)){
                    break;  //SOLUTION FOUND BREAK
                }
                expandNode(traverse, n);
                for(int j = 0; j < traverse.children.size(); j++){  //add all the expansions to a childs list
                    childs.add(traverse.children.get(j));
                    count++;
                }
            }
            if(depth > LIMIT){
                break;
            }
        }

//        //output solution (probably should be in a separate function...)
//        if(depth>LIMIT){
//            System.out.println("FAILED");
//        }
//        else{
//            while (traverse.parent != null){
//                System.out.println();
//                printState(traverse.state,n);
//                traverse = traverse.parent;
//            }
//            System.out.println();
//            printState(traverse.state,n);
//
//            System.out.println("Solved at depth: " + depth + ".\nCreated " + count + " nodes total.");
//        }
    }

    //expandNode()
    //Expands possible moves
    //We have also saved time and memory by cutting down on expanded states
    //and not repeating the parent state
    public static void expandNode(Node node, int n){

        //This save each swap state
        ArrayList<Integer> left = expandLeft(node.state,n);
        ArrayList<Integer> right = expandRight(node.state,n);
        ArrayList<Integer> top = expandTop(node.state,n);
        ArrayList<Integer> bottom = expandBottom(node.state,n);
        //but not all may be valid swaps in the puzzle so
        //we'll do a check and add valid swaps to the list of child nodes

        //list of expansions
        node.children = new ArrayList<>();
        //swapped left number and add to list if valid
        if(left != null){
            if(node.parent == null) {
                node.children.add(new Node(node, left));
            }
            else{
                if(!node.parent.state.equals(left)){//check for repeated states
                    node.children.add(new Node(node, left));
                }
            }
        }
        //swapped right number and add to list if valid
        if(right != null){
            if(node.parent == null) {
                node.children.add(new Node(node, right));
            }
            else{
                if(!node.parent.state.equals(right)){//check for repeated states
                    node.children.add(new Node(node, right));
                }
            }
        }
        //swapped top number and add to list if valid
        if(top != null){
            if(node.parent == null) {
                node.children.add(new Node(node, top));
            }
            else{
                if(!node.parent.state.equals(top)){//check for repeated states
                    node.children.add(new Node(node, top));
                }
            }
        }
        //swapped bottom number and add to list if valid
        if(bottom != null){
            if(node.parent == null) {
                node.children.add(new Node(node, bottom));
            }
            else{
                if(!node.parent.state.equals(bottom)){//check for repeated states
                    node.children.add(new Node(node, bottom));
                }
            }
        }
    }

    //Returns the state of the board if we slide the left tile towards the blank tile
    //If not possible this function simply retruns null
    public static ArrayList<Integer> expandLeft(ArrayList<Integer> root, int n){
        //find the index where the zero is at
        int i = root.indexOf(0);
        ArrayList<Integer> ar;
        //check if left index is slidable
        if( ((i-1)>=0) && (((i-1+1)%n) != 0) ){
            ar = new ArrayList<>(root);
            Collections.swap(ar, i,i-1);
        } else {
            return null;
        }
        return ar;
    }

    //Returns the state of the board if we slide the right tile towards the blank tile
    //If not possible this function simply retruns null
    public static ArrayList<Integer> expandRight(ArrayList<Integer> root, int n){
        //find the index where the zero is at
        int i = root.indexOf(0);
        ArrayList<Integer> ar;
        //check if right index is slidable
        if( ((i+1)<=(n*n-1)) && (((i+1)%n) != 0) ){
            ar = new ArrayList<>(root);
            Collections.swap(ar, i,i+1);
        } else {
            return null;
        }
        return ar;
    }

    //Returns the state of the board if we slide the top tile towards the blank tile
    //If not possible this function simply retruns null
    public static ArrayList<Integer> expandTop(ArrayList<Integer> root, int n){
        //find the index where the zero is at
        int i = root.indexOf(0);
        ArrayList<Integer> ar;
        //check if top index is slidable
        if( (i-n) >= 0 ){
            ar = new ArrayList<>(root);
            Collections.swap(ar, i,i-n);
        } else {
            return null;
        }
        return ar;
    }

    //Returns the state of the board if we slide the bottom tile towards the blank tile
    //If not possible this function simply retruns null
    public static ArrayList<Integer> expandBottom(ArrayList<Integer> root, int n){
        //find the index where the zero is at
        int i = root.indexOf(0);
        ArrayList<Integer> ar;
        //check if bottom index is slidable
        if( (i+n) <= (n*n - 1) ){
            ar = new ArrayList<>(root);
            Collections.swap(ar, i,i+n);
        } else {
            return null;
        }
        return ar;
    }

    //This function simply prints the state of the board in nxn format
    public static void printState(ArrayList<Integer> ar, int n){
        int sqrd = n*n;
        for (int i = 0; i < sqrd; i++){
            if(ar.get(i) == 0){
                //print zero or empty
                System.out.print(" " + " ");
            }
            else{
                System.out.print(ar.get(i) + " ");
            }
            if((i+1)%n==0){
                System.out.println();
            }
        }
    }


    public static void main(String[] args){

        //Initial State
        ArrayList<Integer> dep0 = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,0));
        ArrayList<Integer> dep1 = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,0,8));
        ArrayList<Integer> dep2 = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,0,7,8));
        ArrayList<Integer> dep4 = new ArrayList<>(Arrays.asList(1,2,3,5,0,6,4,7,8));
        ArrayList<Integer> dep8 = new ArrayList<>(Arrays.asList(1,3,6,5,0,2,4,7,8));
        ArrayList<Integer> dep12 = new ArrayList<>(Arrays.asList(1,3,6,5,0,7,4,8,2));
        ArrayList<Integer> dep16 = new ArrayList<>(Arrays.asList(1,6,7,5,0,3,4,8,2));
        ArrayList<Integer> dep20 = new ArrayList<>(Arrays.asList(7,1,2,4,8,5,6,3,0));
        ArrayList<Integer> dep24 = new ArrayList<>(Arrays.asList(0,7,2,4,6,1,3,5,8));

        //List all possible solutions
        ArrayList<ArrayList<Integer>> possibleSolutions = new ArrayList<>(
                Arrays.asList(dep0, dep1, dep2, dep4, dep8, dep12, dep16, dep20, dep24));

        //Solution State
        //for an 8 puzzle: (1,2,3,4,5,6,7,8,0)
        //1,2,3
        //4,5,6
        //7,8,0
        //This is the solution state
        ArrayList<Integer> sol = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,0));

        //ArrayList<Integer> unsolvable = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,8,7,0));

        long start;
        long end;
        double time;

        start = System.nanoTime();
        //pass the inital and solution state through
        uniformCostSearch(dep24,sol,N);
        end = System.nanoTime();
        time = ((end - start) / 1e9);
        System.out.println("Time: " + time + " seconds");



        start = System.nanoTime();
        aStarMisplacedTile(dep24,sol,N);
        end = System.nanoTime();
        time = ((end - start) / 1e9);
        System.out.println("Time: " + time + " seconds");

        return;
    }
}
