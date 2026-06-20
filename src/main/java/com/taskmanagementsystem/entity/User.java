package com.taskmanagementsystem.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property="queueId")
@Entity	
@Data			                                  /*Make this class a JPA entity*/
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserID")
	private Long userId;
	
	@Column(name = "Username")
	private String username;
	
	@Column(name = "Password")
	private String password;
	
	@Column(name = "Email")
	private String email;
	
	@Column(name = "full_name")
	private String fullName;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private List<Tasks> task;
	
	@OneToMany(mappedBy = "user")
	private List<Notification> notification;
	
	@OneToMany(mappedBy = "user")
	private List<Project> project;
	
	@OneToMany(mappedBy = "user")
	private List<Comment> comment;

	private String role = "ROLE_USER";
	

	
	
}


