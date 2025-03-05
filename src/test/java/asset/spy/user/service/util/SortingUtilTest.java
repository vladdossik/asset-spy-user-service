package asset.spy.user.service.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SortingUtilTest {

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "firstname",
            "username",
            "id",
            "dateOfBirth");

    @Test
    void whenAllowedSortFieldsListNotContainsSortFieldTest() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("name"));
        assertThrows(IllegalArgumentException.class, () -> {
            SortingUtil.validateSortField(pageable, ALLOWED_SORT_FIELDS);
        });
    }

    @Test
    void whenAllowedSortFieldsListContainsSortFieldTest() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("username"));
        assertDoesNotThrow(() -> SortingUtil.validateSortField(pageable, ALLOWED_SORT_FIELDS));
    }

    @Test
    void whenSortCriteriaNotProvidedTest() {
        Pageable pageable = PageRequest.of(0, 2);
        assertDoesNotThrow(() -> SortingUtil.validateSortField(pageable, ALLOWED_SORT_FIELDS));
    }
}
