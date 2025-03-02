package asset.spy.user.service.dto.contact;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactUpdateDto {
    @NotBlank(message = "Contact type cannot be blank")
    @Size(min = 2, max = 50)
    private String contactType;
    @NotBlank(message = "Contact value cannot be blank")
    @Size(min = 2, max = 100)
    private String contactValue;
    @Min(0)
    private Integer priority;
}
