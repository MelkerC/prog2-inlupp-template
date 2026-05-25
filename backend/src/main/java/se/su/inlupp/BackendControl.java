package se.su.inlupp;

import java.io.*;
import java.util.*;

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


    public void saveProgram(File saveFile, Map<String, String> guiCityPlacement, String currantBackgroundName) {
        String saveInfo = citiesGraph.getNodes().size() + "\n";

        for(City city : citiesGraph.getNodes()){
            saveInfo += city.getName() + "\n";
            saveInfo += city.getVisited() + "\n";
            saveInfo += citiesGraph.getEdgesFrom(city).size() + "\n";
            for(Edge edge : citiesGraph.getEdgesFrom(city)){
                saveInfo += edge.getDestination() + "\n";
                saveInfo += edge.getName() + "\n";
                saveInfo += edge.getWeight() + "\n";
            }
        }

        saveInfo += guiCityPlacement.size() + "\n";
        for(String guiCity : guiCityPlacement.keySet()){
            saveInfo += guiCity + "\n";
            saveInfo += guiCityPlacement.get(guiCity) + "\n";
        }

        saveInfo += pathLibrary.getAllPaths().size() + "\n";
        for(GraphPath<City> path : pathLibrary.getAllPaths().values()){
            saveInfo += path.getStart() + "\n";
            saveInfo += path.getEnd() + "\n";
            saveInfo += path.getAlgortithm() + "\n";
        }

        saveInfo += currantBackgroundName + "\n";

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            writer.write(saveInfo);
            writer.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadProgram(){

    }

    public boolean addCity(String cityName){
        if(citiesGraph.getCityNames().contains(cityName)){
            return false;
        }
        citiesGraph.addCityName(cityName);
        citiesGraph.add(new City(cityName, false));
        return true;
    }

    public boolean removeCity(String cityName){
        if(!citiesGraph.getCityNames().contains(cityName)){
            System.out.println(citiesGraph.getCityNames());
            return false;
        }

        for(City c : citiesGraph.getNodes()){
            if(c.getName().equals(cityName)){
                citiesGraph.remove(c);
                break;
            }
        }

        citiesGraph.removeCityName(cityName);
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
        try{
            citiesGraph.disconnect(getCity(city1), getCity(city2));
        }catch(NoSuchElementException | IllegalStateException e){
            return false;
        }

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

    public boolean hasNode(String cityName){
        return citiesGraph.getNodes().contains(getCity(cityName));
    }

    public void removePath(GraphPath<City> path){
        pathLibrary.removePath(path.toString());
    }

    public List<String> updateAllPaths(){
        List<GraphPath<City>> removedPaths = new ArrayList<>();
        List<String> returnNames = new ArrayList<>();

        for(GraphPath<City> path : pathLibrary.getAllPaths().values()){
            if(!updatePath(path, path.getAlgorithmIndex())){
                removedPaths.add(path);
                returnNames.add(path.toString());

            }
        }

        for(GraphPath<City> path : removedPaths){
            pathLibrary.removePath(path.toString());
        }

        return returnNames;
    }

    public boolean updatePath(GraphPath<City> path, int algoritihm){
        if(path.getStart() == null || path.getEnd() == null){return false;}
        City from = path.getStart();
        City to = path.getEnd();
        GraphPath<City> updatedPath;

        try{
            updatedPath = (GraphPath<City>) pathFinders.get(algoritihm).findPath(citiesGraph, getCity(from.getName()), getCity(to.getName()));
        }catch(NullPointerException e){
            return false;
        }

        if(updatedPath == null){
            return false;
        }
        pathLibrary.removePath(path.toString());
        pathLibrary.addPath(updatedPath.toString(), updatedPath);
        return true;
    }

    public List<String> getEdgesFrom(String cityName){
        Collection<Edge<City>> temp  = citiesGraph.getEdgesFrom(getCity(cityName));
        List<String> edgeNames = new ArrayList<>();
        for(Edge<City> e : temp){
            edgeNames.add(e.getName());
        }
        return edgeNames;
    }

    public City getCity(String cityName){
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

    public String getEdgeNameBetween(City city1, City city2){
        return citiesGraph.getEdgeBetween(city1, city2).getName();
    }
}
