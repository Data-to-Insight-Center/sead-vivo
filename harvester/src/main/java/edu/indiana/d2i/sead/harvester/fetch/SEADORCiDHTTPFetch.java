package edu.indiana.d2i.sead.harvester.fetch;

import java.io.*;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vivoweb.harvester.util.InitLog;
import org.vivoweb.harvester.util.WebAide;
import org.vivoweb.harvester.util.args.ArgDef;
import org.vivoweb.harvester.util.args.ArgList;
import org.vivoweb.harvester.util.args.ArgParser;
import org.vivoweb.harvester.util.args.UsageException;
import org.vivoweb.harvester.util.repo.RecordHandler;
import org.vivoweb.harvester.util.repo.RecordStreamOrigin;
import org.vivoweb.harvester.util.repo.XMLRecordOutputStream;

public class SEADORCiDHTTPFetch implements RecordStreamOrigin {
    /**
     * SLF4J Logger
     */
    private static Logger log = LoggerFactory.getLogger(SEADORCiDHTTPFetch.class);
    /**
     * Query to run on data
     */
    private String query;
    /**
     * Maximum number of results to fetch
     */
    private String numResults;
    /**
     * Index of the starting result to fetch
     */
    private String startResult;
    /**
     * Number of records to fetch per batch
     */
    private String batchSize;
    /**
     * The Record Handler to write to
     */
    private RecordHandler rh;

    /**
     * a base XMLRecordOutputStream
     */
    protected static XMLRecordOutputStream baseXMLROS;


    /**
     * Constructor
     *
     * @param query       - query to execute on ORCiD
     * @param numResults  - number of results to fetch
     * @param startResult - index of the start result
     * @param batchSize   - size of a batch
     * @param rh          - output configuration
     */
    public SEADORCiDHTTPFetch(String query, String numResults, String startResult, String batchSize, RecordHandler rh) {
        this.query = query;
        this.numResults = numResults;
        this.startResult = startResult;
        this.batchSize = batchSize;
        this.rh = rh;

        // create XMLRecordOutputStream
        String[] tagsToSplitOn = {"orcid-profile"};
        String headerInfo = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<orcid-message>\n";
        String footerInfo = "\n</orcid-message>\n";
        String idLocationRegex = ".*?<path.*?>(.*?)</path>.*?";
        baseXMLROS = new XMLRecordOutputStream(tagsToSplitOn, headerInfo, footerInfo, idLocationRegex, null);
    }

    /**
     * Constructor
     *
     * @param args commandline arguments
     * @throws IOException    error creating task
     * @throws UsageException user requested usage message
     */
    private SEADORCiDHTTPFetch(String[] args) throws IOException, UsageException {
        this(getParser("SEADORCiDHTTPFetch").parse(args));
    }

    /**
     * Constructor
     *
     * @param argList parsed argument list
     * @throws IOException error creating task
     */
    private SEADORCiDHTTPFetch(ArgList argList) throws IOException {
        this(argList.get("q"), argList.get("n"), argList.get("s"), argList.get("b"),
                RecordHandler.parseConfig(argList.get("o"), argList.getValueMap("O")));
    }

    /**
     * Executes the task
     *
     * @throws IOException error processing search
     */
    public void execute() throws IOException {
        int start = Integer.parseInt(this.startResult);
        int batchSize = Integer.parseInt(this.batchSize);
        int rows;
        if (numResults.equalsIgnoreCase("all")) {
            // if all rows are needed, we don't know how many results will come
            // therefore we can't split into batches
            log.info("Fetching all records from search");
            fetchRecords(this.query, start, -1);
        } else {
            rows = Integer.parseInt(this.numResults);
            log.info("Fetching " + rows + " records from search");
            // calculate number of batches needed
            double batches = Math.ceil((double) rows / (double) batchSize);
            // execute batch by batch
            for (int i = 0; i < batches; i++) {
                int batchStart = start + i * batchSize;
                int batchRows = batchSize;
                // this is the last batch
                if (i == batches - 1) {
                    batchRows = rows % batchSize;
                }
                fetchRecords(this.query, batchStart, batchRows);
            }
        }
    }

