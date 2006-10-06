/**

 * EditCustomerStatusAction.java version: 1.0



 * Copyright (c) 2005-2006 Grameen Foundation USA

 * 1029 Vermont Avenue, NW, Suite 400, Washington DC 20005

 * All rights reserved.



 * Apache License
 * Copyright (c) 2005-2006 Grameen Foundation USA
 *

 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the

 * License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an explanation of the license

 * and how it is applied.

 *

 */

package org.mifos.application.customer.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mifos.application.accounts.savings.util.helpers.SavingsConstants;
import org.mifos.application.accounts.util.helpers.AccountTypes;
import org.mifos.application.checklist.business.CustomerCheckListBO;
import org.mifos.application.customer.business.CustomerBO;
import org.mifos.application.customer.business.service.CustomerBusinessService;
import org.mifos.application.customer.exceptions.CustomerException;
import org.mifos.application.customer.struts.actionforms.EditCustomerStatusActionForm;
import org.mifos.application.customer.util.helpers.CustomerStatus;
import org.mifos.application.customer.util.helpers.CustomerStatusFlag;
import org.mifos.application.util.helpers.ActionForwards;
import org.mifos.application.util.helpers.Methods;
import org.mifos.framework.business.service.BusinessService;
import org.mifos.framework.business.service.ServiceFactory;
import org.mifos.framework.components.logger.LoggerConstants;
import org.mifos.framework.components.logger.MifosLogManager;
import org.mifos.framework.components.logger.MifosLogger;
import org.mifos.framework.exceptions.ApplicationException;
import org.mifos.framework.exceptions.PageExpiredException;
import org.mifos.framework.exceptions.PersistenceException;
import org.mifos.framework.exceptions.ServiceException;
import org.mifos.framework.exceptions.StatesInitializationException;
import org.mifos.framework.exceptions.SystemException;
import org.mifos.framework.security.util.UserContext;
import org.mifos.framework.struts.action.BaseAction;
import org.mifos.framework.struts.tags.DateHelper;
import org.mifos.framework.util.helpers.BusinessServiceName;
import org.mifos.framework.util.helpers.CloseSession;
import org.mifos.framework.util.helpers.Constants;
import org.mifos.framework.util.helpers.SessionUtils;
import org.mifos.framework.util.helpers.StringUtils;
import org.mifos.framework.util.helpers.TransactionDemarcate;

public class EditCustomerStatusAction extends BaseAction {

	private CustomerBusinessService customerService;

	private MifosLogger logger = MifosLogManager
			.getLogger(LoggerConstants.CUSTOMERNOTELOGGER);

	public EditCustomerStatusAction() throws ServiceException {
		customerService = (CustomerBusinessService) ServiceFactory
				.getInstance().getBusinessService(BusinessServiceName.Customer);
	}

	@Override
	protected BusinessService getService() {
		return customerService;
	}

	@Override
	protected boolean skipActionFormToBusinessObjectConversion(String method) {
		return true;

	}

	public ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("In EditCustomerStatusAction:load()");
		doCleanUp(form, request);
		CustomerBO customerBO = customerService
				.getCustomer(((EditCustomerStatusActionForm) form)
						.getCustomerIdValue());
		loadInitialData(form, customerBO, getUserContext(request));
		SessionUtils.removeAttribute(Constants.BUSINESS_KEY, request
				.getSession());
		SessionUtils.setAttribute(Constants.BUSINESS_KEY, customerBO, request
				.getSession());

