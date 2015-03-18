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

	<!--header navigation begin-->
	<div id="header-navigation">
		<nav id="site-navigation" class="main-navigation" role="navigation">
		   		<h1 class="menu-toggle">Menu</h1>
	 			<div class="menu-global-navigation-container">
	 				<ul id="menu-global-navigation" class="menunav-menu">
	 					<li class="menu-item "><a href="http://sead-data.net/">Home</a></li>
						<li class="menu-item  menu-item-has-children "><a href="http://sead-data.net/about/">About</a>
						<ul class="sub-menu">
							<li class="menu-item"><a href="http://sead-data.net/about/sead-team/">Project Team</a></li>
							<li class="menu-item"><a href="http://sead-data.net/about/advisory-board/">Advisory Board</a></li>
							<li class="menu-item"><a href="http://sead-data.net/about/publicationspresentations/">Publications &amp; Presentations</a></li>
							<li class="menu-item"><a href="http://sead-data.net/about/newsevents/">News &amp; Events</a></li>
						</ul>
					</li>
					<li class="menu-item menu-item-has-children"><a href="http://sead-data.net/feature-tour/">Features Tour</a>
						<ul class="sub-menu">
							<li class="menu-item"><a href="http://sead-data.net/feature-tour/tools-in-development/">Tools in Development</a></li>
						</ul>
					</li>
					<li class="menu-item"><a href="https://sead.ncsa.illinois.edu/projects/">Project Spaces</a></li>
					<li class="menu-item "><a href="http://seadva.d2i.indiana.edu:8181/sead-access/">Virtual Archive</a></li>
					<li class="menu-item  current-menu-item  menu-item-has-children"><a href="${urls.base}">Research Network</a>
						<ul class="sub-menu">
							<li class="menu-item "><a href="${urls.base}/people">People</a></li>
							<li class="menu-item "><a href="${urls.base}/organizations">Organizations</a></li>
							<li class="menu-item "><a href="${urls.base}/research">Research</a></li>
							<li class="menu-item "><a href="${urls.base}/events">Events</a></li>
						</ul>
					</li>
					<li class="menu-item menu-item-has-children"><a href="http://sead-data.net/help/">Help</a>
						<ul class="sub-menu">
							<li  class="menu-item"><a href="http://sead-data.net/help/faq/">FAQ</a></li>
						</ul>
					</li>
				</ul>
			</div> 
		</nav>
	</div>
	<!--header navigation end-->    

</div> <!-- #header-left-section -->
</div> <!-- header-text-nav-wrap -->

</header>
<div style="margin:auto;">

<div id="wrapper-content" role="main">        
    <#if flash?has_content>
        <#if flash?starts_with("Welcome") >
            <section  id="welcome-msg-container" role="container">
                <section  id="welcome-message" role="alert">${flash}</section>
            </section>
        <#else>
            <section id="flash-message" role="alert">
                ${flash}
            </section>
        </#if>
    </#if>
    
    <!--[if lte IE 8]>
    <noscript>
        <p class="ie-alert">This site uses HTML elements that are not recognized by Internet Explorer 8 and below in the absence of JavaScript. As a result, the site will not be rendered appropriately. To correct this, please either enable JavaScript, upgrade to Internet Explorer 9, or use another browser. Here are the <a href="http://www.enable-javascript.com"  title="java script instructions">instructions for enabling JavaScript in your web browser</a>.</p>
    </noscript>
    <![endif]-->