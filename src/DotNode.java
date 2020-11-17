import java.util.ArrayList;

public class DotNode {
    int rank;
    int id;
    String bool = "";
    String shape = "rec";
    String type = "default";
    String text;
    ArrayList<String> data = new ArrayList<>();
    DotNode leftControlChild;
    DotNode rightControlChild;
    ArrayList<DotNode> dataChildren = new ArrayList<>();

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
        this.dataChildren.clear();
        this.leftControlChild = null;
        this.rightControlChild = null;
    }

    public void setLeftControlChild(DotNode leftControlChild) {
        this.leftControlChild = leftControlChild;
    }

    public void setRightControlChild(DotNode rightControlChild) {
        this.rightControlChild = rightControlChild;
    }

    public void addDataChild(DotNode dataChild) {
        this.dataChildren.add(dataChild);
    }

    public ArrayList<DotNode> getDataChildren() {
        return dataChildren;
    }

    public String printChildren() {
        StringBuilder str = new StringBuilder("DATA CHILDREN OF " + this.text + " of rank " + this.rank + '\n');
        for (DotNode node : this.getDataChildren()) {
            str.append(node.toString());
            str.append('\n');
        }
        str.append("-----------------\n");
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
        return id + "\t:\t" + text + "\trank:\t" + rank + " " + type + '\n';
    }
}

