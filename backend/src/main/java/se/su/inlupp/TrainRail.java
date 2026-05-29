// PROG2 VT2026, Inlämningsuppgift, del 2
// Grupp 070
// Melker Cronmark mecr7998

package se.su.inlupp;

public class TrainRail<T> implements Edge<T>{
    private final T destination;
    private final String name;
    private int weight;

    public TrainRail(T destination, String name, int weight){
        this.destination = destination;
        this.name = name;
        this.weight = weight;
    }
    @Override
    public T getDestination(){
        return destination;
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
        return name;
    }

    @Override
    public String toString(){
        return String.format("till %s med %s tar %d", destination, name, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof TrainRail<?> other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
