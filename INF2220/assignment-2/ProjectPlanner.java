import java.io.FileNotFoundException;
import java.util.*;

public class ProjectPlanner {
    public static void main(String[] args) throws FileNotFoundException {
        TaskGraph taskGraph = new TaskGraph();

        try {
            taskGraph.readFile(args[0]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Error: no file name provided!");
            System.exit(1);
        }

        taskGraph.updateOutEdges();

        if (taskGraph.findCycleDFSStart()) {
            System.out.println("Cycle detected, TERMINATING NOW!!!11");
            System.exit(1);
        }

        Graph eventNodeGraph = taskGraph.eventNodeGraph();
        Graph sortedEventNodeGraph = taskGraph.topologicalSort(eventNodeGraph);
        printSchedule(taskGraph, sortedEventNodeGraph);
    }

    private static void printSchedule(TaskGraph taskGraph, Graph sortedEventNodeGraph) {

        // First calculate all the earliest completion times for each event node.
        final Map<Node, TaskGraph.CompletionTimes> times =
                taskGraph.calculateCompletionTimes(sortedEventNodeGraph);

        // Sort the graph based on the calculated completion times
        List<Node> nodes = sortedEventNodeGraph.getNodes();
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return ((Integer) times.get(n1).ec).compareTo(times.get(n2).ec);
            }
        });

        // Print the data according the the specified format
        int currentTime = -1;
        int currentStaff = 0;
        for (Node node : nodes) {
            int time = times.get(node).ec;
            if (time != currentTime) {
                if (currentTime != -1) {
                    System.out.println("       Current staff: " + currentStaff);
                }
                System.out.println("\nTime: " + time);
                currentTime = time;
            }
            TaskGraph.Event event = (TaskGraph.Event) node;
            String str;
            if (event.isIn()) {
                str = "Starting";
                currentStaff += event.getTask().getStaff();
            } else {
                str = "Finished";
                currentStaff -= event.getTask().getStaff();
            }
            System.out.print("             " + str);
            System.out.println(" " + event.getTask().getId());
        }

        // Print info about each task
        System.out.println();
        System.out.println("Task information");
        System.out.println("================");
        for (Node node : nodes) {
            TaskGraph.Event event = (TaskGraph.Event) node;
            if (!event.isIn()) {
                continue;
            }
            TaskGraph.Task task = event.getTask();
            System.out.println("ID             : " + task.getId());
            System.out.println("Name           : " + task.getName());
            System.out.println("Time           : " + task.getTime());
            System.out.println("Staff          : " + task.getStaff());
            System.out.println("Earliest start : " + times.get(node).ec);
            System.out.println("Latest start   : " + times.get(node).lc);
            System.out.println("Slack          : " + (times.get(node).lc - times.get(node).ec));
            System.out.println();
        }
    }
}
