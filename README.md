VIVOSSO
===========
The VIVOSSO project is built on the VIVO project to enable single sign on feature to it with Google OAuth 2.0.
The VIVO code base that has been used VIVO 1.5.2. 

The documentation of the original VIVO project can be found at http://vivoweb.org/ .

_____________________________
Installation Guide
-----------------------
The installation of VIVO SSO is similar to the original VIVO project. The instructions can be found at
<a href="http://sourceforge.net/projects/vivo/files/Project%20Documentation/VIVO_Release_V1.5_Installation_Guide.pdf/download"> VIVO Rel 1.5 Installation Guide </a>.

<b> Google Signle Sign on Setup: </b>

1. Download the VIVOSSO application source as a .zip file and follow the instructions int eh installation guide to setup VIVO successfully.
2. Register the project as an app with Google Cloud Console.
3. At the top of the VIVO distribution directory, locate the deploy.properties that  contains various properties for compilation and deployement.
4. Configure the following properties in deploy.properties file:
    <table>
      <tr><td><b>Property </b></td>  <td><b>Value </b></td></tr>
      <tr><td>externalAuth.buttonText </td><td> The text to be displayed in the button on the Single Sign On button on the Home Page</td> </tr>
      <tr><td>externalAuth.serverUrl </td><td> The server URL for SSO. Since we use google authentication value will be http://google.com</td> </tr>
      <tr><td>externalAuth.callback_uri </td><td> URI that is invoked after successfull authentication of the google Id. This can be mapped in web.xml of the application to invoke the necessary servlet. This has to be configured at the Google Cloud console in Redirect URI when the application is registered with with Google. The expected value to be set for this project is http://localhost:8080/vivo/callback. </td> </tr>
      <tr><td>externalAuth.client_id </td><td> Client ID that is obtained when the application is registered with google. </td> </tr>
      <tr><td>externalAuth.cient_secret </td><td> the client secret key that is obtained on registering the ap in google console</td> </tr>
  </table>
5. After completing the above configuration perform an ant build, to deploy the application into tomcat.
6. The External Authentication button can now be seen on the home page. 


---------------------
