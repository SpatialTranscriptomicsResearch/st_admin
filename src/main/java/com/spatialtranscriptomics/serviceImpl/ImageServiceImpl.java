/*
 *Copyright © 2012 Spatial Transcriptomics AB
 *Read LICENSE for more information about licensing terms
 *Contact: Jose Fernandez Navarro <jose.fernandez.navarro@scilifelab.se>
 * 
 */
package com.spatialtranscriptomics.serviceImpl;

import com.spatialtranscriptomics.model.ImageMetadata;
import com.spatialtranscriptomics.service.ImageService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * This class implements the store/retrieve logic to the ST API for the data
 * model class ImageMetadata (lightweight detail listing) and actual image payload
 * (either as through S3Resource with JPEG content -- recommended or via
 * BufferedImage -- discouraged). The connection to the ST API is handled in a
 * RestTemplate object, which is configured in mvc-dispather-servlet.xml
 */
@Service
public class ImageServiceImpl implements ImageService {

    // Note: General service URI logging is performed in CustomOAuth2RestTemplate.
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ImageServiceImpl.class);

    @Autowired
    RestTemplate secureRestTemplate;

    @Autowired
    Properties appConfig;

    @Override
    public List<ImageMetadata> list() {
        String url = appConfig.getProperty("url.image");
        ImageMetadata[] imgMetadataArray = secureRestTemplate.getForObject(url,
                ImageMetadata[].class);
        return Arrays.asList(imgMetadataArray);
    }

    @Override
    public BufferedImage find(String id) {
        String url = appConfig.getProperty("url.image") + id;
        return secureRestTemplate.getForObject(url, BufferedImage.class);
    }

    @Override
    public void delete(String id) {
        String url = appConfig.getProperty("url.image");
        secureRestTemplate.delete(url + id);
    }

    @Override
    public void addFromFile(CommonsMultipartFile imageFile) throws IOException {
        String url = appConfig.getProperty("url.image") + imageFile.getOriginalFilename();
        BufferedImage bi = ImageIO.read(imageFile.getInputStream());
        if (bi == null) {
            throw new IOException("Empty or incorrect image file.");
        }
        
        secureRestTemplate.put(url, bi);
    }

}
