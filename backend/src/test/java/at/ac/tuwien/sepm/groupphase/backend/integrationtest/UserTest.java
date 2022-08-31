package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UserTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    public void givenRegisteredUser_whenUpdateUserEmail_thenGetNewValidToken() throws Exception {

        ApplicationUser registeredUser = userRepository.saveUser(TestData.buildApplicationUserWith(DEFAULT_USER));

        String updatedEmail = "updated-" + DEFAULT_USER;

        ApplicationUserDto defaultUserDtoWithNewEmail = userMapper.toDto(registeredUser.toBuilder().emailAddress(updatedEmail).build());

        String body = objectMapper.writeValueAsString(defaultUserDtoWithNewEmail);

        MvcResult mvcResult = this.mockMvc.perform(put(USER_PROFILE_BASE_URI + "/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andExpect(content().contentType("text/plain;charset=UTF-8"))
            .andExpect(status().isOk())
            .andReturn();

        String returnedToken = mvcResult.getResponse().getContentAsString();

        MvcResult getUpdatedProfileWithNewToken = this.mockMvc.perform(get(USER_PROFILE_BASE_URI)
            .header(securityProperties.getAuthHeader(), returnedToken))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = getUpdatedProfileWithNewToken.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        ApplicationUser updatedUser = userMapper.userProfileToDomainUser(objectMapper.readValue(response.getContentAsString(), ApplicationUserProfileDto.class));

        assertAll(
            () -> assertEquals(updatedUser.getEmailAddress(), updatedEmail),
            () -> assertEquals(updatedUser.getFirstName(), registeredUser.getFirstName()),
            () -> assertEquals(updatedUser.getLastName(), registeredUser.getLastName()),
            () -> assertNotEquals(updatedUser.getEmailAddress(), DEFAULT_USER)
        );

    }




}
