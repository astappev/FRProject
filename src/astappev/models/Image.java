package astappev.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Rect;

import astappev.extractPersons.PosEntity;

public class Image implements Serializable {
	
	private static final long serialVersionUID = -6405434019542028678L;

	private String url;
	
	private String filename;

	private String path;

	private String title;

	private String alt;

	private List<Rect> facesRect;

	private List<String> personNames;

	public Image() {

	}

	public Image(String url, String alt, String title) {
		this.url = url;
		this.alt = alt;
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public List<Rect> getFacesRect() {
		if (this.facesRect == null) {
			this.facesRect = new ArrayList<Rect>();
		}

		return facesRect;
	}

	public void setFacesRect(List<Rect> facesRect) {
		this.facesRect = facesRect;
	}

	public List<String> getPersonNames() {
		if (this.personNames == null) {
			this.personNames = new ArrayList<String>();
		}

		return personNames;
	}

	public void setPersonNames(List<String> names) {
		this.personNames = names;
	}

	public void addPersonNames(List<PosEntity> list) {
		if (this.personNames == null) {
			this.personNames = new ArrayList<String>();
		}

		for (PosEntity posEntity : list) {
			this.personNames.add(posEntity.getLable());
		}
	}

	public String getPersonsAsString() {
		StringBuilder sb = new StringBuilder();
		for (String name : this.getPersonNames()) {
			sb.append(name + ", ");
		}

		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Image from \"" + this.getUrl() + " stored as " + this.getPath());
		sb.append("\n-- Alt: " + this.getAlt());
		sb.append("\n-- Title: " + this.getTitle());
		sb.append(
				"\n-- Contains " + this.getFacesRect().size() + " faces and " + this.getPersonNames().size() + " persons");
		sb.append("\n-- Persons: " + this.getPersonsAsString());
		return sb.toString();
	}
}
