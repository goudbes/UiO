import java.util.List;

/**
 * A generic node
 */
public interface Node {
    List<Edge> getOutEdges();
    List<Edge> getInEdges();
}
