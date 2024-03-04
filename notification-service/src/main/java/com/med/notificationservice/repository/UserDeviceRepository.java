package com.med.firebase.repository;

import com.med.firebase.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer> {
    UserDevice findUserDeviceByUserId(Integer userId);
}
