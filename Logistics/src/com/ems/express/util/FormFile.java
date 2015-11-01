package com.ems.express.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormFile {

	
	private byte[] data;
	private InputStream inStream;
	private File file;
	private String fileName;
	private String parameterName;
	private String contentType ="application/octet-stream";
	
	
	public FormFile(String fileName, byte[] data, String parameterName, String contentType) {
        this.data = data;
        this.fileName = fileName;
        this.parameterName = parameterName;
        if(contentType!=null) this.contentType = contentType;
    }
	
	public FormFile(String fileName, File file, String parameterName, String contentType) {
        this.fileName = fileName;
        this.parameterName = parameterName;
        this.file = file;
        try {
            this.inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(contentType!=null) this.contentType = contentType;
    }
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public InputStream getInStream() {
		return inStream;
	}
	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
}
