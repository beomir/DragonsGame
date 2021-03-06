package pl.DragonsGame.service.userSettings;

import pl.DragonsGame.entity.Users;
import java.util.List;

public interface UsersService {

    void add(Users users);

    void addWithoutCodePass(Users users);


    List<Users> getUsers();

    List<Users> getDeactivatedUsers();

    Users findById(Long id);

    Users getUserByActivateToken(String activateToken);

    List<Users> getUser(Long id);

    void delete(String activateToken);

    void remove(Long id);

    void activate(String activateToken);

    String FindUsernameByToken(String username);

    Users getByEmail(String email);

    void setActivateUserAfterEmailValidation(String activateToken);

    void blockAccountAfterUnforeseenRestartPass(String activateToken);

    boolean resetPasswordStatus();

    void resetPassword(Users users,String password2);

    String changePassword(Users users,String email, CheckPassword check);

    void edit(Users users);

}
