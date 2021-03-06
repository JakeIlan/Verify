import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DotGraphBuilder {

    ArrayList<DotNode> dotNodes;

    DotGraphBuilder(ArrayList<DotNode> dotNodes) {
        this.dotNodes = dotNodes;
        File output = new File("src/output_dot.txt");
        try {
            FileWriter writer = new FileWriter(output, false);
            writer.write("digraph G {\n");
            for (DotNode dotNode : dotNodes) {
                if (!dotNode.text.contains("else {")) {
                    writer.write(dotNode.id +
                            " [shape=" + dotNode.shape +
                            ", label =\"" + dotNode.text + "\"];\n");
                }
            }
            for (DotNode dotNode : dotNodes) {
                DotNode right = dotNode.rightControlChild;
                DotNode left = dotNode.leftControlChild;
                if (right != null) {
                    if (!right.bool.equals("True")) {
                        writer.write(dotNode.id + " -> " + right.id + "\n");
                    } else {
                        writer.write(dotNode.id + " -> " + right.id + " [label = " + right.bool + "]" + "\n");
                    }
                }
                if (left != null) {
                    if (!left.bool.equals("False")) {
                        writer.write(dotNode.id + " -> " + left.id + "\n");
                    } else {
                        writer.write(dotNode.id + " -> " + left.id + " [label = " + left.bool + "]" + "\n");
                    }
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

    }

    public void buildDataPath() {
        File output = new File("src/output_dot.txt");
        try {
            FileWriter writer = new FileWriter(output, true);
            for (int i = 0; i < dotNodes.size() - 1; i++) {
                for (DotNode child : dotNodes.get(i).dataChildren) {
                    writer.write(dotNodes.get(i).id + " -> " + child.id
                            + " [weight = 0, color = red]\n");
                }
            }
            writer.write("}");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
