package com.ems.express.frame.widget.gallery;

import java.io.Serializable;

public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
