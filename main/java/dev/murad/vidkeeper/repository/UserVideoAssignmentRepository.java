
package dev.murad.vidkeeper.repository;

import dev.murad.vidkeeper.domain.User;
import dev.murad.vidkeeper.domain.UserVideoAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVideoAssignmentRepository extends JpaRepository<UserVideoAssignment, Long> {
    List<UserVideoAssignment> findByUser(User user);

}