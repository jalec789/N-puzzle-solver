import java.util.ArrayList;

public class Node {
    Node parent;

    boolean visited;

    ArrayList<Integer> state;

    ArrayList<Node> children;//should only go up to 4

    Node(Node p, ArrayList<Integer> a){
        parent = p;
        state = a;
        children = null;
        visited = false;
    }
}
