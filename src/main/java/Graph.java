
import com.sun.source.tree.ReturnTree;
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;

public class Graph<E extends Comparable<E>> implements Serializable {

    protected HashMap<E, HashMap<E, Integer>> adjacencyList;
    protected Orient oriented = Orient.notOriented;
    protected Balance balanced = Balance.weighted;

    Graph() {
        adjacencyList = new HashMap<>();
    }

    Graph(Orient orient, Balance balance) {
        adjacencyList = new HashMap<>();
        oriented = orient;
        balanced = balance;
    }

    Graph(@NotNull File file) {
        deserialize(file);
    }

    Graph(@NotNull Graph<E> graph) {
        adjacencyList = graph.getCopyAdjacencyList();
        oriented = graph.getOriented();
        balanced = graph.getBalanced();
    }

    Graph(HashMap<E, HashMap<E, Integer>> copy) {
        adjacencyList = (HashMap<E, HashMap<E, Integer>>) copy.clone();
    }

    Graph(HashMap<E, HashMap<E, Integer>> copy, Orient orient) {
        adjacencyList = copy;
        oriented = orient;
    }

    public HashMap<E, HashMap<E, Integer>> getCopyAdjacencyList() {
        HashMap<E, HashMap<E, Integer>> adjList = new HashMap<>(adjacencyList);
        for (E firstKey : adjList.keySet()) {
            HashMap<E, Integer> temp = new HashMap<>(adjList.get(firstKey));
            adjList.put(firstKey, temp);
        }
        return adjList;
    }

    private HashMap<E, HashMap<E, Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public Orient getOriented() {
        return oriented;
    }

    public Balance getBalanced() {
        return balanced;
    }

