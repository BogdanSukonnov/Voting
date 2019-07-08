package com.bogdansukonnov.voting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bogdansukonnov.voting.entity.Candidate;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate, Integer>{
	
	public Candidate findById(int id);

}
