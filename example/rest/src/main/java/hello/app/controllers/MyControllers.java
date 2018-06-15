package hello.app.controllers;

import hello.app.MessageDto;
import hello.app.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MyControllers {
    private MyService myService;

    @GetMapping("/")
    public MessageDto helloMessage() {
        return myService.getMessage();
    }

    @PostMapping("/")
    public MessageDto getBackMessage(@RequestBody MessageDto messageDto) {
        return myService.setMessage(messageDto);
    }

    @PostMapping("/ip")
    public MessageDto getMessageIP(HttpServletRequest request) {
        return myService.setMessageIp(request.getRemoteAddr());
    }

    @PostMapping("/{message}")
    public MessageDto getMessageWithUrlAndIP(@PathVariable("message") String message, HttpServletRequest request) {
        return myService.setMessageUrlAndIp(message, request.getRemoteAddr());
    }

    @Autowired
    public void setMyService(MyService myService) {
        this.myService = myService;
    }
}
