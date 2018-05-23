package edu.msg.ro.persistence.user.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
		@NamedQuery(name = LoginHistory.FIND_LAST_FIVE_ATTEMPS, query = "SELECT h FROM LoginHistory h WHERE h.username = :username ORDER BY h.attemptTime DESC"),
		@NamedQuery(name = LoginHistory.GET_FAILED_ATTEMPTS, query = "SELECT l from LoginHistory l where l.username = :username")})
@Entity
public class LoginHistory extends AbstractEntity {
	public static final String FIND_LAST_FIVE_ATTEMPS = "LoginHistory.FindLastFiveAttemps";
	public static final String GET_FAILED_ATTEMPTS = "LoginHistory.getFailedAttempts";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Boolean success;

	@Column(nullable = false)
	private Timestamp attemptTime;

	@Column(nullable = false)
	private String username;

	@Override
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String user) {
		this.username = user;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Timestamp getAttemptTime() {
		return attemptTime;
	}

	public void setAttemptTime(Timestamp attemptTime) {
		this.attemptTime = attemptTime;
	}

	@Override
	public String toString() {
		return "LoginHistory [id=" + id + ", success=" + success + ", attemptTime=" + attemptTime + ", username="
				+ username + "]";
	}

}
