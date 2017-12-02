package com.ifactory.press.db.solr;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class SolrTest {

    static CoreContainer coreContainer;
    protected EmbeddedSolrServer solr;   // rivey
    protected static String indexName = "collection1";
    

    @BeforeClass
    public static void startup() throws Exception {    
        deleteIndexDirectories();
        // start an embedded solr instance
        coreContainer = new CoreContainer("solr");
        coreContainer.load();
    }

    @AfterClass
    public static void shutdown() throws Exception {
        SolrCore core = getDefaultCore();
        if (core != null) {
            core.close();
        }
        coreContainer.shutdown();
        deleteIndexDirectories();
        coreContainer = null;
    }

    @Before
    public void init() throws Exception {
        solr = new EmbeddedSolrServer(coreContainer, indexName);
        clearIndex();
    }

    protected void clearIndex() throws SolrServerException, IOException {
        solr.deleteByQuery("*:*");
        solr.commit(true, true, false);
    }

    protected static SolrCore getDefaultCore() {
        return coreContainer.getCore(indexName);
    }

    @After
    public void cleanup() throws Exception {
      clearIndex();
    }

    protected static void deleteIndexDirectories() throws IOException {
        FileUtils.cleanDirectory(new File(String.format("solr/%s/data/", indexName)));
        FileUtils.cleanDirectory(new File(String.format("solr/%s/suggestIndex/", indexName)));
    }

}
