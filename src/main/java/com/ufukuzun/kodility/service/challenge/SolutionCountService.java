package com.ufukuzun.kodility.service.challenge;

import com.ufukuzun.kodility.domain.challenge.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO ufuk: create a generic cache mechanism
@Service
public class SolutionCountService {

    private static final Map<Long, Long> SOLUTION_COUNT_STORAGE = new ConcurrentHashMap<>();

    @Autowired
    private SolutionService solutionService;

    public Long getSolutionCountOf(Challenge challenge) {
        Long challengeId = challenge.getId();
        Long solutionCount = SOLUTION_COUNT_STORAGE.get(challengeId);
        if (solutionCount == null) {
            solutionCount = solutionService.getSolutionCountOf(challenge);
            SOLUTION_COUNT_STORAGE.put(challengeId, solutionCount);
        }
        return solutionCount;
    }

    public Map<Challenge, Long> prepareSolutionCountMapFor(List<Challenge> challenges) {
        Map<Challenge, Long> solutionCountMap = new HashMap<>();
        for (Challenge eachChallenge : challenges) {
            solutionCountMap.put(eachChallenge, getSolutionCountOf(eachChallenge));
        }
        return solutionCountMap;
    }

    public void clearCacheFor(Long challengeId) {
        SOLUTION_COUNT_STORAGE.remove(challengeId);
    }

}
