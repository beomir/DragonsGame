package pl.DragonsGame.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
//@Profile("local")
public class LoadFixtures {

    private final UsersFixture usersFixture;
    private final UsersRolesFixture usersRolesFixture;



    @Autowired
    public LoadFixtures( UsersFixture usersFixture, UsersRolesFixture usersRolesFixture) {

        this.usersFixture = usersFixture;
        this.usersRolesFixture = usersRolesFixture;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        usersRolesFixture.loadIntoDB();
        usersFixture.loadIntoDB();
    }
}
