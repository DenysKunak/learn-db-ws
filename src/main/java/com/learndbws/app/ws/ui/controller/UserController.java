package com.learndbws.app.ws.ui.controller;

import com.learndbws.app.ws.service.AddressService;
import com.learndbws.app.ws.shared.dto.AddressDTO;
import com.learndbws.app.ws.ui.model.response.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.learndbws.app.ws.exceptions.UserServiceException;
import com.learndbws.app.ws.service.UserService;
import com.learndbws.app.ws.shared.dto.UserDto;
import com.learndbws.app.ws.ui.model.request.UserDetailsRequestModel;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@Autowired
	AddressService addressesService;

	@GetMapping(path="/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		//UserDto userDto = userService.getUserByUserId(id);
		//BeanUtils.copyProperties(userDto, returnValue);
		UserDto userDto = userService.getUserByUserId(id);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);

		return returnValue;
	}

	@PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto =  modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		return returnValue;
	}

	@PutMapping(path="/{id}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createdUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;
	}

	@DeleteMapping(path="/{id}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel deleteUSer(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0")int page,
								   @RequestParam(value = "limit", defaultValue = "25")int limit){
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page,limit);

		for (UserDto userDto : users){
			/*UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto,userModel);
			returnValue.add(userModel);*/

			UserRest userModel = new UserRest();
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.map(userDto,userModel);
			returnValue.add(userModel);
		}

		return returnValue;
	}

	@GetMapping(path="/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<AddressesRest> getUserAddresses(@PathVariable String id) {

		List<AddressesRest> addressesListRestModel = new ArrayList<>();

		//UserDto userDto = userService.getUserByUserId(id);
		//BeanUtils.copyProperties(userDto, returnValue);
		List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

		if (addressesDTO != null && !addressesDTO.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {
			}.getType();
			ModelMapper modelMapper = new ModelMapper();
			addressesListRestModel = modelMapper.map(addressesDTO, listType);

			for(AddressesRest addressRest :addressesListRestModel){
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
				addressRest.add(addressLink);
				Link userLink = linkTo(UserController.class).slash(id).withRel("user");
				addressRest.add(userLink);
			}

		}

		return addressesListRestModel;
	}

	@GetMapping(path="/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public AddressesRest getUserAddress(@PathVariable String userId , @PathVariable String addressId) {

		AddressDTO addressesDto = addressService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();

		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");

		AddressesRest addressesRestModel = modelMapper.map(addressesDto, AddressesRest.class);

		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);

		return addressesRestModel;
	}

}
