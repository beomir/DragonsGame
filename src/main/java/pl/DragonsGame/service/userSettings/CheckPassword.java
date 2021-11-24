package pl.DragonsGame.service.userSettings;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class CheckPassword {
    public String password1;
    public String password2;
}
