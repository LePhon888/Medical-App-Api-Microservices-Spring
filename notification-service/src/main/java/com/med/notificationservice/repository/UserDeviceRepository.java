package com.med.notificationservice.repository;

import com.med.notificationservice.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer> {
    UserDevice findUserDeviceByUserId(Integer userId);
    void deleteUserDeviceByUserId(Integer userId);
}
