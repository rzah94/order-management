package org.example.rzah.ordermanagement.service;

import org.example.rzah.ordermanagement.DataUtils;
import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.CreatedUserRequestDto;
import org.example.rzah.ordermanagement.dto.UserPageResponseDto;
import org.example.rzah.ordermanagement.dto.UserResponseDto;
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

        List<UserResponseDto> expectedUserResponseDto = List.of(
                DataUtils.getJohnDoeResponse(),
                DataUtils.getMikeSmithResponse()
        );

        Page<UserEntity> userPage = new PageImpl<>(userEntities, pageable, 1);
        UserPageResponseDto expectedPageDto = new UserPageResponseDto();
        expectedPageDto.setUsers(expectedUserResponseDto);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userPageMapper.toPageDto(userPage, userMapper)).thenReturn(expectedPageDto);

        UserPageResponseDto result = userService.getUsers(page, size);

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
        UserResponseDto userResponseDto = DataUtils.getJohnDoeResponse();

        Long userId = userEntity.getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userResponseDto);

        UserResponseDto result = userService.getUser(userId);

        assertThat(result).isEqualTo(userResponseDto);
        assertThat(result.getId()).isEqualTo(userResponseDto.getId());
        assertThat(result.getName()).isEqualTo(userResponseDto.getName());
        assertThat(result.getEmail()).isEqualTo(userResponseDto.getEmail());

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
        CreatedUserRequestDto createdUserRequestDto = DataUtils.getJohnDoeRequestCreated();
        UserResponseDto userResponseDto = DataUtils.getJohnDoeResponse();
        UserEntity userEntity = DataUtils.getJohnDoeTransient();
        UserEntity savedUserEntity = DataUtils.getJohnDoePersisted();

        when(userMapper.toEntity(createdUserRequestDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.toDto(savedUserEntity)).thenReturn(userResponseDto);

        UserResponseDto result = userService.save(createdUserRequestDto);

        assertThat(result).isEqualTo(userResponseDto);
        verify(userMapper).toEntity(createdUserRequestDto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toDto(savedUserEntity);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    void save_whenEmailAlreadyExists_ShouldThrowEmailAlreadyExistsException() {
        CreatedUserRequestDto createdUserRequestDto = DataUtils.getJohnDoeRequestCreated();
        UserEntity userEntity = DataUtils.getJohnDoeTransient();

        when(userMapper.toEntity(createdUserRequestDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.save(createdUserRequestDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("User with email " + createdUserRequestDto.getEmail() + " already exists");

        verify(userMapper).toEntity(createdUserRequestDto);
        verify(userRepository).save(userEntity);
        verifyNoMoreInteractions(userRepository);
        verify(userMapper, never()).toDto(any());
    }
}