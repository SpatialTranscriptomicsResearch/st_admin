package com.st.form;

import javax.validation.Valid;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.st.model.Dataset;

/**
 * This class implements the model for the "edit dataset form" (used to edit
 * datasets). Different from the DatasetAddForm, this form does not *require* a
 * feature file (from user or from experiment output). Does validation using
 * Hibernate validator constraints.
 *
 */
public class DatasetEditForm {

    // Dataset being created. 
    @Valid
    Dataset dataset;

    // User uploaded features file. 
    CommonsMultipartFile featureFile;   // not required when editing a dataset

    /**
     * Constructor.
     */
    public DatasetEditForm() {
    }

    /**
     * Constructor.
     *
     * @param dataset dataset.
     */
    public DatasetEditForm(Dataset dataset) {
        this.dataset = dataset;
    }

    public CommonsMultipartFile getFeatureFile() {
        return featureFile;
    }

    public void setFeatureFile(CommonsMultipartFile featureFile) {
        this.featureFile = featureFile;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

}
