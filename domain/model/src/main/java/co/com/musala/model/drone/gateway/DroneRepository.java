package co.com.musala.model.drone.gateway;

import co.com.musala.model.drone.Drone;

import java.util.List;

public interface DroneRepository {

    List<Drone> findAllDrones();
    List<Drone> findByStatus(String status);
    Drone saveDrone(Drone drone);




}
