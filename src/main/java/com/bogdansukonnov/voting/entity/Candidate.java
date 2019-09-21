package com.bogdansukonnov.voting.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "candidates")
@Entity
public class Candidate {
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "candidate_name")
	private String name;
	
	@Column(name = "numberOfVotes")
	private Integer numberOfVotes;
	
	public Candidate() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public Integer getNumberOfVotes() {
		return numberOfVotes;
	}

	public void setNumberOfVotes(Integer numberOfVotes) {
		this.numberOfVotes = numberOfVotes;
	}
	
}
