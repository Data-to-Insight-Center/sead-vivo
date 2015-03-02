<#--
Copyright (c) 2013, Cornell University
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.
    * Neither the name of Cornell University nor the names of its contributors
      may be used to endorse or promote products derived from this software
      without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
-->

	<div id="page" class="hfeed site">
		<header id="masthead" class="site-header clearfix">
			<div id="header-text-nav-wrap" class="clearfix">
	
				<!-- #header-logo-image -->
				<div id="header-left-section">
					<div id="header-logo-image">
						<a href="${urls.home}" title="SEAD" rel="home"><img src="http://sead-data.net/wp-content/uploads/2014/06/logo.png" alt="SEAD"></a>
					</div><!-- #header-logo-image -->
	
	    			<nav role="navigation">
        				<ul id="header-nav" role="list">
            				<li role="listitem"><a href="${urls.index}" title="index">Index</a></li>
            
            				<#if user.loggedIn>
                				<#if user.hasSiteAdminAccess>
                    				<li role="listitem"><a href="${urls.siteAdmin}" title="site admin">Site Admin</a></li>
                				</#if>
                    			<li id="user-menu">${user.loginName}</li>
                    			<#if user.hasProfile>
                      				<li role="listitem"><a href="${user.profileUrl}" title="my profile">My profile</a></li>
                    			</#if>
                    			<#if urls.myAccount??>
                        			<li role="listitem"><a href="${urls.myAccount}" title="my account">My account</a></li>
                        		</#if>
                        		<li role="listitem"><a href="${urls.logout}" title="log out">Log out</a></li>
                        
                				${scripts.add('<script type="text/javascript" src="${urls.base}/js/userMenu/userMenuUtils.js"></script>')}
                
            				<#else>
                				<li role="listitem"><a class="log-out" title="log in to manage this site" href="${urls.login}">Log in</a></li>
            				</#if>
        			</ul>
    			</nav>

				<!-- #header-right-section --> 
				<div id="header-right-section">
					<div id="header-right-sidebar" class="clearfix">			
						<aside id="cnss_widget-3" class="widget widget_cnss_widget">
							<table class="cnss-social-icon" style="width:138px" border="0" cellspacing="0" cellpadding="0">
								<tbody>
									<tr>
										<td>
											<a target="_blank" title="facebook" href="https://www.facebook.com/SEADDataNet">
											<img src="http://sead-data.net/wp-content/uploads/1403285144_facebook.png" border="0" width="32" height="32" alt="facebook">
											</a>
										</td>
										<td >
											<a target="_blank" title="twitter" href="https://twitter.com/SEADdatanet">
											<img src="http://sead-data.net/wp-content/uploads/1403285215_twitter.png" border="0" width="32" height="32" alt="twitter">
											</a>
										</td>
										<td>
											<a target="_blank" title="Slideshare" href="http://www.slideshare.net/SEADdatanet">
											<img src="http://sead-data.net/wp-content/uploads/1403285311_slidershare.png" border="0" width="32" height="32" alt="Slideshare">
											</a>
										</td>
										<td>
											<a target="_blank" title="Rss Feed" href="http://www.sead-data.net/?cat=11,12,13,29,30,31&amp;feed=rss2">
											<img src="http://sead-data.net/wp-content/uploads/1415982481_rss.png" border="0" width="32" height="32" alt="Rss Feed" style="opacity: 1;">
											</a>
										</td>
									</tr>
								</tbody>
							</table>
						</aside>
					</div> <!-- header-right-sidebar -->
				</div> <!-- #header-right-section --> 
                
