package dev.shvetsova.ewmc.users.service.user;

import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import dev.shvetsova.ewmc.users.http.EventClient;
import dev.shvetsova.ewmc.users.http.RequestClient;
import dev.shvetsova.ewmc.users.model.User;
import dev.shvetsova.ewmc.users.model.UserMapper;
import dev.shvetsova.ewmc.users.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.utils.Constants.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventClient eventClient;
    private final RequestClient requestClient;

    @Override
    public UserDto registerUser(NewUserRequest body) {
        try {
            User user = userRepository.save(UserMapper.fromDto(body));
            return UserMapper.toDto(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    String.format("User with email='%s' already exists", body.getEmail()),
                    INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED
            );
        }
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from / size, size);
        final List<User> users = (ids == null || ids.isEmpty())
                ? userRepository.findAll(page).getContent()
                : userRepository.findAllByIdIn(ids, page).getContent();
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public void delete(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
        }
        Boolean hasRequestToEvents = requestClient.checkRequests(userId);
        Boolean hasInitiatedEvents = eventClient.checkEvents(userId);
        if (hasInitiatedEvents == null) throw new RuntimeException("Error request to events-service");
        if (hasRequestToEvents == null) throw new RuntimeException("Error request to requests-service");

        if (hasInitiatedEvents.equals(Boolean.TRUE) || hasRequestToEvents.equals(Boolean.TRUE)) {
            throw new ConflictException(
                    String.format("User with id=%d initiated an event or has a request to participate in an event.", userId),
                    FOR_THE_REQUESTED_OPERATION_THE_CONDITIONS_ARE_NOT_MET);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto changeSubscribeMode(long userId, boolean isAutoSubscribe) {
        final User user = findUserById(userId);
        user.setAutoSubscribe(isAutoSubscribe);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public boolean isExistUser(long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public List<UserDto> getUsersList(long id, List<Long> userIds) {
        return UserMapper.toDto(userRepository.findAllById(userIds));
    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.toDto(findUserById(userId));
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

    }
}
