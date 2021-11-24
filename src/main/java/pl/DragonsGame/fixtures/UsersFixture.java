package pl.DragonsGame.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.DragonsGame.app.TimeUtils;
import pl.DragonsGame.entity.Users;
import pl.DragonsGame.entity.UsersRoles;
import pl.DragonsGame.service.userSettings.UsersRolesService;
import pl.DragonsGame.service.userSettings.UsersService;


import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class UsersFixture {
    private final UsersService usersService;
    private final UsersRolesService usersRolesService;

    private final List<Users> usersList = Arrays.asList(
             new Users(null, "Andrzej", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"beomir89@gmail.com",true,"system",null),
             new Users(null, "Ania", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"buDUJEmPiotrP@wp.pl",true,"system",null)
    );

    @Autowired
    public UsersFixture(UsersService usersService, UsersRolesService usersRolesService) {
        this.usersService = usersService;
        this.usersRolesService = usersRolesService;
    }

    public void loadIntoDB() {


        for (Users users : usersList) {

            usersService.add(users);
        }
        List<UsersRoles> usersRolesList = usersRolesService.getUsersRoles();
        Users users1 = usersList.get(0);
        Users users2 = usersList.get(1);

//
        users1.setUsersRoles(usersRolesList.get(0));
        users2.setUsersRoles(usersRolesList.get(0));

//
        usersService.addWithoutCodePass(users1);
        usersService.addWithoutCodePass(users2);

    }
}
