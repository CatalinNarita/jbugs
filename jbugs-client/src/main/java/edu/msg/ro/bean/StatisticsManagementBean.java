package edu.msg.ro.bean;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import edu.msg.ro.business.bug.service.BugService;
import edu.msg.ro.business.user.service.UserService;
import edu.msg.ro.persistence.bug.entity.BugStatusType;

@ManagedBean
@ViewScoped
public class StatisticsManagementBean {

	@EJB
	private UserService userService;

	@EJB
	private BugService bugService;

	private BarChartModel bugStatusStatistics;

	@PostConstruct
	public void init() {
		createBugStatusStatistics();
	}

	public BarChartModel getBugStatusStatistics() {
		return bugStatusStatistics;
	}

	public void setBugStatusStatistics(BarChartModel bugStatusStatistics) {
		this.bugStatusStatistics = bugStatusStatistics;
	}

	public void createBugStatusStatistics() {
		ResourceBundle messages = ResourceBundle.getBundle("jbugs.messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		BarChartModel model = new BarChartModel();
		ChartSeries bugs = new ChartSeries();
		bugs.setLabel(messages.getString("users.bugPage"));
		bugs.set(messages.getString("bugs.bugForm.status.NEW"), bugService.getNumberOfBugsByStatus(BugStatusType.NEW));
		bugs.set(messages.getString("bugs.bugForm.status.IN_PROGRESS"),
				bugService.getNumberOfBugsByStatus(BugStatusType.IN_PROGRESS));
		bugs.set(messages.getString("bugs.bugForm.status.INFO_NEEDED"),
				bugService.getNumberOfBugsByStatus(BugStatusType.INFO_NEEDED));
		bugs.set(messages.getString("bugs.bugForm.status.REJECTED"),
				bugService.getNumberOfBugsByStatus(BugStatusType.REJECTED));
		bugs.set(messages.getString("bugs.bugForm.status.FIXED"),
				bugService.getNumberOfBugsByStatus(BugStatusType.FIXED));
		bugs.set(messages.getString("bugs.bugForm.status.CLOSED"),
				bugService.getNumberOfBugsByStatus(BugStatusType.CLOSED));
		model.addSeries(bugs);
		bugStatusStatistics = model;
		bugStatusStatistics.setTitle(messages.getString("bug.status.statistics"));
		bugStatusStatistics.setLegendPosition("ne");
		Axis xAxis = bugStatusStatistics.getAxis(AxisType.X);
		xAxis.setLabel(messages.getString("bugs.newBugForm.status"));
		Axis yAxis = bugStatusStatistics.getAxis(AxisType.Y);
		yAxis.setLabel(messages.getString("users.bugPage"));
		yAxis.setMin(0);
	}
}
