package hello.app.controllers;

import hello.app.MessageDto;
import hello.app.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MyController {
    private MyService myService;

    @GetMapping("/")
    public MessageDto getMessage() {
        return myService.getMessage();
    }

    @PostMapping("/")
    public MessageDto setMessage(@RequestBody MessageDto messageDto) {
        return myService.setMessage(messageDto);
    }

    /**
     *
     */
    @PostMapping("/ip")
//    @ImplicitRestParam("request", linkedTo ="remoteAddr")
    public MessageDto setMessageIp(HttpServletRequest request) {
        return myService.setMessageIp(request.getRemoteAddr());
    }

    @PostMapping("/{message}")
    public MessageDto getMessageWithUrlAndIP(@PathVariable("message") String message, String remoteAddr, HttpServletRequest request) {
        return myService.setMessageUrlAndIp(message, request.getRemoteAddr());
    }

    @Autowired
    public void setMyService(MyService myService) {
        this.myService = myService;
    }
}
