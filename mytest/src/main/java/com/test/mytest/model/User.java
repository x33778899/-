package com.test.mytest.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NonNull
	public String getAccount() {
		return account;
	}

	public void setAccount(@NonNull String account) {
		this.account = account;
	}

	@NonNull
	public String getPassword() {
		return password;
	}

	public void setPassword(@NonNull String password) {
		this.password = password;
	}

	@NonNull
	public String getRole() {
		return role;
	}

	public void setRole(@NonNull String role) {
		this.role = role;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@NonNull
	@Column(name = "account", unique = true, nullable = false, length = 50)
	private String account;

	@NonNull
	@Column(name = "password", nullable = false, length = 50)
	private String password;

	@NonNull
	@Column(name = "role", nullable = false, length = 10)
	private String role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonProperty("projects")
	private Set<Project> projects = new HashSet<>();


	public User() {
	}

	// Getter and Setter methods for id, account, password, role...

	public void addProject(Project project) {
		projects.add(project);
		project.setUser(this); // 设置项目的 user_id 字段为当前用户的 ID
	}

	public Set<String> getProjectNames() {
		Set<String> projectNames = new HashSet<>();
		for (Project project : projects) {
			projectNames.add(project.getName());
		}
		return projectNames;
	}

	public boolean containsProject(String projectName) {
		for (Project project : projects) {
			if (project.getName().equals(projectName)) {
				return true;
			}
		}
		return false;
	}
}
