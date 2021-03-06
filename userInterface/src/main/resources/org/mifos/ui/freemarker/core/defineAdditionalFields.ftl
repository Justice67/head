[#ftl]
[#--
* Copyright (c) 2005-2011 Grameen Foundation USA
*  All rights reserved.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
*  implied. See the License for the specific language governing
*  permissions and limitations under the License.
*
*  See also http://www.apache.org/licenses/LICENSE-2.0.html for an
*  explanation of the license and how it is applied.
--]

[@layout.header "title" /]
  [@widget.topNavigationNoSecurity currentTab="Admin" /]
  <!--  Main Content Begins-->
  <div class="content definePageMargin">
    <div class="borders span-22">
      <div class="borderbtm span-22">
        <p class="span-15 arrowIMG orangeheading">[@spring.message "datadisplayandrules.defineAdditionalFields.additionalfieldinformation" /]</p>
        <p class="span-3 arrowIMG1 orangeheading last">[@spring.message "reviewAndSubmit" /]</p>
      </div>
      <div class="subcontent ">
      <form method="" action="" name="formname">
      <p>&nbsp;&nbsp;</p>
        <div class="fontBold">[@spring.message "datadisplayandrules.defineAdditionalFields.defineadditionalfields" /]&nbsp;--&nbsp;<span class="orangeheading">[@spring.message "datadisplayandrules.defineAdditionalFields.enteradditionalfieldinformation" /]</span></div>
        <div>[@spring.message "completethefieldsbelow.ThenclickPreview.ClickCanceltoreturntoAdminwithoutsubmittinginformation" /]</div>
        <div><span class="red">* </span>[@spring.message "fieldsmarkedwithanasteriskarerequired." /] </div>
        <p>&nbsp;&nbsp;</p>
            <p class="fontBold">[@spring.message "datadisplayandrules.defineAdditionalFields.additionalfielddetails" /] </p>
            <p>&nbsp;&nbsp;</p>
        <div class="prepend-3  span-21 last">
            <div class="span-20 "><span class="span-3 rightAlign"><span class="red">* </span>[@spring.message "datadisplayandrules.defineAdditionalFields.category" /]</span><span class="span-4">&nbsp;
                       <select name="select">
                          <option >[@spring.message "--Select--"/]</option>
                        <option >[@spring.message "datadisplayandrules.viewadditionalfields.personnel"/]</option>
                        <option >[@spring.message "datadisplayandrules.viewadditionalfields.office"/]</option>
                        <option >[@spring.message "datadisplayandrules.viewadditionalfields.client"/]</option>
                        <option >[@spring.message "datadisplayandrules.viewadditionalfields.group"/]</option>
                        <option >[@spring.message "datadisplayandrules.viewadditionalfields.center"/]</option>
                        <option >[@spring.message "datadisplayandrules.viewadditionalfields.loan"/]</option>
                        <option >[@spring.message "datadisplayandrules.viewadditionalfields.savings"/]</option>
                    </select></span>
            </div>
            <div class="span-20 "><span class="span-3 rightAlign"><span class="red">* </span>[@spring.message "datadisplayandrules.defineAdditionalFields.label" /]</span><span class="span-5">&nbsp;
                    <input type="text" id="define_additional_fields.input.labelName" /></span>
              </div>
            <div class="span-20 "><span class="span-3 rightAlign">[@spring.message "datadisplayandrules.editadditionalfields.mandatory" /]</span><span class="span-4">&nbsp;
                    <input type="checkbox" /></span>
              </div>
            <div class="span-20 "><span class="span-3 rightAlign"><span class="red">* </span>[@spring.message "datadisplayandrules.defineAdditionalFields.dataType" /]</span><span class="span-5">&nbsp;
                       <select name="select">
                          <option >[@spring.message "--Select--"/]</option>
                        <option >[@spring.message "datadisplayandrules.defineAdditionalFields.numeric"/]</option>
                        <option >[@spring.message "datadisplayandrules.defineAdditionalFields.text"/]</option>
                    </select></span>
            </div>
            <div class="span-20 "><span class="span-3 rightAlign">[@spring.message "datadisplayandrules.defineAdditionalFields.defaultValue" /]</span><span class="span-5">&nbsp;
                    <input type="text" /></span>
              </div>

        </div>
        <div class="clear">&nbsp;</div>
        <hr />
        <div class="prepend-9">
          <input class="buttn" type="button" id="define_additional_fields.button.preview" name="preview" value="[@spring.message "preview"/]"/>
          <input class="buttn2" type="button" id="define_additional_fields.button.cancel" name="cancel" value="[@spring.message "cancel"/]"/>
        </div>
        <div class="clear">&nbsp;</div>
        </form>
      </div><!--Subcontent Ends-->
    </div>
  </div>
  <!--Main Content Ends-->
  [@layout.footer/]