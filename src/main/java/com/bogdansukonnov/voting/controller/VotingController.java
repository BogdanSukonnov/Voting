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
	
	public final Logger logger = Logger.getLogger(VotingController.class);
	
	@Autowired
	CitizenRepo citizenRepo;
	
	@Autowired
	CandidateRepo candidateRepo;
	
	@RequestMapping("/")
	public String goToVote() {
		logger.info("Start log");
		return "vote.html";
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(@RequestParam String name, Model model, HttpSession session) {
				
		Boolean citizenHasVoted = false;
		
		Citizen citizen = citizenRepo.findByName(name);
		if (citizen != null) {		
			citizenHasVoted = citizen.getHasVoted();			
		}
		if (citizenHasVoted) {
			return "alreadyVoted.html";
		}
		else {
			List<Candidate> candidates = candidateRepo.findAll();
			model.addAttribute("candidates", candidates);
			if (citizen == null) {
				citizen = new Citizen();
				citizen.setName(name);				
			}
			session.setAttribute("citizen", citizen);
			return "performVote.html";
		}
		
	}
	
	@RequestMapping("/voteFor")
	public String voteFor(@RequestParam int id, HttpSession session) {
		
		Candidate candidate = candidateRepo.findById(id);
		candidate.setNumberOfVotes(candidate.getNumberOfVotes() + 1);
		candidateRepo.save(candidate);
		
		Citizen citizen = (Citizen) session.getAttribute("citizen");
		citizen.setHasVoted(true);
		citizenRepo.save(citizen);
		
		return "voteAccepted.html";
		
	}

}
