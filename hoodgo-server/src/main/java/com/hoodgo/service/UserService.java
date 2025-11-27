package com.hoodgo.service;

import com.hoodgo.dto.UserLoginDTO;
import com.hoodgo.entity.User;

public interface UserService {

    User wxLogin (UserLoginDTO userLoginDTO);
}