		SessionUtils.setAttribute(SavingsConstants.STATUS_LIST, customerService
				.getStatusList(customerBO.getCustomerStatus(),
						customerBO.getLevel(), getUserContext(
								request).getLocaleId()), request.getSession());
		return mapping.findForward(ActionForwards.load_success.toString());
	}

	public ActionForward preview(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NumberFormatException {
		logger.debug("In EditCustomerStatusAction:preview()");
		CustomerBO customerBO = (CustomerBO) SessionUtils.getAttribute(
				Constants.BUSINESS_KEY, request.getSession());
		setStatusDetails(form, customerBO, request, getUserContext(request));
		return mapping.findForward(ActionForwards.preview_success.toString());
	}

	public ActionForward previous(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("In EditCustomerStatusAction:previous()");
		return mapping.findForward(ActionForwards.previous_success.toString());
	}

	@CloseSession
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("In EditCustomerStatusAction:update()");
		updateStatus(form, request);
		return mapping.findForward(getDetailAccountPage(form));
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("In EditCustomerStatusAction:cancel()");
		return mapping.findForward(getDetailAccountPage(form));
	}

	@TransactionDemarcate(joinToken = true)
	public ActionForward loadStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("In EditCustomerStatusAction:load()");
		doCleanUp(form, request);
		CustomerBO customerBO = customerService
				.getCustomer(((EditCustomerStatusActionForm) form)
						.getCustomerIdValue());
		loadInitialData(form, customerBO, getUserContext(request));
		SessionUtils.removeAttribute(Constants.BUSINESS_KEY, request);
		SessionUtils.setAttribute(Constants.BUSINESS_KEY, customerBO, request);

		SessionUtils.setAttribute(SavingsConstants.STATUS_LIST, customerService
				.getStatusList(customerBO.getCustomerStatus(),
						customerBO.getLevel(), getUserContext(
								request).getLocaleId()), request);
		return mapping.findForward(ActionForwards.loadStatus_success.toString());
	}

	@TransactionDemarcate(joinToken = true)
	public ActionForward previewStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		logger.debug("In EditCustomerStatusAction:preview()");
		CustomerBO customerBO = (CustomerBO) SessionUtils.getAttribute(
				Constants.BUSINESS_KEY, request);
		setCustomerStatusDetails(form, customerBO, request,
				getUserContext(request));
		return mapping.findForward(ActionForwards.previewStatus_success.toString());
	}

	@TransactionDemarcate(joinToken = true)
	public ActionForward previousStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("In EditCustomerStatusAction:previous()");
		return mapping.findForward(ActionForwards.previousStatus_success.toString());
	}

	@CloseSession
	@TransactionDemarcate(validateAndResetToken = true)
	public ActionForward updateStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("In EditCustomerStatusAction:update()");
		updateStatus(form, request);
		return mapping.findForward(getDetailAccountPage(form));
	}

	@TransactionDemarcate(validateAndResetToken = true)
	public ActionForward cancelStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("In EditCustomerStatusAction:cancel()");
		return mapping.findForward(getDetailAccountPage(form));
	}

	public ActionForward validate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		logger.debug("In EditCustomerStatusAction:validate()");
		String method = (String) request
				.getAttribute(SavingsConstants.METHODCALLED);
		String forward = null;
		if (method != null) {
			if (method.equals(Methods.preview.toString()))
				forward = ActionForwards.preview_failure.toString();
			else if (method.equals(Methods.load.toString()))
				forward = getDetailAccountPage(form);
			else if (method.equals(Methods.update.toString()))
				forward = ActionForwards.update_failure.toString();
			else if (method.equals(Methods.previewStatus.toString()))
				forward = ActionForwards.previewStatus_failure.toString();
			else if (method.equals(Methods.loadStatus.toString()))
				forward = getDetailAccountPage(form);
			else if (method.equals(Methods.updateStatus.toString()))
				forward = ActionForwards.updateStatus_failure.toString();
		}
		return mapping.findForward(forward);
	}

	private void setStatusDetails(ActionForm form, CustomerBO customerBO,
			HttpServletRequest request, UserContext userContext) throws ApplicationException, SystemException{
		EditCustomerStatusActionForm statusActionForm = (EditCustomerStatusActionForm) form;
		statusActionForm.setCommentDate(DateHelper.getCurrentDate(userContext
				.getPereferedLocale()));
		String newStatusName = null;
		String flagName = null;
		List<CustomerCheckListBO> checklist = customerService
				.getStatusChecklist(statusActionForm.getNewStatusIdValue(),
						statusActionForm.getLevelIdValue());
		SessionUtils.setAttribute(SavingsConstants.STATUS_CHECK_LIST,
				checklist, request.getSession());
		newStatusName = getStatusName(customerBO, userContext.getLocaleId(),
				statusActionForm.getNewStatusId(), statusActionForm
						.getNewStatusIdValue());
		flagName = getFlagName(customerBO, userContext.getLocaleId(),
				statusActionForm.getNewStatusId(), statusActionForm
						.getNewStatusIdValue(), statusActionForm
						.getFlagIdValue());
		SessionUtils.setAttribute(SavingsConstants.NEW_STATUS_NAME,
				newStatusName, request.getSession());
		SessionUtils.setAttribute(SavingsConstants.FLAG_NAME, flagName, request
				.getSession());

	}

	private String getStatusName(CustomerBO customerBO, Short localeId,
			String statusId, Short statusIdValue){
		if (StringUtils.isNullAndEmptySafe(statusId)) {
			return customerService.getStatusName(localeId, CustomerStatus.getStatus(statusIdValue),
					customerBO.getLevel());
		}
		return null;
	}

	private String getFlagName(CustomerBO customerBO, Short localeId,
			String statusId, Short statusIdValue, Short flagIdValue){
		if (StringUtils.isNullAndEmptySafe(statusId)
				&& isNewStatusCancelledOrClosed(statusIdValue)) {
			return customerService.getFlagName(localeId, CustomerStatusFlag.getStatusFlag(flagIdValue),
					customerBO.getLevel());
		}
		return null;
	}

	private void setCustomerStatusDetails(ActionForm form,
			CustomerBO customerBO, HttpServletRequest request,
			UserContext userContext) throws Exception{
		EditCustomerStatusActionForm statusActionForm = (EditCustomerStatusActionForm) form;
		statusActionForm.setCommentDate(DateHelper.getCurrentDate(userContext
				.getPereferedLocale()));
		String newStatusName = null;
		String flagName = null;
		List<CustomerCheckListBO> checklist = customerService
				.getStatusChecklist(statusActionForm.getNewStatusIdValue(),
						statusActionForm.getLevelIdValue());
		SessionUtils.setAttribute(SavingsConstants.STATUS_CHECK_LIST,
				checklist, request);
		newStatusName = getStatusName(customerBO, userContext.getLocaleId(),
				statusActionForm.getNewStatusId(), statusActionForm
						.getNewStatusIdValue());
		flagName = getFlagName(customerBO, userContext.getLocaleId(),
				statusActionForm.getNewStatusId(), statusActionForm
						.getNewStatusIdValue(), statusActionForm
						.getFlagIdValue());
		SessionUtils.setAttribute(SavingsConstants.NEW_STATUS_NAME,
				newStatusName, request);
		SessionUtils
				.setAttribute(SavingsConstants.FLAG_NAME, flagName, request);

	}

	private boolean isNewStatusCancelledOrClosed(Short newStatusId) {
		return newStatusId.equals(CustomerStatus.CLIENT_CANCELLED.getValue())
				|| newStatusId.equals(CustomerStatus.CLIENT_CLOSED.getValue())
				|| newStatusId
						.equals(CustomerStatus.GROUP_CANCELLED.getValue())
				|| newStatusId.equals(CustomerStatus.GROUP_CLOSED.getValue());
	}

	private void setFormAttributes(ActionForm form, CustomerBO customerBO) {
		EditCustomerStatusActionForm editCustomerStatusActionForm = (EditCustomerStatusActionForm) form;
		editCustomerStatusActionForm.setLevelId(customerBO.getCustomerLevel()
				.getId().toString());
		editCustomerStatusActionForm.setCurrentStatusId(customerBO
				.getCustomerStatus().getId().toString());
		editCustomerStatusActionForm.setGlobalAccountNum(customerBO
				.getGlobalCustNum());
		editCustomerStatusActionForm.setCustomerName(customerBO
				.getDisplayName());
		if (customerBO.getCustomerLevel().isCenter()) {
			editCustomerStatusActionForm.setInput("center");
		} else if (customerBO.getCustomerLevel().isGroup()) {
			editCustomerStatusActionForm.setInput("group");
		} else if (customerBO.getCustomerLevel().isClient()) {
			editCustomerStatusActionForm.setInput("client");
		}

	}

	private void doCleanUp(ActionForm form, HttpServletRequest request) {
		EditCustomerStatusActionForm editCustomerStatusActionForm = (EditCustomerStatusActionForm) form;
		editCustomerStatusActionForm.setSelectedItems(null);
		editCustomerStatusActionForm.setNotes(null);
		editCustomerStatusActionForm.setNewStatusId(null);
		editCustomerStatusActionForm.setFlagId(null);
	}

	private String getDetailAccountPage(ActionForm form) {
		EditCustomerStatusActionForm editStatusActionForm = (EditCustomerStatusActionForm) form;
		String input = editStatusActionForm.getInput();
		String forward = null;
		if (input.equals("center"))
			forward = ActionForwards.center_detail_page.toString();
		else if (input.equals("group"))
			forward = ActionForwards.group_detail_page.toString();
		else if (input.equals("client"))
			forward = ActionForwards.client_detail_page.toString();
		return forward;
	}

	private void checkPermission(CustomerBO customerBO,
			HttpServletRequest request, Short newStatusId, Short flagId) throws Exception{
		if (null != customerBO.getPersonnel())
			customerService.checkPermissionForStatusChange(newStatusId,
					getUserContext(request), flagId, customerBO.getOffice()
							.getOfficeId(), customerBO.getPersonnel()
							.getPersonnelId());
		else
			customerService.checkPermissionForStatusChange(newStatusId,
					getUserContext(request), flagId, customerBO.getOffice()
							.getOfficeId(), getUserContext(request).getId());
	}

	private void loadInitialData(ActionForm form, CustomerBO customerBO,
			UserContext userContext) throws Exception {
		customerBO.setUserContext(userContext);
		customerService.initializeStateMachine(userContext.getLocaleId(),
				customerBO.getOffice().getOfficeId(),AccountTypes.CUSTOMERACCOUNT,customerBO.getLevel());
		setFormAttributes(form, customerBO);
		customerBO.getCustomerStatus().setLocaleId(userContext.getLocaleId());
	}

	private void updateStatus(ActionForm form, HttpServletRequest request)
			throws Exception {
		EditCustomerStatusActionForm editStatusActionForm = (EditCustomerStatusActionForm) form;
		CustomerBO customerBO = customerService
				.getCustomer(editStatusActionForm.getCustomerIdValue());
		customerBO.setUserContext(getUserContext(request));
		customerBO.getCustomerStatus().setLocaleId(
				getUserContext(request).getLocaleId());
		Short flagId = null;
		Short newStatusId = null;
		if (StringUtils.isNullAndEmptySafe(editStatusActionForm.getFlagId()))
			flagId = editStatusActionForm.getFlagIdValue();
		if (StringUtils.isNullAndEmptySafe(editStatusActionForm
				.getNewStatusId()))
			newStatusId = editStatusActionForm.getNewStatusIdValue();
		if(!(customerBO.getCustomerLevel().isCenter()))
			checkPermission(customerBO, request, newStatusId, flagId);
		//setInitialObjectForAuditLogging(customerBO);
		customerBO.changeStatus(newStatusId, flagId, editStatusActionForm
				.getNotes());
		customerBO = null;
	}
}
