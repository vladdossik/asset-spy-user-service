package asset.spy.user.service.controller;

import asset.spy.user.service.dto.ContactDTO;
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
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO createContact(@Valid final ContactDTO contactDTO) {
        log.info("Create contact: {}", contactDTO);
        return contactService.createContact(contactDTO);
    }

    @GetMapping("/{id}")
    public ContactDTO getContactById(final Long id) {
        log.info("Get contact by id: {}", id);
        return contactService.getContactById(id);
    }

    @PutMapping("/{id}")
    public ContactDTO updateContact(@PathVariable Long id, @Valid @RequestBody ContactDTO contactDTO) {
        log.info("Update contact: {}", contactDTO);
        return contactService.updateContact(id, contactDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable Long id) {
        log.info("Delete contact: {}", id);
        contactService.deleteContact(id);
    }

    @GetMapping
    public Page<ContactDTO> getAllContacts(@RequestParam(required = false) Long cursor,
                                        @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Get all contacts: {}", cursor);
        return contactService.getAllContacts(cursor, size);
    }

    @GetMapping("/{userId}/contacts")
    public Page<ContactDTO> getAllContactsForUser(@PathVariable Long userId,
                                                  @RequestParam(required = false) Long cursor,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Get all contacts for user: {}", userId);
        return contactService.getAllContactsForUser(userId, cursor, size);
    }
}
