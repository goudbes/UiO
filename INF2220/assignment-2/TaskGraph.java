import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TaskGraph {
    //Tasks, with ID as key
    HashMap<Integer, Task> tasks = new HashMap<>();

    /**
     * Reading input file
     *
     * @param filename name of the input file
     */
    public void readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File doesn't exist");
        }
        //Starting to read the file.
        Scanner in = new Scanner(file);
        int i = Integer.parseInt(in.nextLine());
        in.nextLine();
        int k = 0;
        while (in.hasNextLine()) {
            if (k < i) {
                String line = in.nextLine();
                Task task = readTask(line);
                tasks.put(task.getId(), task);
                k++;
            } else {
                throw new IllegalArgumentException("Too many lines");
            }
        }
    }

    /**
     * Adding out edges for each task
     */
    public void updateOutEdges() {
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            Task t = entry.getValue();
            for (Integer i : t.getPredececcors()) {
                Task tt = tasks.get(i);
                if (tt == null) {
                    throw new NullPointerException();
                }
                tt.getOutTaskEdges().add(new TaskEdge(tt, t));
            }
        }
    }

    /**
     * Splitting the line from file and creating a new task
     *
     * @param line Input line from the file
     * @return task
     */
    public Task readTask(String line) {
        String[] words = line.split("\\s+");
        int id = Integer.parseInt(words[0]);
        String name = words[1];
        int time = Integer.parseInt(words[2]);
        int staff = Integer.parseInt(words[3]);
        Integer[] pred = new Integer[words.length - 5];
        for (int i = 4; i < words.length - 1; i++) {
            pred[i - 4] = Integer.parseInt(words[i]);
        }
        Task task = new Task(id, name, time, staff, pred);
        return task;
    }

    /**
     * Searches for cycles in the graph. Returns as soon as one is found and prints it.
     *
     * @return true if a graph is found
     */
    public boolean findCycleDFSStart() {
        for (Task t : tasks.values()) {
            // Start parsing at each task with no dependencies
            if (t.getNumberOfPredecessors() == 0) {
                Stack<Task> cycle = new Stack<>();
                // Print cycle if found
                if (findCycleDFS(t, cycle)) {
                    System.out.print("Cycle: ");
                    while (!cycle.empty()) {
                        System.out.print(cycle.pop().getName() + " ");
                    }
                    System.out.println();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Searches for cycles in the graph. Returns as soon as one is found and prints it.
     *
     * @param t     start task
     * @param cycle storage for detected cycle
     * @return true if cycle is detected, false otherwise
     */
    private boolean findCycleDFS(Task t, Stack<Task> cycle) {
        // Mark task as currently being checked
        t.setRecStack(true);

        // Scan all out edges
        for (TaskEdge e : t.getOutTaskEdges()) {
            Task k = e.getTo();
            // Avoid checking already visited tasks
            if (!k.isVisited()) {
                // If we have not seen this task before in this recursion - we recursively dfs down
                if (!k.getRecStack()) {
                    // We only care about a single cycle - so return immediately if found
                    if (findCycleDFS(k, cycle)) {
                        cycle.push(k);
                        t.setRecStack(false);
                        return true;
                    }
                } else { // Already part of the current stack - thus we saw it before and this must be a loop
                    t.setRecStack(false);
                    cycle.push(k);
                    return true;
                }
            }
        }

        t.setVisited(true);
        t.setRecStack(false);
        return false;
    }

    /**
     * Class for storing the pair of events for a task in the event node graph.
     */
    private class EventPair {
        Event in;
        Event out;

        EventPair(Event in, Event out) {
            this.in = in;
            this.out = out;
        }
    }

    /**
     * A node representing an event in the event node graph.
     */
    class Event implements Node {
        private Task task;
        private boolean in;
        private List<Edge> outEdges = new ArrayList<>();
        private List<Edge> inEdges = new ArrayList<>();

        Event(Task task, boolean in) {
            this.task = task;
            this.in = in;
        }

        public void addOutEdge(Edge edge) {
            outEdges.add(edge);
        }

        public void addInEdge(Edge edge) {
            inEdges.add(edge);
        }

        @Override
        public List<Edge> getOutEdges() {
            return outEdges;
        }

        @Override
        public List<Edge> getInEdges() {
            return inEdges;
        }

        public Task getTask() {
            return task;
        }

        public boolean isIn() {
            return in;
        }
    }

    /**
     * An edge in the event node graph.
     */
    class EventEdge implements Edge {
        private Event from;
        private Event to;
        private int cost;

        EventEdge(Event from, Event to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }

        @Override
        public Node getFrom() {
            return from;
        }

        @Override
        public Node getTo() {
            return to;
        }

        @Override
        public int getCost() {
            return cost;
        }
    }

    /**
     * Storage for calculated completion times.
     */
    class CompletionTimes {
        /**
         * Earliest completion time
         */
        int ec = 0;

        /**
         * Latest completion time
         */
        int lc = Integer.MAX_VALUE;
    }

    /**
     * Calculate completion costs for the graph.
     *
     * @param graph an already topologically sorted graph
     * @return an hash map with the calculated data
     */
    Map<Node, CompletionTimes> calculateCompletionTimes(Graph graph) {
        Map<Node, CompletionTimes> data = new HashMap<>();
        List<Node> nodes = graph.getNodes();

        // Iterate over the event graph from start to end
        // to calculate earliest completion times
        for (Node node : nodes) {
            data.put(node, new CompletionTimes());
            for (Edge edge : node.getInEdges()) {
                int fromCost = data.get(edge.getFrom()).ec;
                if (fromCost + edge.getCost() > data.get(node).ec) {
                    data.get(node).ec = fromCost + edge.getCost();
                }
            }
        }

        // Calculating the latest completion time by iterating
        // Calculating the latest completion time by iterating
        // in the opposite direction
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            for (Edge edge : node.getOutEdges()) {
                int toCost = data.get(edge.getTo()).lc;
                if (toCost - edge.getCost() < data.get(node).lc) {
                    data.get(node).lc = toCost - edge.getCost();
                }
            }
            // If there were no out edges it's an end point. Set lc to ec.
            if (data.get(node).lc == Integer.MAX_VALUE) {
                data.get(node).lc = data.get(node).ec;
            }
        }

        return data;
    }

    /**
     * Topological sort of a generic graph.
     * <p>
     * Based on the Kahn algorithm.
     *
     * @param graph unsorted graph
     * @return sorted graph
     */
    Graph topologicalSort(Graph graph) {
        final List<Node> sorted = new ArrayList<>();
        Queue<Node> startNodes = new LinkedList<>();
        Set<Edge> removed = new HashSet<>();

        for (Node node : graph.getNodes()) {
            if (node.getInEdges().isEmpty()) {
                startNodes.add(node);
            }
        }

        while (!startNodes.isEmpty()) {
            Node node = startNodes.remove();
            sorted.add(node);
            for (Edge out : node.getOutEdges()) {
                removed.add(out);
                boolean incoming = false;
                Node m = out.getTo();
                for (Edge in : m.getInEdges()) {
                    if (!removed.contains(in)) {
                        incoming = true;
                        break;
                    }
                }
                if (!incoming) {
                    startNodes.add(m);
                }
            }
        }

        return new Graph() {
            @Override
            public List<Node> getNodes() {
                return sorted;
            }
        };
    }

    /**
     * Takes a wighted acyclic graph and creates an event node graph.
     *
     * @return an event node graph
     */
    Graph eventNodeGraph() {

        Map<Task, EventPair> events = new HashMap<>();
        final List<Node> nodes = new ArrayList<>();

        // Make an event pair for every task
        for (Task t : tasks.values()) {
            Event from = new Event(t, true);
            Event to = new Event(t, false);
            EventPair eventPair = new EventPair(from, to);
            events.put(t, eventPair);
            EventEdge edge = new EventEdge(eventPair.in, eventPair.out, t.getTime());
            from.addOutEdge(edge);
            to.addInEdge(edge);
            nodes.add(eventPair.in);
            nodes.add(eventPair.out);
        }

        // Make edges for all dependencies
        for (Task t : tasks.values()) {
            for (TaskEdge taskEdge : t.getOutTaskEdges()) {
                // The taskEdge in the original graph becomes a zero cost taskEdge
                // in the event-node-graph to satisfy the dependency.
                Event from = events.get(t).out;
                Event to = events.get(taskEdge.getTo()).in;
                EventEdge edge = new EventEdge(from, to, 0);
                from.addOutEdge(edge);
                to.addInEdge(edge);
            }
        }

        return new Graph() {
            @Override
            public List getNodes() {
                return nodes;
            }
        };

    }

    /**
     * An edge in the original task graph.
     */
    class TaskEdge {
        private Task from;
        private Task to;
        //Flag to see if the edge was visited
        private boolean visited = false;

        public TaskEdge(Task from, Task to) {
            this.from = from;
            this.to = to;
        }

        public Task getFrom() {
            return from;
        }

        public Task getTo() {
            return to;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }
    }


    /**
     * A task - (nodes in the original task graph)
     */
    class Task {
        private int id, time, staff;
        private String name;
        private int earliestStart, latestStart;
        private List<TaskEdge> outTaskEdges = new LinkedList<>();
        private int cntPredecessors;
        private Integer[] predecessors;

        private boolean recStack = false;
        private boolean visited = false;
        private Task parent = null;

        public Task(int id, String name, int time, int staff, Integer[] predeccessors) {
            this.id = id;
            this.name = name;
            this.time = time;
            this.staff = staff;
            this.cntPredecessors = predeccessors.length;
            this.predecessors = predeccessors;
        }

        public boolean getRecStack() {
            return recStack;
        }

        public void setRecStack(boolean recStack) {
            this.recStack = recStack;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public Task getParent() {
            return parent;
        }

        public void setParent(Task parent) {
            this.parent = parent;
        }

        /**
         * Returns a list with out egdes for the tasks
         *
         * @return list with out edges
         */
        public List<TaskEdge> getOutTaskEdges() {
            return outTaskEdges;
        }

        /**
         * Prints information about the task
         *
         * @return line with task's information
         */
        @Override
        public String toString() {
            String str = id + " " + getName() + " " + getTime() + " " + getStaff() + " " +
                    +getNumberOfPredecessors() + " OutEdges: ";
            for (TaskEdge taskEdge : outTaskEdges) {
                str += taskEdge.getTo().getId() + " ";
            }
            return str;
        }

        /**
         * Returns task id
         *
         * @return id
         */
        public int getId() {
            return this.id;
        }

        /**
         * Sets task id
         *
         * @param id
         */
        public void setID(int id) {
            this.id = id;
        }

        /**
         * Returns task's time
         *
         * @return time
         */
        public int getTime() {
            return this.time;
        }

        /**
         * Sets task time
         *
         * @param time
         */
        public void setTime(int time) {
            this.time = time;
        }

        /**
         * Returns task's name
         *
         * @return name
         */
        public String getName() {
            return this.name;
        }

        /**
         * Sets task's name
         *
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Returns manpower requirements for the task
         *
         * @return manpower requirements
         */
        public int getStaff() {
            return this.staff;
        }

        /**
         * Sets manpower requirements for the task
         *
         * @param staff requirements
         */
        public void setStaff(int staff) {
            this.staff = staff;
        }

        /**
         * Returns task's predecessors
         *
         * @return array of predecessors
         */
        public Integer[] getPredececcors() {
            return this.predecessors;
        }

        /**
         * Sets task's predecessors
         *
         * @param pr array of predecessors
         */
        public void setPredecessors(Integer[] pr) {
            this.predecessors = pr;
        }

        /**
         * Sets earliest starting time for the task
         *
         * @param earliestStart earliest starting time
         */
        public void setEarliestStart(int earliestStart) {
            this.earliestStart = earliestStart;
        }

        /**
         * Returns earliest starting time for the task
         *
         * @return earliest starting time
         */
        public int getEarliestStart() {
            return earliestStart;
        }

        /**
         * Sets latest starting time for the task
         *
         * @param latestStart latest starting time
         */
        public void setLatestStart(int latestStart) {
            this.latestStart = latestStart;
        }

        /**
         * Returns latest starting time for the task
         *
         * @return latest starting time
         */
        public int getLatestStart() {
            return latestStart;
        }

        /**
         * Returns number of predecessors for the task
         *
         * @return number of predecessors
         */
        public int getNumberOfPredecessors() {
            return this.cntPredecessors;
        }

        /**
         * Sets number of predecessors for the task
         *
         * @param n number of predecessors
         */
        public void setNumberOfPredecessors(int n) {
            this.cntPredecessors = n;
        }

    }
}
