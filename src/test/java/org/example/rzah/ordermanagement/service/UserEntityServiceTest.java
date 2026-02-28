package org.example.rzah.ordermanagement.service;

import org.example.rzah.ordermanagement.DataUtils;
import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.RequestCreatedUserDto;
import org.example.rzah.ordermanagement.dto.ResponseUserDto;
import org.example.rzah.ordermanagement.dto.UserPageDto;
import org.example.rzah.ordermanagement.exception.EmailAlreadyExistsException;
import org.example.rzah.ordermanagement.exception.UserNotFoundException;
import org.example.rzah.ordermanagement.mapper.UserMapper;
import org.example.rzah.ordermanagement.mapper.UserPageMapper;
import org.example.rzah.ordermanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserPageMapper userPageMapper;

    @InjectMocks
    private UserService userService;

    // ============= Тесты для getUsers() =============
    @Test
    void getUsers_shouldReturnUserPageDto() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        List<UserEntity> userEntities = List.of(
                DataUtils.getJohnDoePersisted(),
                DataUtils.getMikeSmithPersisted()
        );

        List<ResponseUserDto> expectedResponseUserDto = List.of(
                DataUtils.getJohnDoeResponse(),
                DataUtils.getMikeSmithResponse()
        );

        Page<UserEntity> userPage = new PageImpl<>(userEntities, pageable, 1);
        UserPageDto expectedPageDto = new UserPageDto();
        expectedPageDto.setUsers(expectedResponseUserDto);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userPageMapper.toPageDto(userPage, userMapper)).thenReturn(expectedPageDto);

        UserPageDto result = userService.getUsers(page, size);

        assertThat(result).isEqualTo(expectedPageDto);
        verify(userRepository).findAll(pageable);
        verify(userPageMapper).toPageDto(userPage, userMapper);
        verifyNoMoreInteractions(userRepository, userPageMapper);
        verifyNoInteractions(userMapper);
    }

    // ============== Тесты для getUser() ==============
    @Test
    void getUser_WhenUserExists_ShouldReturnResponseUserDto() {

        UserEntity userEntity = DataUtils.getJohnDoePersisted();
        ResponseUserDto responseUserDto = DataUtils.getJohnDoeResponse();

        Long userId = userEntity.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(responseUserDto);

        ResponseUserDto result = userService.getUser(userId);

        assertThat(result).isEqualTo(responseUserDto);
        assertThat(result.getId()).isEqualTo(responseUserDto.getId());
        assertThat(result.getName()).isEqualTo(responseUserDto.getName());
        assertThat(result.getEmail()).isEqualTo(responseUserDto.getEmail());

        verify(userRepository).findById(userId);
        verify(userMapper).toDto(userEntity);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    void getUsers_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() {
        long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User " + userId + " not found");

        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void save_WhenEmailIsUnique_ShouldSaveAndReturnResponseUserDto() {
        RequestCreatedUserDto requestCreatedUserDto = DataUtils.getJohnDoeRequestCreated();
        ResponseUserDto responseUserDto = DataUtils.getJohnDoeResponse();
        UserEntity userEntity = DataUtils.getJohnDoeTransient();
        UserEntity savedUserEntity = DataUtils.getJohnDoePersisted();

        when(userMapper.toEntity(requestCreatedUserDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.toDto(savedUserEntity)).thenReturn(responseUserDto);

        ResponseUserDto result = userService.save(requestCreatedUserDto);

        assertThat(result).isEqualTo(responseUserDto);
        verify(userMapper).toEntity(requestCreatedUserDto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toDto(savedUserEntity);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    void save_whenEmailAlreadyExists_ShouldThrowEmailAlreadyExistsException() {
        RequestCreatedUserDto requestCreatedUserDto = DataUtils.getJohnDoeRequestCreated();
        UserEntity userEntity = DataUtils.getJohnDoeTransient();

        when(userMapper.toEntity(requestCreatedUserDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.save(requestCreatedUserDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("User with email " + requestCreatedUserDto.getEmail() + " already exists");

        verify(userMapper).toEntity(requestCreatedUserDto);
        verify(userRepository).save(userEntity);
        verifyNoMoreInteractions(userRepository);
        verify(userMapper, never()).toDto(any());
    }
}