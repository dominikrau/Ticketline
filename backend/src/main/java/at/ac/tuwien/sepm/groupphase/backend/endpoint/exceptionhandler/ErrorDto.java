package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import lombok.Value;

import java.util.List;

@Value
public class ErrorDto {

    List<String> messages;
    int code;
    String timestamp;

}
