package com.bogdansukonnov.voting.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "citizens")
@Entity
public class Citizen {
	
	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	@Column(name = "citizen_name")
	private String name;

	@Column(name = "hasVoted")
	private Boolean hasVoted;
	
	public Citizen() {
		super();
	}	

	public Citizen(String name) {
		super();
		this.name = name;
		this.hasVoted = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public Boolean getHasVoted() {
		return hasVoted;
	}

	public void setHasVoted(Boolean hasVoted) {
		this.hasVoted = hasVoted;
	}

}
