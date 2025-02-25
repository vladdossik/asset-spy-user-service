package asset.spy.user.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotBlank
    @Size(min = 2, max = 50)
    private String contactType;

    @NotBlank
    @Size(min = 2, max = 100)
    private String contactValue;

    @Min(0)
    private Integer priority;
}
