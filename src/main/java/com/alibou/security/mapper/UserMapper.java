package com.alibou.security.mapper;


import com.alibou.security.entity.User;
import com.alibou.security.model.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