    public void fetchRecords(String query, int start, int rows) throws IOException {
        if (query == null) {
            log.error("No query to be executed..");
            return;
        }
        // build the query
//        StringBuilder urlSb = new StringBuilder();
//        urlSb.append("http://pub.orcid.org/v1.1/0000-0002-6940-7971/orcid-profile");
//        urlSb.append("http://pub.sandbox.orcid.org/v1.1/search/orcid-bio?");
//        urlSb.append("&q=");
//        urlSb.append(query);
//        if (start >= 0) {
//            urlSb.append("&start=");
//            urlSb.append(start);
//        }
//        if (rows >= 0) {
//            urlSb.append("&rows=");
//            urlSb.append(rows);
//        }

        // read orcid ids to fetch from file 'orcid-ids'
        File idFile = new File("orcid-ids");
        BufferedReader reader = new BufferedReader(new FileReader(idFile));
        String idLine = reader.readLine().trim();
        String[] ids = idLine.split(",");

        try {
            for (String id : ids) {
                String url = "http://pub.orcid.org/v1.1/" + id + "/orcid-profile";
                log.info("Invoking ORCiD API with query URL : " + url);
                sanitizeXML(WebAide.getURLContents(url));
                log.info("Successfully invoked ORCiD API");
            }
        } catch (MalformedURLException e) {
            throw new IOException("Query URL incorrectly formatted", e);
        }
    }

    /**
     * Sanitizes XML in preparation for writing to output stream
     * <ol>
     * <li>Removes xml namespace attributes</li>
     * <li>Removes XML wrapper tag</li>
     * <li>Splits each record on a new line</li>
     * <li>Writes to outputstream writer</li>
     * </ol>
     *
     * @param strInput The XML to Sanitize.
     * @throws IOException Unable to write XML to record
     */
    private void sanitizeXML(String strInput) throws IOException {
        //used to remove header from xml
        String headerRegEx = "<\\?xml.*?</message-version>";
        //used to remove footer from xml
        String footerRegEx = "</orcid-message>";
        log.debug("Sanitizing Output");
        log.debug("XML File Length - Pre Sanitize: " + strInput.length());

        String newS = strInput.replaceAll(" xmlns=\".*?\"", "");
//        newS = newS.replaceAll("</orcid-search-result>.*?<orcid-search-result", "</orcid-search-result>\n<orcid-search-result");
        newS = newS.replaceAll(headerRegEx, "");
        newS = newS.replaceAll(footerRegEx, "");
        log.debug("XML File Length - Post Sanitze: " + newS.length());
        log.debug("Sanitized Result : \n" + newS);
        log.debug("Sanitization Complete");
        log.trace("Writing to output");

        // create an OutputStreamWriter
        OutputStreamWriter osWriter = new OutputStreamWriter(baseXMLROS.clone().setRso(this));
        osWriter.write(newS);
        //file close statements.  Warning, not closing the file will leave incomplete xml files and break the translate method
        osWriter.write("\n");
        osWriter.flush();
        log.trace("Writing complete");
    }

    public void writeRecord(String id, String data) throws IOException {
        log.trace("Adding Record " + id);
        this.rh.addRecord(id, data, getClass());
    }

    /**
     * Get the ArgParser for this task
     *
     * @param appName the application name
     * @return the ArgParser
     */
    protected static ArgParser getParser(String appName) {
        ArgParser parser = new ArgParser(appName);
        parser.addArgument(new ArgDef().setShortOption('o').setLongOpt("output").setDescription("RecordHandler config file path").withParameter(true, "CONFIG_FILE"));
        parser.addArgument(new ArgDef().setShortOption('O').setLongOpt("outputOverride").withParameterValueMap("RH_PARAM", "VALUE").setDescription("override the RH_PARAM of output record handler using VALUE").setRequired(false));
        parser.addArgument(new ArgDef().setShortOption('q').setLongOpt("query").setDescription("query to be executed on ORCiD").withParameter(true, "QUERY").setDefaultValue("newman"));
        parser.addArgument(new ArgDef().setShortOption('n').setLongOpt("numResults").setDescription("maximum results to return").withParameter(true, "NUMBER").setDefaultValue("100"));
        parser.addArgument(new ArgDef().setShortOption('s').setLongOpt("startResult").setDescription("starting index for results").withParameter(true, "NUMBER").setDefaultValue("0"));
        parser.addArgument(new ArgDef().setShortOption('b').setLongOpt("batchSize").setDescription("number of records to fetch per batch").withParameter(true, "NUMBER").setDefaultValue("1000"));
        return parser;
    }

    /**
     * Main method
     *
     * @param args commandline arguments
     */
    public static void main(String... args) {
        Exception error = null;
        try {
            InitLog.initLogger(args, getParser("SEADORCiDHTTPFetch"));
            log.info("SEADORCiDHTTPFetch: Start");
            new SEADORCiDHTTPFetch(args).execute();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            log.debug("Stacktrace:", e);
            System.out.println(getParser("SEADORCiDHTTPFetch").getUsage());
            error = e;
        } catch (UsageException e) {
            log.info("Printing Usage:");
            System.out.println(getParser("SEADORCiDHTTPFetch").getUsage());
            error = e;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug("Stacktrace:", e);
            error = e;
        } finally {
            log.info("SEADORCiDHTTPFetch: End");
            if (error != null) {
                System.exit(1);
            }
        }
    }
}
