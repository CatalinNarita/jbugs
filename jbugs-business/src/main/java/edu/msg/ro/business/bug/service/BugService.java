package edu.msg.ro.business.bug.service;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import edu.msg.ro.business.bug.dto.BugDTO;
import edu.msg.ro.business.bug.dto.mapper.BugDTOMapper;
import edu.msg.ro.business.exception.JBugsBusinessException;
import edu.msg.ro.persistence.bug.dao.BugDao;
import edu.msg.ro.persistence.bug.entity.Attachment;
import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.bug.entity.BugSeverityType;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.exceptions.JBugsPersistenceException;
import edu.msg.ro.persistence.user.dao.NotificationDao;
import edu.msg.ro.persistence.user.dao.UserDao;
import edu.msg.ro.persistence.user.entity.Comment;
import edu.msg.ro.persistence.user.entity.User;

@Stateless
public class BugService {

    @EJB
    private BugDao bugDao;

    @EJB
    private BugDTOMapper bugMapper;

    @EJB
    private UserDao userDao;

    @EJB
    private NotificationDao notificationDao;

    public List<BugDTO> getAllBugs() throws JBugsBusinessException {
        try {
            final List<Bug> allBugs = bugDao.findAll();
            return allBugs.stream().map(bugEntity -> bugMapper.mapToDTO(bugEntity)).collect(Collectors.toList());
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public int getNumberOfBugsByStatus(BugStatusType status) {
        return bugDao.getNumberOfBugsByStatus(status);
    }

    public int getNumberOfBugsByStatusForUser(BugStatusType status, String assignedTo) {
        return bugDao.getNumberOfBugsByStatusForUser(status, assignedTo);
    }

    public void updateBugFields(Bug bug, BugDTO bugDTO) {
        bug.setTitle(bugDTO.getTitle());
        bug.setDescription(bugDTO.getDescription());
        bug.setVersion(bugDTO.getVersion());
        bug.setFixedInVersion(bugDTO.getFixedInVersion());
        bug.setTargetDate(bugDTO.getTargetDate());
        bug.setSeverity(BugSeverityType.fromDisplayString(bugDTO.getSeverity().toLowerCase()));
        bug.setStatus(BugStatusType.fromDisplayString(bugDTO.getStatus().toLowerCase()));
        bug.setCreatedBy(bugDTO.getCreatedBy());
        bug.setAssignedTo(userDao.getUserByUserName(bugDTO.getAssignedTo().getUsername()));
    }

    public Bug updateBug(BugDTO bugDTO) throws JBugsBusinessException {
        try {
            Bug bug = bugDao.findById(bugDTO.getId());
            updateBugFields(bug, bugDTO);
            return bugDao.save(bug);
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public void updateBugAttachments(BugDTO bugDTO, List<String> attachmentNames) throws JBugsBusinessException {
        try {
            Bug bug = bugDao.findById(bugDTO.getId());
            int oldAttachments = bugDTO.getAttachments().size() - attachmentNames.size();
            for (int i = 0; i < attachmentNames.size(); i++) {
                Attachment att = new Attachment();
                att.setAttachment(bugDTO.getAttachments().get(oldAttachments + i).toByteArray());
                att.setBug(bug);
                att.setFileName(attachmentNames.get(i));
                bug.getAttachments().add(att);
            }
            bugDao.save(bug);
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public Bug addNewBug(String title, String description, String version, String fixedInVersion, Date targetDate,
                         String severity, String createdBy, User assignedTo, List<ByteArrayOutputStream> attachmentValues,
                         List<String> attachmentNames) throws JBugsBusinessException {

        // log "saving bug with id x and title"
        return addNewBugInternal(title, description, version, fixedInVersion, targetDate, severity, createdBy,
                assignedTo, attachmentValues, attachmentNames);
        // log "bug with id saved successfully"

    }

    private Bug addNewBugInternal(String title, String description, String version, String fixedInVersion,
                                  Date targetDate, String severity, String createdBy, User assignedTo,
                                  List<ByteArrayOutputStream> attachmentValues, List<String> attachmentNames) throws JBugsBusinessException {
        final Bug newBug = new Bug();
        newBug.setTitle(title);
        newBug.setDescription(description);
        newBug.setVersion(version);
        newBug.setFixedInVersion(fixedInVersion);
        newBug.setTargetDate(targetDate);
        newBug.setSeverity(BugSeverityType.fromDisplayString(severity.toLowerCase()));
        newBug.setStatus(BugStatusType.NEW);
        newBug.setCreatedBy(userDao.getUserByUserName(createdBy));
        newBug.setAssignedTo(userDao.getUserByUserName(assignedTo.getUsername()));
        List<Attachment> attachments = new ArrayList<>();
        if (attachmentValues != null)
            for (int i = 0; i < attachmentValues.size(); i++) {
                Attachment att = new Attachment();
                att.setAttachment(attachmentValues.get(i).toByteArray());
                att.setBug(newBug);
                att.setFileName(attachmentNames.get(i));
                attachments.add(att);
            }
        newBug.setAttachments(attachments);

        try {
            return bugDao.save(newBug);
        } catch (JBugsPersistenceException e) {
            // TODO Auto-generated catch block
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public List<Attachment> getAttachments(Long id) {
        return bugDao.getAttachments(id);
    }

    public boolean checkStatusChanged(final BugDTO newBug) throws JBugsBusinessException {
        try {
            Bug oldBug = bugDao.findById(newBug.getId());
            return !oldBug.getStatus().toString().equalsIgnoreCase(newBug.getStatus())
                    && !newBug.getStatus().equalsIgnoreCase("closed");
        } catch (JBugsPersistenceException e) {
            // TODO Auto-generated catch block
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public Bug getBugById(final Long id) throws JBugsBusinessException {
        try {
            return bugDao.findById(id);
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public boolean checkModified(final BugDTO newBug) throws JBugsBusinessException {
        try {
            Bug oldBug = bugDao.findById(newBug.getId());
            return !newBug.getTitle().equals(oldBug.getTitle()) || !newBug.getDescription().equals(oldBug.getDescription())
                    || !newBug.getVersion().equals(oldBug.getVersion())
                    || !newBug.getFixedInVersion().equals(oldBug.getFixedInVersion())
                    || !newBug.getSeverity().equalsIgnoreCase(oldBug.getSeverity().toString())
                    || !newBug.getAssignedTo().getUsername().equals(oldBug.getAssignedTo().getUsername());
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public boolean checkIfClosed(BugDTO newBug) throws JBugsBusinessException {
        try {
            Bug oldBug = bugDao.findById(newBug.getId());
            return !oldBug.getStatus().toString().equalsIgnoreCase(newBug.getStatus())
                    && newBug.getStatus().equalsIgnoreCase("closed");
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public Attachment getAttachmentById(Long id) throws JBugsBusinessException {
        try {
            return bugDao.getAttachmentById(id);
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public void deleteAttachment(Long id) throws JBugsBusinessException {
        try {
            bugDao.deleteAttachment(id);
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public List<Attachment> findAttachments(Long id) throws JBugsBusinessException {
        return bugDao.getAttachments(id);
    }

    public List<Comment> getBugComments(Long id) throws JBugsBusinessException {
        try {
            return bugDao.getComments(id);
        } catch (JBugsPersistenceException e) {
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

    public BugDTO postComment(Long bugId, String message, String username) throws JBugsBusinessException {
        BugDTO bug;
        try {
            Comment comment = new Comment();
            comment.setCommentText(message);
            comment.setBug(bugDao.findById(bugId));
            comment.setCommentDate(new Timestamp(System.currentTimeMillis()));
            comment.setUser(userDao.getUserByUserName(username));
            bug = bugMapper.mapToDTO(bugDao.postComment(bugId, comment));
            return bug;
        } catch (JBugsPersistenceException e) {
            // TODO Auto-generated catch block
            throw new JBugsBusinessException(e.getMessage(), e);
        }
    }

}
