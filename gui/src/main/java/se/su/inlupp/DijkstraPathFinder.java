package se.su.inlupp;

import java.util.*;

public class DijkstraPathFinder <T> implements PathFinder<T>{
    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {

        List<T> notVisited = new ArrayList<T>(graph.getNodes());

        T current = from;

        for(Edge<T> edge : graph.getEdgesFrom(current)) {

        }


        return null;
    }
}
