package asset.spy.user.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateDto {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 200, message = "Description must be up to 200 characters")
    private String description;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}
