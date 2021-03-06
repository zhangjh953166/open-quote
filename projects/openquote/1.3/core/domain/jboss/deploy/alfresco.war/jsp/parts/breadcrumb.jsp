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
* http://www.alfresco.com/legal/licensing
--%>
<%-- Breadcrumb area --%>
<%-- Designed to support a variable height breadcrumb --%>
<tr>
<td><img src="<%=request.getContextPath()%>/images/parts/headbar_1.gif" width="4" height="7" alt=""/></td>
<td style="width:100%; background-image: url(<%=request.getContextPath()%>/images/parts/headbar_2.gif)"></td>
<td><img src="<%=request.getContextPath()%>/images/parts/headbar_3.gif" width="4" height="7" alt=""/></td>
</tr>

<tr>
<td style="background-image: url(<%=request.getContextPath()%>/images/parts/headbar_4.gif)"></td>
<td style="background-color: #dfe6ed;">
<%-- Breadcrumb component --%>
<div style="padding-left:8px" class="headbarTitle">
<a:breadcrumb value="#{NavigationBean.location}" styleClass="headbarLink" />
</div>
</td>
<td style="background-image: url(<%=request.getContextPath()%>/images/parts/headbar_6.gif)"></td>
</tr>

<tr>
<td><img src="<%=request.getContextPath()%>/images/parts/headbar_7.gif" width="4" height="10" alt=""/></td>
<td style="width: 100%; background-image: url(<%=request.getContextPath()%>/images/parts/headbar_8.gif)"></td>
<td><img src="<%=request.getContextPath()%>/images/parts/headbar_9.gif" width="4" height="10" alt=""/></td>
</tr>
