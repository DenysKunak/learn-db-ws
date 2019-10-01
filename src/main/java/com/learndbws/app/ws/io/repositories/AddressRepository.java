package com.learndbws.app.ws.io.repositories;

import com.learndbws.app.ws.io.entity.AddressEntity;
import com.learndbws.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
}
