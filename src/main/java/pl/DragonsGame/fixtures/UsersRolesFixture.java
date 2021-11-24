package pl.DragonsGame.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.DragonsGame.app.TimeUtils;
import pl.DragonsGame.entity.UsersRoles;
import pl.DragonsGame.service.userSettings.UsersRolesService;


import java.util.Arrays;
import java.util.List;


@Component
//@Profile("local")
public class UsersRolesFixture {
    private final UsersRolesService usersRolesService;



    private final List<UsersRoles> usersRolesList = Arrays.asList(
            new UsersRoles(null, "ROLE_USER", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "Simple user","system"),
            new UsersRoles(null, "ROLE_ADMIN", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "User with access to all options","system")

    );

    @Autowired
    public UsersRolesFixture(UsersRolesService usersRolesService) {
        this.usersRolesService = usersRolesService;
    }

    public void loadIntoDB() {
        for (UsersRoles usersRoles : usersRolesList) {
            usersRolesService.add(usersRoles);
        }
    }
}
