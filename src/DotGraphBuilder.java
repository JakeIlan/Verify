import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DotGraphBuilder {

    LinkedList<DotNode> dotNodes;

    DotGraphBuilder(LinkedList<DotNode> dotNodes) {
        this.dotNodes = dotNodes;
        File output = new File("src/output_dot.txt");
        try {
            FileWriter writer = new FileWriter(output, false);
            writer.write("digraph G {\n");
            for (DotNode dotNode : dotNodes) {
                if (!dotNode.text.matches("[}]") && !dotNode.text.contains("} else {")) {
                    writer.write(dotNode.id +
                            " [shape=" + dotNode.shape +
                            ", label =\"" + dotNode.text + "\"];\n");
                }
            }
            for (DotNode dotNode : dotNodes) {
                for (DotNode child : dotNode.getControlChildren()) {
                    if ((!child.bool.equals("True") && !child.bool.equals("False")) ||
                            dotNode.rank == 0) {
                        writer.write(dotNode.id + " -> " + child.id + "\n");
                    } else {
                        writer.write(dotNode.id + " -> " + child.id + "[label = " + child.bool + "]" + "\n");
                    }

                }
            }
            writer.write("}");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

    }

    public void buildDataPath() {
        System.out.println("DATA PATH: WORK IN PROGRESS");
        File output = new File("src/output_test.txt");
        try {
            FileWriter writer = new FileWriter(output, false);
            for (DotNode n : dotNodes) {

                writer.write(n.toString() + n.printData());

            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
