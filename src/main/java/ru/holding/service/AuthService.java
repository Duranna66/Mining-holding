package ru.holding.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.holding.dto.AuthDTO;
import ru.holding.dto.UserWithRolesDto;
import ru.holding.entity.Role;
import ru.holding.entity.User;
import ru.holding.enums.Roles;
import ru.holding.repository.RoleRepo;
import ru.holding.repository.UserRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Value("${custom.max.session}")
    private int maxSession;

    @Value("${admin.email}")
    private String adminEmail;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final RedisIndexedSessionRepository redisIndexedSessionRepository;
    private final SessionRegistry sessionRegistry;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final RoleRepo roleRepo;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AuthService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            RedisIndexedSessionRepository redisIndexedSessionRepository,
            SessionRegistry sessionRegistry,
            SecurityContextRepository securityContextRepository,
            RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.redisIndexedSessionRepository = redisIndexedSessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.securityContextRepository = securityContextRepository;
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        this.roleRepo = roleRepo;
    }

    private User buildUser(AuthDTO dto) {
        User user = new User();
        user.setEmail(dto.email().trim());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        return user;
    }

    @Transactional
    public String register(AuthDTO dto, HttpServletRequest request, HttpServletResponse response) {
        String email = dto.email().trim();

        if (userRepo.findByPrincipal(email).isPresent()) {
            throw new IllegalStateException("User already exists: " + email);
        }

        User user = buildUser(dto);

        if (adminEmail.equals(email)) {
            user.addRole(new Role(Roles.ADMIN));
        }

        userRepo.save(user);

        login(dto, request, response);

        return email + " was registered at " + LocalDateTime.now().format(formatter);
    }

    public String login(AuthDTO dto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(dto.email().trim(), dto.password())
        );

        validateMaxSession(authentication);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return dto.email() + " was login at " + LocalDateTime.now().format(formatter);
    }

    private void validateMaxSession(Authentication authentication) {
        if (maxSession <= 0) return;

        var principal = (UserDetails) authentication.getPrincipal();
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);

        if (sessions.size() >= maxSession) {
            sessions.stream()
                    .min(Comparator.comparing(SessionInformation::getLastRequest))
                    .ifPresent(sessionInfo -> redisIndexedSessionRepository.deleteById(sessionInfo.getSessionId()));
        }
    }


    public List<UserWithRolesDto> getAllUsersWithRoles() {
        return userRepo.findAll().stream()
                .map(user -> new UserWithRolesDto(
                        user.getEmail(),
                        user.getRoles().stream()
                                .map(role -> role.getRoles().name())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}