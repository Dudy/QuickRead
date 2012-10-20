package de.podolak.quickread.data;

import java.io.Serializable;

/**
 *
 * @author Dude
 */
public class Node implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String title;
    private String text;
    private Nodes children;

    public Node() {
        this("", "", null);
    }
    
    public Node(String title) {
        this(title, "", null);
    }

    public Node(String title, String text) {
        this(title, text, null);
    }
    
    public Node(String title, String text, Nodes children) {
        this.title = title;
        this.text = text;
        this.children = children;
        
        if (this.children == null) {
            this.children = new Nodes();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Nodes getChildren() {
        return children;
    }

    public void setChildren(Nodes children) {
        this.children = children;
    }
    
    public boolean addChild(Node child) {
        return this.children.add(child);
    }
    
    public boolean removeChild(Node child) {
        return this.children.remove(child);
    }
    
    public int getNumberOfChildren() {
        return this.children.size();
    }

    @Override
    public String toString() {
        return
                "node=[" +
                "title=" + title + "," +
                "text=" + text + "," +
                children +
                "]";
    }

}
