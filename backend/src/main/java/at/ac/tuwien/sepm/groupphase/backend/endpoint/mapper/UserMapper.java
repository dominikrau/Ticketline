package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Gender;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;

    /**
     * Maps the given User Dto to a Domain representation of the user with
     * just the Basic User privileges
     *
     * @param userDto the user to map
     * @return the mapped User
     */
    public ApplicationUser toDomainUser(ApplicationUserDto userDto) {
        return toDomain(userDto)
            .roles(List.copyOf(userDto.getRoles()))
            .build();
    }

    /**
     * Maps the given User Dto to a Domain representation Builder of the user
     *
     * @param userDto the user to map
     * @return the mapped User Builder
     */
    private ApplicationUser.Builder toDomain(ApplicationUserDto userDto) {
        return ApplicationUser.builder()
            .userId(userDto.getUserId())
            .firstName(userDto.getFirstName())
            .lastName(userDto.getLastName())
            .emailAddress(userDto.getEmailAddress())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .dateOfBirth(userDto.getDateOfBirth())
            .gender(Gender.fromCode(userDto.getGender()))
            .address(addressMapper.toDomain(userDto.getAddress()))
            .blocked((userDto.isBlocked()));
    }

    /**
     * Maps the given User to a Data Transfer Object representation of the User
     *
     * @param user the User to be mapped
     * @return the mapped User DTO
     */
    public ApplicationUserDto toDto(ApplicationUser user) {
        return ApplicationUserDto.builder()
            .userId(user.getUserId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .emailAddress(user.getEmailAddress())
            .dateOfBirth(user.getDateOfBirth())
            .gender(user.getGender().getCode())
            .roles(List.copyOf(user.getRoles()))
            .address(addressMapper.toDto(user.getAddress()))
            .blocked(user.isBlocked())
            .build();
    }

    /**
     * Maps the given Users in a List to a List of Data Transfer Object representations of the Users
     *
     * @param users the List of Users to be mapped
     * @return the mapped User DTO List
     */
    public List<ApplicationUserDto> toDto(List<ApplicationUser> users) {
        if ( users == null ) {
            return Collections.emptyList();
        }

        List<ApplicationUserDto> list = new ArrayList<>( users.size() );
        for ( ApplicationUser user1 : users ) {
            list.add(toDto(user1));
        }

        return list;
    }

    /**
     * Maps the given User Profile Dto to a Domain representation of the user
     *
     * @param user the user Profile to map
     * @return the mapped User
     */
    public ApplicationUser userProfileToDomainUser(ApplicationUserProfileDto user) {
        return userProfileToDomain(user).build();
    }

    /**
     * Maps the given User Profile Dto to a Domain representation Builder of the user
     *
     * @param userProfileDto the user Profile DTO to map
     * @return the mapped User Builder
     */
    private ApplicationUser.Builder userProfileToDomain(ApplicationUserProfileDto userProfileDto) {
        return ApplicationUser.builder()
            .firstName(userProfileDto.getFirstName())
            .lastName(userProfileDto.getLastName())
            .emailAddress(userProfileDto.getEmailAddress())
            .dateOfBirth(userProfileDto.getDateOfBirth())
            .gender(Gender.fromCode(userProfileDto.getGender()))
            .address(addressMapper.toDomain(userProfileDto.getAddress()));
    }


}
