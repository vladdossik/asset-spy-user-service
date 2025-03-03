package asset.spy.user.service.dto.user;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID externalId;
    private String username;
    private String description;
    private LocalDate dateOfBirth;
    private OffsetDateTime createdAt;
}
