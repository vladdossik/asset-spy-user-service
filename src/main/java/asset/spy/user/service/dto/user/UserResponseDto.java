package asset.spy.user.service.dto.user;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String description;
    private LocalDate dateOfBirth;
    private OffsetDateTime createdAt;
}
