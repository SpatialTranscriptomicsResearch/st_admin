package com.st.serviceImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.st.model.Chip;
import com.st.service.ChipService;
import com.st.util.NDFParser;

/**
 * This class implements the store/retrieve logic to the ST API for the data
 * model class "Chip". The connection to the ST API is handled in a RestTemplate
 * object, which is configured in mvc-dispather-servlet.xml
 */
@Service
public class ChipServiceImpl implements ChipService {

    // Note: General service URI logging is performed in CustomOAuth2RestTemplate.
    @SuppressWarnings("unused")
    private static final Logger logger = Logger
            .getLogger(ChipServiceImpl.class);

    @Autowired
    RestTemplate secureRestTemplate;

    @Autowired
    Properties appConfig;

    @Override
    public Chip find(String id) {
        if (id == null || id.equals("")) { return null; }
        String url = appConfig.getProperty("url.chip");
        url += id;
        Chip chip = secureRestTemplate.getForObject(url, Chip.class);
        return chip;
    }

    @Override
    public List<Chip> list() {
        String url = appConfig.getProperty("url.chip");
        Chip[] chipArray = secureRestTemplate.getForObject(url, Chip[].class);
        List<Chip> chipList = Arrays.asList(chipArray);
        return chipList;
    }

    @Override
    public Chip create(Chip chip) {
        String url = appConfig.getProperty("url.chip");
        Chip chipResponse = secureRestTemplate.postForObject(url, chip,
                Chip.class);
        return chipResponse;
    }

    @Override
    public Chip addFromFile(CommonsMultipartFile chipFile, String name) throws IOException {
        // parse ndf file to get Values
        NDFParser parser = new NDFParser(chipFile.getInputStream());
        Chip chip = parser.readChip();
        chip.setName(name);

        String url = appConfig.getProperty("url.chip");
        Chip chipResponse = secureRestTemplate.postForObject(url, chip,
                Chip.class);
        return chipResponse;
    }

    @Override
    public void update(Chip chip) {
        String url = appConfig.getProperty("url.chip");
        String id = chip.getId();
        secureRestTemplate.put(url + id, chip);
    }

    @Override
    public void delete(String id) {
        String url = appConfig.getProperty("url.chip");
        secureRestTemplate.delete(url + id + "?cascade=true");
    }

}
