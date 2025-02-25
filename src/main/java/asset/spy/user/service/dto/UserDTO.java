package asset.spy.user.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String username;

    private String description;
    private LocalDate dateOfBirth;
}
