package com.mbacms.DTO;

import java.util.List;

public record GeneralStatDto(
        List<String> labels,
        List<Long> counts
) {
}
