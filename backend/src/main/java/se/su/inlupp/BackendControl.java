package se.su.inlupp;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BackendControl {

    //Memory data
    private final ListGraph<City> citiesGraph = new ListGraph<>();
    private final PathLibrary<TrainRail<City>> pathLibrary = new PathLibrary<>();
    private FileReader fileReader;
    private FileWriter fileWriter;

    //Pathfinders
    private final PathFinder<City> pathFinderBFS = new BFSPathFinder<>();
    private final PathFinder<City> pathFinderDFS = new DFSPathFinder<>();
    private final PathFinder<City> pathFinderDijkstra = new DijkstraPathFinder<>();

    public boolean addCity(String cityName){
        if(citiesGraph.getCityNames().contains(cityName)){
            return false;
        }
        citiesGraph.addCityName(cityName);
        citiesGraph.add(new City(cityName, false));
        return true;
    }

    public boolean removeCity(String cityName){
        if(citiesGraph.getCityNames().contains(cityName)){
            return false;
        }

        citiesGraph.removeCityName(cityName);

        for(City c : citiesGraph.getNodes()){
            if(c.getName().equals(cityName)){citiesGraph.remove(c);}
        }
        return true;
    }

    public boolean connectCities(String city1, String city2, String rail, int weight){

        try{
            citiesGraph.connect(getCity(city1), getCity(city2), rail, weight);
        }catch(NoSuchElementException | IllegalStateException | IllegalArgumentException e){
            return false;
        }
        return true;
    }

    public boolean disConnectCities(String city1, String city2){
        return true;
    }

    public boolean createPath(String city1, String city2){
        return true;
    }

    private City getCity(String cityName){
        for(City c : citiesGraph.getNodes()){
            if(c.getName().equals(cityName)){return c;}
        }
        return new City(cityName, false);
    }

    public List<String> getPaths(){
        return new ArrayList<>(pathLibrary.getAllPaths().keySet());
    }

    private String addConnection(City city1, City city2, int weight){
        return null;
    }
}
