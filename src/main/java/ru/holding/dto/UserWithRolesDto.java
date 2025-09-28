package ru.holding.dto;

import java.util.List;

public record UserWithRolesDto(
        String email,
        List<String> roles
) {
}
