package asset.spy.user.service.util;

import asset.spy.user.service.Initializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortingUtilTest extends Initializer {

    @Test
    void whenAllowedSortFieldsListNotContainsSortFieldTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            SortingUtil.validateSortField(pageRequestWithNotValidSortField, ALLOWED_SORT_FIELDS);
        });
    }

    @Test
    void whenAllowedSortFieldsListContainsSortFieldTest() {
        assertDoesNotThrow(() -> SortingUtil.validateSortField(pageRequestWithValidSortField, ALLOWED_SORT_FIELDS));
    }

    @Test
    void whenSortCriteriaNotProvidedTest() {
        assertDoesNotThrow(() -> SortingUtil.validateSortField(pageRequestWithoutSortField, ALLOWED_SORT_FIELDS));
    }
}
