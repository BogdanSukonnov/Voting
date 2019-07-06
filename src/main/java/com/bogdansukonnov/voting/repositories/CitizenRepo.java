package com.bogdansukonnov.voting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bogdansukonnov.voting.entity.Citizen;

@Repository
public interface CitizenRepo extends JpaRepository<Citizen,Integer> {

}
