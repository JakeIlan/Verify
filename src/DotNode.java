import java.util.ArrayList;

public class DotNode {
    int rank;
    int id;
    String bool = "";
    String shape = "rec";
    String type = "default";
    String text;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<DotNode> controlChildren = new ArrayList<>();
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
        this.controlChildren.clear();
        this.dataChildren.clear();
    }

    public void addControlChild(DotNode controlChild) {
        this.controlChildren.add(controlChild);
    }

    public void addDataChild(DotNode dataChild) {
        this.dataChildren.add(dataChild);
    }

    public ArrayList<DotNode> getControlChildren() {
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

//    public ArrayList<DotNode> getChildTree(DotNode node) {
//        ArrayList<DotNode> res = new ArrayList<>();
//        for (DotNode child : node.getControlChildren()) {
//            if (!res.contains(child)) {
//                res.add(child);
//            }
//
//        }
//
//        return res;
//    }

    @Override
    public String toString() {
        return  id + "\t:\t" + text + "\ttype\t" + type + '\n';
    }
}

