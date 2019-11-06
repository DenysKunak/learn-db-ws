package com.learndbws.app.ws.service.impl;

import com.learndbws.app.ws.io.entity.UserEntity;
import com.learndbws.app.ws.io.repositories.UserRepository;
import com.learndbws.app.ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest extends Object {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    void testGetUser() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserId("qwerty1234");
        userEntity.setFirstName("Denys");
        userEntity.setEncryptedPassword("qwerty1234");

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("example@crap.com");

        assertNotNull(userDto);
        assertEquals("Denys",userDto.getFirstName());
    }

    @Test
    void userNotFoundException_GetUser(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,

                ()->{
            userService.getUser("example@crap.com");
        }
                );

    }
}