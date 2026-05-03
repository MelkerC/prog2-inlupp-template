package se.su.inlupp;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListGraph <T> implements Graph<T>{

    public Iterator<T> iterator(){

        return null;
    }

    @Override
    public void add(T node) {

    }

    @Override
    public void remove(T node) {

    }

    @Override
    public boolean hasNode(T node) {
        return false; //Temporär return statment
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {

    }

    @Override
    public void disconnect(T node1, T node2) {

    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {

    }

    @Override
    public Set<T> getNodes() {
        return null; //Temporär return statment
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        return null; //Temporär return statment
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        return null; //Temporär return statment
    }
}
