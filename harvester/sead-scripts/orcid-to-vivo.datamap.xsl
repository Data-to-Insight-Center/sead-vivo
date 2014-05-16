<!-- <?xml version="1.0"?> -->
<!-- Header information for the Style Sheet
	The style sheet requires xmlns for each prefix you use in constructing
	the new elements
-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:core="http://vivoweb.org/ontology/core#"
                xmlns:foaf="http://xmlns.com/foaf/0.1/"
                xmlns:score='http://vivoweb.org/ontology/score#'
                xmlns:bibo='http://purl.org/ontology/bibo/'
                xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#'
                xmlns:ufVivo='http://vivo.ufl.edu/ontology/vivo-ufl/'>

    <!-- This will create indenting in xml readers -->
    <xsl:output method="xml" indent="yes"/>
    <xsl:variable name="baseURI">http://www.orcid.org/ns/orcid/</xsl:variable>
    <xsl:variable name="path"><xsl:value-of select="/orcid-message/orcid-profile/orcid-identifier/path" /></xsl:variable>

    <xsl:template match="/orcid-message">
        <rdf:RDF xmlns:owlPlus='http://www.w3.org/2006/12/owl2-xml#'
                 xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'
                 xmlns:skos='http://www.w3.org/2008/05/skos#'
                 xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#'
                 xmlns:owl='http://www.w3.org/2002/07/owl#'
                 xmlns:vocab='http://purl.org/vocab/vann/'
                 xmlns:swvocab='http://www.w3.org/2003/06/sw-vocab-status/ns#'
                 xmlns:dc='http://purl.org/dc/elements/1.1/'
                 xmlns:vitro='http://vitro.mannlib.cornell.edu/ns/vitro/0.7#'
                 xmlns:core='http://vivoweb.org/ontology/core#'
                 xmlns:foaf='http://xmlns.com/foaf/0.1/'
                 xmlns:score='http://vivoweb.org/ontology/score#'
                 xmlns:xs='http://www.w3.org/2001/XMLSchema#'
                 xmlns:ufVivo='http://vivo.ufl.edu/ontology/vivo-ufl/'>
            <xsl:apply-templates select="orcid-profile" />
        </rdf:RDF>
    </xsl:template>

    <!-- The search result -->
    <xsl:template match="orcid-profile">
        <rdf:Description rdf:about="{$baseURI}author/{$path}">
            <rdf:type rdf:resource="http://xmlns.com/foaf/0.1/Person"/>
            <rdf:type rdf:resource="http://vivoweb.org/harvester/excludeEntity"/>
            <rdf:type rdf:resource="http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Flag1Value1Thing"/>
            <ufVivo:harvestedBy>ORCiD-Harvester</ufVivo:harvestedBy>
            <core:orcidId><xsl:value-of select="orcid-identifier/path" /></core:orcidId>
            <score:uri><xsl:value-of select="orcid-identifier/uri" /></score:uri>
            <score:host><xsl:value-of select="orcid-identifier/host" /></score:host>
            <foaf:firstName><xsl:value-of select="orcid-bio/personal-details/given-names" /></foaf:firstName>
            <foaf:lastName><xsl:value-of select="orcid-bio/personal-details/family-name" /></foaf:lastName>
            <score:credit-name><xsl:value-of select="orcid-bio/personal-details/credit-name" /></score:credit-name>
            <core:overview><xsl:value-of select="orcid-bio/biography" /></core:overview>
            <xsl:apply-templates select="orcid-bio/contact-details/email" />
            <xsl:apply-templates select="orcid-bio/researcher-urls/researcher-url" mode="urlRef"/>
            <xsl:apply-templates select="orcid-activities/orcid-works/orcid-work" mode="workRef"/>
        </rdf:Description>
        <xsl:apply-templates select="orcid-bio/researcher-urls/researcher-url" mode="urlFull"/>
        <xsl:apply-templates select="orcid-activities/orcid-works/orcid-work" mode="workFull"/>
    </xsl:template>

    <xsl:template match="orcid-activities/orcid-works/orcid-work" mode="workRef">
        <core:authorInAuthorship rdf:resource="{$baseURI}authorship/{$path}authorship{position()}" />
    </xsl:template>

    <xsl:template match="orcid-activities/orcid-works/orcid-work" mode="workFull">
        <rdf:Description rdf:about="{$baseURI}work/{$path}work{position()}">
            <rdf:type rdf:resource="http://purl.org/ontology/bibo/AcademicArticle"/>
            <rdf:type rdf:resource="http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Flag1Value1Thing"/>
            <ufVivo:harvestedBy>ORCiD-Harvester</ufVivo:harvestedBy>
            <score:work-id><xsl:value-of select="@put-code" /></score:work-id>
            <bibo:doi><xsl:value-of select="work-external-identifiers/work-external-identifier/work-external-identifier-id" /></bibo:doi>
            <core:Title><xsl:value-of select="work-title/title" /></core:Title>
            <bibo:abstract><xsl:value-of select="work-title/title" /></bibo:abstract>
            <rdfs:label><xsl:value-of select="work-title/title" /></rdfs:label>
            <score:journal-title><xsl:value-of select="journal-title" /></score:journal-title>
            <score:citation-type><xsl:value-of select="work-citation/work-citation-type" /></score:citation-type>
            <score:citation><xsl:value-of select="work-citation/citation" /></score:citation>
            <score:work-type><xsl:value-of select="work-type" /></score:work-type>
            <xsl:apply-templates select="url" mode="workurlRef">
                <xsl:with-param name="work_number" select="position()"/>
            </xsl:apply-templates>
            <core:dateTimeValue>
                <rdf:Description rdf:about="{$baseURI}work/year{publication-date/year}">
                    <rdf:type rdf:resource="http://vivoweb.org/ontology/core#DateTimeValue"/>
                    <core:dateTimePrecision rdf:resource="http://vivoweb.org/ontology/core#yearPrecision"/>
                    <core:dateTime rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">
                        <xsl:value-of select="publication-date/year"/>-01-01T00:00:00
                    </core:dateTime>
                </rdf:Description>
            </core:dateTimeValue>
            <core:informationResourceInAuthorship rdf:resource="{$baseURI}authorship/{$path}authorship{position()}"/>
        </rdf:Description>
        <rdf:Description rdf:about="{$baseURI}authorship/{$path}authorship{position()}">
            <rdf:type rdf:resource="http://vivoweb.org/ontology/core#Authorship"/>
            <rdf:type rdf:resource="http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Flag1Value1Thing"/>
            <rdf:type rdf:resource="http://vitro.mannlib.cornell.edu/ns/vitro/0.7#DependentResource"/>
            <rdf:type rdf:resource="http://vivoweb.org/ontology/core#DependentResource"/>
            <ufVivo:harvestedBy>ORCiD-Harvester</ufVivo:harvestedBy>
            <core:linkedAuthor rdf:resource="{$baseURI}author/{$path}"/>
            <core:linkedInformationResource rdf:resource="{$baseURI}work/{$path}work{position()}"/>
        </rdf:Description>
        <xsl:apply-templates select="url" mode="workurlFull">
            <xsl:with-param name="work_number" select="position()"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--email addresses-->
    <xsl:template match="orcid-bio/contact-details/email">
        <score:email><xsl:value-of select="." /></score:email>
    </xsl:template>

    <xsl:template match="orcid-bio/researcher-urls/researcher-url" mode="urlRef">
        <core:webpage rdf:resource="{$baseURI}researcher-url/{$path}url{position()}" />
    </xsl:template>

    <xsl:template match="orcid-bio/researcher-urls/researcher-url" mode="urlFull">
        <rdf:Description rdf:about="{$baseURI}researcher-url/{$path}url{position()}" >
            <ufVivo:harvestedBy>ORCiD-Harvester</ufVivo:harvestedBy>
            <rdf:type rdf:resource="http://purl.org/ontology/bibo/Webpage"/>
            <rdf:type rdf:resource="http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Flag1Value1Thing" />
            <core:Title><xsl:value-of select="url-name"/></core:Title>
            <core:linkURI><xsl:value-of select="url"/></core:linkURI>
            <core:webpageOf rdf:resource="{$baseURI}author/{$path}"/>
        </rdf:Description>
    </xsl:template>

    <xsl:template match="url" mode="workurlRef">
        <xsl:param name="work_number"/>
        <core:webpage rdf:resource="{$baseURI}work-url/{$path}work-url{$work_number}" />
    </xsl:template>

    <xsl:template match="url" mode="workurlFull">
        <xsl:param name="work_number"/>
        <rdf:Description rdf:about="{$baseURI}work-url/{$path}work-url{$work_number}" >
            <ufVivo:harvestedBy>ORCiD-Harvester</ufVivo:harvestedBy>
            <rdf:type rdf:resource="http://purl.org/ontology/bibo/Webpage"/>
            <rdf:type rdf:resource="http://vitro.mannlib.cornell.edu/ns/vitro/0.7#Flag1Value1Thing" />
            <core:linkURI><xsl:value-of select="."/></core:linkURI>
            <core:webpageOf rdf:resource="{$baseURI}work/{$path}work{$work_number}"/>
        </rdf:Description>
    </xsl:template>

</xsl:stylesheet>


