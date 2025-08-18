package com.shruti.user_management.Service;

import com.shruti.user_management.DTO.UserDTO;
import com.shruti.user_management.Model.User;
import com.shruti.user_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service //Marks the class as service bean
@Transactional //Makes method run in DB transaction by default
public class UserServiceImpl implements UserService {

  
    private final UserRepository userRepository;

    @Autowired //constructor injection
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //Create User
    @Override
    public  UserDTO createUser(UserDTO userDTO){
        if(userDTO == null) throw new IllegalArgumentException("User Data cannot be null");
        if(userDTO.getEmail() == null || userDTO.getEmail().isBlank()) throw new IllegalArgumentException("Email is required");
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new IllegalArgumentException("Email already in use" + userDTO.getEmail());
        }
        User user = mapToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    //Read All Users -getAllUsers
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
    }

    //Read One user 
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user =  userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("User not found with ID" + id));
            return mapToDTO(user);                
    }
    
    //Update
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO){
        User user =  userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User Not found with Id" + id));

        //Update only provided fields(partial updates dont mess existing data)
        if (userDTO.getName() != null) user.setName(userDTO.getName());

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new IllegalArgumentException("Email already in use: " + userDTO.getEmail());
            }
            user.setEmail(userDTO.getEmail());
        }
    
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(userDTO.getPassword());
        }
        
        User updated =  userRepository.save(user);
        return mapToDTO(updated);
    }

    //Delete
    @Override
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID " + id));
        userRepository.delete(user);
    }
    

    //MAPPING HELPERS
    //Mapping DTO <-> Entity
    private User mapToEntity(UserDTO dto){
        User u = new User();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        return u;
    }

    // Mapping repository <-> DTO
    private UserDTO mapToDTO(User u){
    UserDTO dto = new UserDTO();
    dto.setName(u.getName());
    dto.setEmail(u.getEmail());
    dto.setPassword(u.getPassword());
    return dto;
    }
    

}
