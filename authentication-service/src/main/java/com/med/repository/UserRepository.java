package com.med.repository;

import com.med.dto.UserResponse;
import com.med.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT new com.med.dto.UserResponse(u.id, u.firstName, u.lastName, u.birthday, u.address, u.gender, u.phoneNumber, u.email, u.image) " +
            "FROM User u WHERE u.email = :email")
    UserResponse getUserResponseByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);



    @Query("SELECT u FROM User u WHERE u.code = :code")
    public User findByCode(@Param("code") String code);
}
