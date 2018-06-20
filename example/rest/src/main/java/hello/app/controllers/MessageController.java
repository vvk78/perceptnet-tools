package hello.app.controllers;

import hello.app.MessageDto;
import hello.app.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("message")
public class MessageController {
    private MessageService messageService;

    @GetMapping("/")
    public MessageDto getMessage() {
        return messageService.getMessage();
    }

    @PostMapping("/")
    public MessageDto setMessage(@RequestBody MessageDto messageDto) {
        return messageService.setMessage(messageDto);
    }

    /**
     *
     */
    @PostMapping("/ip")
//    @ImplicitRestParam("request", linkedTo ="remoteAddr")
    public MessageDto setMessageIp(HttpServletRequest request) {
        return messageService.setMessageIp(request.getRemoteAddr());
    }

    @PostMapping("/{message}")
    public MessageDto getMessageWithUrlAndIP(@PathVariable("message") String message, String remoteAddr, HttpServletRequest request) {
        return messageService.setMessageUrlAndIp(message, request.getRemoteAddr());
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
