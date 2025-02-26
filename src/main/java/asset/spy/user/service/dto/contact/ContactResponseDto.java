package asset.spy.user.service.dto.contact;

import lombok.Data;

@Data
public class ContactResponseDto {
    private Long id;
    private Long userId;
    private String contactType;
    private String contactValue;
    private int priority;
}
