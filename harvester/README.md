sead-harvester
==============

Set of VIVO Harvesters to fetch profiles from different remote APIs.

How to setup
------------
1. Download VIVO harvester 1.5 and extract 
2. Copy contents of the bin directory into harvester/bin
3. Set harvester path in the run-sead.sh
4. Build this project using 'mvn clean install'
5. Copy sead-harvester jar into harvester bin
6. Update the vivo.modle.xml with proper VIVO database connection information (these can be found
   in tomcat/webapps/vivotest/WEB-INF/classes/deploy.properties file)
7. Change the namespace in all configuration files
8. Provide a comma separated list of ORCiD ID's be fetched in 'orcid-ids' file
9. Execute run-sead.sh
