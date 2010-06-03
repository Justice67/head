/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package org.mifos.application.meeting.struts.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mifos.application.meeting.business.MeetingBO;
import org.mifos.application.meeting.exceptions.MeetingException;
import org.mifos.application.meeting.struts.actionforms.MeetingActionForm;
import org.mifos.application.meeting.util.helpers.MeetingConstants;
import org.mifos.application.meeting.util.helpers.MeetingType;
import org.mifos.application.meeting.util.helpers.RankOfDay;
import org.mifos.application.meeting.util.helpers.RecurrenceType;
import org.mifos.application.servicefacade.MeetingUpdateRequest;
import org.mifos.application.util.helpers.ActionForwards;
import org.mifos.config.FiscalCalendarRules;
import org.mifos.customers.business.CustomerBO;
import org.mifos.customers.business.service.CustomerBusinessService;
import org.mifos.customers.util.helpers.CustomerConstants;
import org.mifos.customers.util.helpers.CustomerLevel;
import org.mifos.framework.business.service.BusinessService;
import org.mifos.framework.exceptions.PageExpiredException;
import org.mifos.framework.exceptions.ServiceException;
import org.mifos.framework.struts.action.BaseAction;
import org.mifos.framework.util.DateTimeService;
import org.mifos.framework.util.helpers.BusinessServiceName;
import org.mifos.framework.util.helpers.CloseSession;
import org.mifos.framework.util.helpers.Constants;
import org.mifos.framework.util.helpers.SessionUtils;
import org.mifos.framework.util.helpers.TransactionDemarcate;
import org.mifos.security.util.ActionSecurity;
import org.mifos.security.util.SecurityConstants;

public class MeetingAction extends BaseAction {

    @Override
    protected BusinessService getService() throws ServiceException {
        return null;
    }

    @Override
    protected boolean skipActionFormToBusinessObjectConversion(@SuppressWarnings("unused") String method) {
        return true;
    }

    public static ActionSecurity getSecurity() {
        ActionSecurity security = new ActionSecurity("meetingAction");
        security.allow("load", SecurityConstants.VIEW);
        security.allow("create", SecurityConstants.VIEW);
        security.allow("update", SecurityConstants.VIEW);
        security.allow("edit", SecurityConstants.VIEW);
        security.allow("cancelCreate", SecurityConstants.VIEW);
        security.allow("cancelUpdate", SecurityConstants.VIEW);
        return security;
    }

