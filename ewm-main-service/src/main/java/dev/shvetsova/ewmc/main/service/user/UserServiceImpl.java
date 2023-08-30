package dev.shvetsova.ewmc.main.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import dev.shvetsova.ewmc.main.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.main.dto.user.UserDto;
import dev.shvetsova.ewmc.main.exception.ConflictException;
import dev.shvetsova.ewmc.main.exception.NotFoundException;
import dev.shvetsova.ewmc.main.mapper.UserMapper;
import dev.shvetsova.ewmc.main.model.User;
import dev.shvetsova.ewmc.main.repository.EventRepository;
import dev.shvetsova.ewmc.main.repository.RequestRepository;
import dev.shvetsova.ewmc.main.repository.UserRepository;
import dev.shvetsova.ewmc.main.utils.Constants;

import java.util.List;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.main.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.main.utils.Constants.USER_WITH_ID_D_WAS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public UserDto registerUser(NewUserRequest body) {
        try {
            User user = userRepository.save(UserMapper.fromDto(body));
            return UserMapper.toDto(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    String.format("User with email='%s' already exists", body.getEmail()),
                    Constants.INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED
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
                    String.format(Constants.USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
        }
        if (eventRepository.existsByInitiatorId(userId) || requestRepository.existsByRequesterId(userId)) {
            throw new ConflictException(
                    String.format("User with id=%d initiated an event or has a request to participate in an event.", userId),
                    Constants.FOR_THE_REQUESTED_OPERATION_THE_CONDITIONS_ARE_NOT_MET);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void checkExistById(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
        }
    }

    @Override
    public UserDto changeSubscribeMode(long userId, boolean isAutoSubscribe) {
        final User user = findUserById(userId);
        user.setAutoSubscribe(isAutoSubscribe);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

    }
}
