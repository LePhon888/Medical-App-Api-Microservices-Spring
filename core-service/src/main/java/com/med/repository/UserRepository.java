package com.med.repository;

import com.med.dto.UserDTO;
import com.med.model.Appointment;
import com.med.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.code = :code")
    public User findByVerificationCode(@Param("code") String code);


    @Query("SELECT new com.med.dto.UserDTO(u.id, u.firstName, u.lastName, u.birthday) FROM User u WHERE u.parent.id = :parentId")
    public List<UserDTO> findByParentId(@Param("parentId") int parentId);

}
