
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.lang.Math;


public class eightPuzzle {

    //I think this is 31 for an 8 puzzle, change DIAMETER if N changes
    final static int DIAMETER = 31;
    //N is the dimensions of the nxn matrix
    final static int N = 3;





    //-----------------------------------------------------------------------------
    // A* MANHATTAN SEARCH FUNCTIONS
    //-----------------------------------------------------------------------------

    //This function does a search and weighs each node based on the sum of the distance of each misplaced tile
    //n: is the size of the matrix, nxn
    //initial: initial state
    //solution: solution state
    public static Node aStarManhattanSearch(ArrayList<Integer> intial, ArrayList<Integer> solution, int n){
        int LIMIT = DIAMETER;
        //Assign the initial state to the root node
        //We'll track tree nodes which will contain a board states in each node
        ArrayList<Integer> initState = new ArrayList<>(intial);
        Node root = new Node(null, initState, manhattanDistanceCount(initState, solution, n));

        PriorityQueue<Node> pq = new PriorityQueue<>();

        long count = 0; //track nodes created

        //initialize
        Node traverse = root;
        count++;
        pq.add(traverse);

        while(!pq.isEmpty()){
            pq.remove();//pop traverse, marks as visited
            if(traverse.state.equals(solution)){
                //SOLUTION FOUND so return solved Node
                System.out.println("Manhattan Search solved, created " + count + " nodes total.");
                return traverse;
            }
            if(traverse.depth < LIMIT){
                manhattanExpandNode(traverse,solution,n);
                for(int i = 0; i < traverse.children.size(); i++){
                    pq.add(traverse.children.get(i));
                    traverse.children.get(i).depth = traverse.depth + 1;
                    count++;
                }
            }
            traverse = pq.peek();
        }
        return null; //NO SOLUTION FOUND
    }

    //This function compares <state> with <solution> state and
    // returns the total distance of each misplaced tile to the solution state
    public static int manhattanDistanceCount(ArrayList<Integer> state, ArrayList<Integer> solution, int n){
        int count = 0;
        int rowMoves;
        int colMoves;
        int j;
        int total = n*n;
        for(int i = 0; i < total; i++){
            //System.out.println(state.get(i) + " " + solution.get(i));
            if(state.get(i) == 0) {
                //do nothing, we want to skip the blank tile
            }
            else if(state.get(i) != solution.get(i)){
                j = solution.indexOf(state.get(i));
                rowMoves = Math.abs((i % n) - (j % n));
                colMoves = Math.abs((i / n) - (j / n));
                count += rowMoves + colMoves;
//                System.out.println("\nRow: " +rowMoves + "\nCol: " + colMoves +"\nCount: " + count);
            }
//            else{
//                System.out.println("skip index: " + i);
//            }
        }
        //System.out.println(count);

        return count;
    }

