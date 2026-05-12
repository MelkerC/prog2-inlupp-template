package se.su.inlupp;

public class TrainRail<T> implements Edge<T>{

    private final T destination;
    private final String name;
    private int weight, time;

    public TrainRail(T destination, String name, int weight, int time){
        this.destination = destination;
        this.name = name;
        this.weight = weight;
        this.time = time;
    }
    @Override
    public T getDestination(){
        return destination;
    }

    @Override
    public int getWeight(){
        return weight;
    }

    public int getTime(){return time;}

    @Override
    public void setWeight(int weight){
        if(weight < 0){
            throw new IllegalArgumentException("weight must be greater than 0");
        }
        this.weight = weight;
    }

    public void setTime(int time){
        if(time < 0){
            throw new IllegalArgumentException("time must be greater than 0");
        }
        this.time = time;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return String.format("%s goes to %s and is %d km long and has a travel time of %d minutes\n", name, destination.toString(), weight, time);
    }
}
