package edu.msg.ro.persistence.user.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import edu.msg.ro.persistence.bug.entity.Bug;

@NamedQueries({
		@NamedQuery(name = Comment.GET_COMMENT, query = "SELECT c from Bug b, Comment c where b.id = c.bug.id and b.id = :bugId"),
		@NamedQuery(name = Comment.GET_COMMENTS, query = "SELECT c from Comment c where c.bug.id = :bugId") })
@Entity
public class Comment extends AbstractEntity {
	public static final String GET_COMMENT = "Comment.getComment";
	public static final String GET_COMMENTS = "Comment.getComments";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String commentText;

	@Column
	private Timestamp commentDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private Bug bug;

	@Override
	public Long getId() {
		return this.id;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(final String commentText) {
		this.commentText = commentText;
	}

	public Timestamp getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(final Timestamp commentDate) {
		this.commentDate = commentDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Bug getBug() {
		return bug;
	}

	public void setBug(Bug bug) {
		this.bug = bug;
	}
}
