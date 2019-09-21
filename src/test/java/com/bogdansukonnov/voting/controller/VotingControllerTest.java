package com.bogdansukonnov.voting.controller;

import com.bogdansukonnov.voting.entity.Candidate;
import com.bogdansukonnov.voting.entity.Citizen;
import com.bogdansukonnov.voting.repositories.CandidateRepo;
import com.bogdansukonnov.voting.repositories.CitizenRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VotingControllerTest {

    @InjectMocks
    private VotingController vc = new VotingController();

    @Mock
    private CitizenRepo citizenRepo;
    @Mock
    private CandidateRepo candidateRepo;
    @Mock
    private Model model;
    @Mock
    private HttpSession session;
    @Mock
    private Citizen citizen;
    @Mock
    private Candidate candidate;

    @Test
    public void getAutorisationPage() {
        assertEquals("autorisation.html", vc.getAutorisationPage());
    }

    @Test
    public void doLoginNewCitizen() {
        String citizenName = "NewCitizen";
        //return page
        assertEquals("performVote.html", vc.doLogin(citizenName, model, session));
        //citizen looked in the database
        verify(citizenRepo).findByName(citizenName);
        //citizen saved in the database
        verify(citizenRepo).save(any(Citizen.class));
        //citizen added to session attribute
        verify(session).setAttribute(same("citizen"), any(Citizen.class));
        //get candidates
        verify(candidateRepo).findAll();
        //candidates added to model
        verify(model).addAttribute(same("candidates"), any());
    }

    @Test
    public void doLoginVotedCitizen() {
        //setup
        String citizenName = "VotedCitizen";
        when(citizenRepo.findByName(citizenName)).thenReturn(citizen);
        when(citizen.getHasVoted()).thenReturn(true);
        //return page
        assertEquals("alreadyVoted.html", vc.doLogin(citizenName, model, session));
        //find citizen in database
        verify(citizenRepo).findByName(citizenName);
        //check if citizen voted
        verify(citizen).getHasVoted();
    }

    @Test
    public void vote() {
        //setup
        int candidateId = 7;
        int initVotes = 1;
        when(candidateRepo.findById(candidateId)).thenReturn(candidate);
        when(candidate.getNumberOfVotes()).thenReturn(initVotes);
        when(session.getAttribute("citizen")).thenReturn(citizen);
        //return page
        assertEquals("voteAccepted.html", vc.voteFor(candidateId, session));
        //get number of votes
        verify(candidate).getNumberOfVotes();
        //set increased number of votes
        verify(candidate).setNumberOfVotes(initVotes + 1);
        //save candidate to database
        verify(candidateRepo).save(candidate);
        //set citizen has voted
        verify(citizen).setHasVoted(true);
        //save citizen to database
        verify(citizenRepo).save(citizen);
    }

}