package edu.msg.ro.persistence.bug.dao;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import edu.msg.ro.persistence.bug.entity.Attachment;
import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.exceptions.JBugsPersistenceException;
import edu.msg.ro.persistence.user.entity.Comment;

@Stateless
public class BugDao {

	@PersistenceContext(unitName = "jbugs-persistence")
	private EntityManager em;

	private static final Logger LOGGER = Logger.getLogger(BugDao.class.getName());

	public Bug save(final Bug bug) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "saving bug with id " + bug.getId() + "and title " + bug.getTitle());
			em.persist(bug);
			LOGGER.log(Level.ALL,
					"saving bug with id " + bug.getId() + "and title " + bug.getTitle() + "was successful");
			return bug;
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public Bug delete(final Bug bug) throws JBugsPersistenceException {
		em.remove(bug);
		return bug;
	}

	public List<Bug> findAll() throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "finding all bugs");
			final TypedQuery<Bug> query = em.createNamedQuery(Bug.FIND_ALL_BUGS, Bug.class);
			LOGGER.log(Level.ALL, "finding all bugs successful");
			return query.getResultList();
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public int getNumberOfBugsByStatus(BugStatusType status) {
		final TypedQuery<Bug> query = em.createNamedQuery(Bug.FIND_BUGS_BY_STATUS, Bug.class);
		query.setParameter("status", status);
		return query.getResultList().size();
	}

	public int getNumberOfBugsByStatusForUser(BugStatusType status, String assignedTo) {
		final TypedQuery<Bug> query = em.createNamedQuery(Bug.FIND_BUGS_BY_STATUS_FOR_USER, Bug.class);
		query.setParameter("status", status);
		query.setParameter("assignedTo", assignedTo);
		return query.getResultList().size();
	}

	/*
	 * Use it only if you don't have time to write a filter query. Note: very
	 * inefficient on a large database. Recommendation: write your own query for
	 * a filter.
	 */
	public List<Bug> findAll(Predicate<Bug> p) throws JBugsPersistenceException {
		try {
			return findAll().stream().filter(p).collect(Collectors.toList());
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public Bug update(final Bug bug) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "updating bug with id " + bug.getId() + "and title " + bug.getTitle());
			em.merge(bug);
			LOGGER.log(Level.ALL,
					"updating bug with id " + bug.getId() + "and title " + bug.getTitle() + " was successful");
			return bug;
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public Bug findById(final Long id) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "finding the bug with id " + id);
			return this.em.find(Bug.class, id);
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public List<Attachment> getAttachments(final Long id) {
		final TypedQuery<Attachment> query = em.createNamedQuery(Attachment.GET_ATTACHMENT, Attachment.class);
		query.setParameter("bugId", id);
		try {
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Attachment> findAttachements(final Long id) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "finding the attachements of bug with id " + id);
			return findById(id).getAttachments();
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public Attachment getAttachmentById(final Long id) throws JBugsPersistenceException {
		try {
			return em.find(Attachment.class, id);
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public void deleteAttachment(final Long id) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "finding the attachement with id " + id);
			em.remove(em.find(Attachment.class, id));
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public Comment getCommentsById(final Long id) {
		return em.find(Comment.class, id);
	}

	public List<Comment> findComments(final Long id) throws JBugsPersistenceException {
		try {
			LOGGER.log(Level.ALL, "finding the comments of bug with id " + id);
			return findById(id).getComments();
		} catch (PersistenceException e) {
			LOGGER.log(Level.ALL, "a persistence exception was thrown", e);
			throw new JBugsPersistenceException(e.getMessage(), e);
		}
	}

	public void deleteComment(final Long id) {
		em.remove(em.find(Comment.class, id));
	}

	public List<Comment> getComments(final Long id) throws JBugsPersistenceException {
		return findById(id).getComments();
	}
	
	public Bug postComment(Long id,Comment comment) throws JBugsPersistenceException{
		Bug bug = findById(id);
		bug.addComment(comment);
		return bug;
	}
}