    @TransactionDemarcate(joinToken = true)
    public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        populateActionForm(request, (MeetingActionForm) form);
        loadMasterData(request);
        return mapping.findForward(ActionForwards.load_success.toString());
    }

    @TransactionDemarcate(joinToken = true)
    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        MeetingActionForm actionForm = (MeetingActionForm) form;
        MeetingBO meeting = createMeeting(actionForm);
        SessionUtils.setAttribute(CustomerConstants.CUSTOMER_MEETING, meeting, request);
        return mapping.findForward(forwardForCreate(actionForm.getCustomerLevelValue()).toString());
    }

    @TransactionDemarcate(conditionToken = true)
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        MeetingActionForm actionForm = (MeetingActionForm) form;
        clearActionForm(actionForm);
        CustomerBO customerInSession = (CustomerBO) SessionUtils.getAttribute(Constants.BUSINESS_KEY, request);
        CustomerBO customer = getCustomerBusinessService().getCustomer(customerInSession.getCustomerId());
        customer.setVersionNo(customerInSession.getVersionNo());
        SessionUtils.setAttribute(Constants.BUSINESS_KEY, customer, request);
        customerInSession = null;
        ActionForward forward = null;
        loadMasterData(request);
        if (customer.getCustomerMeeting() != null) {
            loadMasterData(request);

            MeetingBO meeting = customer.getCustomerMeeting().getMeeting();

            setValuesInActionForm(actionForm, meeting);
            forward = mapping.findForward(ActionForwards.edit_success.toString());
            actionForm.setInput(MeetingConstants.INPUT_EDIT);
        } else {
            actionForm.setInput(MeetingConstants.INPUT_CREATE);
            forward = mapping.findForward(ActionForwards.createMeeting_success.toString());
        }
        return forward;
    }

    @TransactionDemarcate(validateAndResetToken = true)
    @CloseSession
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        MeetingActionForm actionForm = (MeetingActionForm) form;
        CustomerBO customerInSession = (CustomerBO) SessionUtils.getAttribute(Constants.BUSINESS_KEY, request);

        MeetingUpdateRequest meetingUpdateRequest = createMeetingUupdateRequest(customerInSession, actionForm);
        meetingServiceFacade.updateCustomerMeeting(meetingUpdateRequest, getUserContext(request));

        ActionForwards forward = forwardForUpdate(actionForm.getCustomerLevelValue());
        return mapping.findForward(forward.toString());
    }

    @TransactionDemarcate(joinToken = true)
    public ActionForward cancelCreate(ActionMapping mapping, ActionForm form, @SuppressWarnings("unused") HttpServletRequest request,
            @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        MeetingActionForm maf = (MeetingActionForm) form;
        return mapping.findForward(forwardForCreate(maf.getCustomerLevelValue()).toString());
    }

    @TransactionDemarcate(validateAndResetToken = true)
    public ActionForward cancelUpdate(ActionMapping mapping, ActionForm form, @SuppressWarnings("unused") HttpServletRequest request,
            @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        ActionForwards forward = forwardForUpdate(((MeetingActionForm) form).getCustomerLevelValue());
        return mapping.findForward(forward.toString());
    }

    @TransactionDemarcate(joinToken = true)
    public ActionForward validate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        String method = (String) request.getAttribute("methodCalled");
        MeetingActionForm maf = (MeetingActionForm) form;
        if (maf.getInput() == null || maf.getInput().equals(MeetingConstants.INPUT_EDIT)) {
            return mapping.findForward(method + "_failure");
        }

        return mapping.findForward(ActionForwards.createMeeting_failure.toString());
    }

    private void populateActionForm(HttpServletRequest request, MeetingActionForm form) throws PageExpiredException {
        MeetingBO meeting = (MeetingBO) SessionUtils.getAttribute(CustomerConstants.CUSTOMER_MEETING, request);
        clearActionForm(form);
        if (meeting != null) {
            setValuesInActionForm(form, meeting);
        }
    }

    private void setValuesInActionForm(MeetingActionForm form, MeetingBO meeting) {
        if (meeting.isWeekly()) {
            form.setFrequency(RecurrenceType.WEEKLY.getValue().toString());
            form.setWeekDay(meeting.getMeetingDetails().getWeekDay().getValue().toString());
            form.setRecurWeek(meeting.getMeetingDetails().getRecurAfter().toString());
        } else if (meeting.isMonthly()) {
            form.setFrequency(RecurrenceType.MONTHLY.getValue().toString());
            if (meeting.isMonthlyOnDate()) {
                form.setMonthType("1");
                form.setDayRecurMonth(meeting.getMeetingDetails().getRecurAfter().toString());
                form.setMonthDay(meeting.getMeetingDetails().getDayNumber().toString());
            } else {
                form.setMonthType("2");
                form.setRecurMonth(meeting.getMeetingDetails().getRecurAfter().toString());
                form.setMonthWeek(meeting.getMeetingDetails().getWeekDay().getValue().toString());
                form.setMonthRank(meeting.getMeetingDetails().getWeekRank().getValue().toString());
            }
        }
        form.setMeetingPlace(meeting.getMeetingPlace());
    }

    private MeetingUpdateRequest createMeetingUupdateRequest(CustomerBO customerInSession, MeetingActionForm actionForm) {

        MeetingUpdateRequest meetingUpdateRequest;

        switch (actionForm.getRecurrenceType()) {
        case DAILY:
            throw new UnsupportedOperationException("Daily recurrence is not supported for customer meetings.");
        case WEEKLY:
            meetingUpdateRequest = new MeetingUpdateRequest(customerInSession.getCustomerId(), customerInSession.getVersionNo(), actionForm.getRecurrenceType(), actionForm.getMeetingPlace(), actionForm.getRecurWeekValue(), actionForm.getWeekDayValue(), actionForm.getMonthDayValue(), actionForm.getMonthWeekValue(), actionForm.getMonthRankValue());
            break;
        case MONTHLY:
            if (actionForm.isMonthlyOnDate()) {
                meetingUpdateRequest = new MeetingUpdateRequest(customerInSession.getCustomerId(), customerInSession.getVersionNo(), actionForm.getRecurrenceType(), actionForm.getMeetingPlace(), actionForm.getDayRecurMonthValue(), actionForm.getWeekDayValue(), actionForm.getMonthDayValue(), actionForm.getMonthWeekValue(), actionForm.getMonthRankValue());
            } else {
                meetingUpdateRequest = new MeetingUpdateRequest(customerInSession.getCustomerId(), customerInSession.getVersionNo(), actionForm.getRecurrenceType(), actionForm.getMeetingPlace(), actionForm.getRecurMonthValue(), actionForm.getWeekDayValue(), actionForm.getMonthDayValue(), actionForm.getMonthWeekValue(), actionForm.getMonthRankValue());
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown recurrence for customer meetings.");
        }

        return meetingUpdateRequest;
    }

    private MeetingBO createMeeting(MeetingActionForm form) throws MeetingException {
        MeetingBO meeting = null;
        Date startDate = new DateTimeService().getCurrentJavaDateTime();
        if (form.getRecurrenceType().equals(RecurrenceType.WEEKLY)) {
            meeting = new MeetingBO(form.getWeekDayValue(), form.getRecurWeekValue(), startDate,
                    MeetingType.CUSTOMER_MEETING, form.getMeetingPlace());
        } else if (form.isMonthlyOnDate()) {
            meeting = new MeetingBO(form.getMonthDayValue(), form.getDayRecurMonthValue(), startDate,
                    MeetingType.CUSTOMER_MEETING, form.getMeetingPlace());
        } else {
            meeting = new MeetingBO(form.getMonthWeekValue(), form.getMonthRankValue(), form.getRecurMonthValue(),
                    startDate, MeetingType.CUSTOMER_MEETING, form.getMeetingPlace());
        }
        return meeting;
    }

    private ActionForwards forwardForCreate(CustomerLevel customerLevel) {
        if (customerLevel.equals(CustomerLevel.CENTER)) {
            return ActionForwards.loadCreateCenter;
        } else if (customerLevel.equals(CustomerLevel.GROUP)) {
            return ActionForwards.loadCreateGroup;
        } else {
            return ActionForwards.loadCreateClient;
        }
    }

    private ActionForwards forwardForUpdate(CustomerLevel customerLevel) {
        if (customerLevel.equals(CustomerLevel.CENTER)) {
            return ActionForwards.center_detail_page;
        } else if (customerLevel.equals(CustomerLevel.GROUP)) {
            return ActionForwards.group_detail_page;
        } else {
            return ActionForwards.client_detail_page;
        }
    }

    private void loadMasterData(HttpServletRequest request) throws Exception {
        SessionUtils.setCollectionAttribute(MeetingConstants.WEEKDAYSLIST, new FiscalCalendarRules().getWorkingDays(),
                request);
        SessionUtils.setCollectionAttribute(MeetingConstants.WEEKRANKLIST, RankOfDay.getRankOfDayList(), request);
    }

    private void clearActionForm(MeetingActionForm form) {
        form.setFrequency(RecurrenceType.WEEKLY.getValue().toString());
        form.setMonthType(null);
        form.setWeekDay(null);
        form.setRecurWeek(null);
        form.setMonthDay(null);
        form.setDayRecurMonth(null);
        form.setMonthWeek(null);
        form.setMonthRank(null);
        form.setRecurMonth(null);
        form.setMeetingPlace(null);
        form.setInput(null);
    }

    private CustomerBusinessService getCustomerBusinessService() {
        return new CustomerBusinessService();
    }
}
