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

<#-- Upload a replacement main image for an Individual. -->

${scripts.add('<script type="text/javascript" src="${urls.base}/js/jquery.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/imageUpload/imageUploadUtils.js"></script>')}

${stylesheets.add('<link rel="stylesheet" href="${urls.base}/css/uploadImages.css" />')}

<section id="photoUploadContainer" role="region">
    <h2>Photo Upload</h2>
              
    <#if errorMessage??>
        <section id="error-alert" role="alert"><img src="${urls.images}/iconAlert.png" alt="Error alert icon" />
            <p>${errorMessage}</p>
        </section>
    </#if>

    <section id="photoUploadDefaultImage" role="region">
        <h3>Current Photo</h3>
        
        <img src="${thumbnailUrl}" width="115" height="115" alt="Individual photo" /> 
    </section>
          
    <form id="photoUploadForm" action="${formAction}" enctype="multipart/form-data" method="post" role="form">
        <label>Upload a photo <span> (JPEG, GIF or PNG)</span></label>
        
        <input id="datafile" type="file" name="datafile" size="30" />
         <p class="note">Maximum file size: ${maxFileSize} megabytes<br />
        Minimum image dimensions: ${thumbnailWidth} x ${thumbnailHeight} pixels</p>
        <input class="submit" type="submit" value="Upload photo"/>
        
        <span class="or"> or <a class="cancel"  href="${cancelUrl}" title="cancel">Cancel</a></span>
    </form>
</section>