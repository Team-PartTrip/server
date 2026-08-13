package com.example.PartTrip.application.community;

import com.example.PartTrip.application.community.data.LikeResponseDto;
import com.example.PartTrip.domain.community.entity.LikeEntity;
import com.example.PartTrip.domain.community.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;

    // 좋아요 토글 (이미 눌렀으면 취소, 안 눌렀으면 추가)
    public LikeResponseDto toggleLike(String userId, String targetType, Long targetId) {
        Optional<LikeEntity> existing =
                likeRepository.findByTargetTypeAndTargetIdAndUserId(targetType, targetId, userId);

        boolean liked;
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            liked = false;
        } else {
            LikeEntity like = new LikeEntity();
            like.setTargetType(targetType);
            like.setTargetId(targetId);
            like.setUserId(userId);
            like.setCreateDate(LocalDateTime.now());
            likeRepository.save(like);
            liked = true;
        }

        long count = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
        return new LikeResponseDto(liked, count);
    }
}
