package pl.DragonsGame.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.DragonsGame.entity.Users;
import pl.DragonsGame.service.userSettings.CheckPassword;
import pl.DragonsGame.service.userSettings.UsersService;
import pl.DragonsGame.service.userSettings.UsersServiceImpl;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("")
public class StartController {

    private final UsersService usersService;
    private final UsersServiceImpl usersServiceImpl;




    public StartController(UsersService usersService, UsersServiceImpl usersServiceImpl) {
        this.usersService = usersService;
        this.usersServiceImpl = usersServiceImpl;
    }


    @GetMapping("/index")
    public String login() {
        return "index";
    }

    @GetMapping("/game_introduction")
    public String gameIntroduction() {
        return "game_introduction";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }


    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }


    @GetMapping("/users/myProfile/{token}")
    public String userPanel(@PathVariable String token, Model model) {

        Users users = usersService.getUserByActivateToken(token);
        log.debug(users.getPassword());
        model.addAttribute(users);
        CheckPassword checkPass = new CheckPassword();
        model.addAttribute("checkPassword", checkPass);
        model.addAttribute("localDateTime", LocalDateTime.now());
        log.debug(usersServiceImpl.alertMessage);
        model.addAttribute("alertMessage", usersServiceImpl.alertMessage);
        usersServiceImpl.oldPass = users.getPassword();
        return "userSettings/myProfile";
    }

    @PostMapping("/users/myProfile")
    public String usersDataChanges(Users users,String email, CheckPassword check) {
        return usersService.changePassword(users,email,check);
    }


}