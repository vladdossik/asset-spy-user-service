package asset.spy.user.service.dto.contact;

import lombok.Data;

import java.util.UUID;

@Data
public class ContactResponseDto {
    private UUID externalId;
    private UUID userExternalId;
    private String contactType;
    private String contactValue;
    private Integer priority;
}
