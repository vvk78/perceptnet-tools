package hello.service;

import hello.app.MessageDto;
import hello.app.MyService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService {

    @Override
    public MessageDto getMessage() {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("Hello World");
        return messageDto;
    }

    @Override
    public MessageDto setMessage(MessageDto messageDto) {
        return messageDto;
    }

    @Override
    public MessageDto setMessageIp(String remoteAddr) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(remoteAddr);
        return messageDto;
    }

    @Override
    public MessageDto setMessageUrlAndIp(String message, String remoteAddr) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("Url: " + message + " Address: " + remoteAddr);
        return messageDto;
    }
}
