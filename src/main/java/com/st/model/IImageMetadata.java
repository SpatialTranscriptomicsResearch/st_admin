package com.st.model;

import org.joda.time.DateTime;

/**
 * This interface defines the ImageMetadata model. Should reflect the
 * corresponding model in ST API.
 */
public interface IImageMetadata {

    public String getImageType();

    public void setImageType(String imageType);

    public String getFilename();

    public void setFilename(String filename);

    public DateTime getLastModified();

    public void setLastModified(DateTime d);

    public DateTime getCreated();

    public void setCreated(DateTime d);

    public long getSize();

    public void setSize(long size);

    public String getReadableSize();

}
