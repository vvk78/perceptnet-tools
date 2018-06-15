package hello.app;

public interface MyService {

    MessageDto getMessage();

    MessageDto setMessage(MessageDto messageDto);

    MessageDto setMessageIp(String remoteAddr);

    MessageDto setMessageUrlAndIp(String message, String remoteAddr);
}
