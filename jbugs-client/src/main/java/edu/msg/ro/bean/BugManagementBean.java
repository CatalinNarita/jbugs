package edu.msg.ro.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import edu.msg.ro.business.bug.dto.BugDTO;
import edu.msg.ro.business.bug.service.BugService;
import edu.msg.ro.business.exception.JBugsBusinessException;
import edu.msg.ro.business.notification.service.NotificationMessageGenerator;
import edu.msg.ro.business.notification.service.NotificationService;
import edu.msg.ro.business.user.dto.UserDTO;
import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.export.csv.CSV;
import edu.msg.ro.export.pdf.PDF;
import edu.msg.ro.persistence.bug.entity.Attachment;
import edu.msg.ro.persistence.bug.entity.Bug;
import edu.msg.ro.persistence.bug.entity.BugSeverityType;
import edu.msg.ro.persistence.bug.entity.BugStatusType;
import edu.msg.ro.persistence.role.entity.PermissionType;
import edu.msg.ro.persistence.user.entity.Notification;
import edu.msg.ro.persistence.user.entity.NotificationType;
import edu.msg.ro.persistence.user.entity.User;

@ManagedBean
@ViewScoped
public class BugManagementBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -365020077363607409L;

	@EJB
	private UserService userService;

	@EJB
	private BugService bugService;

	@EJB
	private NotificationService notificationService;

	private List<UserDTO> allUsers;

	private List<String> allUsernames;

	private String username;

	private List<BugDTO> bugs;

	private List<BugDTO> filteredBugs;

	private List<String> statusList;

	private List<String> severityList;

	private BugDTO selectedBugForDetails;

	private boolean bugManagerPermission = false;

	private boolean closePermission = false;

	private boolean exportPermission = false;

	private BugDTO newBug = new BugDTO();

	private List<BugDTO> selectedBugs;

	@EJB
	private NotificationMessageGenerator messageGenerator;

	public List<UserDTO> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<UserDTO> allUsers) {
		this.allUsers = allUsers;
	}

	public List<BugDTO> getSelectedBugs() {
		return selectedBugs;
	}

	public void setSelectedBugs(List<BugDTO> selectedBugs) {
		this.selectedBugs = selectedBugs;
	}

	public List<String> getAllUsernames() {
		return allUsernames;
	}

	private Map<BugStatusType, List<BugStatusType>> bugStatusTransitions = new HashMap<BugStatusType, List<BugStatusType>>();

	private List<BugSeverityType> bugSeverityTypes;

	public List<BugStatusType> getBugStatusTransitionList(BugStatusType bugStatusType) {
		return bugStatusTransitions.get(bugStatusType);
	}

	private List<String> uploadedAttachmentNames = new ArrayList<>();

	@PostConstruct
	public void init() {
		bugs = getAllBugs();
		allUsers = userService.getAllUsers();
		// pt dropdown de la add user, instantiere ca sa scapam de null
		newBug.setAssignedTo(new User());
		allUsernames = allUsers.stream().map(user -> user.getUsername()).collect(Collectors.toList());

		statusList = Stream.of(BugStatusType.values()).map(BugStatusType::name).collect(Collectors.toList());
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		username = (String) session.getAttribute("username");

		bugManagerPermission = userService.hasPermission(username, PermissionType.BUG_MANAGEMENT);
		closePermission = userService.hasPermission(username, PermissionType.BUG_CLOSE);
		exportPermission = userService.hasPermission(username, PermissionType.BUG_EXPORT_PDF);

		severityList = Stream.of(BugSeverityType.values()).map(BugSeverityType::name).collect(Collectors.toList());
		List<BugStatusType> list = new ArrayList<>();

		list.add(BugStatusType.NEW);
		list.add(BugStatusType.IN_PROGRESS);
		list.add(BugStatusType.REJECTED);
		bugStatusTransitions.put(BugStatusType.NEW, list);

		list = new ArrayList<>();
		list.add(BugStatusType.IN_PROGRESS);
		list.add(BugStatusType.INFO_NEEDED);
		list.add(BugStatusType.REJECTED);
		list.add(BugStatusType.FIXED);
		bugStatusTransitions.put(BugStatusType.IN_PROGRESS, list);

		list = new ArrayList<>();
		list.add(BugStatusType.INFO_NEEDED);
		list.add(BugStatusType.IN_PROGRESS);
		bugStatusTransitions.put(BugStatusType.INFO_NEEDED, list);

		list = new ArrayList<>();
		list.add(BugStatusType.REJECTED);
		if (closePermission) {
			list.add(BugStatusType.CLOSED);
		}
		bugStatusTransitions.put(BugStatusType.REJECTED, list);

		list = new ArrayList<>();
		list.add(BugStatusType.FIXED);
		list.add(BugStatusType.NEW);
		if (closePermission) {
			list.add(BugStatusType.CLOSED);
		}
		bugStatusTransitions.put(BugStatusType.FIXED, list);

		list = new ArrayList<>();
		list.add(BugStatusType.CLOSED);
		bugStatusTransitions.put(BugStatusType.CLOSED, list);

		bugSeverityTypes = Arrays.asList(BugSeverityType.values());

	}

	public List<String> getSeverityList() {
		return severityList;
	}

	public void setSeverityList(List<String> severityList) {
		this.severityList = severityList;
	}

	public BugDTO getSelectedBugForDetails() {
		return selectedBugForDetails;
	}

	public void setSelectedBugForDetails(BugDTO selectedBugForDetails) {
		this.selectedBugForDetails = selectedBugForDetails;
	}

	public List<BugDTO> getFilteredBugs() {
		return filteredBugs;
	}

	public void setFilteredBugs(List<BugDTO> filteredBugs) {
		this.filteredBugs = filteredBugs;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public List<BugDTO> getBugs() {
		return bugs;
	}

	public void setBugs(List<BugDTO> bugs) {
		this.bugs = bugs;
	}

	public Map<BugStatusType, List<BugStatusType>> getBugStatusTransitions() {
		return bugStatusTransitions;
	}

	public List<BugSeverityType> getBugSeverityTypes() {
		return bugSeverityTypes;
	}

	public List<BugDTO> getAllBugs() {
		try {
			return bugService.getAllBugs();
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getUploadedAttachmentNames() {
		return uploadedAttachmentNames;
	}

	public void setUploadedAttachmentNames(List<String> uploadedAttachmentNames) {
		this.uploadedAttachmentNames = uploadedAttachmentNames;
	}

	public void onRowEdit(RowEditEvent event) {
		BugDTO selectedBug = (BugDTO) event.getObject();

		try {
			if (bugService.checkStatusChanged(selectedBug)) {
				updateBug(selectedBug, NotificationType.BUG_STATUS_UPDATED, "Bug status updated!");
			}
			if (bugService.checkModified(selectedBug)) {
				updateBug(selectedBug, NotificationType.BUG_UPDATED, "Bug updated!");
			}
			if (bugService.checkIfClosed(selectedBug)) {
				updateBug(selectedBug, NotificationType.BUG_CLOSED, "Bug closed!");
			}
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
		}
	}

	public void updateBug(BugDTO selectedBug, NotificationType status, String summary) {
	try {	Long id = selectedBug.getId();
		BugStatusType type = bugService.getBugById(selectedBug.getId()).getStatus();
		Bug bug;
		Notification notification = new Notification();
		if (status.equals(NotificationType.BUG_CLOSED)) {
			notification = notificationService.saveBugCloseNotification(selectedBug);
		} else if (status.equals(NotificationType.BUG_UPDATED)) {
			notification = notificationService.saveBugUpdatedNotification(selectedBug);
		} else if (status.equals(NotificationType.BUG_STATUS_UPDATED)) {
			notification = notificationService.saveBugStatusNotification(selectedBug, type);
		}
		try {
			bugService.updateBug(selectedBug);
		} catch (JBugsBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bug = bugService.getBugById(id);
		String detail = notification.getMessage();
		String channel = "/notify/" + selectedBug.getCreatedBy().getId();
		pushNotification(summary, detail, channel);
		channel = "/notify/" + bug.getAssignedTo().getId();
		pushNotification(summary, detail, channel);
	} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
		}
	}

	public void pushNotification(String summary, String detail, String channel) {
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish(channel, new FacesMessage(summary, detail));
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", ((BugDTO) event.getObject()).getTitle());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void updateBugAttachments() {
		try {
			if (uploadedAttachmentNames.size() > 0) {
				bugService.updateBugAttachments(selectedBugForDetails, uploadedAttachmentNames);
				notificationService.saveBugUpdatedNotification(selectedBugForDetails);
			}
			uploadedAttachmentNames = new ArrayList<>();
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
		}
	}

	public void generateCSV() throws IOException {
		try {
			CSV csv = new CSV();
			if (this.getSelectedBugs().size() == 0) {
				this.setSelectedBugs(bugService.getAllBugs());
			}
			csv.generate(selectedBugs);
			exportBugs(System.getProperty("user.home") + "\\bugs.csv", "bugs.csv");
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
		}
	}

	public void generatePDF() throws IOException {
		try {
			PDF pdf = new PDF();
			if (this.getSelectedBugs().size() == 0) {
				this.setSelectedBugs(bugService.getAllBugs());
			}
			pdf.generate(selectedBugs);
			exportBugs(System.getProperty("user.home") + "\\bugs.pdf", "bugs.pdf");
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
		}
	}

	public void exportBugs(String path, String fileName) throws IOException {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		byte[] att = Files.readAllBytes(Paths.get(path));

		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(att);
		fos.close();

		String contentType = ec.getMimeType(fileName);

		ec.responseReset();
		ec.setResponseContentType(contentType);
		ec.setResponseContentLength(att.length);
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		OutputStream output = ec.getResponseOutputStream();
		File file = new File(fileName);

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
	}

	public BugDTO getNewBug() {
		return newBug;
	}

	public void setNewBug(BugDTO newBug) {
		this.newBug = newBug;
	}

	public String getSessionUsername() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return (String) session.getAttribute("username");
	}

	public String doCreateBug() throws JBugsBusinessException {
		try {
			Bug bug = bugService.addNewBug(newBug.getTitle(), newBug.getDescription(), newBug.getVersion(),
					newBug.getFixedInVersion(), newBug.getTargetDate(), newBug.getSeverity(), getSessionUsername(),
					newBug.getAssignedTo(), newBug.getAttachments(), uploadedAttachmentNames);

			notificationService.saveBugCreatedNotification(bug);
			UserManagementBean.showMessage("bugs.newBugForm.add.success");
			return "bugs.xhtml/?faces-redirect=true";
		} catch (JBugsBusinessException e) {
			UserManagementBean.showMessage("bugs.newBugForm.add.fail");
		}
		return "bugs.xhtml/?faces-redirect=false";
	}

	public List<Attachment> getBugAttachments(Long id) {
		return bugService.getAttachments(id);
	}

	/*
	 * private static List<String> getFilename(List<Part> parts) { List<String>
	 * filenames = new ArrayList<>(); for (Part part : parts) { for (String cd :
	 * part.getHeader("content-disposition").split(";")) { if
	 * (cd.trim().startsWith("filename")) { String filename =
	 * cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	 * filenames.add(filename.substring(filename.lastIndexOf('/') + 1)
	 * .substring(filename.lastIndexOf('\\') + 1)); } } } return filenames; }
	 */

	public void doDownload(Long id) throws IOException, SQLException {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			Attachment attachment = bugService.getAttachmentById(id);

			if (attachment == null) {
				FacesMessage msg = new FacesMessage("Attachment:", "Bug has no attachment!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}

			byte[] att = attachment.getAttachment();

			FileOutputStream fos = new FileOutputStream(attachment.getFileName());
			fos.write(att);
			fos.close();

			String fileName = attachment.getFileName();
			String contentType = ec.getMimeType(fileName);

			ec.responseReset();
			ec.setResponseContentType(contentType);
			ec.setResponseContentLength(att.length);
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
			e.printStackTrace();
		}

	}

	public void deleteAttachment(Long id) {
		try {
			bugService.deleteAttachment(id);
			notificationService.saveBugUpdatedNotification(selectedBugForDetails);
		} catch (JBugsBusinessException e) {
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", facesMsg);
			e.printStackTrace();
		}
	}

	public ByteArrayOutputStream convertToByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[10240];
		for (int length = 0; (length = input.read(buffer)) > 0;)
			output.write(buffer, 0, length);
		return output;
	}

	public void uploadAttachment(FileUploadEvent e) {
		UploadedFile file = e.getFile();
		if (newBug.getAttachments() == null)
			newBug.setAttachments(new ArrayList<>());
		Long totalSize = 0L;
		for (ByteArrayOutputStream b : newBug.getAttachments())
			totalSize += b.size();
		totalSize += file.getSize();
		ResourceBundle messages = ResourceBundle.getBundle("jbugs.messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		if (totalSize > 26214400L) {
			FacesMessage msg = new FacesMessage(messages.getString("bugs.newBugForm.attachment") + ": "
					+ messages.getString("bugs.newBugForm.upload.exceededSize") + " " + file.getFileName() + " "
					+ messages.getString("bugs.newBugForm.upload.fail"));
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else
			try {
				newBug.getAttachments().add(convertToByteArray(file.getInputstream()));
				uploadedAttachmentNames.add(file.getFileName());
				FacesMessage msg = new FacesMessage(messages.getString("bugs.newBugForm.attachment") + ": "
						+ file.getFileName() + " " + messages.getString("bugs.newBugForm.upload.success"));
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}

	public void editUpload(FileUploadEvent e) {
		UploadedFile file = e.getFile();
		if (selectedBugForDetails.getAttachments() == null)
			selectedBugForDetails.setAttachments(new ArrayList<>());
		Long totalSize = 0L;
		for (ByteArrayOutputStream b : selectedBugForDetails.getAttachments())
			totalSize += b.size();
		totalSize += file.getSize();
		ResourceBundle messages = ResourceBundle.getBundle("jbugs.messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		if (totalSize > 26214400L) {
			FacesMessage msg = new FacesMessage(messages.getString("bugs.newBugForm.attachment") + ": "
					+ messages.getString("bugs.newBugForm.upload.exceededSize") + " " + file.getFileName() + " "
					+ messages.getString("bugs.newBugForm.upload.fail"));
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else
			try {
				selectedBugForDetails.getAttachments().add(convertToByteArray(file.getInputstream()));
				uploadedAttachmentNames.add(file.getFileName());
				FacesMessage msg = new FacesMessage(messages.getString("bugs.newBugForm.attachment") + ": "
						+ file.getFileName() + " " + messages.getString("bugs.newBugForm.upload.success"));
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Bug Selected", ((BugDTO) event.getObject()).getTitle());
		FacesContext.getCurrentInstance().addMessage(null, msg);

		System.out.println(selectedBugs);
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

	public String goToBugdetails(Long id) {
		return "/bugdetails.xhtml?id=" + id + "&faces-redirect=true";
	}

	public boolean hasPermissionToEdit(String username) {
		return getSessionUsername().equals(username);
	}

}
