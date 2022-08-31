package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MessageInquiryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Security is a cross-cutting concern, however for the sake of simplicity it is tested against the message endpoint
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserRepository userRepository;

    private Message message = Message.builder()
        .title(TEST_NEWS_TITLE)
        .summary(TEST_NEWS_SUMMARY)
        .text(TEST_NEWS_TEXT)
        .publishedAt(TEST_NEWS_PUBLISHED_AT)
        .imageUrl(TEST_NEWS_IMAGEURL)
        .build();

    @BeforeEach
    public void beforeEach() {
        userRepository.saveUser(TestData.buildApplicationUserWith(DEFAULT_USER));
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteUser(userRepository.findUserByEmail(DEFAULT_USER));
    }

    @Test
    public void givenUserLoggedIn_whenFindAll_then200() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(MESSAGE_BASE_URI)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );
    }

    @Test
    public void givenNoOneLoggedIn_whenFindAll_then401() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(MESSAGE_BASE_URI))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void givenAdminLoggedIn_whenPost_then201() throws Exception {
        MessageInquiryDto messageInquiryDto = messageMapper.messageToMessageInquiryDto(message);
        String body = objectMapper.writeValueAsString(messageInquiryDto);

        MvcResult mvcResult = this.mockMvc.perform(post(MESSAGE_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void givenNoOneLoggedIn_whenPost_then401() throws Exception {
        message = Message.builder()
            .title(message.getTitle())
            .summary(message.getSummary())
            .text(message.getText())
            .publishedAt(null)
            .build();

        MessageInquiryDto messageInquiryDto = messageMapper.messageToMessageInquiryDto(message);
        String body = objectMapper.writeValueAsString(messageInquiryDto);

        MvcResult mvcResult = this.mockMvc.perform(post(MESSAGE_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void givenUserLoggedIn_whenPost_then403() throws Exception {
        message = Message.builder()
            .title(message.getTitle())
            .summary(message.getSummary())
            .text(message.getText())
            .publishedAt(null)
            .imageUrl(TEST_NEWS_IMAGEURL)
            .build();

        MessageInquiryDto messageInquiryDto = messageMapper.messageToMessageInquiryDto(message);
        String body = objectMapper.writeValueAsString(messageInquiryDto);

        MvcResult mvcResult = this.mockMvc.perform(post(MESSAGE_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    void canAccessRegistrationWithoutLogin() throws Exception {
        ApplicationUserDto userDto = ApplicationUserDto.builder()
            .firstName("Testus")
            .lastName("Maximus")
            .gender("M")
            .dateOfBirth(LocalDate.parse("1999-01-01"))
            .emailAddress("test@test.at")
            .password("password1")
            .address(AddressDto.builder()
                .country("Austria")
                .city("Vienna")
                .street("Karlsplatz 3")
                .postalCode("1030")
                .build())
            .build();
        String body = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post(REGISTRATION_BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        ).andExpect(status().is2xxSuccessful()
        ).andExpect(status().isCreated());
    }

    @Test
    void cantAccessRegistrationWithInvalidUser() throws Exception {
        ApplicationUserDto user = ApplicationUserDto.builder().build();
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(post(REGISTRATION_BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        ).andExpect(status().is4xxClientError()
        ).andExpect(status().isBadRequest());
    }

}
