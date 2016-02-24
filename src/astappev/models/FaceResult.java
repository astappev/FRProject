package astappev.models;

import java.util.Arrays;
import java.util.List;

import org.opencv.core.Rect;

public final class FaceResult {
	private final String path;
	private final Rect[] rects;

	public FaceResult(String path, Rect[] rects) {
		this.path = path;
		this.rects = rects;
	}

	public String getPath() {
		return path;
	}

	public List<Rect> getRects() {
		return Arrays.asList(rects);
	}
}
