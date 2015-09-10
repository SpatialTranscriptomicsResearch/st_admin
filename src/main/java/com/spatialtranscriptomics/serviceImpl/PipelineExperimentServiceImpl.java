/*
 *Copyright © 2012 Spatial Transcriptomics AB
 *Read LICENSE for more information about licensing terms
 *Contact: Jose Fernandez Navarro <jose.fernandez.navarro@scilifelab.se>
 * 
 */

package com.spatialtranscriptomics.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.spatialtranscriptomics.model.PipelineExperiment;
import com.spatialtranscriptomics.service.PipelineExperimentService;

/**
 * This class implements the store/retrieve logic to the ST API for the data
 * model class "PipelineExperiment". The connection to the ST API is handled in
 * a RestTemplate object, which is configured in mvc-dispather-servlet.xml
 */
@Service
public class PipelineExperimentServiceImpl implements PipelineExperimentService {

    // Note: General service URI logging is performed in CustomOAuth2RestTemplate.
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(PipelineExperimentServiceImpl.class);

    @Autowired
    RestTemplate secureRestTemplate;

    @Autowired
    Properties appConfig;

    @Override
    public PipelineExperiment find(String id) {
        String url = appConfig.getProperty("url.pipelineexperiment") + "/" + id;
        return secureRestTemplate.getForObject(url, PipelineExperiment.class);
    }

    @Override
    public List<PipelineExperiment> list() {
        String url = appConfig.getProperty("url.pipelineexperiment");
        try {
            PipelineExperiment[] experimentsArray = secureRestTemplate.getForObject(url, PipelineExperiment[].class);
            //TODO remove this
            logger.info("Retrieving pipeline experiments array");
            return Arrays.asList(experimentsArray);
        } catch (Exception e) {
            logger.info("Something happened retrieving experiments", e);
        }
        
        return null;
    }

    @Override
    public PipelineExperiment add(PipelineExperiment experiment) {
        String url = appConfig.getProperty("url.pipelineexperiment");
        return secureRestTemplate.postForObject(url, experiment, PipelineExperiment.class);
    }

    @Override
    public void update(PipelineExperiment experiment) {
        String url = appConfig.getProperty("url.pipelineexperiment") + "/" + experiment.getId();
        secureRestTemplate.put(url, experiment);
    }

    @Override
    public void delete(String id) {
        String url = appConfig.getProperty("url.pipelineexperiment");
        secureRestTemplate.delete(url + "/" + id);
    }

    @Override
    public List<PipelineExperiment> findForAccount(String accountId) {
        String url = appConfig.getProperty("url.pipelineexperiment") + "/?account=" + accountId;
        return Arrays.asList(secureRestTemplate.getForObject(url, PipelineExperiment[].class));
    }

}
