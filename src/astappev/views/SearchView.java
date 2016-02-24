package astappev.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import astappev.models.Image;
import astappev.service.SiteProcessor;

@ManagedBean
@ViewScoped
public class SearchView implements Serializable {

	private static final long serialVersionUID = -1695592790834921381L;

	private String websiteUrl;
	
	private int processedPages = 0;
	
	private int processedImages = 0;
	
	private int totalFaces = 0;
	
	private int totalNames = 0;

	private List<Image> results = null;
	
	private SiteProcessor sp = null;

	private Integer progress;

	public void search() throws Exception {
		try {
			sp = new SiteProcessor();
			sp.run(websiteUrl, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void onComplete() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Scan Completed"));
    }
    
    public void onUpdate() {
    	/*Image one = new Image();
		one.setPath("/images/large.png");
		one.setAlt("Alt");
		one.setTitle("Title");

		getResults().add(one);
    	
        processedImages++;*/
    }
     
    public void cancel() {
        progress = null;
        sp = null;
    }
    
    public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String url) {
		this.websiteUrl = url;
	}

	public List<Image> getResults() {
		if (results == null)
		{
			results = new ArrayList<Image>();
		}
		
		return results;
	}
	
	public int getProcessedPages() {
		return this.processedPages;
	}
	
	public void setProcessedPages(int value) {
		this.processedPages = value;
	}

	public void addProcessedPages(int value) {
		this.processedPages += value;
	}
	
	public int getProcessedImages() {
		return this.processedImages;
	}
	
	public void setProcessedImages(int value) {
		this.processedImages = value;
	}

	public void addProcessedImages(int value) {
		this.processedImages += value;
	}
	
	public int getTotalFaces() {
		return this.totalFaces;
	}
	
	public void setTotalFaces(int value) {
		this.totalFaces = value;
	}

	public void addTotalFaces(int value) {
		this.totalFaces += value;
	}
	
	public int getTotalNames() {
		return this.totalNames;
	}
	
	public void setTotalNames(int value) {
		this.totalNames = value;
	}

	public void addTotalNames(int value) {
		this.totalNames += value;
	}
	
	public Integer getProgress() {
		return 0;
        /*if(progress == null) {
            progress = 0;
        }
        else {
            progress = progress + (int)(Math.random() * 5);
             
            if(progress > 100)
                progress = 100;
        }
        
        return progress;*/
    }
 
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
