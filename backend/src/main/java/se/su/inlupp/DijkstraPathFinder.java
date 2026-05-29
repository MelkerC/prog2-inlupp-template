// PROG2 VT2026, Inlämningsuppgift, del 1
// Grupp 070
// Melker Cronmark mecr7998

package se.su.inlupp;

import java.util.*;

public class DijkstraPathFinder <T> implements PathFinder<T>{
    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {
        Map<T, Integer> distance = new HashMap<>();

        Map<T, T> predecessor = new HashMap<>();

        for(T node : graph.getNodes()){
            distance.put(node, Integer.MAX_VALUE);
            predecessor.put(node, null);
        }
        distance.put(from, 0);

        PriorityQueue<T> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        queue.add(from);

        while(!queue.isEmpty()){
            T current = queue.poll();

            if(current.equals(to)){
                break;
            }

            for(Edge<T> edge : graph.getEdgesFrom(current)){
                T neighbor = edge.getDestination();
                int newDistance = distance.get(current) + edge.getWeight();

                if(newDistance < distance.get(neighbor)){
                    distance.put(neighbor, newDistance);
                    predecessor.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if(distance.get(to) == Integer.MAX_VALUE){
            return null;
        }

        LinkedList<T> path = new LinkedList<>();
        for(T at = to; at != null; at = predecessor.get(at)){
            path.add(at);
        }
        Collections.reverse(path);

        LinkedList<Edge<T>> edgePath = new LinkedList<>();

        for(int i = 0; i < path.size() - 1; i++){
            T fromNode = path.get(i);
            T toNode = path.get(i + 1);
            edgePath.add(graph.getEdgeBetween(fromNode, toNode));
        }

        GraphPath<T> newPath = new GraphPath<>(path, edgePath, "Dijkstra");

        return newPath;
    }
}
