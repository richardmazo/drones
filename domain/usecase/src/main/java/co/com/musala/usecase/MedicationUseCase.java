package co.com.musala.usecase;

import co.com.musala.model.medication.Medication;
import co.com.musala.model.medication.gateway.MedicationRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MedicationUseCase {

    private final MedicationRepository medicationRepository;

    public List<Medication> getAllMedications(){
        return medicationRepository.findAllMedications();
    }

    public List<Medication> getMedicationsByIdDrone(Long idDrone){
        return medicationRepository.findByIdDrone(idDrone);
    }

    public Medication saveMedication(Medication medication){
        return medicationRepository.saveMedication(medication);
    }
}
