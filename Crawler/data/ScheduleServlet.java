18
https://raw.githubusercontent.com/WeBankFinTech/Schedulis/master/azkaban-web-server/src/main/java/azkaban/webapp/servlet/ScheduleServlet.java
/*
 * Copyright 2012 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package azkaban.webapp.servlet;

import azkaban.Constants;
import azkaban.executor.ExecutionOptions;
import azkaban.flow.Flow;
import azkaban.flow.Node;
import azkaban.project.Project;
import azkaban.project.ProjectLogEvent.EventType;
import azkaban.project.ProjectManager;
import azkaban.scheduler.Schedule;
import azkaban.scheduler.ScheduleManager;
import azkaban.scheduler.ScheduleManagerException;
import azkaban.server.HttpRequestUtils;
import azkaban.server.session.Session;
import azkaban.sla.SlaOption;
import azkaban.user.Permission;
import azkaban.user.Permission.Type;
import azkaban.user.User;
import azkaban.utils.Utils;
import azkaban.webapp.AzkabanWebServer;
import com.alibaba.fastjson.JSONObject;
import com.webank.wedatasphere.schedulis.common.i18nutils.LoadJsonUtils;
import com.webank.wedatasphere.schedulis.common.system.SystemManager;
import com.webank.wedatasphere.schedulis.common.system.SystemUserManagerException;
import com.webank.wedatasphere.schedulis.common.system.common.TransitionService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.DateTimeFormat;

public class ScheduleServlet extends LoginAbstractAzkabanServlet {

  private static final long serialVersionUID = 1L;
  private static final Logger logger = Logger.getLogger(ScheduleServlet.class);
  private ProjectManager projectManager;
  private ScheduleManager scheduleManager;
  private TransitionService transitionService;
  private SystemManager systemManager;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    final AzkabanWebServer server = (AzkabanWebServer) getApplication();
    this.projectManager = server.getProjectManager();
    this.scheduleManager = server.getScheduleManager();
    this.transitionService = server.getTransitionService();
    this.systemManager = transitionService.getSystemManager();
  }

  @Override
  protected void handleGet(final HttpServletRequest req, final HttpServletResponse resp,
      final Session session) throws ServletException, IOException {
    if (hasParam(req, "ajax")) {
      handleAJAXAction(req, resp, session);
    } else {
      handleGetAllSchedules(req, resp, session);
    }
  }

  private void handleAJAXAction(final HttpServletRequest req,
      final HttpServletResponse resp, final Session session) throws ServletException,
      IOException {
    final HashMap<String, Object> ret = new HashMap<>();
    final String ajaxName = getParam(req, "ajax");

    if (ajaxName.equals("slaInfo")) {//加载SLA告警接口
      ajaxSlaInfo(req, ret, session.getUser());
    } else if (ajaxName.equals("setSla")) {//设置SLA告警接口
      ajaxSetSla(req, ret, session.getUser());
      // alias loadFlow is preserved for backward compatibility
    } else if (ajaxName.equals("fetchSchedules") || ajaxName.equals("loadFlow")) {
      ajaxFetchSchedules(ret);
    } else if (ajaxName.equals("scheduleFlow")) {
      ajaxScheduleFlow(req, ret, session.getUser());
    } else if (ajaxName.equals("scheduleCronFlow")) {
      ajaxScheduleCronFlow(req, ret, session.getUser());
	  // FIXME Added interface to submit scheduled tasks for all job streams under the project.
    } else if (ajaxName.equals("ajaxScheduleCronAllFlow")) {
      ajaxScheduleCronAllFlow(req, ret, session.getUser());
    } else if (ajaxName.equals("fetchSchedule")) {
      ajaxFetchSchedule(req, ret, session.getUser());
	  // FIXME Added interface to get scheduling information based on ScheduleId.
    } else if (ajaxName.equals("getScheduleByScheduleId")) {
      ajaxGetScheduleByScheduleId(req, ret, session.getUser());
	  // FIXME Added interfaces to update scheduled task information.
    } else if (ajaxName.equals("scheduleEditFlow")) {
      ajaxUpdateSchedule(req, ret, session.getUser());
	  // FIXME Added interface to get information about all scheduled tasks.
    } else if(ajaxName.equals("ajaxFetchAllSchedules")){
      ajaxFetchAllSchedules(req, ret, session);
    }

    if (ret != null) {
      this.writeJSON(resp, ret);
    }
  }

  /**
   * 加载 ScheduleServlet 中的异常信息等国际化资源
   * @return
   */
  private Map<String, String> loadScheduleServletI18nData() {
    String languageType = LoadJsonUtils.getLanguageType();
    Map<String, String> dataMap;
    if (languageType.equalsIgnoreCase("zh_CN")) {
      dataMap = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-zh_CN.json",
          "azkaban.webapp.servlet.ScheduleServlet");
    }else {
      dataMap = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-en_US.json",
          "azkaban.webapp.servlet.ScheduleServlet");
    }
    return dataMap;
  }

  private void ajaxFetchSchedules(final HashMap<String, Object> ret) throws ServletException {
    final List<Schedule> schedules;
    try {
      schedules = this.scheduleManager.getSchedules();
    } catch (final ScheduleManagerException e) {
      throw new ServletException(e);
    }
    // See if anything is scheduled
    if (schedules.size() <= 0) {
      return;
    }

    final List<HashMap<String, Object>> output =
        new ArrayList<>();
    ret.put("items", output);

    for (final Schedule schedule : schedules) {
      try {
        writeScheduleData(output, schedule);
      } catch (final ScheduleManagerException e) {
        throw new ServletException(e);
      }
    }
  }

  private void writeScheduleData(final List<HashMap<String, Object>> output,
      final Schedule schedule) throws ScheduleManagerException {

    final HashMap<String, Object> data = new HashMap<>();
    data.put("scheduleid", schedule.getScheduleId());
    data.put("flowname", schedule.getFlowName());
    data.put("projectname", schedule.getProjectName());
    data.put("time", schedule.getFirstSchedTime());
    data.put("cron", schedule.getCronExpression());

    final DateTime time = DateTime.now();
    long period = 0;
    if (schedule.getPeriod() != null) {
      period = time.plus(schedule.getPeriod()).getMillis() - time.getMillis();
    }
    data.put("period", period);
    data.put("history", false);
    output.add(data);
  }

  private void ajaxSetSla(final HttpServletRequest req, final HashMap<String, Object> ret,
      final User user) {
    try {
      final int scheduleId = getIntParam(req, "scheduleId");
      final Schedule sched = this.scheduleManager.getSchedule(scheduleId);
      if (sched == null) {
        ret.put("error",
            "Error loading schedule. Schedule " + scheduleId
                + " doesn't exist");
        return;
      }

      final Project project = this.projectManager.getProject(sched.getProjectId());
      if (!hasPermission(project, user, Permission.Type.SCHEDULE)) {
        ret.put("error", "User " + user
            + " does not have permission to set SLA for this flow.");
        return;
      }

      final Flow flow = project.getFlow(sched.getFlowName());
      if (flow == null) {
        ret.put("status", "error");
        ret.put("message", "Flow " + sched.getFlowName() + " cannot be found in project "
            + project.getName());
        return;
      }

      String departmentSlaInform;
      if (hasParam(req, "departmentSlaInform")) {
        departmentSlaInform = getParam(req, "departmentSlaInform");
      } else {
        departmentSlaInform = "false";
      }
      final String emailStr = getParam(req, "slaEmails");
      final String[] emailSplit = emailStr.split("\\s*,\\s*|\\s*;\\s*|\\s+");
      final List<String> slaEmails = Arrays.asList(emailSplit);

      final Map<String, String> settings = getParamGroup(req, "settings");

      final Map<String, String> finishSettings = getParamGroup(req, "finishSettings");
      //设置SLA 超时告警配置项
      final List<SlaOption> slaOptions = new ArrayList<>();
      for (final String set : settings.keySet()) {
        final SlaOption sla;
        try {
          sla = parseSlaSetting(settings.get(set), flow, project);
        } catch (final Exception e) {
          throw new ServletException(e);
        }
        if (sla != null) {
          sla.getInfo().put(SlaOption.INFO_FLOW_NAME, sched.getFlowName());
          sla.getInfo().put(SlaOption.INFO_EMAIL_LIST, slaEmails);
          sla.getInfo().put(SlaOption.INFO_DEP_TYPE_INFORM, departmentSlaInform);
          slaOptions.add(sla);
        }
      }
      // FIXME Set the configuration of the task success and failure alarm.
      for (final String finish : finishSettings.keySet()) {
        final SlaOption sla;
        try {
          sla = parseFinishSetting(finishSettings.get(finish), flow, project);
        } catch (final Exception e) {
          throw new ServletException(e);
        }
        if (sla != null) {
          sla.getInfo().put(SlaOption.INFO_FLOW_NAME, sched.getFlowName());
          sla.getInfo().put(SlaOption.INFO_EMAIL_LIST, slaEmails);
          sla.getInfo().put(SlaOption.INFO_DEP_TYPE_INFORM, departmentSlaInform);
          slaOptions.add(sla);
        }
      }

      if (slaOptions.isEmpty()) {
        logger.warn(String.format("定时调度:[%s], 没有设置超时或者sla告警.", scheduleId));
      }

      sched.setSlaOptions(slaOptions);
      this.scheduleManager.insertSchedule(sched);
      this.projectManager.postProjectEvent(project, EventType.SLA,
          user.getUserId(), "SLA for flow " + sched.getFlowName()
              + " has been added/changed.");

    } catch (final ServletException e) {
      ret.put("error", e.getMessage());
    } catch (final ScheduleManagerException e) {
      logger.error(e.getMessage(), e);
      ret.put("error", e.getMessage());
    }

  }

  //解析前端规则字符串 转换成SlaOption对象
  private SlaOption parseSlaSetting(final String set, final Flow flow, final Project project) throws ScheduleManagerException {
    logger.info("Tryint to set sla with the following set: " + set);

    final String slaType;
    final List<String> slaActions = new ArrayList<>();
    final Map<String, Object> slaInfo = new HashMap<>();
    final String[] parts = set.split(",", -1);
    final String id = parts[0];
    final String rule = parts[1];
    final String duration = parts[2];
    final String level = parts[3];
    final String emailAction = parts[4];
    final String killAction = parts[5];

    Map<String, String> dataMap = loadScheduleServletI18nData();

    List<Flow> embeddedFlows = project.getFlows();

    if (emailAction.equals("true") || killAction.equals("true")) {
      if (emailAction.equals("true")) {
        slaActions.add(SlaOption.ACTION_ALERT);
        slaInfo.put(SlaOption.ALERT_TYPE, "email");
      }
      if (killAction.equals("true")) {
        final String killActionType =
            id.equals("") ? SlaOption.ACTION_CANCEL_FLOW : SlaOption.ACTION_KILL_JOB;
        slaActions.add(killActionType);
      }
      if (id.equals("")) {//FLOW告警模式设置
        if (rule.equals("SUCCESS")) {
          slaType = SlaOption.TYPE_FLOW_SUCCEED;
        } else {
          slaType = SlaOption.TYPE_FLOW_FINISH;
        }
      } else {
	  	// FIXME JOB alarm mode is optimized to implement job, sub-job, and sub-flow alarms.
        Node node = flow.getNode(id);
        if(node != null && "flow".equals(node.getType())){//如果是flow类型的Job获取它真正执行的FlowId
          slaInfo.put(SlaOption.INFO_JOB_NAME, id);
          slaInfo.put(SlaOption.INFO_EMBEDDED_ID, node.getEmbeddedFlowId());
        }else{
          slaInfo.put(SlaOption.INFO_JOB_NAME, id);
        }

        String str[] = id.split(":");
        for (Flow f: embeddedFlows) {
          Node n = f.getNode(str[str.length -1]);
          if(n != null && n.getType().equals("flow")) {
            logger.info(id + " is embeddedFlow.");
            slaInfo.put(SlaOption.INFO_EMBEDDED_ID, n.getEmbeddedFlowId());
            break;
          }
        }

        if (rule.equals("SUCCESS")) {
          slaType = SlaOption.TYPE_JOB_SUCCEED;
        } else {
          slaType = SlaOption.TYPE_JOB_FINISH;
        }
      }

      final ReadablePeriod dur;
      try {
        dur = parseDuration(duration);
      } catch (final Exception e) {
        throw new ScheduleManagerException(dataMap.get("schTimeFormatError"));
      }

      slaInfo.put(SlaOption.INFO_DURATION, Utils.createPeriodString(dur));
      final SlaOption r = new SlaOption(slaType, slaActions, slaInfo, level);
      logger.info("Parsing sla as id:" + id + " type:" + slaType + " rule:"
          + rule + " Duration:" + duration + " actions:" + slaActions);
      return r;
    }
    return null;
  }

  private ReadablePeriod parseDuration(final String duration) {
    final int hour = Integer.parseInt(duration.split(":")[0]);
    final int min = Integer.parseInt(duration.split(":")[1]);
    return Minutes.minutes(min + hour * 60).toPeriod();
  }

  private void ajaxFetchSchedule(final HttpServletRequest req,
      final HashMap<String, Object> ret, final User user) throws ServletException {

    final int projectId = getIntParam(req, "projectId");
    final String flowId = getParam(req, "flowId");
    try {
      final Schedule schedule = this.scheduleManager.getSchedule(projectId, flowId);

      if (schedule != null) {
        final Map<String, Object> jsonObj = new HashMap<>();
        jsonObj.put("scheduleId", Integer.toString(schedule.getScheduleId()));
        jsonObj.put("submitUser", schedule.getSubmitUser());
        jsonObj.put("firstSchedTime",
            utils.formatDateTime(schedule.getFirstSchedTime()));
        jsonObj.put("nextExecTime",
            utils.formatDateTime(schedule.getNextExecTime()));
        jsonObj.put("period", utils.formatPeriod(schedule.getPeriod()));
        jsonObj.put("cronExpression", schedule.getCronExpression());
        jsonObj.put("executionOptions", schedule.getExecutionOptions());
        ret.put("schedule", jsonObj);
      }
    } catch (final ScheduleManagerException e) {
      logger.error(e.getMessage(), e);
      ret.put("error", e);
    }
  }
  //前端加载已有数据
  private void ajaxSlaInfo(final HttpServletRequest req, final HashMap<String, Object> ret,
      final User user) {
    final int scheduleId;
    try {
      scheduleId = getIntParam(req, "scheduleId");
      final Schedule sched = this.scheduleManager.getSchedule(scheduleId);
      if (sched == null) {
        ret.put("error",
            "Error loading schedule. Schedule " + scheduleId
                + " doesn't exist");
        return;
      }

      final Project project =
          getProjectAjaxByPermission(ret, sched.getProjectId(), user, Type.READ);
      if (project == null) {
        ret.put("error",
            "Error loading project. Project " + sched.getProjectId()
                + " doesn't exist");
        return;
      }

      final Flow flow = project.getFlow(sched.getFlowName());
      if (flow == null) {
        ret.put("error", "Error loading flow. Flow " + sched.getFlowName()
            + " doesn't exist in " + sched.getProjectId());
        return;
      }

      final List<SlaOption> slaOptions = sched.getSlaOptions();
      final ExecutionOptions flowOptions = sched.getExecutionOptions();

      if (slaOptions != null && slaOptions.size() > 0) {
        ret.put("slaEmails", slaOptions.get(0).getInfo().get(SlaOption.INFO_EMAIL_LIST));
        ret.put("departmentSlaInform", slaOptions.get(0).getInfo().get(SlaOption.INFO_DEP_TYPE_INFORM));

        final List<Object> setObj = new ArrayList<>();
        final List<Object> finishObj = new ArrayList<>();
        for (final SlaOption sla : slaOptions) {

          if(sla.getType().equals(SlaOption.TYPE_FLOW_FAILURE_EMAILS) ||
              sla.getType().equals(SlaOption.TYPE_FLOW_SUCCESS_EMAILS) ||
              sla.getType().equals(SlaOption.TYPE_FLOW_FINISH_EMAILS) ||
              sla.getType().equals(SlaOption.TYPE_JOB_FAILURE_EMAILS) ||
              sla.getType().equals(SlaOption.TYPE_JOB_SUCCESS_EMAILS) ||
              sla.getType().equals(SlaOption.TYPE_JOB_FINISH_EMAILS)){
            finishObj.add(sla.toWebObject());
          }else{
            setObj.add(sla.toWebObject());
          }

        }
        ret.put("settings", setObj);
        ret.put("finishSettings", finishObj);
      } else if (flowOptions != null) {
        if (flowOptions.getFailureEmails() != null) {
          final List<String> emails = flowOptions.getFailureEmails();
          if (emails.size() > 0) {
            ret.put("slaEmails", emails);
          }
        }
      } else {
        if (flow.getFailureEmails() != null) {
          final List<String> emails = flow.getFailureEmails();
          if (emails.size() > 0) {
            ret.put("slaEmails", emails);
          }
        }
      }

      final List<String> allJobs = new ArrayList<>();
	  // FIXME Show all jobs including sub-job stream jobs.
      getAllJob(allJobs, flow.getNodes(), project, flow.getId());
      List<String> jobAndFlow = allJobs.stream().map(item -> item.replace(flow.getId() + ":","")).collect(Collectors.toList());
      ret.put("allJobNames", jobAndFlow);
    } catch (final ServletException e) {
      ret.put("error", e);
    } catch (final ScheduleManagerException e) {
      logger.error(e.getMessage(), e);
      ret.put("error", e);
    }
  }

  private void getAllJob(List<String> allJobs, Collection<Node> nodes, Project project, String flowName){
    for (final Node n : nodes) {
      if(n.getEmbeddedFlowId() != null){
        final Flow childFlow = project.getFlow(n.getEmbeddedFlowId());
        getAllJob(allJobs, childFlow.getNodes(), project, flowName + ":" + n.getId());
      }
      allJobs.add(flowName + ":" + n.getId());
    }
  }

  protected Project getProjectAjaxByPermission(final Map<String, Object> ret,
      final int projectId, final User user, final Permission.Type type) {
    final Project project = this.projectManager.getProject(projectId);

    if (project == null) {
      ret.put("error", "Project '" + project + "' not found.");
    } else if (!hasPermission(project, user, type)) {
      ret.put("error",
          "User '" + user.getUserId() + "' doesn't have " + type.name()
              + " permissions on " + project.getName());
    } else {
      return project;
    }

    return null;
  }

  private void handleGetAllSchedules(final HttpServletRequest req,
      final HttpServletResponse resp, final Session session) throws ServletException,
      IOException {

    final Page page = newPage(req, resp, session,"azkaban/webapp/servlet/velocity/scheduledflowpage.vm");

    final List<Schedule> schedules = new ArrayList<>();
	  // FIXME New function Permission judgment, admin user can view all shedule tasks, user can only view their own tasks.
    try {

      //如果输入了快捷搜索
      if (hasParam(req, "search")) {
        //去除搜索字符串的空格
        final String searchTerm = getParam(req, "searchterm").trim();
        Set<String> userRoleSet = new HashSet<>();
        userRoleSet.addAll(session.getUser().getRoles());
        //权限判断 admin 用户能查看所有的 shedule 任务, user只能查看自己的
        if(userRoleSet.contains("admin")){
          for (Schedule schedule : this.scheduleManager.getSchedules()){
            if(schedule.getProjectName().contains(searchTerm) || schedule.getFlowName().contains(searchTerm)){
              schedules.add(schedule);
            }
          }
        }else{
          List<Project> userProjectList = this.projectManager.getUserAllProjects(session.getUser(), null);
          for(Schedule schedule : this.scheduleManager.getSchedules()){
            for(Project project : userProjectList){
              if(project.getId() == schedule.getProjectId()){
                if(schedule.getProjectName().contains(searchTerm) || schedule.getFlowName().contains(searchTerm)){
                  schedules.add(schedule);
                }
              }
            }
          }
        }
      } else {
        Set<String> userRoleSet = new HashSet<>();
        userRoleSet.addAll(session.getUser().getRoles());
        //权限判断 admin 用户能查看所有的 shedule 任务, user只能查看自己的
        if(userRoleSet.contains("admin")){
          schedules.addAll(this.scheduleManager.getSchedules());
        }else{
          List<Project> userProjectList = this.projectManager.getUserAllProjects(session.getUser(), null);
          //schedules = this.scheduleManager.getSchedulesByUser(session.getUser());

          for(Schedule schedule : this.scheduleManager.getSchedules()){
            for(Project project : userProjectList){
              if(project.getId() == schedule.getProjectId()){
                schedules.add(schedule);
              }
            }
          }
        }
      }

    } catch (final ScheduleManagerException e) {
      throw new ServletException(e);
    }
    String languageType = LoadJsonUtils.getLanguageType();
    Map<String, String> scheduledflowpageMap;
    Map<String, String> subPageMap1;
    Map<String, String> subPageMap2;
    Map<String, String> subPageMap3;
    Map<String, String> subPageMap4;
    if (languageType.equalsIgnoreCase("zh_CN")) {
      scheduledflowpageMap = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-zh_CN.json",
          "azkaban.webapp.servlet.velocity.scheduledflowpage.vm");
      subPageMap1 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-zh_CN.json",
          "azkaban.webapp.servlet.velocity.nav.vm");
      subPageMap2 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-zh_CN.json",
          "azkaban.webapp.servlet.velocity.slapanel.vm");
      subPageMap3 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-zh_CN.json",
          "azkaban.webapp.servlet.velocity.schedule-flow-edit-panel.vm");
      subPageMap4 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-zh_CN.json",
          "azkaban.webapp.servlet.velocity.messagedialog.vm");
    }else {
      scheduledflowpageMap = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-en_US.json",
          "azkaban.webapp.servlet.velocity.scheduledflowpage.vm");
      subPageMap1 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-en_US.json",
          "azkaban.webapp.servlet.velocity.nav.vm");
      subPageMap2 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-en_US.json",
          "azkaban.webapp.servlet.velocity.slapanel.vm");
      subPageMap3 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-en_US.json",
          "azkaban.webapp.servlet.velocity.schedule-flow-edit-panel.vm");
      subPageMap4 = LoadJsonUtils.transJson("/com.webank.wedatasphere.schedulis.i18n.conf/azkaban-web-server-en_US.json",
          "azkaban.webapp.servlet.velocity.messagedialog.vm");
    }
    scheduledflowpageMap.forEach(page::add);
    subPageMap1.forEach(page::add);
    subPageMap2.forEach(page::add);
    subPageMap3.forEach(page::add);
    subPageMap4.forEach(page::add);

    page.add("loginUser", session.getUser().getUserId());
    Collections.sort(schedules, (a,b) -> a.getScheduleId() > b.getScheduleId() ? -1 : 1);
    page.add("schedules", schedules);
    page.add("currentlangType", languageType);
    page.render();
  }

  private void ajaxFetchAllSchedules(final HttpServletRequest req,
      final HashMap<String, Object> ret,
      final Session session) throws ServletException, IOException {
    int pageNum = getIntParam(req, "page", 1);
    final int pageSize = getIntParam(req, "size", 20);

    if (pageNum < 0) {
      pageNum = 1;
    }

    final List<Schedule> schedules = new ArrayList<>();
	  // FIXME New function Permission judgment, admin user can view all shedule tasks, user can only view their own tasks.
    try {
      User user = session.getUser();
      //如果输入了快捷搜索
      if (hasParam(req, "search")) {
        //去除搜索字符串的空格
        final String searchTerm = getParam(req, "searchterm").trim();
        Set<String> userRoleSet = new HashSet<>();
        userRoleSet.addAll(user.getRoles());
        //权限判断 admin 用户能查看所有的 schedule 任务, user只能查看自己的
        if(userRoleSet.contains("admin")){
          for (Schedule schedule : this.scheduleManager.getSchedules()){
            // 匹配搜索参数
            checkAndAddSchedule(searchTerm, schedule, schedules);
          }

        } else {
          List<Project> userProjectList = this.projectManager.getUserAllProjects(session.getUser(), null);
          for(Schedule schedule : this.scheduleManager.getSchedules()){
            for(Project project : userProjectList){
              if(project.getId() == schedule.getProjectId()){
                // 匹配搜索参数
                checkAndAddSchedule(searchTerm, schedule, schedules);
              }
            }
          }
        }
      } else {
        Set<String> userRoleSet = new HashSet<>();
        userRoleSet.addAll(session.getUser().getRoles());
        //权限判断 admin 用户能查看所有的 shedule 任务, user只能查看自己的
        if(userRoleSet.contains("admin")){
          for (Schedule adminSchedule : this.scheduleManager.getSchedules()) {
            // 检查工作流是否有效
            checkValidFlows(schedules, adminSchedule);
          }
        } else {
          List<Project> userProjectList = this.projectManager.getUserAllProjects(session.getUser(), null);
          for(Schedule schedule : this.scheduleManager.getSchedules()){

            for(Project project : userProjectList){
              if(project.getId() == schedule.getProjectId()){
                // 检查工作流是否有效
                checkValidFlows(schedules, schedule);
              }
            }
          }
        }
      }

    } catch (final ScheduleManagerException e) {
      throw new ServletException(e);
    }
    Collections.sort(schedules, (a,b) -> a.getScheduleId() > b.getScheduleId() ? -1 : 1);
    int total = schedules.size();
    List<Schedule> subList = getSchedules(pageNum, pageSize, total, schedules);
    ret.put("total", schedules.size());
    ret.put("page", pageNum);
    ret.put("size", pageSize);
    Map<String, String> dataMap = loadScheduleServletI18nData();

    ret.put("schConfig", dataMap.get("schConfig"));
    ret.put("slaSetting", dataMap.get("slaSetting"));
    ret.put("deleteSch", dataMap.get("deleteSch"));
    ret.put("showParam", dataMap.get("showParam"));
    ret.put("schedules", subList);
  }

  /**
   * 匹配查询参数
   * @param searchTerm
   * @param schedule
   * @param schedules
   */
  private void checkAndAddSchedule(String searchTerm, Schedule schedule, List<Schedule> schedules) {
    boolean projectContainResult = schedule.getProjectName().contains(searchTerm);
    boolean flowNameContainResult = schedule.getFlowName().contains(searchTerm);
    boolean submitUserContainsResult = schedule.getSubmitUser().contains(searchTerm);
    if(projectContainResult || flowNameContainResult || submitUserContainsResult){
      // 检查工作流是否有效
      checkValidFlows(schedules, schedule);
    }
  }

  /**
   * 检查工作流是否有效
   * @param schedules
   * @param schedule
   */
  private void checkValidFlows(List<Schedule> schedules, Schedule schedule) {
    String flowName = schedule.getFlowName();

    // 查找项目, 获取项目所有工作流
    int projectId = schedule.getProjectId();
    Project dbProject = this.projectManager.getProject(projectId);
    if (null != dbProject) {
      List<Flow> flows = dbProject.getFlows();

      Map<String, Object> otherOption = schedule.getOtherOption();
      // 取出当前项目的所有flow名称进行判断
      List<String> flowNameList = flows.stream().map(Flow::getId).collect(Collectors.toList());
      if (flowNameList.contains(flowName)) {
        otherOption.put("validFlow", true);
      } else {
        otherOption.put("validFlow", false);
      }
      schedule.setOtherOption(otherOption);
      schedules.add(schedule);
    }

  }

  private List<Schedule> getSchedules(int pageNum, int pageSize, int total, final List<Schedule> schedules){
    List<Schedule> list = new ArrayList<>();
    int startIndex = (pageNum-1) * pageSize;
    int endIndex = pageNum * pageSize;
    try {
      if(endIndex <= total) {
        list = schedules.subList((pageNum - 1) * pageSize, pageNum * pageSize);
      } else if(startIndex < total){
        list = schedules.subList((pageNum - 1) * pageSize, total);
      }
    } catch (Exception e){
      logger.error("截取schedule list失败 " + e);
    }
    return list;
  }

  @Override
  protected void handlePost(final HttpServletRequest req, final HttpServletResponse resp,
      final Session session) throws ServletException, IOException {
    if (hasParam(req, "ajax")) {
      handleAJAXAction(req, resp, session);
    } else {
      final HashMap<String, Object> ret = new HashMap<>();
      if (hasParam(req, "action")) {
        final String action = getParam(req, "action");
        if (action.equals("scheduleFlow")) {
          ajaxScheduleFlow(req, ret, session.getUser());
        } else if (action.equals("scheduleCronFlow")) {
          ajaxScheduleCronFlow(req, ret, session.getUser());
        } else if (action.equals("removeSched")) {
          ajaxRemoveSched(req, ret, session.getUser());
        }
      }

      if (ret.get("status") == ("success")) {
        setSuccessMessageInCookie(resp, (String) ret.get("message"));
      } else {
        setErrorMessageInCookie(resp, (String) ret.get("message"));
      }

      this.writeJSON(resp, ret);
    }
  }

  private void ajaxRemoveSched(final HttpServletRequest req, final Map<String, Object> ret,
      final User user) throws ServletException {
    final int scheduleId = getIntParam(req, "scheduleId");
    final Schedule sched;

    Map<String, String> dataMap = loadScheduleServletI18nData();

    try {
      sched = this.scheduleManager.getSchedule(scheduleId);
    } catch (final ScheduleManagerException e) {
      throw new ServletException(e);
    }
    if (sched == null) {
      ret.put("message", "Schedule with ID " + scheduleId + " does not exist");
      ret.put("status", "error");
      return;
    }

    final Project project = this.projectManager.getProject(sched.getProjectId());

    if (project == null) {
      ret.put("message", "Project " + sched.getProjectId() + " does not exist");
      ret.put("status", "error");
      return;
    }

    if (!hasPermission(project, user, Type.SCHEDULE)) {
      ret.put("status", "error");
      ret.put("message", "Permission denied. Cannot remove schedule with id "
          + scheduleId);
      return;
    }

    this.scheduleManager.removeSchedule(sched);
    logger.info("User '" + user.getUserId() + " has removed schedule "
        + sched.getScheduleName());
    this.projectManager
        .postProjectEvent(project, EventType.SCHEDULE, user.getUserId(),
            "Schedule " + sched.toString() + " has been removed.");

    ret.put("status", "success");
    ret.put("message", dataMap.get("flow") + sched.getFlowName() + dataMap.get("deleteFromSch"));
    return;
  }
  //远程 ajax API 提供给远程 HTTP 调用
  @Deprecated
  private void ajaxScheduleFlow(final HttpServletRequest req,
      final HashMap<String, Object> ret, final User user) throws ServletException {
    final String projectName = getParam(req, "projectName");
    final String flowName = getParam(req, "flow");
    final int projectId = getIntParam(req, "projectId");

    final Project project = this.projectManager.getProject(projectId);

    Map<String, String> dataMap = loadScheduleServletI18nData();
    if (project == null) {
      ret.put("message", "Project " + projectName + " does not exist");
      ret.put("status", "error");
      return;
    }

    if (!hasPermission(project, user, Type.SCHEDULE)) {
      ret.put("status", "error");
      ret.put("message", "Permission denied. Cannot execute " + flowName);
      return;
    }

    final Flow flow = project.getFlow(flowName);
    if (flow == null) {
      ret.put("status", "error");
      ret.put("message", "Flow " + flowName + " cannot be found in project "
          + projectName);
      return;
    }

    final String scheduleTime = getParam(req, "scheduleTime");
    final String scheduleDate = getParam(req, "scheduleDate");
    final DateTime firstSchedTime;
    try {
      firstSchedTime = parseDateTime(scheduleDate, scheduleTime);
    } catch (final Exception e) {
      ret.put("error", "Invalid date and/or time '" + scheduleDate + " "
          + scheduleTime);
      return;
    }

    final long endSchedTime = getLongParam(req, "endSchedTime",
        Constants.DEFAULT_SCHEDULE_END_EPOCH_TIME);
    try {
      // Todo kunkun-tang: Need to verify if passed end time is valid.
    } catch (final Exception e) {
      ret.put("error", "Invalid date and time: " + endSchedTime);
      return;
    }

    ReadablePeriod thePeriod = null;
    try {
      if (hasParam(req, "is_recurring")
          && getParam(req, "is_recurring").equals("on")) {
        thePeriod = Schedule.parsePeriodString(getParam(req, "period"));
      }
    } catch (final Exception e) {
      ret.put("error", e.getMessage());
    }

    ExecutionOptions flowOptions = null;
    try {
      flowOptions = HttpRequestUtils.parseFlowOptions(req);
      HttpRequestUtils.filterAdminOnlyFlowParams(flowOptions, user);
    } catch (final Exception e) {
      ret.put("error", e.getMessage());
    }

    final List<SlaOption> slaOptions = null;

    final Schedule schedule =
        this.scheduleManager.scheduleFlow(-1, projectId, projectName, flowName,
            "ready", firstSchedTime.getMillis(), endSchedTime, firstSchedTime.getZone(),
            thePeriod, DateTime.now().getMillis(), firstSchedTime.getMillis(),
            firstSchedTime.getMillis(), user.getUserId(), flowOptions,
            slaOptions);
    logger.info("User '" + user.getUserId() + "' has scheduled " + "["
        + projectName + flowName + " (" + projectId + ")" + "].");
    this.projectManager.postProjectEvent(project, EventType.SCHEDULE,
        user.getUserId(), "Schedule " + schedule.toString()
            + " has been added.");

    ret.put("status", "success");
    ret.put("scheduleId", schedule.getScheduleId());
    ret.put("message", projectName + "." + flowName + dataMap.get("startSch"));
  }

  private void ajaxScheduleCronAllFlow(final HttpServletRequest req,
      final HashMap<String, Object> ret, final User user) throws ServletException {
    JSONObject request = HttpRequestUtils.parseRequestToJsonObject(req);
    String projectName = request.getString("project");
    final Project project = this.projectManager.getProject(projectName);
    Map<String, String> dataMap = loadScheduleServletI18nData();
    if (project == null) {
      ret.put("error", "Project " + projectName + " does not exist");
      ret.put("status", "error");
      logger.error(projectName + ", project " + projectName + " does not exist");
      return;
    }
    if (project.getFlows() == null || project.getFlows().size() == 0) {
      ret.put("error", projectName + dataMap.get("haveNoFlows"));
      ret.put("status", "error");
      logger.error(projectName + ", 没有作业流。");
      return;
    }
    final int projectId = project.getId();
    if (!hasPermission(project, user, Type.SCHEDULE)) {
      ret.put("status", "error");
      ret.put("error", "Permission denied. Cannot execute " + projectName);
      logger.error(projectName + ", Permission denied. Cannot execute.");
      return;
    }
    List<Flow> rootFlows = project.getAllRootFlows();
    StringBuilder sb = new StringBuilder();
    for(Flow flow: rootFlows){
      try {
        scheduleAllFlow(project, flow, ret, request, user, sb);
      }catch (ServletException se){
        logger.error("flow: " + flow.getId() + "新增定时调度失败");
        sb.append(String.format("Error, flow:%s, msg:", flow.getId(), dataMap.get("addSchFailed")));
      }
    }
    ret.put("code", "200");
    ret.put("message", sb.toString());
  }

  private boolean scheduleAllFlow(Project project, Flow flow, Map<String, Object> ret, JSONObject json, User user, StringBuilder msg) throws ServletException {
    final String projectName = project.getName();
    final String flowName = flow.getId();
    final int projectId = project.getId();

    final boolean hasFlowTrigger;

    Map<String, String> dataMap = loadScheduleServletI18nData();
    try {
      hasFlowTrigger = this.projectManager.hasFlowTrigger(project, flow);
    } catch (final Exception ex) {
      logger.error(ex);
      msg.append(String.format("Error, looking for flow trigger of flow: %s.%s. <br/>",
          projectName, flowName));
      return false;
    }

    if (hasFlowTrigger) {
      msg.append(String.format("Error: Flow %s.%s is already "
              + "associated with flow trigger, so schedule has to be defined in flow trigger config. <br/>",
          projectName, flowName));
      return false;
    }

    final DateTimeZone timezone = DateTimeZone.getDefault();
    final DateTime firstSchedTime = getPresentTimeByTimezone(timezone);

    String cronExpression = null;
    try {
      if (json.containsKey("cronExpression")) {
        // everything in Azkaban functions is at the minute granularity, so we add 0 here
        // to let the expression to be complete.
        cronExpression = json.getString("cronExpression");
        if (azkaban.utils.Utils.isCronExpressionValid(cronExpression, timezone) == false) {
          ret.put("error", "Error," + dataMap.get("thisExpress") + cronExpression + dataMap.get("outRuleQuartz") + "<br/>");
          return false;
        }
      }
      if (cronExpression == null) {
        msg.append("Cron expression must exist.<br/>");
        return false;
      }
    } catch (final Exception e) {
      msg.append(e.getMessage() + "<br/>");
      logger.error(e);
      return false;
    }

    final long endSchedTime = (Long) json.getOrDefault("endSchedTime",
        Constants.DEFAULT_SCHEDULE_END_EPOCH_TIME);

    ExecutionOptions flowOptions = null;
    try {
      flowOptions = HttpRequestUtils.parseFlowOptions(json);
      HttpRequestUtils.filterAdminOnlyFlowParams(flowOptions, user);
    } catch (final Exception e) {
      logger.error(e);
    }
    //设置其他参数配置
    Map<String, Object> otherOptions = new HashMap<>();
    //设置失败重跑配置
    final List<Map<String, String>> jobRetryList = new ArrayList<>();
    otherOptions.put("jobFailedRetryOptions", jobRetryList);

    //设置失败跳过配置
    final List<String> jobSkipList = new ArrayList<>();
    otherOptions.put("jobSkipFailedOptions", jobSkipList);

    //设置通用告警级别
    if (json.containsKey("failureAlertLevel")) {
      otherOptions.put("failureAlertLevel", json.getString("failureAlertLevel"));
    }
    if (json.containsKey("successAlertLevel")) {
      otherOptions.put("successAlertLevel", json.getString("successAlertLevel"));
    }

    try {
      //设置告警用户部门信息
      String userDep = transitionService.getUserDepartmentByUsername(user.getUserId());
      otherOptions.put("alertUserDeparment", userDep);
    } catch (SystemUserManagerException e) {
      logger.error("setting department info failed， " + e.getMessage());
      msg.append("setting department info failed. <br/>");
      return false;
    }

    final List<SlaOption> slaOptions = null;
    // Because either cronExpression or recurrence exists, we build schedule in the below way.
    final Schedule schedule = this.scheduleManager
        .cronScheduleFlow(-1, projectId, projectName, flowName,
            "ready", firstSchedTime.getMillis(), endSchedTime, firstSchedTime.getZone(),
            DateTime.now().getMillis(), firstSchedTime.getMillis(),
            firstSchedTime.getMillis(), user.getUserId(), flowOptions,
            slaOptions, cronExpression, otherOptions);

    logger.info("User '" + user.getUserId() + "' has scheduled " + "["
        + projectName + flowName + " (" + projectId + ")" + "].");
    this.projectManager.postProjectEvent(project, EventType.SCHEDULE,
        user.getUserId(), "Schedule " + schedule.toString()
            + " has been added.");

    ret.put("scheduleId", schedule.getScheduleId());
    msg.append(String.format("Success, flow:%s, msg:%s. <br/>", flow.getId(), dataMap.get("startSch")));
    return true;
  }

  /**
   * This method is in charge of doing cron scheduling.页面开始定时任务调用方法
   */
  private void ajaxScheduleCronFlow(final HttpServletRequest req,
      final HashMap<String, Object> ret, final User user) throws ServletException {
    final String projectName = getParam(req, "projectName");
    final String flowName = getParam(req, "flow");

    final Project project = this.projectManager.getProject(projectName);

    Map<String, String> dataMap = loadScheduleServletI18nData();
    if (project == null) {
      ret.put("message", "Project " + projectName + " does not exist");
      ret.put("status", "error");
      return;
    }
    final int projectId = project.getId();

    if (!hasPermission(project, user, Type.SCHEDULE)) {
      ret.put("status", "error");
      ret.put("message", "Permission denied. Cannot execute " + flowName);
      return;
    }

    final Flow flow = project.getFlow(flowName);
    if (flow == null) {
      ret.put("status", "error");
      ret.put("message", "Flow " + flowName + " cannot be found in project "
          + projectName);
      return;
    }

    final boolean hasFlowTrigger;
    try {
      hasFlowTrigger = this.projectManager.hasFlowTrigger(project, flow);
    } catch (final Exception ex) {
      logger.error(ex);
      ret.put("status", "error");
      ret.put("message", String.format("Error looking for flow trigger of flow: %s.%s ",
          projectName, flowName));
      return;
    }

    if (hasFlowTrigger) {
      ret.put("status", "error");
      ret.put("message", String.format("<font color=\"red\"> Error: Flow %s.%s is already "
              + "associated with flow trigger, so schedule has to be defined in flow trigger config </font>",
          projectName, flowName));
      return;
    }

    final DateTimeZone timezone = DateTimeZone.getDefault();
    final DateTime firstSchedTime = getPresentTimeByTimezone(timezone);

    String cronExpression = null;
    try {
      if (hasParam(req, "cronExpression")) {
        // everything in Azkaban functions is at the minute granularity, so we add 0 here
        // to let the expression to be complete.
        cronExpression = getParam(req, "cronExpression");
        if (azkaban.utils.Utils.isCronExpressionValid(cronExpression, timezone) == false) {
          ret.put("error",  dataMap.get("thisExpress") + cronExpression + dataMap.get("outRuleQuartz"));
          return;
        }
      }
      if (cronExpression == null) {
        throw new Exception("Cron expression must exist.");
      }
    } catch (final Exception e) {
      ret.put("error", e.getMessage());
    }

    final long endSchedTime = getLongParam(req, "endSchedTime",
        Constants.DEFAULT_SCHEDULE_END_EPOCH_TIME);
    try {
      // Todo kunkun-tang: Need to verify if passed end time is valid.
    } catch (final Exception e) {
      ret.put("error", "Invalid date and time: " + endSchedTime);
      return;
    }

    ExecutionOptions flowOptions = null;
    try {
      flowOptions = HttpRequestUtils.parseFlowOptions(req);
      HttpRequestUtils.filterAdminOnlyFlowParams(flowOptions, user);
    } catch (final Exception e) {
      ret.put("error", e.getMessage());
    }

    // FIXME New function, set other parameter configuration, such as rerun failure, skip over failure, skip execution parameters by date.
    Map<String, Object> otherOptions = new HashMap<>();
    //设置失败重跑配置
    Map<String, String> jobFailedRetrySettings = getParamGroup(req, "jobFailedRetryOptions");
    final List<Map<String, String>> jobRetryList = new ArrayList<>();
    for (final String set : jobFailedRetrySettings.keySet()) {
      String[] setOption = jobFailedRetrySettings.get(set).split(",");
      Map<String, String> jobOption = new HashMap<>();

      String jobName = setOption[0].trim();
      String interval = setOption[1].trim();
      String count = setOption[2].trim();
      if(jobName.split(" ")[0].equals("all_jobs")){
        Map<String, String> flowFailedRetryOption = new HashMap<>();
        flowFailedRetryOption.put("job.failed.retry.interval", interval);
        flowFailedRetryOption.put("job.failed.retry.count", count);
        otherOptions.put("flowFailedRetryOption", flowFailedRetryOption);
      }
      jobOption.put("jobName", jobName);
      jobOption.put("interval", interval);
      jobOption.put("count", count);
      jobRetryList.add(jobOption);
    }
    otherOptions.put("jobFailedRetryOptions", jobRetryList);

    //设置失败跳过配置
    Map<String, String> jobSkipFailedSettings = getParamGroup(req, "jobSkipFailedOptions");
    final List<String> jobSkipList = new ArrayList<>();
    for (final String set : jobSkipFailedSettings.keySet()) {
      String jobName = jobSkipFailedSettings.get(set).trim();
      if(jobName.startsWith("all_jobs ")){
        otherOptions.put("flowFailedSkiped", true);
      }
      jobSkipList.add(jobName);
    }

    otherOptions.put("jobSkipFailedOptions", jobSkipList);

    //设置通用告警级别
    if (hasParam(req, "failureAlertLevel")) {
      otherOptions.put("failureAlertLevel", getParam(req, "failureAlertLevel"));
    }
    if (hasParam(req, "successAlertLevel")) {
      otherOptions.put("successAlertLevel", getParam(req, "successAlertLevel"));
    }

    Map<String, String> jobCronExpressOptions = getParamGroup(req, "jobCronExpressOptions");
    Map<String, String> jobCronExpressMap = new HashMap<>();
    for(String key: jobCronExpressOptions.keySet()){
      String tmp[] = jobCronExpressOptions.get(key).split("#_#");
      String jobNestId = tmp[0];
      String cronExpress = tmp[1];
      if (!azkaban.utils.Utils.isCronExpressionValid(cronExpress, timezone)) {
        ret.put("error", dataMap.get("skipRunTimeSet") + dataMap.get("thisExpress") + cronExpression + dataMap.get("outRuleQuartz"));
        return;
      }
      jobCronExpressMap.put(jobNestId, cronExpress);
    }

    otherOptions.put("job.cron.expression", jobCronExpressMap);

    try {
      //设置告警用户部门信息
      String userDep = transitionService.getUserDepartmentByUsername(user.getUserId());
      otherOptions.put("alertUserDeparment", userDep);
    } catch (SystemUserManagerException e) {
      logger.error("setting department info failed， " + e.getMessage());
      ret.put("status", "failed");
      ret.put("message", "setting department info failed.");
      return;
    }

    final List<SlaOption> slaOptions = null;

    // Because either cronExpression or recurrence exists, we build schedule in the below way.
    final Schedule schedule = this.scheduleManager
        .cronScheduleFlow(-1, projectId, projectName, flowName,
            "ready", firstSchedTime.getMillis(), endSchedTime, firstSchedTime.getZone(),
            DateTime.now().getMillis(), firstSchedTime.getMillis(),
            firstSchedTime.getMillis(), user.getUserId(), flowOptions,
            slaOptions, cronExpression, otherOptions);

    logger.info("User '" + user.getUserId() + "' has scheduled " + "["
        + projectName + flowName + " (" + projectId + ")" + "].");
    this.projectManager.postProjectEvent(project, EventType.SCHEDULE,
        user.getUserId(), "Schedule " + schedule.toString()
            + " has been added.");

    ret.put("status", "success");
    ret.put("scheduleId", schedule.getScheduleId());
    ret.put("message", projectName + "." + flowName + dataMap.get("startSch"));
  }

  private DateTime parseDateTime(final String scheduleDate, final String scheduleTime) {
    // scheduleTime: 12,00,pm,PDT
    final String[] parts = scheduleTime.split(",", -1);
    int hour = Integer.parseInt(parts[0]);
    final int minutes = Integer.parseInt(parts[1]);
    final boolean isPm = parts[2].equalsIgnoreCase("pm");

    final DateTimeZone timezone =
        parts[3].equals("UTC") ? DateTimeZone.UTC : DateTimeZone.getDefault();

    // scheduleDate: 02/10/2013
    DateTime day = null;
    if (scheduleDate == null || scheduleDate.trim().length() == 0) {
      day = new LocalDateTime().toDateTime();
    } else {
      day = DateTimeFormat.forPattern("MM/dd/yyyy")
          .withZone(timezone).parseDateTime(scheduleDate);
    }

    hour %= 12;

    if (isPm) {
      hour += 12;
    }

    final DateTime firstSchedTime =
        day.withHourOfDay(hour).withMinuteOfHour(minutes).withSecondOfMinute(0);

    return firstSchedTime;
  }

  /**
   * @param cronTimezone represents the timezone from remote API call
   * @return if the string is equal to UTC, we return UTC; otherwise, we always return default
   * timezone.
   */
  private DateTimeZone parseTimeZone(final String cronTimezone) {
    if (cronTimezone != null && cronTimezone.equals("UTC")) {
      return DateTimeZone.UTC;
    }

    return DateTimeZone.getDefault();
  }

  private DateTime getPresentTimeByTimezone(final DateTimeZone timezone) {
    return new DateTime(timezone);
  }

  //解析前端规则字符串 转换成SlaOption对象
  private SlaOption parseFinishSetting(final String set, final Flow flow, final Project project) throws ScheduleManagerException {
    logger.info("Tryint to set sla with the following set: " + set);

    final String slaType;
    final List<String> slaActions = new ArrayList<>();
    final Map<String, Object> slaInfo = new HashMap<>();
    final String[] parts = set.split(",", -1);
    final String id = parts[0];
    final String rule = parts[1];
    final String level = parts[2];

    List<Flow> embeddedFlows = project.getFlows();

    slaActions.add(SlaOption.ACTION_ALERT);
    slaInfo.put(SlaOption.ALERT_TYPE, "email");

    if (id.equals("")) {//FLOW告警模式设置
      if (rule.equals("FAILURE EMAILS")) {
        slaType = SlaOption.TYPE_FLOW_FAILURE_EMAILS;
      } else if (rule.equals("SUCCESS EMAILS")){
        slaType = SlaOption.TYPE_FLOW_SUCCESS_EMAILS;
      } else {
        slaType = SlaOption.TYPE_FLOW_FINISH_EMAILS;
      }
    } else {//JOB告警模式设置
      Node node = flow.getNode(id);
      if(node != null && "flow".equals(node.getType())){//如果是flow类型的Job获取它真正执行的FlowId
        slaInfo.put(SlaOption.INFO_JOB_NAME, id);
        slaInfo.put(SlaOption.INFO_EMBEDDED_ID, node.getEmbeddedFlowId());
      }else{
        slaInfo.put(SlaOption.INFO_JOB_NAME, id);
      }
      String str[] = id.split(":");
      for (Flow f: embeddedFlows) {
        Node n = f.getNode(str[str.length -1]);
        if(n != null && n.getType().equals("flow")) {
          logger.info(id + " is embeddedFlow.");
          slaInfo.put(SlaOption.INFO_EMBEDDED_ID, n.getEmbeddedFlowId());
          break;
        }
      }

      if (rule.equals("FAILURE EMAILS")) {
        slaType = SlaOption.TYPE_JOB_FAILURE_EMAILS;
      } else if (rule.equals("SUCCESS EMAILS")){
        slaType = SlaOption.TYPE_JOB_SUCCESS_EMAILS;
      } else {
        slaType = SlaOption.TYPE_JOB_FINISH_EMAILS;
      }

    }

    final SlaOption r = new SlaOption(slaType, slaActions, slaInfo, level);
    logger.info("Parsing finish as id:" + id + " type:" + slaType + " rule:"
        + rule + " actions:" + slaActions);
    return r;

  }

  private void ajaxGetScheduleByScheduleId(final HttpServletRequest req,
      final HashMap<String, Object> ret, final User user) throws ServletException {

    //final int projectId = getIntParam(req, "projectId");
    //final String flowId = getParam(req, "flowId");
    final int schedultId = getIntParam(req, "scheduleId");
    try {
      final Schedule schedule = this.scheduleManager.getSchedule(schedultId);

      if (schedule != null) {
        final Map<String, Object> jsonObj = new HashMap<>();
        jsonObj.put("scheduleId", Integer.toString(schedule.getScheduleId()));
        jsonObj.put("submitUser", schedule.getSubmitUser());
        jsonObj.put("firstSchedTime", utils.formatDateTime(schedule.getFirstSchedTime()));
        jsonObj.put("nextExecTime", utils.formatDateTime(schedule.getNextExecTime()));
        jsonObj.put("period", utils.formatPeriod(schedule.getPeriod()));
        jsonObj.put("cronExpression", schedule.getCronExpression());
        jsonObj.put("executionOptions", HttpRequestUtils.parseWebOptions(schedule.getExecutionOptions()));
        jsonObj.put("otherOptions", schedule.getOtherOption());
        jsonObj.put("projectName", schedule.getProjectName());
        jsonObj.put("flowId", schedule.getFlowName());

        ret.put("schedule", jsonObj);
      }
    } catch (final ScheduleManagerException e) {
      logger.error(e.getMessage(), e);
      ret.put("error", e);
    }
  }

  private void ajaxUpdateSchedule(final HttpServletRequest req, final HashMap<String, Object> ret,
      final User user) {
    try {
      final int scheduleId = getIntParam(req, "scheduleId");
      final Schedule sched = this.scheduleManager.getSchedule(scheduleId);

      Map<String, String> dataMap = loadScheduleServletI18nData();
      if (sched == null) {
        ret.put("error",
            "Error loading schedule. Schedule " + scheduleId
                + " doesn't exist");
        return;
      }

      final Project project = this.projectManager.getProject(sched.getProjectId());
      if (!hasPermission(project, user, Permission.Type.SCHEDULE)) {
        ret.put("error", "User " + user
            + " does not have permission to set SLA for this flow.");
        return;
      }

      final Flow flow = project.getFlow(sched.getFlowName());
      if (flow == null) {
        ret.put("status", "error");
        ret.put("message", "Flow " + sched.getFlowName() + " cannot be found in project "
            + project.getName());
        return;
      }

      final DateTimeZone timezone = DateTimeZone.getDefault();
      final DateTime firstSchedTime = getPresentTimeByTimezone(timezone);

      String cronExpression = null;
      try {
        if (hasParam(req, "cronExpression")) {
          // everything in Azkaban functions is at the minute granularity, so we add 0 here
          // to let the expression to be complete.
          cronExpression = getParam(req, "cronExpression");
          if (!Utils.isCronExpressionValid(cronExpression, timezone)) {
            ret.put("error", dataMap.get("thisExpress") + cronExpression + dataMap.get("outRuleQuartz"));
            return;
          }
        }
        if (cronExpression == null) {
          throw new Exception("Cron expression must exist.");
        }
      } catch (final Exception e) {
        ret.put("error", e.getMessage());
      }

      final long endSchedTime = getLongParam(req, "endSchedTime",
          Constants.DEFAULT_SCHEDULE_END_EPOCH_TIME);
      try {
        // Todo kunkun-tang: Need to verify if passed end time is valid.
      } catch (final Exception e) {
        ret.put("error", "Invalid date and time: " + endSchedTime);
        return;
      }

      ExecutionOptions flowOptions = null;
      try {
        flowOptions = HttpRequestUtils.parseFlowOptions(req);
        HttpRequestUtils.filterAdminOnlyFlowParams(flowOptions, user);
      } catch (final Exception e) {
        ret.put("error", e.getMessage());
      }

      sched.setFlowOptions(flowOptions);

      //设置其他参数配置
      Map<String, Object> otherOptions = new HashMap<>();
      //设置失败重跑配置
      Map<String, String> jobFailedRetrySettings = getParamGroup(req, "jobFailedRetryOptions");
      final List<Map<String, String>> jobRetryList = new ArrayList<>();
      for (final String set : jobFailedRetrySettings.keySet()) {
        String[] setOption = jobFailedRetrySettings.get(set).split(",");
        Map<String, String> jobOption = new HashMap<>();
        String jobName = setOption[0].trim();
        String interval = setOption[1].trim();
        String count = setOption[2].trim();
        if(jobName.split(" ")[0].equals("all_jobs")){
          Map<String, String> flowFailedRetryOption = new HashMap<>();
          flowFailedRetryOption.put("job.failed.retry.interval", interval);
          flowFailedRetryOption.put("job.failed.retry.count", count);
          otherOptions.put("flowFailedRetryOption", flowFailedRetryOption);
        }
        jobOption.put("jobName", jobName);
        jobOption.put("interval", interval);
        jobOption.put("count", count);
        jobRetryList.add(jobOption);

      }
      otherOptions.put("jobFailedRetryOptions", jobRetryList);

      //设置失败跳过配置
      Map<String, String> jobSkipFailedSettings = getParamGroup(req, "jobSkipFailedOptions");
      final List<String> jobSkipList = new ArrayList<>();
      for (final String set : jobSkipFailedSettings.keySet()) {
        String jobName = jobSkipFailedSettings.get(set).trim();
        if(jobName.startsWith("all_jobs ")){
          otherOptions.put("flowFailedSkiped", true);
        }
        jobSkipList.add(jobName);
      }

      otherOptions.put("jobSkipFailedOptions", jobSkipList);

      //设置通用告警级别
      if (hasParam(req, "failureAlertLevel")) {
        otherOptions.put("failureAlertLevel", getParam(req, "failureAlertLevel"));
      }
      if (hasParam(req, "successAlertLevel")) {
        otherOptions.put("successAlertLevel", getParam(req, "successAlertLevel"));
      }

      try {
        //设置告警用户部门信息
        String userDep = transitionService.getUserDepartmentByUsername(user.getUserId());
        otherOptions.put("alertUserDeparment", userDep);
      } catch (SystemUserManagerException e) {
        logger.error("setting department info failed, " + e.getMessage());
        ret.put("error", e.getMessage());
        return;
      }

      //set job skiped cronexpression
      Map<String, String> jobCronExpressOptions = getParamGroup(req, "jobCronExpressOptions");
      Map<String, String> jobCronExpressMap = new HashMap<>();
      for(String key: jobCronExpressOptions.keySet()){
        String tmp[] = jobCronExpressOptions.get(key).split("#_#");
        String jobNestId = tmp[0];
        String cronExpress = tmp[1];
        if (!azkaban.utils.Utils.isCronExpressionValid(cronExpress, timezone)) {
          ret.put("error", dataMap.get("skipRunTimeSet") + dataMap.get("thisExpress") + cronExpression + dataMap.get("outRuleQuartz"));
          return;
        }
        jobCronExpressMap.put(jobNestId, cronExpress);
      }
      otherOptions.put("job.cron.expression", jobCronExpressMap);

      final List<SlaOption> slaOptions = sched.getSlaOptions();

      // Because either cronExpression or recurrence exists, we build schedule in the below way.
      final Schedule schedule = this.scheduleManager
          .cronScheduleFlow(scheduleId, project.getId(), project.getName(), flow.getId(),
              "ready", firstSchedTime.getMillis(), endSchedTime, firstSchedTime.getZone(),
              DateTime.now().getMillis(), firstSchedTime.getMillis(),
              firstSchedTime.getMillis(), user.getUserId(), flowOptions,
              slaOptions, cronExpression, otherOptions);
      this.projectManager.postProjectEvent(project, EventType.SCHEDULE,
          user.getUserId(), "Schedule flow " + sched.getFlowName()
              + " has been changed.");


      ret.put("message", dataMap.get("scheduleJobId") + scheduleId + dataMap.get("modifyConfigSuccess"));
    } catch (final ServletException e) {
      ret.put("error", e.getMessage());
    } catch (final ScheduleManagerException e) {
      logger.error(e.getMessage());
      ret.put("error", e.getMessage());
    }

  }


}
