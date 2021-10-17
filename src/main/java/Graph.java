
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;

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
        return new HashMap<>(adjacencyList);
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
        if (!oriented.getOrient()) {
            throw new IllegalClassFormatException("Граф не ориентированный!");
        }

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
    public Graph<E> orientGraphWithMirrorEdge()
    {
        Graph<E> curGraph = new Graph<>(oriented,balanced);
        HashMap<E, HashMap<E, Integer>> curAdjList = getAdjacencyList();

        for (E firstKey: curAdjList.keySet())
        {
            for(E secondKey: curAdjList.get(firstKey).keySet())
            {
                if (curAdjList.get(secondKey).containsKey(firstKey))
                {
                    try {
                        curGraph.addRib(firstKey,secondKey,curAdjList.get(firstKey).get(secondKey));
                    } catch (Exception ignored) {}
                    try {
                        curGraph.addRib(secondKey,firstKey,curAdjList.get(secondKey).get(firstKey));
                    } catch (Exception ignored) {}
                }
            }
        }
        return curGraph;

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

