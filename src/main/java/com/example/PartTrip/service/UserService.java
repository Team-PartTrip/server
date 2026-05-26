package com.example.PartTrip.service;


import com.example.PartTrip.dto.UserManageDto;
import com.example.PartTrip.entity.UserEntity;
import com.example.PartTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    // 회원 가입

    public UserEntity saveUser(UserManageDto dto) {
        UserEntity user = new UserEntity();

        user.setUserPwd(dto.getUserPwd());
        user.setUserMail(dto.getUserMail());
        user.setNickName(dto.getNickName());
        user.setSignUpDivision(dto.getSignUpDivision());
        user.setPhnNumber(dto.getPhnNumber());
        user.setMyCountry(dto.getMyCountry());
        user.setTravelType(dto.getTravelType());


        return userRepository.save(user);
    }


}
