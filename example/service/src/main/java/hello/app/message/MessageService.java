package hello.app.message;

import hello.app.MessageDto;

public interface MessageService {

    MessageDto getMessage();

    MessageDto setMessage(MessageDto messageDto);

    MessageDto setMessageIp(String remoteAddr);

    MessageDto setMessageUrlAndIp(String message, String remoteAddr);
}
