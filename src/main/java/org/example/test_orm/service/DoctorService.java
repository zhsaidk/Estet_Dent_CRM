package org.example.test_orm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.repository.DoctorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorService implements UserDetailsService {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(Doctor doctor) {
        try{
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctorRepository.save(doctor);
        }
        catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email or login already exists");
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save doctor");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return doctorRepository.findDoctorByEmail(email)
                .map(doctor -> new User(
                        doctor.getEmail(),
                        doctor.getPassword(),
                        Set.of(doctor.getRole())
                ))
                .orElseThrow(()->new UsernameNotFoundException(email));
    }
}
