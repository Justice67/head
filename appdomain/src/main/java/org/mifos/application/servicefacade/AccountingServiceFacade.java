/*
 * Copyright (c) 2005-2011 Grameen Foundation USA
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
package org.mifos.application.servicefacade;

import java.util.Date;
import java.util.List;

import org.mifos.application.accounting.business.FinancialYearBO;
import org.mifos.application.accounting.business.GlBalancesBO;
import org.mifos.application.accounting.business.GlMasterBO;
import org.mifos.dto.domain.AccountingDto;
import org.mifos.dto.domain.OfficeGlobalDto;
import org.mifos.dto.domain.ViewTransactionsDto;

/**
 * Service facade for Mifos Accounting functionality.
 */
public interface AccountingServiceFacade {

	List<OfficeGlobalDto> loadOfficesForLevel(Short officeLevelId);

	GlBalancesBO loadExistedGlBalancesBO(Integer officeLevelId,String officeId,String glCodeValue);

	List<OfficeGlobalDto> loadCustomerForLevel(Short customerLevelId);

	List<AccountingDto> mainAccountForCash();

	List<AccountingDto> loadDebitAccounts();

	List<AccountingDto> loadCreditAccounts(String glCode);

	List<AccountingDto> mainAccountForBank();

	List<AccountingDto> accountHead(String glCode);

	List<ViewTransactionsDto> getAccountingTransactions(Date trxnDate,
			int startRecord, int numberOfRecords);

	int getNumberOfTransactions(Date trxnDate);

	String getLastProcessDate();

	boolean processMisPostings(Date lastProcessDate, Date processTillDate,Short createdBy);

	FinancialYearBO getFinancialYear();

	List<AccountingDto> findTotalGlAccounts();

	FinancialYearBO getFinancialYearBO(int financialYearId);

	boolean savingAccountingTransactions(GlMasterBO bo);

	boolean savingOpeningBalances(GlBalancesBO bo);
}