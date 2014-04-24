package com.spatialtranscriptomics.model;

import java.util.Date;

/**
 * This interface defines the Task model. Should reflect the corresponding model in ST API.
 */
public interface ITask {

	public String getId();

	public void setId(String id);

	public String getName();

	public void setName(String name);

	public String getStatus();

	public void setStatus(String status);

	public Date getStart();

	public void setStart(Date start);

	public Date getEnd();

	public void setEnd(Date end);

	public String[] getSelection_ids();

	public void setSelection_ids(String[] selection_ids);

	public String getAccount_id();

	public void setAccount_id(String account_id);

	public String getParameters();

	public void setParameters(String parameters);
}