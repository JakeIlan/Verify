import java.util.LinkedList;

public class DotNode {
    int rank = 0;
    int id;
    String bool = "";
    String type = "rec";
    String data;
    LinkedList<DotNode> controlChildren = new LinkedList<>();
    LinkedList<DotNode> dataChildren = new LinkedList<>();

    DotNode(String data, int id, int rank) {
        this.id = id;
        this.data = data;
        this.rank = rank;
    }

    DotNode(String data, String type, int id) {
        this.data = data;
        this.type = type;
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setBool(String bool) {
        this.bool = bool;
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
        StringBuilder str = new StringBuilder("CHILDREN OF " + this.data + " of rank " + this.rank + '\n');
        for (DotNode node : this.getControlChildren()) {
            str.append(node.toString());
        }
        return str.toString();
    }

    @Override
    public String toString() {
        return  id + ": " + data + " rank - " + rank + '\n';
    }
}

