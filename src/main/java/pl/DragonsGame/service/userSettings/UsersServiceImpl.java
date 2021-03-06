package pl.DragonsGame.service.userSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.DragonsGame.app.SecurityUtils;
import pl.DragonsGame.app.SendEmailService;
import pl.DragonsGame.app.TimeUtils;
import pl.DragonsGame.entity.Users;
import pl.DragonsGame.repository.UsersRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean resetPassword;
    public String alertMessage = "";
    public String oldPass;
    private SendEmailService sendEmailService;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder,  SendEmailService sendEmailService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.sendEmailService = sendEmailService;

    }

    @Override
    public void add(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(users);
    }

    @Override
    public void edit(Users users) {
        usersRepository.save(users);
    }

    @Override
    public void addWithoutCodePass(Users users) {
        users.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(users);
    }


    @Override
    public List<Users> getUser(Long id) {
        return usersRepository.getUser(id);
    }


    @Override
    public List<Users> getUsers() {
        return usersRepository.getUsers();
    }

    @Override
    public List<Users> getDeactivatedUsers() {
        return usersRepository.getDeactivatedUsers();
    }

    @Override
    public Users findById(Long id) {
        return usersRepository.getOne(id);
    }

    @Override
    public Users getUserByActivateToken(String activateToken) {
        return usersRepository.getUserByActivateToken(activateToken);
    }


    @Override
    public void delete(String activateToken) {
        Users users = usersRepository.getUserByActivateToken(activateToken);
        users.setActive(false);
        users.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        users.setChangeBy(SecurityUtils.usernameForActivations());
        usersRepository.save(users);
    }

    @Override
    public void remove(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public void activate(String activateToken) {
        Users users = usersRepository.getUserByActivateToken(activateToken);
        users.setActive(true);
        users.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        users.setChangeBy(SecurityUtils.usernameForActivations());
        usersRepository.save(users);
    }



    @Override
    public String FindUsernameByToken(String username) {
        return usersRepository.FindUsernameByToken(username);
    }

    @Override
    public Users getByEmail(String email) {
        return usersRepository.getByEmail(email);
    }

    @Override
    public void setActivateUserAfterEmailValidation(String activateToken) {
        Users user = usersRepository.getUserByActivateToken(activateToken);
        user.setActive(true);
        user.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        user.setChangeBy("Activation");
        user.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(user);
    }

    @Override
    public void blockAccountAfterUnforeseenRestartPass(String activateToken) {
        Users user = usersRepository.getUserByActivateToken(activateToken);
        user.setActive(false);
        user.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        user.setChangeBy("Deactivated by User");
        user.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(user);
    }

    @Override
    public boolean resetPasswordStatus() {
        return resetPassword;
    }

    @Override
    public void resetPassword(Users users, String password2) {
        if (usersRepository.getByEmail(users.getEmail()) != null) {
            if (users.getPassword().equals(password2)) {
                users.setId(usersRepository.getByEmail(users.getEmail()).getId());
                users.setChangeBy("Reset Password by: " + SecurityUtils.usernameForActivations());
                users.setLast_update(TimeUtils.timeNowLong());
                users.setUsersRoles(usersRepository.getByEmail(users.getEmail()).getUsersRoles());
                users.setUsername(usersRepository.getByEmail(users.getEmail()).getUsername());
                users.setCreated(usersRepository.getByEmail(users.getEmail()).getCreated());
                users.setPassword(passwordEncoder.encode(users.getPassword()));
                users.setActivateToken(SecurityUtils.uuidToken());
                users.setActive(true);
                resetPassword = true;
                resetPasswordStatus();
                usersRepository.save(users);
            } else {
                resetPassword = false;
                resetPasswordStatus();
            }
        }
    }

    @Override
    public String changePassword(Users users,String email, CheckPassword check ) {

        String enteredPassword = check.password1;
        String password =  oldPass.substring(8);

        System.out.println(BCrypt.checkpw(enteredPassword, password));
        if(check.password1.equals(check.password2) == true && check.password1 != null && check.password2 != null && BCrypt.checkpw(enteredPassword,password) ) {
            sendEmailService.sendEmailFromContactForm("<p>Twoje dane po dokonanych zmianach:<br/><br/><b> Nazwa u??ytkownika:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Has??o:</b>" + users.getPassword() + "</p><p>Your data after changes:<br/><br/><b> Username:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Password:</b>" + users.getPassword() + "</p><p>???????? ???????????? ?????????? ??????????????????:<br/><b> ?????? ????????????????????????:</b>" + users.getUsername() + "<br/><br/> <b>????. ??????????:</b>" + users.getEmail() + "<br/><br/><b>????????????:</b>" + users.getPassword() + "</p><p>Vos donn??es apr??s modifications:<br/><b> Nom d'utilisateur:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Mot de passe:</b>" + users.getPassword() + "</p>",email,"CLS WMS data changed");
            add(users);
            return "redirect:/users/data-changed";
        }
        else if(check.password1 == null || check.password2 == null){
            alertMessage = "ERROR: One from old credentials field to fill were empty";
            return "redirect:/users/myProfile/" + usersRepository.FindUsernameByToken(users.getUsername());
        }
        else if(!check.password1.equals(check.password2)){
            alertMessage = "ERROR: Old credentials are not the same";
            return "redirect:/users/myProfile/" + usersRepository.FindUsernameByToken(users.getUsername());
        }
        else{
            alertMessage = "ERROR: The old password entered is incorrect";
            return "redirect:/users/myProfile/" + usersRepository.FindUsernameByToken(users.getUsername());
        }

    }
}
