package edu.msg.ro.persistence.bug.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.msg.ro.persistence.user.entity.AbstractEntity;
import edu.msg.ro.persistence.user.entity.Comment;
import edu.msg.ro.persistence.user.entity.Notification;
import edu.msg.ro.persistence.user.entity.User;

@NamedQueries({ @NamedQuery(name = Bug.FIND_ALL_BUGS, query = "SELECT b from Bug b"),
		@NamedQuery(name = Bug.FIND_BUGS_BY_STATUS, query = "SELECT b from Bug b WHERE b.status = :status"),
		@NamedQuery(name = Bug.FIND_BUGS_BY_STATUS_FOR_USER, query = "SELECT b from Bug b WHERE b.status = :status and b.assignedTo.username = :assignedTo") })
@Entity
public class Bug extends AbstractEntity {
	public static final String FIND_ALL_BUGS = "Bug.findAllBugs";
	public static final String FIND_BUGS_BY_STATUS = "Bug.findBugsByStatus";
	public static final String FIND_BUGS_BY_STATUS_FOR_USER = "Bug.findBugsByStatusForUser";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Override
	public Long getId() {
		return this.id;
	}

	@Column
	private String title;

	@Column(length = 1000)
	private String description;

	@Column
	private String version;

	@Column
	private String fixedInVersion = "";

	@Column
	private Date targetDate;

	@Column
	private BugSeverityType severity;

	@Column
	private BugStatusType status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private User assignedTo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bug", cascade = CascadeType.ALL)
	private List<Comment> comments;

	@OneToOne(mappedBy = "bug")
	private Notification notification;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bug", cascade = CascadeType.PERSIST)
	private List<Attachment> attachments = new ArrayList<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFixedInVersion() {
		return fixedInVersion;
	}

	public void setFixedInVersion(String fixedInVersion) {
		this.fixedInVersion = fixedInVersion;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(final Date targetDate) {
		this.targetDate = targetDate;
	}

	public BugSeverityType getSeverity() {
		return severity;
	}

	public void setSeverity(final BugSeverityType severity) {
		this.severity = severity;
	}

	public BugStatusType getStatus() {
		return status;
	}

	public void setStatus(final BugStatusType status) {
		this.status = status;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(final User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	@Override
	public String toString() {
		return "Bug [id=" + id + ", title=" + title + ", description=" + description + ", version=" + version
				+ ", fixedInVersion=" + fixedInVersion + ", targetDate=" + targetDate + ", severity=" + severity
				+ ", status=" + status + ", createdBy=" + createdBy + ", assignedTo=" + assignedTo + ", comments="
				+ comments + ", notification=" + notification + ", attachments=" + attachments + "]";
	}

}
