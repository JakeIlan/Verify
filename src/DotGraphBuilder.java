import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class DotGraphBuilder {

    DotGraphBuilder(LinkedList<DotNode> dotNodes) {
        File output = new File("src/output_dot.txt");
        try {
            int i = 0;
            FileWriter writer = new FileWriter(output, false);
            writer.write("digraph G {\n");
            for (DotNode n : dotNodes) {
                writer.write(n.id + " [shape=" + n.type + ", label =\"" + n.data + "\"];\n");
            }
            for (DotNode n : dotNodes) {

                for (DotNode child : n.getControlChildren()) {
                    writer.write(n.id + " -> " + child.id + "\n");
                }

            }
            writer.write("}");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
