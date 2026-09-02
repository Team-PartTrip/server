package com.example.PartTrip.signup.service;

import com.example.PartTrip.signup.dto.SignUpRequestDto;
import com.example.PartTrip.signup.repository.PendingSignUpRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.support.NickNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
}