    public void addVertex(E vertex) throws Exception {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new HashMap<>());
        } else {
            throw new Exception("This vertex already have");
        }
    }

    public void addRib(E firstVertex, E secondVertex, Integer length) throws Exception {
        boolean have_first = adjacencyList.containsKey(firstVertex);
        boolean have_second = adjacencyList.containsKey(secondVertex);

        if (!have_first) {
            adjacencyList.put(firstVertex, new HashMap<>());
        }
        if (!have_second) {
            adjacencyList.put(secondVertex, new HashMap<>());
        }
        String exp = "";
        if (have_first && have_second)

            if (adjacencyList.get(firstVertex).containsKey(secondVertex))
                exp += "The edge already exists. It has been updated | ";
        adjacencyList.get(firstVertex).put(secondVertex, length);

        if (!oriented.getOrient()) {
            adjacencyList.get(secondVertex).put(firstVertex, length);
        }


        if (!have_first)
            exp += "Was added first vertex " + firstVertex.toString() + " | ";
        if (!have_second)
            exp += "Was added second vertex " + secondVertex.toString();
        if (!exp.equals("")) {
            throw new Exception(exp + "\n");
        }
    }

    public void addRib(E firstVertex, E secondVertex) throws Exception {
        addRib(firstVertex, secondVertex, 0);

    }

    public void deleteVertex(E vertex) throws Exception {
        //Samara == vertex
        if (!adjacencyList.containsKey(vertex))
            throw new Exception("Vertex does not exist");
        for (E firstKey : adjacencyList.keySet()) {
            adjacencyList.get(firstKey).remove(vertex);
        }
        adjacencyList.remove(vertex);
    }

    public void deleteRib(E firstVertex, E secondVertex) throws Exception {
        if (adjacencyList.containsKey(firstVertex) && adjacencyList.containsKey(secondVertex)) {
            if (adjacencyList.get(firstVertex).containsKey(secondVertex))
                adjacencyList.get(firstVertex).remove(secondVertex);
            else throw new Exception("Edge does not exist");
        } else {
            throw new Exception("Vertex is not exist");
        }

        if (oriented == Orient.notOriented) {
            if (adjacencyList.containsKey(secondVertex)) {
                adjacencyList.get(secondVertex).remove(firstVertex);
            }
        }
    }

    public void serialize(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deserialize(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Graph p = (Graph) ois.readObject();
            this.adjacencyList = p.getAdjacencyList();
            this.oriented = p.getOriented();
            this.balanced = p.getBalanced();
        } catch (IOException ex) {
            this.adjacencyList = new HashMap<>();
            System.out.println("IOException, was created  empty adjacencyList");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    //Вернуть полустепень исхода данной вершины орграфа.
    //№2
    public int halfPowerOut(E vertex) throws Exception {


        if (adjacencyList.containsKey(vertex)) {
            return adjacencyList.get(vertex).size();
        } else {
            throw new IllegalArgumentException("Вершины не существует");
        }
    }

    //Определить, существует ли вершина, в которую есть дуга как из вершины u,
    // так и из вершины v. Вывести такую вершину.
    //№19
    public boolean isFromUAndFromV(E U, E V) throws Exception {
        if (adjacencyList.containsKey(U) && adjacencyList.containsKey(V)) {
            for (E first : adjacencyList.get(U).keySet()) {
                if (adjacencyList.get(V).containsKey(first)) {
                    System.out.println(first);
                    return true;
                }
            }
            return false;
        } else {
            throw new Exception("Vertex does not exist");
        }
    }

    //Построить граф, полученный удалением дуг данного орграфа,
    // не имеющих противоположно направленных парных дуг.
    //2b
    //№ 14
    public Graph<E> orientGraphWithMirrorEdge() {
        Graph<E> curGraph = new Graph<>(oriented, balanced);
        HashMap<E, HashMap<E, Integer>> curAdjList = getAdjacencyList();

        for (E firstKey : curAdjList.keySet()) {
            try {
                curGraph.addVertex(firstKey);
            } catch (Exception e) {
            }
            for (E secondKey : curAdjList.get(firstKey).keySet()) {
                if (curAdjList.get(secondKey).containsKey(firstKey)) {
                    try {
                        curGraph.addRib(firstKey, secondKey, curAdjList.get(firstKey).get(secondKey));
                    } catch (Exception ignored) {
                    }
                    try {
                        curGraph.addRib(secondKey, firstKey, curAdjList.get(secondKey).get(firstKey));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return curGraph;

    }

    //Выяснить, является ли орграф сильно связным. №12 II
    //Используем обход в глубину
    public boolean isStrongConnect() {

        for (E startVertex : adjacencyList.keySet()) {
            Stack<E> stack = new Stack<>();
            stack.push(startVertex);

            HashMap<E, Boolean> used = new HashMap<>();
            for (E el : adjacencyList.keySet()) used.put(el, false);

            while (!stack.isEmpty()) {
                E cur = stack.pop();
                if (used.get(cur)) continue;
                used.put(cur, true);
                for (E neighbor : adjacencyList.get(cur).keySet()) {
                    if (!used.get(neighbor)) {
                        stack.push(neighbor);
                    }
                }

            }
            for (E key : used.keySet()) {
                if (!used.get(key))
                    return false;
            }
        }
        return true;
    }


    //Вывести все вершины, длины кратчайших (по числу дуг)
    // путей от которых до всех остальных не превосходят k. №34 II
    // Использовать обход в ширину
    //Complete
    Map<E, Boolean> used = new HashMap<>();

    public List<E> allWayLowerThenK(int k) {
        List<E> answer = new LinkedList<>();
        for (E startVertex : adjacencyList.keySet()) {
            for (E vertex : adjacencyList.keySet())
                used.put(vertex, false);
            used.put(startVertex, true);
            int res = maxDepth(startVertex);
            boolean allVisited = true;
            //System.out.println(res);
            for (E key : used.keySet()) {
                if (!used.get(key)) {
                    allVisited = false;
                    break;
                }
            }
            if (res <= k && allVisited) {
                answer.add(startVertex);
            }
        }
        return answer;
    }

    private int maxDepth(E cur) {
        int curDeep = 0;
        boolean have = false;
        LinkedList<E> queue = new LinkedList<>();
        queue.add(cur);
        while (!queue.isEmpty()) {
            E temp = queue.pop();
            used.put(temp, true);
            for (E vertex : adjacencyList.get(temp).keySet()) {
                if (!used.get(vertex)) {
                    have = true;
                    queue.add(vertex);
                }
            }
            if (have) curDeep++;
            have = false;
        }
        return curDeep;
    }

    //для сравнения строк как вершин и весов между ними
    private class StringComparator implements Comparator<String> {
        private HashMap<E, HashMap<E, Integer>> adj;

        public StringComparator(Graph graph) {
            this.adj = graph.getCopyAdjacencyList();
        }

        public int compare(String obj1, String obj2) {
            Integer w1;
            Integer w2;

            w1 = adj.get(obj1.split("#")[0]).get(obj1.split("#")[1]);
            w2 = adj.get(obj2.split("#")[0]).get(obj2.split("#")[1]);
            if (w1 == w2) {
                return 0;
            }
            if (w1 == null) {
                return -1;
            }
            if (w2 == null) {
                return 1;
            }
            return w1.compareTo(w2);
        }
    }

    //Каркас методом Краскала
    //Exception : Если граф ориентированный
    public List<String> kruskal() throws Exception {
        if (oriented == Orient.oriented) {
            throw new Exception("Graph is directed");
        }
        //Воозможно использование и на не взвешенном графе
        HashSet<String> used = new HashSet<>();
        List<String> edged = new ArrayList<>();
        List<String> ans = new LinkedList<>();
        for (E firstKey : adjacencyList.keySet()) {
            for (E secondKey : adjacencyList.get(firstKey).keySet()) {
                if (used.contains(firstKey) && used.contains(secondKey)) {
                    continue;
                }
                edged.add(firstKey.toString() + "#" + secondKey.toString());

            }
        }

        used.clear();
        edged.sort(new StringComparator(this));
        for (String edge : edged) {
            String[] ed = edge.split("#");
            if (!used.contains(ed[0]) || !used.contains(ed[1])) {
                ans.add(edge);
                used.add(ed[0]);
                used.add(ed[1]);
            }
        }
        return ans;
    }

    public Map<E, Integer> dijkstra(E u) {
        System.out.println(adjacencyList.get(u));
        Set<E> used = new HashSet<>();
        Map<E, Integer> distant = new HashMap();

        for (E key : this.adjacencyList.keySet()) {
            if (key.equals(u)) {
                distant.put(u, 0);
            } else {
                distant.put(key, Integer.MAX_VALUE);
            }
        }

        while (used.size() != this.adjacencyList.size()) {
            E curVertex = null;
            Integer min_val = Integer.MAX_VALUE;
            for (E key : distant.keySet()) {
                if (used.contains(key))
                    continue;

                if (min_val > distant.get(key)) {
                    curVertex = key;
                    min_val = distant.get(key);
                }
            }

            if (curVertex == null)
                break;
            used.add(curVertex);
            for (E key : this.adjacencyList.get(curVertex).keySet()) {
                Integer res = distant.get(curVertex) + this.adjacencyList.get(curVertex).get(key);
                if (distant.get(key) > res) {
                    distant.put(key, res);
                }
            }
        }


        return distant;
    }

    /*
    Task IV a. Num 1.
    Определить, существует ли путь длиной не более L между двумя заданными вершинами графа.
    Решено с использованием алгоритма Дейкстры
     */
    public boolean isMinWayUAndWLessThenL(E u, E w, Integer L) {
        Map<E, Integer> allMinWayFromU = dijkstra(u);

        System.out.println(this);
        System.out.println(allMinWayFromU);
        return allMinWayFromU.get(w) < L;
    }


    private Map<E, Integer> bellmanFord(E u) throws Exception {
        Map<E, Integer> dist = new HashMap<>();
        for (E key : adjacencyList.keySet()) {
            dist.put(key, Integer.MAX_VALUE);
        }
        dist.put(u, 0);

        for (int i = 0; i < adjacencyList.size(); i++) {
            for (E firstKey : adjacencyList.keySet()) {
                for (E secondKey : adjacencyList.get(firstKey).keySet()) {
                    if (dist.get(firstKey) != Integer.MAX_VALUE & dist.get(firstKey)
                                                                  + adjacencyList.get(firstKey).get(secondKey)
                                                                  < dist.get(secondKey)) {
                        dist.put(secondKey, dist.get(firstKey) + adjacencyList.get(firstKey).get(secondKey));
                    }
                }
            }
        }

        for (E firstKey : adjacencyList.keySet()) {
            for (E secondKey : adjacencyList.get(firstKey).keySet()) {
                if (dist.get(firstKey) != Integer.MAX_VALUE & dist.get(firstKey)
                                                              + adjacencyList.get(firstKey).get(secondKey)
                                                              < dist.get(secondKey)) {
                    throw new Exception("Graph contains negative weight cycle");
                }
            }
        }

        return dist;
    }

    /*
    Task IV b. Num 14
    Вывести все кратчайшие пути из вершины u.
     */
    public Map<E, Integer> allShortestPathsFromU(E u) {
        Map<E, Integer> ans = new HashMap<>();
        try {
            ans = bellmanFord(u);
            for (E key : ans.keySet()) {
                if (ans.get(key) == Integer.MAX_VALUE)
                    ans.put(key, -1);
            }
        } catch (Exception ignored) {
        } finally {
            return ans;
        }
    }

    /*
    Task IV c. Num 17
    Вывести кратчайшие пути для всех пар вершин.
    В графе могут быть циклы отрицательного веса.
    Флойда
     */
    private HashMap<E, HashMap<E, Integer>> floyd()
    {
        HashMap<E, HashMap<E, Integer>> ans = getCopyAdjacencyList();

        for (E firstKey: ans.keySet())
        {
            for (E secondKey : ans.keySet())
            {
                ans.get(firstKey).putIfAbsent(secondKey, Integer.MAX_VALUE / 2);
                ans.get(secondKey).putIfAbsent(firstKey, Integer.MAX_VALUE / 2);
            }
        }
        for (E key : ans.keySet()) {
            ans.get(key).put(key, 0);
        }

        for (E k : ans.keySet()) {
            for (E i : ans.keySet()) {
                for (E j : ans.keySet()) {
                    Integer el = Math.min(ans.get(i).get(j), ans.get(i).get(k) + ans.get(k).get(j));
                    ans.get(i).put(j, el);
                }
            }
        }


        return ans;
    }
    public HashMap<E, HashMap<E, Integer>> allShortestPath() {

        HashMap<E, HashMap<E,Integer>> ans = new HashMap<>();
        try {
            ans = floyd();
        }
        catch (Exception e)
        {
            return ans;
        }

        for (E key : ans.keySet())
        {
            for (E secKey : ans.get(key).keySet())
            {
                if (ans.get(key).get(secKey) == Integer.MAX_VALUE/2)
                {
                    ans.get(key).put(secKey, null);
                }
            }
        }
        return  ans;
    }

    public static void main(String[] strings) {
        Graph<String> graph = new Graph<>(Orient.oriented, Balance.weighted);
        try {
            graph.addRib("1", "2", 20);
        } catch (Exception e) {

        }
        try {
            graph.addRib("1", "3", 5);
        } catch (Exception e) {

        }
        try {
            graph.addRib("3", "2", 10);
        } catch (Exception e) {

        }


        System.out.println(graph.allShortestPath());

    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (E firstKey : adjacencyList.keySet()) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(firstKey.toString()).append(" --- ");

            for (E secondKey : adjacencyList.get(firstKey).keySet()) {
                stringBuilder1.append(secondKey.toString());
                if (balanced == Balance.weighted) {
                    stringBuilder1.append("(").append(adjacencyList.get(firstKey).get(secondKey).toString()).append(")");
                }
                stringBuilder1.append(", ");
            }
            stringBuilder.append(stringBuilder1.toString()).append('\n');
        }

        return stringBuilder.toString();
    }
}

