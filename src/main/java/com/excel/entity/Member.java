package com.excel.entity;

import java.time.LocalDateTime;

import com.excel.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Member  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "client_name", nullable = false)
	private String clientName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password", nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "register_time", nullable = false)
	private String registerTime;

	@Column(name = "division")
	// 부서
	private String division;

	@Column(name = "rank")
	// 직급
	private String rank;

	@Column(name = "description")
	// 비고
	private String description;

	@Column(name = "failedAttempts")
	private int failedAttempts = 0;

	@Column(name = "lock_time")
	private LocalDateTime lockTime;

	@Column(name = "last_access_time")
	private String accessTime;

	@Builder
	public Member(String email, String name, String password, String clientName, Role role, String division,
		String rank,
		String description, String registerTime) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.clientName = clientName;
		this.role = role;
		this.division = division;
		this.rank = rank;
		this.description = description;
		this.registerTime = registerTime;
	}

	// @Override
	// public Collection<? extends GrantedAuthority> getAuthorities() {
	// 	return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
	// }


}