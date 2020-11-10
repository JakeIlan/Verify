import java.util.ArrayList;
import java.util.LinkedList;

public class DotNode {
    int rank;
    int id;
    String bool = "";
    String shape = "rec";
    String type = "default";
    String text;
    ArrayList<String> data = new ArrayList<>();
    LinkedList<DotNode> controlChildren = new LinkedList<>();
    LinkedList<DotNode> dataChildren = new LinkedList<>();

    DotNode(String text, int id, int rank) {
        this.id = id;
        this.text = text;
        this.rank = rank;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public void setBool(String bool) {
        this.bool = bool;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void removeAllChildren() {
        this.controlChildren.clear();
        this.dataChildren.clear();
    }

    public void addControlChild(DotNode controlChild) {
        this.controlChildren.addLast(controlChild);
    }

    public void addDataChild(DotNode dataChild) {
        this.dataChildren.addLast(dataChild);
    }

    public LinkedList<DotNode> getControlChildren() {
        return controlChildren;
    }

    public String printChildren() {
        StringBuilder str = new StringBuilder("CHILDREN OF " + this.text + " of rank " + this.rank + '\n');
        for (DotNode node : this.getControlChildren()) {
            str.append(node.toString());
        }
        return str.toString();
    }

    public String printData() {
        StringBuilder str = new StringBuilder();
        for (String v : data) {
            str.append(v);
            str.append('\n');
        }
        return str.toString();
    }

    @Override
    public String toString() {
        return  id + "\t:\t" + text + "\ttype\t" + type + '\n';
    }
}

