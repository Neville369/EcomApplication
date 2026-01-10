package com.nexaro.ecomapplication.service;

import com.nexaro.ecomapplication.dto.AddressDTO;
import com.nexaro.ecomapplication.dto.UserRequest;
import com.nexaro.ecomapplication.dto.UserResponse;
import com.nexaro.ecomapplication.model.Address;
import com.nexaro.ecomapplication.model.User;
import com.nexaro.ecomapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user , userRequest);
        userRepository.save(user);
    }


    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    public boolean updateUser (Long id, UserRequest updatedUserRequest) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }
                ).orElse(false);
    }



    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse Response = new UserResponse();
        Response.setId(user.getId());
        Response.setFirstName(user.getFirstName());
        Response.setLastName(user.getLastName());
        Response.setEmail(user.getEmail());
        Response.setPhone(user.getPhone());
        Response.setRole(user.getRole());
        if (user.getAddress() != null) {
            AddressDTO  AddressDTO = new AddressDTO();
            AddressDTO.setCity(user.getAddress().getCity());
            AddressDTO.setCountry(user.getAddress().getCountry());
            AddressDTO.setStreet(user.getAddress().getStreet());
            AddressDTO.setZipcode(user.getAddress().getZipcode());
            AddressDTO.setState(user.getAddress().getState());
            Response.setAddress(AddressDTO);
       }
        return Response;

    }
}
