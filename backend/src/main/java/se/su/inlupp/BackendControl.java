package se.su.inlupp;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BackendControl {

    //Memory data
    private final ListGraph<City> citiesGraph = new ListGraph<>();
    private final PathLibrary<City> pathLibrary = new PathLibrary<>();
    private FileReader fileReader;
    private FileWriter fileWriter;

    //Pathfinders
    private final PathFinder<City> pathFinderBFS = new BFSPathFinder<>();
    private final PathFinder<City> pathFinderDFS = new DFSPathFinder<>();
    private final PathFinder<City> pathFinderDijkstra = new DijkstraPathFinder<>();
    private final ArrayList<PathFinder<City>> pathFinders = new ArrayList<>(List.of(pathFinderDijkstra, pathFinderBFS, pathFinderDFS));


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

    public boolean createPath(String city1, String city2, int algoritihm){
        GraphPath<City> newPath = (GraphPath<City>) pathFinders.get(algoritihm).findPath(citiesGraph, getCity(city1), getCity(city2));

        if(newPath == null){
            return false;
        }

        pathLibrary.addPath(newPath.toString(), newPath);
        return true;
    }

    public void removePath(GraphPath<City> path){
        pathLibrary.removePath(path.toString());
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

    public GraphPath<City> getPathByName(String pathName){
        return pathLibrary.getPath(pathName);
    }
}
