package se.su.inlupp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GraphPath<T> implements Path<T>{

    private final List<Edge<T>> pathEdges =  new LinkedList<>();
    private final List<T> pathNodes = new LinkedList<>();

/*
    public GraphPath(LinkedList<T> pathEdges){
        this.pathNodes.addAll(pathEdges);
    }*/

    public Iterator<Edge<T>> iterator(){

        return null; //här behövs funktionalitet
    }

    @Override
    public T getStart() {
        return pathNodes.getFirst();
    }

    @Override
    public T getEnd() {
        return pathNodes.getLast();
    }

    @Override
    public int getTotalWeight() {
        int totalWeight = 0;

        for(Edge<T> edge: pathEdges){
            totalWeight += edge.getWeight();
        }
        return totalWeight;
    }

    @Override
    public List<Edge<T>> getEdges() {
        return pathEdges;
    }

    @Override
    public List<T> getNodes() {
        return pathNodes;
    }

    public String toString(){
        return pathNodes.toString();
    }
}