package com.job.portal.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class JobRounds {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.PERSIST})
    private Company company;
    
	@JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Student student;
    
    @Column(name = "round")
    private int round;
    
    @Column(name = "round_name")
    private String roundName;
    
    @Column(name = "date")
    private LocalDateTime date; 
    
    @Column(name = "round_status")
    private String roundStatus;
    
    
}
