<!--
 
 * viewsavingstrxnhistory.jsp  version: 1.0
 
 
 
 * Copyright � 2005-2006 Grameen Foundation USA
 
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
 
 -->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html-el"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/tags/mifos-html" prefix = "mifos"%>
<%@ taglib uri="/mifos/customtags" prefix="mifoscustom"%>
<%@ taglib uri="/mifos/custom-tags" prefix="customtags"%>
<%@taglib uri="/loan/loanfunctions" prefix="loanfn"%>

<tiles:insert definition=".clientsacclayoutsearchmenu">
	<tiles:put name="body" type="string">
<SCRIPT >
	function ViewDetails(){
		closedaccsearchactionform.submit();
	}
	function ViewLoanDetails(form){
		form.submit();
	}

</SCRIPT>
	<html-el:form method="post" action="/loanAction.do?method=get" >

      <table width="95%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td class="bluetablehead05">
			  <span class="fontnormal8pt">
	          	<customtags:headerLink/> 
	          	
	          	<c:choose>
	          	<c:when test="${param.input == 'LoanDetails'}">
	          	  <html-el:link action="loanAction.do?globalAccountNum=${param.globalAccountNum}&method=get"> /
	          	    <c:out value="${param.prdOfferingName}"></c:out>
	          	  </html-el:link>
	          	</c:when>
	          	<c:otherwise>
	          	<html-el:link href="javascript:ViewDetails()"> /
	          	<c:if test="${param.input == 'ViewCenterCharges'}">
	          	   <mifos:mifoslabel name="${ConfigurationConstants.CENTER}"/>
	          	</c:if>
	           <c:if test="${param.input == 'ViewGroupCharges'}">
	          	   <mifos:mifoslabel name="${ConfigurationConstants.GROUP}"/>
	          	</c:if>
	          	<c:if test="${param.input == 'ViewClientCharges'}">
	          	  <mifos:mifoslabel name="${ConfigurationConstants.CLIENT}"/>
	          	</c:if>
	          	<mifos:mifoslabel name="Center.Charges" bundle="CenterUIResources"/>
	          	</html-el:link>
	          	</c:otherwise>
	          	</c:choose>
	          </span>               
          </td>
        </tr>
      </table>
      <table width="95%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td align="left" valign="top" class="paddingL15T15" >
          
            <table width="95%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="83%" class="headingorange">
                	<span class="heading">
                	<c:out value="${param.prdOfferingName}"></c:out> # <c:out value="${param.globalAccountNum}"></c:out> - 
                	</span> 
                	<mifos:mifoslabel name="Savings.Transactionhistory"/>
	            </td>
              </tr>
            </table>
            <br>
    
            <mifoscustom:mifostabletag source="trxnhistoryList" scope="session" xmlFileName="TrxnHistory.xml" moduleName="accounts" passLocale="true"/>
            <br>
            
            <table width="95%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
					
					
					<c:choose>
					<c:when test="${param.input == 'LoanDetails'}">
					<html-el:button property="returnToAccountDetailsbutton"
					       onclick="javascript:ViewLoanDetails(this.form)"
						     styleClass="buttn" style="width:165px;">
								<mifos:mifoslabel name="accounts.returndetails" />
						</html-el:button>
					</c:when>
					<c:otherwise>
					   <html-el:button property="returnToAccountDetailsbutton"
					       onclick="javascript:ViewDetails()"
						     styleClass="buttn" style="width:165px;">
								<mifos:mifoslabel name="accounts.backtocharges" />
						</html-el:button>
					</c:otherwise>	
					</c:choose>	
					</td>
				</tr>
    		</table>
          </td>
        </tr>
      </table>
<html-el:hidden property="statusId" value="${param.statusId}"/>  
</html-el:form>
<html-el:form  action="closedaccsearchaction.do?method=search">
<html-el:hidden property="searchNode(search_name)" value="${param.searchInput}"/>
<html-el:hidden property="prdOfferingName" value="${param.prdOfferingName}"/> 
<html-el:hidden property="globalAccountNum" value="${param.globalAccountNum}"/> 
<html-el:hidden property="accountId" value="${param.accountId}"/> 
<html-el:hidden property="accountType" value="${param.accountType}"/> 
<html-el:hidden property="input" value="${param.input}"/> 
<html-el:hidden property="statusId" value="${param.statusId}"/>
</html-el:form>
</tiles:put>
</tiles:insert>        