package com.example.erp.service;

import com.example.erp.entity.Accreditation;
import com.example.erp.repository.AccreditationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccreditationService {
    
    private final AccreditationRepository accreditationRepository;
    
    public List<Accreditation> getAllAccreditations() {
        return accreditationRepository.findAll();
    }
    
    public Optional<Accreditation> getAccreditationById(Long id) {
        return accreditationRepository.findById(id);
    }
    
    public Accreditation createAccreditation(Accreditation accreditation) {
        return accreditationRepository.save(accreditation);
    }
    
    public Accreditation updateAccreditation(Long id, Accreditation accreditationDetails) {
        Accreditation accreditation = accreditationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Accreditation not found with id: " + id));
        
        if (accreditationDetails.getName() != null) {
            accreditation.setName(accreditationDetails.getName());
        }
        if (accreditationDetails.getDescription() != null) {
            accreditation.setDescription(accreditationDetails.getDescription());
        }
        if (accreditationDetails.getAccreditingBody() != null) {
            accreditation.setAccreditingBody(accreditationDetails.getAccreditingBody());
        }
        if (accreditationDetails.getValidFrom() != null) {
            accreditation.setValidFrom(accreditationDetails.getValidFrom());
        }
        if (accreditationDetails.getValidTo() != null) {
            accreditation.setValidTo(accreditationDetails.getValidTo());
        }
        if (accreditationDetails.getStatus() != null) {
            accreditation.setStatus(accreditationDetails.getStatus());
        }
        
        return accreditationRepository.save(accreditation);
    }
    
    public void deleteAccreditation(Long id) {
        if (!accreditationRepository.existsById(id)) {
            throw new RuntimeException("Accreditation not found with id: " + id);
        }
        accreditationRepository.deleteById(id);
    }
}
