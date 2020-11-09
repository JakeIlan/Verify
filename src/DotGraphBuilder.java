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

                writer.write(dotNode.id +
                        " [shape=" + dotNode.type +
                        ", label =\"" + dotNode.data + "\"];\n");
            }
            for (int i = 0; i < dotNodes.size(); i++) {
                if (i > 0) {
                    if (dotNodes.get(i).rank > dotNodes.get(i - 1).rank &&
                            dotNodes.get(i).rank > 1) {
                        writer.write("subgraph cluster_" + i + " {\n style = invis;\n");
                        for (DotNode child : dotNodes.get(i).getControlChildren()) {
                            writer.write(" " + dotNodes.get(i).id + " -> " + child.id + "\n");
                        }
                    }
                    if (i < dotNodes.size() - 1 && dotNodes.get(i).rank < dotNodes.get(i + 1).rank &&
                            dotNodes.get(i).rank > 1) {
                        writer.write("}\n");
                    }
                }

                for (DotNode child : dotNodes.get(i).getControlChildren()) {
                    writer.write(dotNodes.get(i).id + " -> " + child.id + "\n");
                }

            }
            writer.write("}");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
