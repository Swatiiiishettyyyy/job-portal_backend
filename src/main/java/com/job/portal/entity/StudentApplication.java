package com.job.portal.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StudentApplication {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "company_id")
    private Company company;
    
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Student student;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "applied_date")
    private LocalDateTime AppliedDate;
    
//    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<JobRounds> jobRounds = new ArrayList<>();
    
}
