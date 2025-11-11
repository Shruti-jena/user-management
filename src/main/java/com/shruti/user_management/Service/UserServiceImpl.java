package com.shruti.user_management.Service;

import com.shruti.user_management.DTO.UserDTO;
import com.shruti.user_management.Exception.UserNotFoundException;
import com.shruti.user_management.Model.User;
import com.shruti.user_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //Marks the class as service bean
@Transactional //Makes method run in DB transaction by default
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; 
    private final PasswordEncoder passwordEncoder;

    @Autowired //constructor injection
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Create User
    @Override
    public  UserDTO createUser(UserDTO userDTO){
        if(userDTO == null) throw new IllegalArgumentException("User Data cannot be null");
        if(userDTO.getEmail() == null || userDTO.getEmail().isBlank()) throw new IllegalArgumentException("Email is required");
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new IllegalStateException("Email already in use" + userDTO.getEmail());
        }
        User user = mapToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);
        UserDTO responUserDTO = mapToDTO(savedUser);
        responUserDTO.setPassword(null);
        return responUserDTO;
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
                            .orElseThrow(() -> new UserNotFoundException("User not found with ID" + id));
            UserDTO DTO = mapToDTO(user);    
            DTO.setPassword(null);
            return DTO;            
    }
    
    //Update
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO){
        User user =  userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User Not found with Id" + id));

        //Update only provided fields(partial updates dont mess existing data)
        if (userDTO.getName() != null) user.setName(userDTO.getName());

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new IllegalStateException("Email already in use: " + userDTO.getEmail());
            }
            user.setEmail(userDTO.getEmail());
        }
    
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        User updated =  userRepository.save(user);
        UserDTO DTO = mapToDTO(updated);
        DTO.setPassword(null);
        return DTO;
    }

    //Delete
    @Override
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID " + id));
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
