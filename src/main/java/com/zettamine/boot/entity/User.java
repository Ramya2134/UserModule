package com.zettamine.boot.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@Table(name = "users")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class User {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	
	@Column(name = "f_name")
	private String firstName;
	
	@Column(name = "pwd")
	private String password;
	
	@Column(name = "temp_pwd")
	private String tempPwd;
	
	@Column(name = "l_name")
	private String lastName;
	
	@Column(unique = true)
	private String email;
	
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "login_status")
	private Integer loginStatus;
	
	@Column(name ="acc_lock")
	private boolean accountLock;
	
	@Column(name = "attempts")
	private Integer attempts;
	
	@Lob
    private byte[] image;
	
	@ManyToOne
	private Role role;
}
