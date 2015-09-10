/*
 *Copyright © 2012 Spatial Transcriptomics AB
 *Read LICENSE for more information about licensing terms
 *Contact: Jose Fernandez Navarro <jose.fernandez.navarro@scilifelab.se>
 * 
 */

package com.spatialtranscriptomics.service;

import java.util.List;
import com.spatialtranscriptomics.model.Account;

/**
 * Interface for the account service.
 */
public interface AccountService {

    /**
     * Returns an account.
     * @param id id.
     * @return account.
     */
    public Account find(String id);

    /**
     * Returns all accounts. 
     * @return the accounts.
     */
    public List<Account> list();

    /**
     * Finds all accounts for a dataset.
     * @param datasetId the dataset.
     * @return the accounts.
     */
    public List<Account> findForDataset(String datasetId);

    /**
     * Adds an account.
     * @param acc account.
     * @return the account with ID set.
     */
    public Account add(Account acc);

    /**
     * 
     * Updates an account.
     * @param acc the account.
     */
    public void update(Account acc);

    /**
     * Cascade deletes an account
     * @param id the account.
     */
    public void delete(String id);
}
