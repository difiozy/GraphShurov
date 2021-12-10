import java.util.*;

public class GraphSRW_NIR<E extends Comparable<E>> extends Graph {
    public GraphSRW_NIR(Orient o, Balance b)
    {
        super(o, b);
    }

    public Set<E> approximateFindCoating()
    {
        final Random randomIndex = new Random();

        HashMap<E, HashMap<E, Integer>> adjList = new HashMap<>(super.adjacencyList);
        for (E firstKey : adjList.keySet())
        {
            HashMap<E, Integer> temp = new HashMap<>(adjList.get(firstKey));
            adjList.put(firstKey, temp);
        }


        TreeSet<E> result = new TreeSet<>();

        while (adjList.size() > 0)
        {
            E firstVertex = (E) adjList.keySet().toArray()[randomIndex.nextInt(adjList.keySet().size())];
            if (adjList.get(firstVertex).isEmpty())
            {
                deleteVertexFromAdjList(firstVertex, adjList);
                continue;
            }
            E secondVertex = (E) adjList.get(firstVertex).keySet().toArray()[randomIndex.nextInt(adjList.get(firstVertex).keySet().size())];

            result.add(firstVertex);
            result.add(secondVertex);
            deleteVertexFromAdjList(firstVertex, adjList);
            deleteVertexFromAdjList(secondVertex, adjList);
        }

        return result;
    }

    private E getMaxInMapFromValue(Map<E, Integer> m) {
        int maxValue = 0;
        E maxKey = null;

        for (E key : m.keySet())
        {
            if (m.get(key) > maxValue)
            {
                maxValue = m.get(key);
                maxKey = key;
            }
        }
        return maxKey;
    }

    public Set<E> findCoating() {
        HashMap<E, HashMap<E, Integer>> adjList = super.getCopyAdjacencyList();
        LinkedHashSet<E> result = new LinkedHashSet<>();

        TreeMap<E, Integer> count = new TreeMap<>();

        for (E firstKey : adjList.keySet()) {
            if (!count.containsKey(firstKey)) {
                count.put(firstKey, 0);
            }
            count.put(firstKey, adjList.get(firstKey).size());
        }

        E curKey = getMaxInMapFromValue(count);
        while (curKey != null) {
            result.add(curKey);
            deleteVertexFromAdjList(curKey, adjList);
            count.remove(curKey);
            for (E keyDec : adjList.keySet()) {
                count.put(keyDec, adjList.get(keyDec).size());
            }
            curKey = getMaxInMapFromValue(count);

        }

        return result;
    }

    private void deleteVertexFromAdjList(E vertex, HashMap<E, HashMap<E, Integer>> adjList) {
        //Samara == vertex
        for (E firstKey : adjList.keySet()) {
            adjList.get(firstKey).remove(vertex);
        }
        adjList.remove(vertex);
    }

    public static void generateRandomGraph(Graph graph, int countVertex, int maxCountEdge) {
        Random random = new Random();

        maxCountEdge = Math.min(countVertex, maxCountEdge);

        for (int i = 0; i <= countVertex; i++) {
            for (int j = 0; j <= random.nextInt(maxCountEdge + 1); j++) {
                try {
                    graph.addRib(((Integer) random.nextInt(countVertex)).toString(), ((Integer) random.nextInt(countVertex)).toString());
                } catch (Exception e) {

                }

            }
        }
    }

    public static void generateRandomGraphWithCountEdge(Graph graph, int countVertex, int countEdge) {
        Random random = new Random();
        for (int i=0; i<countVertex; i++)
        {
            try {
                graph.addVertex(((Integer)i).toString());
            } catch (Exception e) {}
        }
        for (int i = 0; i < countEdge; i++)
        {
            try {
                graph.addRib(((Integer) random.nextInt(countVertex)).toString(), ((Integer) random.nextInt(countVertex)).toString());
            } catch (Exception ignored) {}
        }
    }

    public static void main(String[] strings) {
        GraphSRW_NIR<String> graph = new GraphSRW_NIR<>(Orient.notOriented, Balance.notWeighted);
        try {
            graph.addVertex('7');
        } catch (Exception e) {
        }
        try {
            graph.addVertex('8');
        } catch (Exception e) {
        }
        try {
            graph.addVertex('0');
        } catch (Exception e) {
        }
        try {
            graph.addRib('1', '2');
        } catch (Exception e) {
        }
        try {
            graph.addRib('1', '3');
        } catch (Exception e) {
        }
        try {
            graph.addRib('2', '3');
        } catch (Exception e) {
        }
        try {
            graph.addRib('3', '5');
        } catch (Exception e) {
        }
        try {
            graph.addRib('3', '4');
        } catch (Exception e) {
        }
        try {
            graph.addRib('4', '6');
        } catch (Exception e) {
        }
        try {
            graph.addRib('5', '6');
        } catch (Exception e) {
        }

        graph.adjacencyList.clear();
        generateRandomGraphWithCountEdge(graph, 20_000, 10_000);

        System.out.println("ANS");
        long firstMethodStart = System.currentTimeMillis();
        Set<String> el = graph.approximateFindCoating();
        long firstMethodEnd = System.currentTimeMillis();
        System.out.println(el.size());

        System.out.println("Time first method");
        System.out.println(firstMethodEnd - firstMethodStart);

        System.out.println("New method");
        long secondMethodStart = System.currentTimeMillis();
        Set<String> el2 = graph.findCoating();
        long secondMethodEnd = System.currentTimeMillis();
        System.out.println(el2.size());

        System.out.println("Time second method");
        System.out.println(secondMethodEnd - secondMethodStart);
    }
}
