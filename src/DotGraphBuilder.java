import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class DotGraphBuilder {

    DotGraphBuilder(LinkedList<DotNode> dotNodes) {
        File output = new File("src/output_dot.txt");
        try {
            FileWriter writer = new FileWriter(output, false);
            writer.write("digraph G {\n");
            for (DotNode dotNode : dotNodes) {
                if (!dotNode.data.matches("[}]")) {
                    writer.write(dotNode.id +
                            " [shape=" + dotNode.type +
                            ", label =\"" + dotNode.data + "\"];\n");
                }

            }
            for (DotNode dotNode : dotNodes) {

                for (DotNode child : dotNode.getControlChildren()) {
                    writer.write(dotNode.id + " -> " + child.id + "\n");
                }

            }
            writer.write("}");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
