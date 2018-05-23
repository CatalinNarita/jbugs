package edu.msg.ro.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.msg.ro.business.bug.dto.BugDTO;
import edu.msg.ro.business.bug.dto.mapper.BugDTOMapper;
import edu.msg.ro.business.bug.service.BugService;
import edu.msg.ro.business.exception.JBugsBusinessException;
import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.bug.dao.BugDao;
import edu.msg.ro.persistence.bug.entity.Attachment;
import edu.msg.ro.persistence.exceptions.JBugsPersistenceException;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.user.entity.Comment;

@ManagedBean
@ViewScoped
public class BugDetailsBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5784024951225412693L;

    @EJB
    private BugService bugService;

    @EJB
    private UserService userService;

    @EJB
    private BugDao bugDao;

    @EJB
    private BugDTOMapper bdtm;

    // @ManagedProperty("#{param.id}")
    private Long id;

    private boolean bugManagerPermission = false;

    private boolean closePermission = false;

    private boolean exportPermission = false;

    private BugDTO bugDetails;

    private String username;

    private List<Comment> comments;

    private String newCommnet = "";
    List<Attachment> attachments;

    @PostConstruct
    public void init() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        username = (String) session.getAttribute("username");
        bugManagerPermission = userService.hasPermission(username, PermissionType.BUG_MANAGEMENT);
        closePermission = userService.hasPermission(username, PermissionType.BUG_CLOSE);
        exportPermission = userService.hasPermission(username, PermissionType.BUG_EXPORT_PDF);
        id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        try {
            attachments = bugService.findAttachments(id);
        } catch (JBugsBusinessException e) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
            e.printStackTrace();
        }

        try {
            if (bugDao.findById(id) == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("bugs.xhtml");
            } else {
                bugDetails = bdtm.mapToDTO(bugDao.findById(id));
                comments = bugService.getBugComments(id);
            }
        } catch (JBugsPersistenceException | IOException | JBugsBusinessException e) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("bugs.xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public BugDTO getBugDetails() {
        return bugDetails;
    }

    public void setBugDetails(BugDTO bugDetails) {
        this.bugDetails = bugDetails;
    }

    public long getSelectedBugId(BugDTO bugDetails) {
        return bugDetails.getId();
    }

    public boolean isBugManagerPermission() {
        return bugManagerPermission;
    }

    public void setBugManagerPermission(boolean bugManagerPermission) {
        this.bugManagerPermission = bugManagerPermission;
    }

    public boolean isClosePermission() {
        return closePermission;
    }

    public void setClosePermission(boolean closePermission) {
        this.closePermission = closePermission;
    }

    public boolean isExportPermission() {
        return exportPermission;
    }

    public void setExportPermission(boolean exportPermission) {
        this.exportPermission = exportPermission;
    }

    public String goToDetails() {
        return "bugdetails";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BugService getBugService() {
        return bugService;
    }

    public void setBugService(BugService bugService) {
        this.bugService = bugService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public BugDao getBugDao() {
        return bugDao;
    }

    public void setBugDao(BugDao bugDao) {
        this.bugDao = bugDao;
    }

    public BugDTOMapper getBdtm() {
        return bdtm;
    }

    public void setBdtm(BugDTOMapper bdtm) {
        this.bdtm = bdtm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Attachment> getBugAttachments() {
        return attachments;
    }

    public void doDownload(Long id) throws IOException, SQLException {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            Attachment attachment = bugService.getAttachmentById(id);

            byte[] att = attachment.getAttachment();
            FileOutputStream fos = new FileOutputStream(attachment.getFileName());
            fos.write(att);
            fos.close();

            String fileName = attachment.getFileName();
            String contentType = ec.getMimeType(fileName);

            ec.responseReset();
            ec.setResponseContentType(contentType);
            // ec.setResponseContentLength(att.length);
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            OutputStream output = ec.getResponseOutputStream();
            File file = new File(attachment.getFileName());

            InputStream fileInputStream = new FileInputStream(file);

            byte[] bytesBuffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) {
                output.write(bytesBuffer, 0, bytesRead);
            }

            output.flush();

            fileInputStream.close();
            output.close();

            fc.responseComplete();
        } catch (JBugsBusinessException e) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
        }

    }

    public void postComment() {
        if (newCommnet == null || newCommnet.isEmpty() || newCommnet.trim().isEmpty()) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Comment", "Empty Comment");
            FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
        } else if (newCommnet.length() > 250) {
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Comment", "Too large");
            FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
        } else {

            try {
                bugService.postComment(id, newCommnet, username);
                FacesMessage facesMsg = new FacesMessage(FacesMessage.FACES_MESSAGES, "Comment was posted!");
                FacesContext.getCurrentInstance().addMessage("msg", facesMsg);

            } catch (JBugsBusinessException e) {
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
                FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
            } finally {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("bugdetails.xhtml?id=" + id);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public String severity() {
        return bugDetails.getSeverity();
    }

    public String status() {
        return bugDetails.getStatus();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getNewCommnet() {
        return newCommnet;
    }

    public void setNewCommnet(String newCommnet) {
        this.newCommnet = newCommnet;
    }

}
