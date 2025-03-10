package org.example.test_orm.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.entity.Token;
import org.example.test_orm.exception.DoctorSaveException;
import org.example.test_orm.exception.DuplicateDoctorException;
import org.example.test_orm.repository.DoctorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(Doctor doctor) {
        try {
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctorRepository.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDoctorException("Логин или почта уже существует");
        } catch (Exception e) {
            throw new DoctorSaveException("Не удалось сохранить данные");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Doctor doctor = getDoctor(login);
        return new User(doctor.getLogin(), doctor.getPassword(), Set.of(doctor.getRole()));
    }

    private Doctor getDoctor(String login) {
        Optional<Doctor> optionalDoctor = doctorRepository.findDoctorByLogin(login);
        if (optionalDoctor.isEmpty()) {
            log.warn("Пользователь не найден");
            throw new UsernameNotFoundException("Доктор не найден");
        }
        return optionalDoctor.get();
    }

}
