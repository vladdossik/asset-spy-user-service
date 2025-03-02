package asset.spy.user.service.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class SortingUtil {

    public static void validateSortField(Pageable pageable, List<String> allowedSortFields) {
        List<String> allowedSortFieldsList = allowedSortFields.stream().toList();
        String sortField = pageable.getSort().stream()
                .findFirst()
                .map(Sort.Order::getProperty)
                .orElse("id");

        if (!allowedSortFieldsList.contains(sortField)) {
            throw new IllegalArgumentException("Invalid sort field " + sortField +
                    ". Allowed fields are " + allowedSortFieldsList);
        }
    }
}
