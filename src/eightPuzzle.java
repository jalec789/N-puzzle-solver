
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class eightPuzzle {

    //Does the BFS
    public static void uniformCostSearch(ArrayList<Integer> intial, ArrayList<Integer> solution, int n){
        int depth = 0;
        int LIMIT = 31;//diameter I think is 31

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

        //first iteration outside loop
        Node traverse = parents.get(0);
        expandNode(parents.get(0), n);
        ArrayList<Node> childs = new ArrayList<>();

        //...still part of first iteration
        for(int j = 0; j < traverse.children.size(); j++){
            childs.add(traverse.children.get(j));
        }

        //Okay now this is the loop we will do BFS if the root node isn't already the solution
        while(!traverse.state.equals(solution)){
            depth++;//tracks the depth of the tree
            parents = childs;
            childs = new ArrayList<>();
            for(int i = 0; i < parents.size(); i++){    //for each parent check with solution and expand
                traverse = parents.get(i);
                //System.out.println();//DEBUG
                //printState(traverse.state,n);//DEBUG
                if(traverse.state.equals(solution)){
                    break;
                }
                expandNode(traverse, n);
                for(int j = 0; j < traverse.children.size(); j++){  //add all the expansions to a childs list
                    childs.add(traverse.children.get(j));
                }
            }
            if(depth > LIMIT){
                break;
            }
        }

        //output solution (probably should be in a separate function...)
        while (traverse.parent != null){
            System.out.println();
            printState(traverse.state,n);
            traverse = traverse.parent;
        }
        System.out.println();
        printState(traverse.state,n);

        System.out.println("Solved at depth: " + depth);
    }

    //expandNode()
    //Expands possible moves
    //We have also saved time and memory by cutting down on expanded states
    //and not repeating the parent state
    public static void expandNode(Node node, int n){
        ArrayList<Integer> left = expandLeft(node.state,n);
        ArrayList<Integer> right = expandRight(node.state,n);
        ArrayList<Integer> top = expandTop(node.state,n);
        ArrayList<Integer> bottom = expandBottom(node.state,n);

        node.children = new ArrayList<>();
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
            System.out.print(ar.get(i) + " ");
            if((i+1)%n==0){
                System.out.println();
            }
        }
    }


    public static void main(String[] args){

        //N is the dimensions of the nxn matrix
        int N = 3;

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

        //Solution State
        //for an 8 puzzle: (1,2,3,4,5,6,7,8,0)
        //1,2,3
        //4,5,6
        //7,8,0
        ArrayList<Integer> sol = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,0));


        uniformCostSearch(dep24, sol, N);

        return;
    }
}
