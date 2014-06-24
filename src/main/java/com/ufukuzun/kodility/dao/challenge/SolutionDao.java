package com.ufukuzun.kodility.dao.challenge;

import com.ufukuzun.kodility.dao.AbstractHibernateDao;
import com.ufukuzun.kodility.domain.challenge.Challenge;
import com.ufukuzun.kodility.domain.challenge.Solution;
import com.ufukuzun.kodility.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SolutionDao extends AbstractHibernateDao<Solution> {

    protected SolutionDao() {
        super(Solution.class);
    }

    public Solution findByChallengeAndUser(Challenge challenge, User user) {
        Map<String, Object> criteriaMap = new HashMap<>();
        criteriaMap.put("challenge", challenge);
        criteriaMap.put("user", user);
        return findOneBy(criteriaMap);
    }

}