package asset.spy.user.service.dto.contact;

import lombok.Data;

import java.util.UUID;

@Data
public class ContactResponseDto {
    private UUID externalId;
    private Long userId;
    private String contactType;
    private String contactValue;
    private Integer priority;
}
