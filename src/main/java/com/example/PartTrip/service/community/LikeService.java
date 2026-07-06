package com.example.PartTrip.service.community;

import com.example.PartTrip.dto.community.LikeResponseDto;
import com.example.PartTrip.entity.community.LikeEntity;
import com.example.PartTrip.repository.community.LikeRepository;
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
