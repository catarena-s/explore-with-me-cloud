package dev.shvetsova.ewmc.users.keycloak;

import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {
    private final RealmResource realmResource;
    private final UsersResource usersResource;

    public KeycloakService(@Value("${keycloak.realm}") String realm, Keycloak keycloak) {
        this.realmResource = keycloak.realm(realm);
        this.usersResource = realmResource.users();
    }

    public Response createKeycloakUser(NewUserRequest userDto) {
        List<UserRepresentation> userList = usersResource.searchByUsername(userDto.getName(), true);
        if (!userList.isEmpty()) {
            throw new ConflictException(String.format("Account with username='%s' already exist.", userDto.getName()));
        }
        userList = usersResource.searchByEmail(userDto.getEmail(), true);
        if (!userList.isEmpty()) {
            throw new ConflictException(String.format("Account with email='%s' already exist.", userDto.getEmail()));
        }

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userDto.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userDto.getName());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(userDto.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        var response = usersResource.create(kcUser);
        return response;
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


    public void addRole(String userId, List<String> roles) {
        List<RoleRepresentation> kcRoles = new ArrayList<>();
        for (String role : roles) {
            RoleRepresentation representation = realmResource.roles().get(role).toRepresentation();
            kcRoles.add(representation);
        }

        UserResource user = usersResource.get(userId);
        user.roles().realmLevel().add(kcRoles);
    }

    public void deleteUser(String userId) {
        List<UserRepresentation> users = usersResource.search("id:" + userId);
        if(users.isEmpty()) return;

        UserResource user = usersResource.get(userId);
        user.remove();
    }

    public void updateKeycloakUser(UserDto userDto) {
        // какие поля обновляем
        UserRepresentation kcUser = new UserRepresentation();
        if (userDto.getUserName() != null) {
            kcUser.setUsername(userDto.getUserName());
        }
        if (userDto.getPassword() != null) {
            CredentialRepresentation credentialRepresentation = createPasswordCredentials(userDto.getPassword());
            kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        }
        if (userDto.getEmail() != null) {
            kcUser.setEmail(userDto.getEmail());
        }
        // получаем пользователя
        UserResource uniqueUserResource = usersResource.get(userDto.getId());
        uniqueUserResource.update(kcUser); // обновление

    }

    public UserRepresentation findUserById(String userId) {
        List<UserRepresentation> users = usersResource.search("id:" + userId);
        if (users.isEmpty())
            throw new NotFoundException(String.format(Constants.USER_WITH_ID_D_WAS_NOT_FOUND, userId));

        return users.get(0);
    }
}
