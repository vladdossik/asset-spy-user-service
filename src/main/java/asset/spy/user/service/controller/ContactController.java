package asset.spy.user.service.controller;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/save/{userExternalId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponseDto createContact(@PathVariable UUID userExternalId,
                                            @Valid @RequestBody ContactCreateDto contactCreateDto) {
        return contactService.createContact(contactCreateDto, userExternalId);
    }

    @GetMapping("/{externalId}")
    public ContactResponseDto getContactById(@PathVariable UUID externalId) {
        return contactService.getContactByExternalId(externalId);
    }

    @PutMapping("/{externalId}")
    public ContactResponseDto updateContact(@PathVariable UUID externalId,
                                            @Valid @RequestBody ContactUpdateDto contactUpdateDto) {
        return contactService.updateContact(externalId, contactUpdateDto);
    }

    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable UUID externalId) {
        contactService.deleteContact(externalId);
    }

    @GetMapping
    public Page<ContactResponseDto> getAllContacts(Pageable pageable,
                                                   @RequestParam(required = false) String contactType,
                                                   @RequestParam(required = false) String contactValue,
                                                   @RequestParam(required = false) Integer priority) {
        return contactService.getAllContacts(pageable,
                contactType, contactValue, priority);
    }
}