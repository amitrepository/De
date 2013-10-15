package sagan.team.web;

import sagan.team.service.CachedTeamService;
import sagan.util.service.DateService;
import sagan.team.MemberProfile;
import sagan.team.MemberProfileBuilder;
import sagan.team.TeamLocation;
import sagan.blog.service.CachedBlogService;
import sagan.blog.view.PostViewFactory;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TeamControllerTests {

    @Mock
    private CachedBlogService blogService;

    @Mock
    private CachedTeamService teamService;

    private ExtendedModelMap model = new ExtendedModelMap();

    private TeamController teamController;

    @Before
    public void setUp() throws Exception {
        this.teamController = new TeamController(this.teamService, this.blogService,
                new PostViewFactory(new DateService()));
        List<MemberProfile> members = new ArrayList<>();

        members.add(MemberProfileBuilder.profile()
                .name("Norman")
                .geoLocation(10, 5)
                .username("normy")
                .id(123L)
                .build());
        members.add(MemberProfileBuilder.profile()
                .name("Patrick")
                .geoLocation(-5, 15)
                .username("pat")
                .id(321L)
                .build());

        BDDMockito.given(this.teamService.fetchActiveMembers()).willReturn(members);
    }

    @Test
    public void includeTeamLocationsInModel() throws Exception {
        this.teamController.showTeam(this.model);
        @SuppressWarnings("unchecked")
        List<TeamLocation> teamLocations = (List<TeamLocation>) this.model
                .get("teamLocations");

        TeamLocation norman = teamLocations.get(0);
        MatcherAssert.assertThat(norman.getName(), Matchers.equalTo("Norman"));
        MatcherAssert.assertThat(norman.getLatitude(), Matchers.equalTo(10f));
        MatcherAssert.assertThat(norman.getLongitude(), Matchers.equalTo(5f));
        MatcherAssert.assertThat(norman.getMemberId(), Matchers.equalTo(123L));

        TeamLocation patrick = teamLocations.get(1);
        MatcherAssert.assertThat(patrick.getName(), Matchers.equalTo("Patrick"));
        MatcherAssert.assertThat(patrick.getLatitude(), Matchers.equalTo(-5f));
        MatcherAssert.assertThat(patrick.getLongitude(), Matchers.equalTo(15f));
        MatcherAssert.assertThat(patrick.getMemberId(), Matchers.equalTo(321L));
    }
}
