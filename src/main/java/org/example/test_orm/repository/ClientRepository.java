package org.example.test_orm.repository;

import org.example.test_orm.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

}
