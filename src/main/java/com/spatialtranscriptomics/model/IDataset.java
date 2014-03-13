/*
*Copyright © 2012 Spatial Transcriptomics AB
*Read LICENSE for more information about licensing terms
*Contact: Jose Fernandez Navarro <jose.fernandez.navarro@scilifelab.se>
* 
*/

package com.spatialtranscriptomics.model;

import java.util.Date;

/**
 * This interface defines the Dataset model. Should reflect the corresponding model in ST API.
 */

public interface IDataset {

	public String getId();

	public void setId(String id);

	public String getName();

	public void setName(String name);
	
	public String getImage_alignment_id();
	
	public void setImage_alignment_id(String imal);

	public String getTissue();

	public void setTissue(String tissue);

	public String getSpecies();

	public void setSpecies(String species);
	
	public int getOverall_gene_count();

	public void setOverall_gene_count(int count);
	
	public int getUnique_gene_count();

	public void setUnique_gene_count(int count);

	public int getOverall_barcode_count();

	public void setOverall_barcode_count(int count);

	public int getUnique_barcode_count();

	public void setUnique_barcode_count(int count);

	public int getOverall_hit_count();
	
	public void setOverall_hit_count(int count);
	
	public double[] getOverall_hit_quartiles();

	public void setOverall_hit_quartiles(double[] quartiles);

	public double[] getGene_pooled_hit_quartiles();

	public void setGene_pooled_hit_quartiles(double[] quartiles);

	public String[] getObo_foundry_terms();

	public void setObo_foundry_terms(String[] obo_foundry_terms);
	
	public String getComment();
	
	public void setComment(String comm);
	
	public Date getLast_modified();

	public void setLast_modified(Date lastmod);

}
