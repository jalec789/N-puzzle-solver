import java.util.ArrayList;
import java.util.Objects;

public class Node implements Comparable<Node>{
    Node parent;    //parent node, doesnt actually hold state information unless specified
    ArrayList<Integer> state;   //actual state of the board
    ArrayList<Node> children;//should only go up to 4
    boolean visited;
    int weight;
    int depth;

    //Node constructor for uniform cost search
    Node(Node p, ArrayList<Integer> a){
        parent = p;
        state = a;
        children = null;
        visited = false;
        weight = 999;
        depth = 0;
    }

    //Node constructor for A*
    Node(Node p, ArrayList<Integer> a, int w){
        parent = p;
        state = a;
        children = null;
        visited = false;
        weight = w;
        depth = 0;
    }

    public int getWeight() {
        return weight;
    }

    //Override for A* priority queue to order based on weight
    @Override
    public int compareTo(Node node){
        if(this.getWeight() > node.getWeight()){
            return 1;
        }
        else if(this.getWeight() < node.getWeight()){
            return -1;
        }
        else{
            return 0;
        }
    }
}
