package ru.holding.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.holding.entity.Role;
import ru.holding.enums.Roles;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Role r WHERE r.user.id IN (SELECT u.id FROM User u WHERE u.email = :email) AND r.roles = :role")
    void deleteByUserEmailAndRole(
            @Param("email") String email,
            @Param("role") Roles role
    );

}
