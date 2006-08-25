<!-- 

/**

 * viewgroupchargesdetails.jsp    version: 1.0

 

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

-->

<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/tags/mifos-html" prefix="mifos"%>
<%@taglib uri="http://struts.apache.org/tags-html-el" prefix="html-el"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/mifos/customtags" prefix="mifoscustom" %>
<%@ taglib uri="/userlocaledate" prefix="userdatefn"%>
<%@ taglib uri="/mifos/custom-tags" prefix="customtags"%>

<tiles:insert definition=".clientsacclayoutsearchmenu">
 <tiles:put name="body" type="string">
 <%--   <table width="95%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="bluetablehead05">
                  <span class="fontnormal8pt">
           <a href="CustomerSearchAction.do?method=getOfficeHomePage&officeId=<c:out value="${sessionScope.linkValues.customerOfficeId}"/>&officeName=<c:out value="${sessionScope.linkValues.customerOfficeName}"/>&loanOfficerId=<c:out value="${requestScope.Context.userContext.id}"/>">
	           <c:out value="${sessionScope.linkValues.customerOfficeName}"/>            	
           </a> /
           	<c:if test="${!empty sessionScope.linkValues.customerParentName}">
               	<a href="centerAction.do?method=get&globalCustNum=<c:out value="${sessionScope.linkValues.customerParentGCNum}"/>">
			       	<c:out value="${sessionScope.linkValues.customerParentName}"/>
		       	</a> /  
		    </c:if>
          <a href="GroupAction.do?method=get&globalCustNum=<c:out value="${sessionScope.linkValues.globalCustNum}"/>">
          	<c:out value="${sessionScope.linkValues.customerName}"/>
           </a>/
            </span>

		<span class="fontnormal8ptbold">
			<mifos:mifoslabel name="${ConfigurationConstants.GROUP}"/>   
			<mifos:mifoslabel name="Group.charges" bundle="GroupUIResources"/>
		</span></td>
      </tr>
    </table>--%>
    <table width="95%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="bluetablehead05"><span class="fontnormal8pt"> <customtags:headerLink />/
				</span><span class="fontnormal8ptbold"> <mifos:mifoslabel
					name="${ConfigurationConstants.GROUP}" /> <mifos:mifoslabel name="Group.charges" bundle="GroupUIResources"/></span></td>
			</tr>
		</table>
      <table width="95%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="70%" height="24" align="left" valign="top" class="paddingL15T15">
          <table width="96%" border="0" cellpadding="3" cellspacing="0">
          <font class="fontnormalRedBold"><html-el:errors bundle="CenterUIResources" /></font>
              <tr>
                <td width="70%" class="headingorange">
			<mifos:mifoslabel name="${ConfigurationConstants.GROUP}"/>   
			<mifos:mifoslabel name="Group.charges" bundle="GroupUIResources"/>
               </td>
                </tr>
              <tr>
                <td align="right" class="headingorange">
                	<img src="pages/framework/images/trans.gif" width="10" height="5">
                </td>
              </tr>
            </table>
            <c:if test="${param.statusId == 7 || param.statusId == 8 || param.statusId == 9 || param.statusId == 10}">
	            <table width="96%" border="0" cellpadding="0" cellspacing="0">
	              <tr>
	               <td bgcolor="#F0D4A5" style="padding-left:10px; padding-bottom:3px;">
	               <span class="fontnormalbold"><mifos:mifoslabel name="Group.applyTransaction" bundle="GroupUIResources"/>:</span>
	               
	               	               	&nbsp;&nbsp;&nbsp;&nbsp;
	                	<html-el:link href="applyPaymentAction.do?method=load&searchInput=ClientChargesDetails&statusId=${param.statusId}&globalCustNum=${sessionScope.linkValues.globalCustNum}&prdOfferingName=${sessionScope.linkValues.customerName}&input=ViewGroupCharges&globalAccountNum=${param.globalAccountNum}&accountType=${param.accountType}&accountId=${param.accountId}&securityParamInput=Center">
	                    	<mifos:mifoslabel name="accounts.apply_payment" />
	                    </html-el:link>
	               
	               
	               <c:if test="${param.statusId == 9 || param.statusId == 10}"> 
		               &nbsp;&nbsp;&nbsp;&nbsp;
		                    <html-el:link href="custApplyAdjustment.do?method=loadAdjustment&statusId=${param.statusId}&globalCustNum=${sessionScope.linkValues.globalCustNum}&prdOfferingName=${sessionScope.linkValues.customerName}&input=ViewGroupCharges&globalAccountNum=${param.globalAccountNum}&accountType=${param.accountType}&accountId=${param.accountId}&securityParamInput=Group">
		                    <mifos:mifoslabel name="Group.applyAdjustment" bundle="GroupUIResources"/>
		                    </html-el:link>
		            </c:if>
		            <c:if test="${param.statusId == 7 || param.statusId == 8 || param.statusId == 9 || param.statusId == 10}">
	                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    <html-el:link href="applyChargeAction.do?method=load&accountId=${param.accountId}&input=ViewGroupCharges&globalCustNum=${sessionScope.linkValues.globalCustNum}&prdOfferingName=${param.prdOfferingName}&headingInput=ViewGroupCharges&searchInput=ClientChargesDetails&statusId=${param.statusId}">
	                    <mifos:mifoslabel name="client.ApplyCharges" bundle="ClientUIResources"/>
	                    </html-el:link>
	                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                 </c:if>
	               </td>
	              </tr>
	            </table><br>
	          </c:if>
            <table width="96%" border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td width="66%" class="headingorange">
<mifos:mifoslabel name="client.accsum" bundle="ClientUIResources"/>
                </td>
              </tr>
              <tr>
                <td class="fontnormal">
                <span class="fontnormal">
					<mifos:mifoslabel name="client.amtdue" bundle="ClientUIResources"/>:
					<c:out value='${requestScope.Context.businessResults["ClientFeeChargeDue"].amountDoubleValue}'/>
                    </span>
                    <c:if test='${requestScope.Context.businessResults["ClientFeeChargeDue"].amountDoubleValue != 0.0}'>
    	                  <html-el:link href="customerAction.do?method=waiveChargeDue&globalCustNum=${param.globalCustNum}&accountType=${param.accountType}&prdOfferingName=${param.prdOfferingName}&statusId=${param.statusId}&type=Group&accountId=${param.accountId}&globalAccountNum=${param.globalAccountNum}">
		              	<mifos:mifoslabel name="client.waive" bundle="ClientUIResources"/>
	    		          </html-el:link>
	    		    </c:if>
	              <br>
                          <span class="fontnormal">
<mifos:mifoslabel name="client.amtoverdue" bundle="ClientUIResources"/>: 
<c:out value='${requestScope.Context.businessResults["ClientFeeChargeOverDue"].amountDoubleValue}'/>
						</span>
						<c:if  test='${requestScope.Context.businessResults["ClientFeeChargeOverDue"].amountDoubleValue != 0.0}'>
                 <html-el:link href="customerAction.do?method=waiveChargeOverDue&globalCustNum=${param.globalCustNum}&accountType=${param.accountType}&prdOfferingName=${param.prdOfferingName}&statusId=${param.statusId}&type=Group&accountId=${param.accountId}&globalAccountNum=${param.globalAccountNum}">
					<mifos:mifoslabel name="client.waive" bundle="ClientUIResources"/>
	              </html-el:link>
	              </c:if>
	              <BR>
	              <span class="fontnormalbold">
	               <mifos:mifoslabel name="accounts.total" isColonRequired="Yes"></mifos:mifoslabel>
	               <c:out value='${requestScope.Context.businessResults["ClientFeeChargeOverDue"].amountDoubleValue + requestScope.Context.businessResults["ClientFeeChargeDue"].amountDoubleValue}'></c:out>
	              </span>
	              
						</td>
              </tr>
            </table>
            <br>
            <table width="96%" border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td width="70%" class="headingorange">
<mifos:mifoslabel  name="client.upcomcharges" bundle="ClientUIResources"/>
(<c:out value='${userdatefn:getUserLocaleDate(sessionScope.UserContext.pereferedLocale,requestScope.Context.businessResults["UpcomingChargesDate"])}' />)
<!-- c:out value='${requestScope.Context.businessResults["UpcomingChargesDate"]}'/-->
                 </td>
                <td width="70%" align="right" class="fontnormal">
                        <html-el:link href="accountAppAction.do?method=getTrxnHistory&statusId=${param.statusId}&globalCustNum=${param.globalCustNum}&input=ViewGroupCharges&globalAccountNum=${param.globalAccountNum}&accountId=${param.accountId}&accountType=${param.accountType}&prdOfferingName=${param.prdOfferingName}&headingInput=ViewGroupCharges&searchInput=ClientChargesDetails">
                    	<mifos:mifoslabel name="Center.TransactionHistory" />
                    	</html-el:link>
                
				</td>
              </tr>
            </table>
            
<mifoscustom:mifostabletag moduleName="customer/client" scope="request" source="ClientUpcomingFeeChargesList" xmlFileName="ClientUpcomingFeeChargesList.xml"/>
<br>
            <table width="96%" border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td width="28%" class="headingorange">
<mifos:mifoslabel  name="client.recaccact" bundle="ClientUIResources"/>                
                </td>
                <td width="72%" align="right" class="fontnormal">
   	                <html-el:link href="customerAction.do?method=getAllActivity&statusId=${param.statusId}&type=Group&globalCustNum=${sessionScope.linkValues.globalCustNum}&prdOfferingName=${sessionScope.linkValues.customerName}&input=ViewGroupCharges&globalAccountNum=${param.globalAccountNum}&accountType=${param.accountType}&accountId=${param.accountId}">
                		View all account activity
                	</html-el:link>
                	
                </td>
              </tr>
            </table>
			<mifoscustom:mifostabletag moduleName="customer/client" scope="request" source="RecentAcctActivityList" xmlFileName="ClientRecentActivity.xml" passLocale="true"/>            
            <br>
            <table width="96%" border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td width="35%" class="headingorange"><mifos:mifoslabel  name="Group.recurringAccountFees" bundle="GroupUIResources"/></td>
                </tr>
            </table>
            <table width="96%" border="0" cellpadding="3" cellspacing="0">
              <c:forEach items="${requestScope.RecurrenceFeesChargesList}" var="recurrenceFees">
             <tr class="fontnormal">
                <td width="15%"><c:out value="${recurrenceFees.feeName}"/>:</td>
                
                <td width="30%"><c:out value="${recurrenceFees.amount}"/>&nbsp;&nbsp;(<c:out value="${recurrenceFees.meeting.simpleMeetingSchedule}"/>)</td>
                <td width="55%">
				<html-el:link href="accountAppAction.do?method=removeFees&globalCustNum=${param.globalCustNum}&statusId=${param.statusId}&feeId=${recurrenceFees.feeId}&accountId=${recurrenceFees.accountId}&fromPage=group&globalAccountNum=${param.globalAccountNum}&accountType=${param.accountType}&prdOfferingName=${param.prdOfferingName}"> 
                <mifos:mifoslabel  name="Group.remove"/>
                </html-el:link></td>
              </tr>
            </c:forEach>
            </table><br></td>
        </tr>
      </table> 
			<html-el:hidden property="searchNode(search_name)" value="ClientChargesDetails"/>  
			<html-el:hidden property="globalAccountNum" value="${param.globalAccountNum}" />
			<html-el:hidden property="accountId" value="${param.accountId}" />
			<html-el:hidden property="accountType" value="${param.accountType}" /> 
			<html-el:hidden property="prdOfferingName" value="${param.prdOfferingName}"/>
			<html-el:hidden property="headingInput" value="ViewGroupCharges"/>
			<mifos:SecurityParam property="Group" />
			<html-el:hidden property="statusId" value="${param.statusId}"/> 
			<html-el:hidden property="globalCustNum" value="${param.globalCustNum}" />
</tiles:put>
</tiles:insert>      
