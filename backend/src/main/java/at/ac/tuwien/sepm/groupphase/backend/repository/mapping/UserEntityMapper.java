package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ApplicationUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserEntityMapper {

    private static final String ROLE_DELIMITER = ";";
    private final AddressEntityMapper addressMapper;

    /**
     * Maps the given Application User Entity to a Domain representation of the Application User
     *
     * @param userEntity the Application User Entity to be mapped
     * @return the mapped Application User
     */
    public ApplicationUser toDomain(ApplicationUserEntity userEntity) {
        return ApplicationUser.builder()
            .userId(userEntity.getId())
            .firstName(userEntity.getFirstName())
            .lastName(userEntity.getLastName())
            .emailAddress(userEntity.getEmailAddress())
            .password(userEntity.getPassword())
            .dateOfBirth(userEntity.getDateOfBirth())
            .gender(Gender.fromCode(userEntity.getGender()))
            .roles(List.of(userEntity.getRoles().split(ROLE_DELIMITER)))
            .address(addressMapper.toDomain(userEntity.getAddress()))
            .blocked(userEntity.isBlocked())
            .loginAttempts(userEntity.getLoginAttempts())
            .build();
    }

    /**
     * Maps the given Application User to a Repository Entity representation of the Application User
     *
     * @param user the Application User to be mapped
     * @return the mapped Application User Entity
     */
    public ApplicationUserEntity toEntity(ApplicationUser user) {
        return ApplicationUserEntity.builder()
            .id(user.getUserId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .emailAddress(user.getEmailAddress())
            .password(user.getPassword())
            .dateOfBirth(user.getDateOfBirth())
            .gender(user.getGender().getCode())
            .roles(String.join(ROLE_DELIMITER, user.getRoles()))
            .address(addressMapper.toEntity(user.getAddress()))
            .blocked(user.isBlocked())
            .loginAttempts(user.getLoginAttempts())
            .build();
    }
}
