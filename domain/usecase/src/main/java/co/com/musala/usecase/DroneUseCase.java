package co.com.musala.usecase;

import co.com.musala.model.drone.Drone;
import co.com.musala.model.drone.gateway.DroneRepository;
import co.com.musala.model.medication.Medication;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class DroneUseCase {


    private final DroneRepository droneRepository;
    private final MedicationUseCase medicationUseCase;
    private final Logger logger = Logger.getLogger(DroneUseCase.class.toString());

    public List<Drone> getAllDrones(){
        return droneRepository.getAllDronesWithMedications();
    }


    public Drone saveDrone(Drone drone){
        if (drone.getSerialNumber().length()>100){
            throw new IllegalArgumentException("The serial number exceeds the limit (100).");
        }
        if (!drone.getModel().toLowerCase().equals("lightweight") && !drone.getModel().toLowerCase().equals("middleweight") && !drone.getModel().toLowerCase().equals("cruiserweight") && !drone.getModel().toLowerCase().equals("heavyweight")) {
            throw new IllegalArgumentException("The model required is lightweight or middleweight or cruiserweight or heavyweight.");
        }
        if (drone.getWeightLimit()>500.0){
            throw new IllegalArgumentException("The weight limit  is (500).");
        }
        if (!drone.getState().toLowerCase().equals("idle") && !drone.getState().toLowerCase().equals("loading") && !drone.getState().toLowerCase().equals("loaded") && !drone.getState().toLowerCase().equals("delivering") && !drone.getState().toLowerCase().equals("delivered") && !drone.getState().toLowerCase().equals("returning")){
            throw new IllegalArgumentException("The state required is IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING.");
        }
        return droneRepository.saveDrone(drone);
    }

    public Drone updateDrone(Drone drone){
            double totalWeight = 0.0;
            for (Medication medication : drone.getMedicationList()) {
                totalWeight += medication.getWeight();
            }
            if(totalWeight>500.0){
                throw new IllegalArgumentException("The total weight of the medications exceeds the weight limit of the drone.");
            }else{
                List<Medication> medicationUpdateList = new ArrayList<>();
                for (Medication medication : drone.getMedicationList()) {
                    Medication medicationUpdate = medicationUseCase.saveMedication(medication);
                    medicationUpdateList.add(medicationUpdate);
                }
                if(totalWeight==500){
                    drone.setState("LOADED");
                }else{
                    drone.setState("LOADING");
                }

                Drone droneUpdate =  this.saveDrone(drone);
                droneUpdate.setMedicationList(medicationUpdateList);
                return droneUpdate;
            }
    }

    public Drone findById(Long idDrone) {
        return droneRepository.findById(idDrone);
    }

    public Drone checkBatteryLevel(Long idDrone) {
        return droneRepository.findById(idDrone);
    }

    public List<Drone> findByAvailability() {
        List<Drone> droneList = this.getAllDrones();
        List<Drone> droneAvailable = new ArrayList<>();
        for(Drone drone : droneList){
            double totalWeight = 0.0;
            for (Medication medication : drone.getMedicationList()) {
                totalWeight += medication.getWeight();
            }
            if(totalWeight<500.0 && (drone.getState().toLowerCase().equals("idle")
                    || drone.getState().toLowerCase().equals("delivered"))
                    || drone.getState().toLowerCase().equals("loading")){
                droneAvailable.add(drone);
            }
        }
      return droneAvailable;
    }
}