    //same thing as expandNode() except we now calculate the weight
    //Expands POSSIBLE moves and links <node> to the expanded states as children nodes with respective weights
    //weights are based on distance to the misplaced tiles
    //We have also saved time and memory by cutting down on expanded states
    //and not repeating the parent state of <node>
    public static void manhattanExpandNode(Node node, ArrayList<Integer> solution, int n){

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
                node.children.add(new Node(node, left, manhattanDistanceCount(left,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(left)){//check for repeated states
                    node.children.add(new Node(node, left, manhattanDistanceCount(left,solution,n) + (node.depth + 1)));
                }
            }
        }
        //swapped right number and add to list if valid
        if(right != null){
            if(node.parent == null) {
                node.children.add(new Node(node, right, manhattanDistanceCount(right,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(right)){//check for repeated states
                    node.children.add(new Node(node, right, manhattanDistanceCount(right,solution,n) + (node.depth + 1)));
                }
            }
        }
        //swapped top number and add to list if valid
        if(top != null){
            if(node.parent == null) {
                node.children.add(new Node(node, top, manhattanDistanceCount(top,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(top)){//check for repeated states
                    node.children.add(new Node(node, top, manhattanDistanceCount(top,solution,n) + (node.depth + 1)));
                }
            }
        }
        //swapped bottom number and add to list if valid
        if(bottom != null){
            if(node.parent == null) {
                node.children.add(new Node(node, bottom, manhattanDistanceCount(bottom,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(bottom)){//check for repeated states
                    node.children.add(new Node(node, bottom, manhattanDistanceCount(bottom,solution,n) + (node.depth + 1)));
                }
            }
        }
    }





    //-----------------------------------------------------------------------------
    // A* MISPLACED TILES SEARCH FUNCTIONS
    //-----------------------------------------------------------------------------

    //This function does a search and weighs each node based on the amount of misplaced tiles
    //n: is the size of the matrix, nxn
    //initial: initial state
    //solution: solution state
    public static Node aStarMisplacedTileSearch(ArrayList<Integer> intial, ArrayList<Integer> solution, int n){
        //int depth = 0;
        int LIMIT = DIAMETER;
        //Assign the initial state to the root node
        //We'll track tree nodes which will contain a board states in each node
        ArrayList<Integer> initState = new ArrayList<>(intial);
        Node root = new Node(null, initState, misplacedTileCount(initState, solution, n));

        PriorityQueue<Node> pq = new PriorityQueue<>();

        long count = 0; //track nodes created

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
        count++;
        pq.add(traverse);

        while(!pq.isEmpty()){
            pq.remove();//pop traverse, marks as visited
            if(traverse.state.equals(solution)){
                //SOLUTION FOUND so return solved Node
                System.out.println("Misplaced Tiles Search solved, created " + count + " nodes total.");
                return traverse;
            }
            if(traverse.depth < LIMIT){
                //Expand traverse (make children on traverse)
                misplacedTileExpandNode(traverse,solution,n);
                for(int i = 0; i < traverse.children.size(); i++){
                    pq.add(traverse.children.get(i));
                    traverse.children.get(i).depth = traverse.depth + 1;
//                    System.out.println(traverse.children.get(i).getWeight());
                    count++;
                }
//                System.out.println();
            }
            traverse = pq.peek();
//            System.out.println("current front:" + pq.peek().getWeight());
//            printState(pq.peek().state, n);
        }
//        if(pq.isEmpty()){
//            System.out.println("No Solution is possible.");
//        }
        return null;//NO SOLUTION FOUND
    }

    //This function compares <state> with <solution> state and
    // returns the number of misplaced tiles in <state>
    public static int misplacedTileCount(ArrayList<Integer> state, ArrayList<Integer> solution, int n){
        int count = 0;
        int total = n*n;
        for(int i = 0; i < total; i++){
            //System.out.println(state.get(i) + " " + solution.get(i));
            if(state.get(i) != solution.get(i)){
                count++;
            }
        }
        //System.out.println(count);
        return count;
    }

    //same thing as expandNode() except we now calculate the weight
    //Expands POSSIBLE moves and links <node> to the expanded states as children nodes with respective weights
    //weights are based on misplaced tile count
    //We have also saved time and memory by cutting down on expanded states
    //and not repeating the parent state of <node>
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
                //weight f(n) = g(n) + h(n) = number of misplaced tiles + depth aka number of moves
                node.children.add(new Node(node, left, misplacedTileCount(left,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(left)){//check for repeated states
                    node.children.add(new Node(node, left, misplacedTileCount(left,solution,n) + (node.depth + 1)));
                }
            }
        }
        //swapped right number and add to list if valid
        if(right != null){
            if(node.parent == null) {
                node.children.add(new Node(node, right, misplacedTileCount(right,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(right)){//check for repeated states
                    node.children.add(new Node(node, right, misplacedTileCount(right,solution,n) + (node.depth + 1)));
                }
            }
        }
        //swapped top number and add to list if valid
        if(top != null){
            if(node.parent == null) {
                node.children.add(new Node(node, top, misplacedTileCount(top,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(top)){//check for repeated states
                    node.children.add(new Node(node, top, misplacedTileCount(top,solution,n) + (node.depth + 1)));
                }
            }
        }
        //swapped bottom number and add to list if valid
        if(bottom != null){
            if(node.parent == null) {
                node.children.add(new Node(node, bottom, misplacedTileCount(bottom,solution,n) + 1));
            }
            else{
                if(!node.parent.state.equals(bottom)){//check for repeated states
                    node.children.add(new Node(node, bottom, misplacedTileCount(bottom,solution,n) + (node.depth + 1)));
                }
            }
        }
    }






    //-----------------------------------------------------------------------------
    // UNIFORM COST SEARCH FUNCTIONS
    //-----------------------------------------------------------------------------

    //This function does the BFS traversal
    //n: is the size of the matrix, nxn
    //initial: initial state
    //solution: solution state
    public static Node uniformCostSearch(ArrayList<Integer> intial, ArrayList<Integer> solution, int n){
//        int depth = 0;
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
        long count = 1;

        //first iteration outside loop
        Node traverse = parents.get(0);
        traverse.depth = 0;
        expandNode(parents.get(0), n);
        ArrayList<Node> childs = new ArrayList<>();

        //...still part of first iteration, creating children
        for(int j = 0; j < traverse.children.size(); j++){
            childs.add(traverse.children.get(j));
            traverse.children.get(j).depth = traverse.depth + 1;
            //each child made is a new node added, so count+1
            count++;
        }

        //Okay now this is the loop we will do BFS if the root node isn't already the solution
        while(!traverse.state.equals(solution)){
//            depth++;//tracks the depth of the tree
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
                    traverse.children.get(j).depth = traverse.depth + 1;
                    count++;
                }
            }
            if(traverse.depth > LIMIT){
                return null;
            }
        }
        System.out.println("Uniform Cost Search solved, created " + count + " nodes total.");
        return traverse;
    }

    //expandNode()
    //Expands POSSIBLE moves and links the expanded states as children nodes
    //We have also saved time and memory by cutting down on expanded states
    //and not repeating the parent state of <node>
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





    //-----------------------------------------------------------------------------
    // The following functions are used generically through all search algorithms
    //-----------------------------------------------------------------------------

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

    //This function prints the parent trace
    public static void printTrace(Node leaf, int n){

        Node traverse = leaf;

        if(traverse == null){
            System.out.println("FAILED. No solution found");
        }
        else{
//            while (traverse != null){
//                printState(traverse.state,n);
//                System.out.println();
//                if(traverse.parent == null){
//                   break;
//                }else{
//                    traverse = traverse.parent;
//                }
//            }
//            System.out.println();
//            printState(traverse.state,n);

            System.out.println("Solved at depth: " + leaf.getDepth());
        }
    }



    public static void main(String[] args){

        //Initial States
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

        //Here's an unsolvable solution, Used for testing only
        //ArrayList<Integer> unsolvable = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,8,7,0));

        ArrayList<Integer> cust = new ArrayList<>(Arrays.asList(1,2,5,3,6,8,7,0,4));
//        printState(cust,N);
//        printState(sol, N);
//        aStarManhattanSearch(dep8, sol, N);

        Node solved;


        long start;
        long end;
        double time;


        ArrayList<Integer> test = dep24;

        start = System.nanoTime();  //START TIMING FUNCTION
        //pass the initial and solution state through
        solved = uniformCostSearch(test,sol,N);
        end = System.nanoTime();    //END TIMING FUNCTION
        time = ((end - start) / 1e9);
        System.out.println("Time: " + time + " seconds");
        printTrace(solved,N);
        System.out.println();


        start = System.nanoTime();  //START TIMING FUNCTION
        //pass the initial and solution state through
        solved = aStarMisplacedTileSearch(test,sol,N);
        end = System.nanoTime();    //END TIMING FUNCTION
        time = ((end - start) / 1e9);
        System.out.println("Time: " + time + " seconds");
        printTrace(solved, N);
        System.out.println();


        start = System.nanoTime();  //START TIMING FUNCTION
        //pass the initial and solution state through
        solved = aStarManhattanSearch(test,sol,N);
        end = System.nanoTime();    //END TIMING FUNCTION
        time = ((end - start) / 1e9);
        System.out.println("Time: " + time + " seconds");
        printTrace(solved, N);




        //Questions:

        //How do you expect to input items, Should we be performing checks if it is correct input???
        //      similar to deniz.co/8-puzzle-solver/  ???


        //What do you think of the naming in this code so far, I know it said book similar but I have this rn???









        return;
    }
}
