package com.ufukuzun.kodility.service.challenge;

import com.ufukuzun.kodility.dao.challenge.UserPointDao;
import com.ufukuzun.kodility.domain.challenge.Challenge;
import com.ufukuzun.kodility.domain.challenge.UserPoint;
import com.ufukuzun.kodility.domain.user.User;
import com.ufukuzun.kodility.utils.DateUtils;
import com.ufukuzun.kodility.testutils.builder.ChallengeBuilder;
import com.ufukuzun.kodility.testutils.builder.UserBuilder;
import com.ufukuzun.kodility.utils.Clock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserPointServiceTest {

    @InjectMocks
    private UserPointService service;

    @Mock
    private UserPointDao userPointDao;

    @Test
    public void shouldGiveUserPointForTheChallenge() {
        Clock.freeze(DateUtils.toDate("10/12/2012"));

        User user = new UserBuilder().id(2L).build();
        Challenge challenge = new ChallengeBuilder().id(3L).point(12).build();

        service.givePoint(challenge, user);

        ArgumentCaptor<UserPoint> pointCaptor = ArgumentCaptor.forClass(UserPoint.class);

        verify(userPointDao).save(pointCaptor.capture());
        UserPoint savedUserPoint = pointCaptor.getValue();

        assertThat(savedUserPoint.getChallenge(), equalTo(challenge));
        assertThat(savedUserPoint.getUser(), equalTo(user));
        assertThat(savedUserPoint.getPointAmount(), equalTo(12));
        assertThat(savedUserPoint.getGivenDate(), equalTo(Clock.getTime()));

        Clock.unfreeze();
    }

    @Test
    public void shouldReturnTrueIfUserHasNotWonPointFromTheChallengeBeforeAndChallengeIsApproved() {
        User user = new UserBuilder().id(2L).build();
        Challenge challenge = new ChallengeBuilder().id(3L).approved(true).build();

        when(userPointDao.countByChallengeAndUser(challenge, user)).thenReturn(0L);

        assertTrue(service.canUserWinPoint(challenge, user));
    }

    @Test
    public void shouldReturnFalseIfUserHasWonPointFromTheChallengeBefore() {
        User user = new UserBuilder().id(2L).build();
        Challenge challenge = new ChallengeBuilder().id(3L).approved(true).build();

        when(userPointDao.countByChallengeAndUser(challenge, user)).thenReturn(1L);

        assertFalse(service.canUserWinPoint(challenge, user));
    }

    @Test
    public void shouldReturnFalseIfChallengeIsNotApproved() {
        User user = new UserBuilder().id(2L).build();
        Challenge challenge = new ChallengeBuilder().id(3L).approved(false).build();

        assertFalse(service.canUserWinPoint(challenge, user));

        verifyZeroInteractions(userPointDao);
    }

}