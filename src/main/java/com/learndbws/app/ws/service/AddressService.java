package com.learndbws.app.ws.service;

import com.learndbws.app.ws.shared.dto.AddressDTO;
import org.apache.tomcat.jni.Address;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddresses(String userId);
    AddressDTO getAddress(String addressId);
}
