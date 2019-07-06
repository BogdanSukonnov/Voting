package com.bogdansukonnov.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bogdansukonnov.voting.entity.Citizen;
import com.bogdansukonnov.voting.repositories.CitizenRepo;

@RestController
public class index {
	
	@Autowired
	CitizenRepo citizenRepo;
	
	@RequestMapping("/doAction")
	public String doAction() {
		
		Citizen newSitizen = new Citizen((long)1, "Bob");
		citizenRepo.save(newSitizen);
		
		return "Success";
		
	}

}
