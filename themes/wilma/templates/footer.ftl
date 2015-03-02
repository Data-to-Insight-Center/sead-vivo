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


</div>
</div> <!-- #wrapper-content -->
</div>

<footer id="colophon" class="clearfix">	
			<div class="footer-socket-wrapper clearfix">
				<h1 class="menu-toggle">Footer</h1>
				<div class="inner-wrap">
					<div class="footer-socket-area">
						<div class="copyright">
							&nbsp;
        					<#if copyright??>
            					<small>&copy;${copyright.year?c}
            				<#if copyright.url??>
                				<a href="${copyright.url}" title="copyright">${copyright.text}</a>
            				<#else>
                				${copyright.text}
            				</#if>
             | 					<a class="terms" href="${urls.termsOfUse}" title="terms of use">Terms of Use</a></small> | 
        					</#if>
        						Powered by <a class="powered-by-vivo" href="http://vivoweb.org" target="_blank" title="powered by VIVO"><strong>VIVO</strong></a>
        					<#if user.hasRevisionInfoAccess>
             | 					Version <a href="${version.moreInfoUrl}" title="version">${version.label}</a>
        					</#if>
						</div>	
					</div>				
				</div>
			</div>
			<div class="footer-socket-wrapper clearfix">
				<div class="inner-wrap">
					<div class="footer-socket-area">
						<div class="copyright"><img style="vertical-align: middle" id="nsf" src="http://sead-data.net/wp-content/uploads/2014/06/nsf2.png" alt="NSF " width="30px" height="30px">SEAD is funded by the National Science Foundation under cooperative agreement #OCI0940824.</div>						
						<nav class="small-menu">
							<div class="menu-footer-menu-container">
								<ul id="menu-footer-menu" class="menu">	
									<a href="http://sead-data.net/contactus/">Contact Us</a>
								</li>
								</ul>
							</div>	
						</nav>	
					</div>
				</div>
			</div>			
		</footer>


<#include "scripts.ftl">