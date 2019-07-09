package com.bogdansukonnov.voting.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bogdansukonnov.voting.entity.Candidate;
import com.bogdansukonnov.voting.entity.Citizen;
import com.bogdansukonnov.voting.repositories.CandidateRepo;
import com.bogdansukonnov.voting.repositories.CitizenRepo;

@Controller
public class VotingController {
		
	final Logger logger = Logger.getLogger(VotingController.class);
	
	@Autowired
	CitizenRepo citizenRepo;
	
	@Autowired
	CandidateRepo candidateRepo;
	
	
	@RequestMapping("/")
	public String getAutorisationPage() {		
		return "autorisation.html";		
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(@RequestParam String name, Model model, HttpSession session) {				
		/*
		 * Citizens can have only unique names 
		 */
		Citizen citizen = citizenRepo.findByName(name);		
		if (citizen == null) {			
			//new name, create Citizen with name, hasVoted = false
			logger.info("New Citizen with name " + name);
			citizen = new Citizen(name);
			//save Citizen to database
			citizenRepo.save(citizen);
		}			
		
		if (citizen.getHasVoted()) {
			//Citizen can't vote more than once 
			return "alreadyVoted.html";
		} else {
			//Citizen can vote
			//inject citizen to session
			session.setAttribute("citizen", citizen);
			//get candidates
			List<Candidate> candidates = candidateRepo.findAll();
			//inject candidates to model
			model.addAttribute("candidates", candidates);			
			return "performVote.html";
		}		
	}
	
	@RequestMapping("/voteFor")
	public String voteFor(@RequestParam int id, HttpSession session) {		
		//Increase candidate's votes
		Candidate candidate = candidateRepo.findById(id);
		candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
		candidateRepo.save(candidate);
		//Mark Citizen's hasVoted field		
		Citizen citizen = (Citizen) session.getAttribute("citizen");
		citizen.setHasVoted(true);
		citizenRepo.save(citizen);		
		return "voteAccepted.html";		
	}

}
