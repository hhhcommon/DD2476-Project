2
https://raw.githubusercontent.com/ELATTARIYassine/Angular-SpringBoot-cinema-management/master/backend/src/main/java/com/shanks/cinema/dao/VilleRepository.java
package com.shanks.cinema.dao;

import com.shanks.cinema.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface VilleRepository extends JpaRepository<Ville, Long> {
}
