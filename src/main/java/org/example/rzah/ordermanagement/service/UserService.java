package org.example.rzah.ordermanagement.service;

import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.RequestCreatedUserDto;
import org.example.rzah.ordermanagement.dto.ResponseUserDto;
import org.example.rzah.ordermanagement.dto.UserPageDto;
import org.example.rzah.ordermanagement.exception.EmailAlreadyExistsException;
import org.example.rzah.ordermanagement.exception.UserNotFoundException;
import org.example.rzah.ordermanagement.mapper.UserMapper;
import org.example.rzah.ordermanagement.mapper.UserPageMapper;
import org.example.rzah.ordermanagement.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final UserPageMapper userPageMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, UserPageMapper userPageMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userPageMapper = userPageMapper;
    }

    @Transactional(readOnly = true)
    public UserPageDto getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<UserEntity> userPage = userRepository.findAll(pageable);

        return userPageMapper.toPageDto(userPage, userMapper);
    }

    @Transactional(readOnly = true)
    public ResponseUserDto getUser(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User " + id + " not found"));
        return userMapper.toDto(userEntity);
    }

    @Transactional
    public ResponseUserDto save(RequestCreatedUserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);

        try {
            UserEntity savedEntity = userRepository.save(userEntity);
            return userMapper.toDto(savedEntity);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }
    }
}
