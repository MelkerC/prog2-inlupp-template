package se.su.inlupp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GraphEdge<T> implements Edge<T>{

    private int weight;

    @Override
    public T getDestination(){
        return null; //Prelliminär siffra för kompelering
    }

    @Override
    public int getWeight(){
        return weight;
    }

    @Override
    public void setWeight(int weight){
        if(weight < 0){
            throw new IllegalArgumentException("weight must be greater than 0");
        }
        this.weight = weight;
    }

    @Override
    public String getName(){
        return null;//Prelliminär siffra för kompelering
    }

    @Override
    public String toString(){
        return getName();
    }
}
