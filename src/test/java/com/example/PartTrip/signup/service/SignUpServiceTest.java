package com.example.PartTrip.signup.service;

import com.example.PartTrip.signup.dto.SignUpRequestDto;
import com.example.PartTrip.signup.entity.PendingSignUpEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.PendingSignUpRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.support.NickNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PendingSignUpRepository pendingSignUpRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailService mailService;
    @Mock
    private NickNameGenerator nickNameGenerator;
    @InjectMocks
    private SignUpService signUpService;

    @Test
    void rejectsEmailAlreadyUsedByAnotherAccountIgnoringCaseAndSpaces() {
        SignUpRequestDto request = new SignUpRequestDto();
        request.setUserId("newuser1");
        request.setUserMail("  090626IHS@DGSW.HS.KR  ");
        when(userRepository.existsByUserId("newuser1")).thenReturn(false);
        when(userRepository.existsByUserMailIgnoreCase("090626ihs@dgsw.hs.kr"))
                .thenReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> signUpService.startSignUp(request))
                .withMessage("이미 가입된 이메일입니다.");

        verify(pendingSignUpRepository, never()).save(any());
        verify(mailService, never()).sendCode(anyString());
    }

    @Test
    void translatesConcurrentDuplicateEmailConstraintViolation() {
        PendingSignUpEntity pending = new PendingSignUpEntity();
        pending.setUserId("newuser1");
        pending.setUserMail("user@example.com");
        pending.setUserPwd("encoded-password");
        pending.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        when(pendingSignUpRepository.findById("user@example.com"))
                .thenReturn(Optional.of(pending));
        when(nickNameGenerator.generate()).thenReturn("traveler123");
        when(userRepository.saveAndFlush(any(UserEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate email"));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> signUpService.completeSignUp(" USER@EXAMPLE.COM "))
                .withMessage("이미 가입된 이메일입니다.");

        verify(pendingSignUpRepository, never()).delete(any());
    }
}
