package com.example.PartTrip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_manage")
@Getter
@Setter
@NoArgsConstructor
public class UserManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
