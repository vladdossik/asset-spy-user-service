package asset.spy.user.service.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Size(max = 200, message = "Description must be up to 200 characters")
    private String description;

    private LocalDate dateOfBirth;
}
