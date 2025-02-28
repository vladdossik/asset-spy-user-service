package asset.spy.user.service.controller;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.service.ContactService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/save/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponseDto createContact(@PathVariable Long userId,
                                            @Valid @RequestBody ContactCreateDto contactCreateDto) {
        return contactService.createContact(contactCreateDto, userId);
    }

    @GetMapping("/{id}")
    public ContactResponseDto getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @PutMapping("/{id}")
    public ContactResponseDto updateContact(@PathVariable Long id, @Valid @RequestBody ContactUpdateDto contactUpdateDto) {
        return contactService.updateContact(id, contactUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
    }

    @GetMapping
    public Page<ContactResponseDto> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String contactType,
            @RequestParam(required = false) String contactValue,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer priority) {
        return contactService.getAllContacts(page, size, sortField, sortDirection, contactType, contactValue, userId, priority);
    }
}
