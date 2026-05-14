package com.example.PartTrip.dto;


import lombok.AllArgsConstructor;
import lombok.Data; // Getter + Setter = Data
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // getter + setter
@NoArgsConstructor
@AllArgsConstructor
public class UserManageDto {
    private Long userId;
    private String userPwd;
    private String userMail;
    private String nickName;
    private String signUpDivision;
    private String phnNumber;
    private String myCountry;
    private String travelType;
    private LocalDateTime createDate;









}

