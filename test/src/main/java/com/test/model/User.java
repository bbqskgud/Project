package com.test.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class User {
	@Id
	private String username;
	private String name;
	private String password;
	private String roll;
	private String email;
}
