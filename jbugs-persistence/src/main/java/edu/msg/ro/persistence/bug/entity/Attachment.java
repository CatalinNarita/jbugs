package edu.msg.ro.persistence.bug.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import edu.msg.ro.persistence.user.entity.AbstractEntity;

@NamedQueries({
		@NamedQuery(name = Attachment.GET_ATTACHMENT, query = "SELECT a from Bug b, Attachment a where b.id = a.bug.id and b.id = :bugId"),
		@NamedQuery(name = Attachment.GET_ATTACHMENTS, query = "SELECT a from Attachment a where a.bug.id = :bugId") })
@Entity
public class Attachment extends AbstractEntity {
	public static final String GET_ATTACHMENT = "Attachment.getAttachment";
	public static final String GET_ATTACHMENTS = "Attachment.getAttachments";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private Bug bug;

	@Column
	@Lob
	private byte[] attachment;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column
	private String fileName;

	public Bug getBug() {
		return bug;
	}

	public void setBug(Bug bug) {
		this.bug = bug;
	}

	@Override
	public Long getId() {
		return this.id;
	}

}
