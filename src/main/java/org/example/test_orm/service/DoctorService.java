package org.example.test_orm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.exception.DoctorSaveException;
import org.example.test_orm.exception.DuplicateDoctorException;
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

    public void save(Doctor doctor) {
        try{
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctorRepository.save(doctor);
        }
        catch (DataIntegrityViolationException e){ // Нужно установить constraint unique в таблицу
            throw new DuplicateDoctorException("Логин или почта уже существует");
        }
        catch(Exception e){
            throw new DoctorSaveException("Не удалось сохранить данные");
        }
    }

    public boolean existsByEmail(String email){
        return doctorRepository.findDoctorByEmail(email).isPresent();
    }

    public boolean existsByLogin(String login){
        return doctorRepository.findDoctorByLogin(login).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        return doctorRepository.findDoctorByEmail(emailOrUsername)
                .or(()-> doctorRepository.findDoctorByLogin(emailOrUsername))
                .map(doctor -> new User(
                        doctor.getEmail(),
                        doctor.getPassword(),
                        Set.of(doctor.getRole())
                ))
                .orElseThrow(()->new UsernameNotFoundException("User not found " + emailOrUsername));
    }
}
