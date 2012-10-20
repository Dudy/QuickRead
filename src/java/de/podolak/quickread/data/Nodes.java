package de.podolak.quickread.data;

import java.util.ArrayList;

/**
 *
 * @author Dude
 */
public class Nodes extends ArrayList<Node> {

    @Override
    public String toString() {
        StringBuilder nodesStringbuilder = new StringBuilder();
        for (Node node : this) {
            nodesStringbuilder.append(node);
            nodesStringbuilder.append(",");
        }
        if (size() > 0) {
            nodesStringbuilder.deleteCharAt(nodesStringbuilder.length() - 1);
        }
        
        return
                "nodes=[" +
                nodesStringbuilder +
                "]";
    }
    
}
