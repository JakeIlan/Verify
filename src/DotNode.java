import java.util.LinkedList;

public class DotNode {
    int id;
    String type = "oval";
    String data;
    LinkedList<DotNode> controlChildren = new LinkedList<>();
    LinkedList<DotNode> dataChildren = new LinkedList<>();

    DotNode(String data, int id) {
        this.id = id;
        this.data = data;
    }

    DotNode(String data, String type, int id) {
        this.data = data;
        this.type = type;
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "DotNode data='" + data + '\'' + '\n';
    }
}

