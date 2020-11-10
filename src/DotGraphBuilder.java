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

            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

    }

    public void buildDataPath() {
        System.out.println("DATA PATH: WORK IN PROGRESS");
        File output = new File("src/output_dot.txt");
        try {
            FileWriter writer = new FileWriter(output, true);
            for (int i = 0; i < dotNodes.size() - 1; i++) {
                if (dotNodes.get(i).type.equals("variable")) {
                    for (int j = i + 1; j < dotNodes.size(); j++) {
                        for (String d : dotNodes.get(i).data) {
                            if (dotNodes.get(j).data.contains(d)) {
                                writer.write(dotNodes.get(i).id + " -> " + dotNodes.get(j).id
                                    + " [style = dashed, weight = 0.1, color = grey]\n");
                            }
                        }
                    }
                }
            }
            writer.write("}");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
