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

package org.mifos.framework.util.helpers;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.mifos.accounts.savings.persistence.GenericDao;
import org.mifos.accounts.savings.persistence.GenericDaoHibernate;
import org.mifos.application.master.MessageLookup;
import org.mifos.application.master.business.LookUpEntity;
import org.mifos.config.Localization;
import org.mifos.config.persistence.ApplicationConfigurationDao;
import org.mifos.config.persistence.ApplicationConfigurationDaoHibernate;
import org.mifos.config.util.helpers.ConfigurationConstants;
import org.mifos.dto.domain.ConfigurableLookupLabelDto;
import org.mifos.framework.struts.tags.MifosPropertyMessageResources;
import org.mifos.security.login.util.helpers.LoginConstants;
import org.mifos.security.util.UserContext;

/**
 * This Util class is a singleton class used by the MifosLabelTag to obtain the
 * Label based on the Locale. The class is also used to determine whether the
 * eleemnt is configurable mandatory and also to obtain the currency of the
 * Locale, if the element is of type currency.
 */
public class LabelTagUtils {

    /**
     * private constructor to prevent multiple instance creation.
     */
    private LabelTagUtils() {
    }

    /**
     * The static instance of the class
     */
    private static LabelTagUtils instance = new LabelTagUtils();

    /**
     * static method to obatin the instance of the class.
     */
    public static LabelTagUtils getInstance() {
        return instance;
    }

    /**
     * The method is used to obtain the label associated with the key based on
     * the Locale.
     *
     * @param key
     *            -- The key used to obtain the Label
     * @return Label associated with key of the Locale
     */
    public String getLabel(PageContext pageContext, String bundle, String localeKey, String key, String[] args)
            throws JspException {
        return TagUtils.getInstance().message(pageContext, bundle, localeKey, key, args);
    }

    public String getLabel(PageContext pageContext, String bundle, Locale locale, String key, String[] args)
            throws JspException {
        String message = null;
        MessageResources resources = TagUtils.getInstance().retrieveMessageResources(pageContext, bundle, false);
        if (args == null) {
            message = resources.getMessage(locale, key);
        } else {
            message = resources.getMessage(locale, key, args);
        }

        if (StringUtils.isBlank(message)) {
            String labelKey = key;
            if (ConfigurationConstants.SERVICE_CHARGE.equalsIgnoreCase(key)) {
                labelKey = ConfigurationConstants.INTEREST;
            }
            if (ConfigurationConstants.CENTER.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.GROUP.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.CLIENT.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.LOAN.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.SAVINGS.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.INTEREST.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.STATE.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.POSTAL_CODE.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.ETHINICITY.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.CITIZENSHIP.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.HANDICAPPED.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.GOVERNMENT_ID.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.ADDRESS1.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.ADDRESS2.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.ADDRESS3.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.EXTERNALID.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.BULKENTRY.equalsIgnoreCase(labelKey) ||
                    ConfigurationConstants.CITY.equalsIgnoreCase(labelKey)) {
                message = MessageLookup.getInstance().lookupLabel(labelKey);
            }
        }

        if (StringUtils.isBlank(message)) {
            // FOR PersonnelUIResources.properties
            String newKey = "Personnel." + key;
            message = resources.getMessage(locale, newKey);
        }

        if (StringUtils.isBlank(message)) {
            message = "";
        }

        return message;
    }

    /**
     * Only use is in MifosSelectNew, which doesn't appear to be used.
     */
    @SuppressWarnings("unchecked")
    public Collection getCustomValueListElements(PageContext pageContext, String bundle, Locale locale, String key,
            String mappingKey, String[] args) throws JspException {
        Collection returnVal = null;
        MifosPropertyMessageResources resources = (MifosPropertyMessageResources) TagUtils.getInstance()
                .retrieveMessageResources(pageContext, bundle, false);
        returnVal = resources.getCustomValueListElements(locale, key, mappingKey);

        return returnVal;
    }

    /**
     * The method is used to check, if the element associated with the key is
     * configurable mandatory or not.
     *
     * @param key
     *            -- The key used to determine if the element associated with is
     *            configurable mandatory or not
     * @return true if the element associated with the key is configurable
     *         mandatory
     */
    @SuppressWarnings("unchecked")
    public boolean isConfigurableMandatory(String key, PageContext pageContext) {
        // TODO get is mandatory or not from the cache.
        Map mandatoryMap = (Map) pageContext.getSession().getAttribute("ConfigurableMandatory");
        if (mandatoryMap == null) {
            return false;
        }

        return mandatoryMap.containsKey(key);
    }

    /**
     * The method is used to check, if the element associated with the key is
     * hidden or not.
     *
     * @param key
     *            --The key used to determine if the element associated with is
     *            hidden or not
     * @return true if the element associated with the key is hidden
     */
    @SuppressWarnings("unchecked")
    public boolean isHidden(String key, PageContext pageContext) {
        // TODO get is hidden or not from the cache.
        Map hiddenMap = (Map) pageContext.getSession().getAttribute("Hidden");
        if (hiddenMap == null) {
            return false;
        }

        return hiddenMap.containsKey(key);
    }

    /**
     * The method is used to check, if the element associated with the key is
     * confidential or not.
     *
     * @param key
     *            --The key used to determine if the element associated with is
     *            confidential or not
     * @return true if the element associated with the key is confidential
     */
    @SuppressWarnings("unchecked")
    public boolean isConfidential(String key, PageContext pageContext) {
        // TODO get is confidential or not from the cache.
        Map confidentialMap = (Map) pageContext.getSession().getAttribute("Confidential");
        if (confidentialMap == null) {
            return false;
        }

        return confidentialMap.containsKey(key);
    }

    /**
     * This helper method returns the User Preferred Locale, if any. Otherwise
     * it returns default Mifos Locale
     *
     * @param pageContext
     * @return User Preferred Locale
     */
    public String getUserPreferredLocale(PageContext pageContext) {
        String userPreferredLocale = null;
        UserContext userContext = getUserContextFromSession(pageContext.getSession());
        if (null != userContext) {
            Locale locale = userContext.getCurrentLocale();
            if (null != locale) {
                userPreferredLocale = locale.getLanguage() + "_" + locale.getCountry();
            }

        }
        return userPreferredLocale;

    }

    public static String getUserPreferredLocaleHelper(UserContext userContext) {
        String userPreferredLocale = null;
        if (null != userContext) {
            Locale locale = userContext.getCurrentLocale();
            if (null != locale) {
                userPreferredLocale = locale.getLanguage() + "_" + locale.getCountry();
            }

        }

        return userPreferredLocale;

    }

    public Locale getUserPreferredLocaleObject(PageContext pageContext) {
        Locale locale = null;
        UserContext userContext = getUserContextFromSession(pageContext.getSession());
        if (null != userContext) {
            locale = userContext.getCurrentLocale();
        } else {
            locale = Localization.getInstance().getMainLocale();
        }

        return locale;
    }

    private UserContext getUserContextFromSession(HttpSession session) {
        UserContext userContext = null;
        if (null != session) {
            userContext = (UserContext) session.getAttribute(LoginConstants.USERCONTEXT);
        }
        return userContext;
    }
}
