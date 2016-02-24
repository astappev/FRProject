package astappev.crawler;

import java.util.HashMap;
import java.util.List;

import org.opencv.core.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import astappev.extractPersons.ExtractPersons;
import astappev.extractPersons.PosEntity;
import astappev.models.FaceResult;
import astappev.models.Image;
import astappev.service.FaceDetector;

public class CrawlImages {

	static final Logger logger = LoggerFactory.getLogger(CrawlImages.class);
	
	private String outputDir;
	
	private FaceDetector fd;
	private ExtractPersons ep;

	private int processedPages = 0;
	private int processedImages = 0;
	private int totalFaces = 0;
	private int totalNames = 0;
	
	private HashMap<String, Image> collectedImages = new HashMap<String, Image>();
	private HashMap<String, Image> filteredImages = new HashMap<String, Image>();

	public CrawlImages(String outputDir) {
		fd = new FaceDetector();
		ep = new ExtractPersons();
		this.outputDir = outputDir;
	}

	public HashMap<String, Image> getCollectedImages() {
		return collectedImages;
	}

	public void setCollectedImages(HashMap<String, Image> collectedImages) {
		this.collectedImages = collectedImages;
	}

	public void addCollectedImages(String src, String url, String alt, String title) {
		if (!this.collectedImages.containsKey(src)) {
			Image image = new Image(url, alt, title);

			FaceResult faceResult = fd.processImage(src, outputDir);
			List<Rect> faces = faceResult.getRects();
			List<PosEntity> names = ep.findPersons(image.getAlt(), image.getTitle());
			if (faces.size() > 0) {
				totalFaces += faces.size();
				totalNames += names.size();
				image.setFilename(faceResult.getPath());
				image.setPath("http://localhost:8080/images/target/" + faceResult.getPath());
				image.setFacesRect(faces);
				image.addPersonNames(names);
				this.filteredImages.put(src, image);
				logger.info(image.toString());
			}
			
			this.collectedImages.put(src, image);
		}
	}

	public HashMap<String, Image> getFilteredImages() {
		return filteredImages;
	}

	public void setFilteredImages(HashMap<String, Image> filteredImages) {
		this.filteredImages = filteredImages;
	}
	
	public int getProcessedPages() {
		return this.processedPages;
	}

	public void setProcessedPages(int value) {
		this.processedPages = value;
	}

	public void incProcessedPages() {
		this.processedPages++;
	}
	
	public int getProcessedImages() {
		return this.processedImages;
	}

	public void setProcessedImages(int value) {
		this.processedImages = value;
	}

	public void incProcessedImages() {
		this.processedImages++;
	}
	
	public int getTotalFaces() {
		return this.totalFaces;
	}

	public void setTotalFaces(int value) {
		this.totalFaces = value;
	}

	public void incTotalFaces() {
		this.totalFaces++;
	}
	
	public int getTotalNames() {
		return this.totalNames;
	}

	public void setTotalNames(int value) {
		this.totalNames = value;
	}

	public void incTotalNames() {
		this.totalNames++;
	}
}
