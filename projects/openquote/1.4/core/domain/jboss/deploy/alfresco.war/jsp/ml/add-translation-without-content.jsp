<%--
* Copyright (C) 2005-2007 Alfresco Software Limited.

* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

* As a special exception to the terms and conditions of version 2.0 of
* the GPL, you may redistribute this Program in connection with Free/Libre
* and Open Source Software ("FLOSS") applications as described in Alfresco's
* FLOSS exception.  You should have recieved a copy of the text describing
* the FLOSS exception, and it is also available here:
* http://www.alfresco.com/legal/licensing"
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/alfresco.tld" prefix="a" %>
<%@ taglib uri="/WEB-INF/repo.tld" prefix="r" %>

<h:panelGrid columns="1" cellpadding="2" style="padding-top: 4px; padding-bottom: 4px;" width="100%" rowClasses="wizardSectionHeading">
<h:outputText value="#{msg.properties}" escape="false" />
</h:panelGrid>

<h:panelGrid columns="3" cellpadding="3" cellspacing="3" border="0">

<h:graphicImage/>
<h:outputText value="#{msg.title}:"/>
<h:inputText id="title" value="#{DialogManager.bean.title}"  maxlength="1024" size="35" immediate="false"/>

<h:graphicImage/>
<h:outputText value="#{msg.description}:"/>
<h:inputTextarea id="description" value="#{DialogManager.bean.description}" cols="35" rows="5" immediate="false"/>

<h:graphicImage value="/images/icons/required_field.gif" alt="#{msg.required_field}" />
<h:outputText value="#{msg.author}:"/>
<h:inputText id="author" value="#{DialogManager.bean.author}"   maxlength="1024" size="35" immediate="false" onkeyup="checkButtonState();" onchange="checkButtonState();"/>

<h:graphicImage value="/images/icons/required_field.gif" alt="#{msg.required_field}" />
<h:outputText value="#{msg.language}:"/>
<h:selectOneMenu id="language" value="#{DialogManager.bean.language}" immediate="false"  onchange="checkButtonState();" onkeydown="checkButtonState();" onkeyup="checkButtonState();">
<f:selectItem  itemLabel="#{msg.select_language}" itemValue="null"/>
<f:selectItems value="#{DialogManager.bean.unusedLanguages}"/>
</h:selectOneMenu>
</h:panelGrid>

<h:panelGrid columns="1" cellpadding="2" style="padding-top: 4px; padding-bottom: 4px;" width="100%" rowClasses="wizardSectionHeading">
<h:outputText value="#{msg.other_properties}" escape="false" />
</h:panelGrid>

<h:panelGrid columns="2" cellpadding="3" cellspacing="3" border="0">
<h:selectBooleanCheckbox value="#{DialogManager.bean.showOtherProperties}" />
<h:outputText id="text10" value="#{msg.modify_props_when_page_closes}" />
</h:panelGrid>


<script type="text/javascript">

function checkButtonState()
{
if (document.getElementById("dialog:dialog-body:author").value.length == 0 ||
document.getElementById("dialog:dialog-body:language").selectedIndex == 0 )
{
document.getElementById("dialog:finish-button").disabled = true;
}
else
{
document.getElementById("dialog:finish-button").disabled = false;
}
}

</script>


