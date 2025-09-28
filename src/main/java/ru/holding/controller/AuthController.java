package ru.holding.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.holding.dto.AuthDTO;
import ru.holding.dto.UserWithRolesDto;
import ru.holding.entity.Student;
import ru.holding.entity.User;
import ru.holding.enums.Roles;
import ru.holding.repository.StudentRepository;
import ru.holding.repository.UserRepo;
import ru.holding.service.AuthService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/v1/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserRepo userRepo;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTO authDTO,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        return ResponseEntity
                .status(CREATED)
                .body(this.authService.register(authDTO, request, response));
    }

    @GetMapping("/users")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public List<UserWithRolesDto> getAllUsersWithRoles(Authentication authentication) {
        return authService.getAllUsersWithRoles();
    }

    //http://localhost:8080/api/v1/auth/login
    /*
    {
        "email": "admin@admin.ru",
        "password": "123"
    }
     */
    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(
            @Valid @RequestBody AuthDTO authDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return new ResponseEntity<>(authService.login(authDTO, request, response), OK);
    }


    @GetMapping("/check")
    public long checkSession(Authentication authentication) {


        // Предположим, ты используешь email в качестве principal
        String email = authentication.getName();

        // Получаем ID студента по email (или пользователя)
        User user = userRepo.findByPrincipal(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());

        return user.getId();
    }




    @GetMapping("/whoami")
    public Object currentUser(Authentication auth) {
        return auth.getAuthorities();
    }

    @GetMapping("/roles")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ResponseEntity<String[]> getAllRoles() {
        String[] roles = Arrays.stream(Roles.values())
                .map(Enum::name)
                .toArray(String[]::new);
        return ResponseEntity.ok(roles);
    }

}
