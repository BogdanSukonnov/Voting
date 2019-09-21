package com.bogdansukonnov.voting;

import com.bogdansukonnov.voting.controller.VotingController;
import com.bogdansukonnov.voting.entity.Citizen;
import com.bogdansukonnov.voting.repositories.CandidateRepo;
import com.bogdansukonnov.voting.repositories.CitizenRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class VotingApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VotingController votingController;
    @Autowired
    private CitizenRepo citizenRepo;
    @Autowired
    private CandidateRepo candidateRepo;

    @Test
    public void contextLoads() {
        assertThat(votingController).isNotNull();
    }

    @Test
    public void indexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Insert your name")));
    }

    @Test
    @Sql(value = {"/create-citizens.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-citizens.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void votedCitizenLoginRejection() throws Exception {
        String votedCitizenName = "VotedCitizen";
        mockMvc.perform(post("/doLogin").param("name", votedCitizenName))
                .andExpect(status().isOk())
                .andExpect(view().name("alreadyVoted.html"))
                .andExpect(content().string(containsString("You have already voted")));
    }

    @Test
    @Sql(value = {"/delete-citizens.sql", "/create-candidates.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-citizens.sql", "/delete-candidates.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void notVotedCitizenLogin() throws Exception {
        String notVotedCitizenName = "NotVotedCitizen";
        //vote page
        mockMvc.perform(post("/doLogin").param("name", notVotedCitizenName))
                .andExpect(status().isOk())
                .andExpect(view().name("performVote.html"))
                .andExpect(content().string(containsString("Click to vote for")))
                .andExpect(xpath("//a[@class='candidate']").nodeCount(3));
        //citizen has been added in database
        Citizen citizen = citizenRepo.findByName(notVotedCitizenName);
        assert (citizen != null);
        //and citizen has not voted
        assert (!citizen.getHasVoted());
    }

    @Test
    @Sql(value = {"/create-citizens.sql", "/create-candidates.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/delete-candidates.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void voteForCandidate() throws Exception {
        String notVotedCitizenName = "NotVotedCitizen";
        int candidateId = 1;
        int votes = candidateRepo.findById(candidateId).getNumberOfVotes();
        //set citizen to session's attribute
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("citizen", citizenRepo.findByName(notVotedCitizenName));
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/voteFor")
                .session(session);
        //response page
        mockMvc.perform(builder.param("id", String.valueOf(candidateId)))
                .andExpect(status().isOk())
                .andExpect(view().name("voteAccepted.html"))
                .andExpect(content().string(containsString("Thank you for voting")));
        //citizen has voted mark
        assert (citizenRepo.findByName(notVotedCitizenName).getHasVoted());
        //candidate votes increased
        assert (candidateRepo.findById(candidateId).getNumberOfVotes() == votes + 1);
    }

}
