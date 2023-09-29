package dev.shvetsova.ewmc.users.service.user;

import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import dev.shvetsova.ewmc.users.http.EventClient;
import dev.shvetsova.ewmc.users.http.KeycloakClient;
import dev.shvetsova.ewmc.users.http.RequestClient;
import dev.shvetsova.ewmc.users.model.User;
import dev.shvetsova.ewmc.users.model.UserMapper;
import dev.shvetsova.ewmc.users.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.shvetsova.ewmc.utils.Constants.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventClient eventClient;
    private final RequestClient requestClient;

    private final KeycloakClient keycloakClient;

    private static final String USER_ROLE = "user";

    @Override
    @Transactional
    public UserDto registerUser(NewUserRequest newUser) {
        final List<String> defaultRole = List.of(USER_ROLE);
        newUser.setDefaultRole(defaultRole);
        final String userResponseId = keycloakClient.registerUser(newUser);

        return addUserToDataBase(newUser, userResponseId);
    }


    @Override
    public List<UserDto> getUsers(List<String> ids, Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from / size, size);
        final List<User> users = (ids == null || ids.isEmpty())
                ? userRepository.findAll(page).getContent()
                : userRepository.findAllByUidIn(ids, page).getContent();
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }


    @Override
    @Transactional
    public void delete(String userId) {
        Boolean hasInitiatedEvents = eventClient.checkEvents(userId);
        Boolean hasRequestToEvents = requestClient.checkRequests(userId);
        if (hasInitiatedEvents == null) throw new RuntimeException("Error request to events-service");
        if (hasRequestToEvents == null) throw new RuntimeException("Error request to requests-service");

        if (hasInitiatedEvents.equals(Boolean.TRUE) || hasRequestToEvents.equals(Boolean.TRUE)) {
            throw new ConflictException(
                    String.format("User with id='%s' initiated an event or has a request to participate in an event.", userId),
                    FOR_THE_REQUESTED_OPERATION_THE_CONDITIONS_ARE_NOT_MET);
        }
        keycloakClient.delete(userId);
        userRepository.deleteByUid(userId);
    }

    @Override
    @Transactional
    public UserDto changeSubscribeMode(String userId, boolean isAutoSubscribe) {
        final User user = findUserById(userId);
        user.setAutoSubscribe(isAutoSubscribe);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsersList(List<String> userIds) {
        return UserMapper.toDto(userRepository.findAllByUidIn(userIds));
    }

    private User findUserById(String userId) {
        return userRepository.findByUid(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

    }


    private UserDto addUserToDataBase(NewUserRequest newUser, String createdId) {
        try {
            User user = userRepository.findByEmail(newUser.getEmail())
                    .orElse(UserMapper.fromDto(newUser, createdId));
            userRepository.save(user);
            return UserMapper.toDto(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    String.format("User with email='%s' already exists", newUser.getEmail()),
                    INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED
            );
        }
    }
}
