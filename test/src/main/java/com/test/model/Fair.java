package com.test.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="fair")
@NoArgsConstructor
public class Fair {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String fname;
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date sdate;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date edate;
	
	private String location;
	private String fimg;
	private String info;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User uname;
	
	@Enumerated(EnumType.STRING)
	private FairType fairType;
	
	@Builder
	public Fair(String fname,Date sdate,Date edate,String location,String fimg,String info) {
		this.fname=fname;
		this.sdate=sdate;
		this.edate=edate;
		this.location=location;
		this.fimg=fimg;
		this.info=info;
	}


}
