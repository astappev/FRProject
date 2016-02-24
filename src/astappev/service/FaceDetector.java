package astappev.service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import astappev.models.FaceResult;

public class FaceDetector implements Serializable {
	
	private static final long serialVersionUID = 2936017235531169440L;

	static final Logger logger = LoggerFactory.getLogger(FaceDetector.class);
	
	private static String haarcascadeFace = "E:\\fr-project\\haarcascade_frontalface_alt.xml";

	private CascadeClassifier faceCascade;

	public FaceDetector() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public Rect[] checkImage(String url) {
		Mat image = bufferedImageToMat(loadImageFromUrl(url));
		MatOfRect faceDetections = new MatOfRect();
		getFaceCascade().detectMultiScale(image, faceDetections);

		return faceDetections.toArray();
	}

	public FaceResult processImage(String url, String outputDir) {
		Mat image = bufferedImageToMat(loadImageFromUrl(url));
		MatOfRect faceDetections = new MatOfRect();
		getFaceCascade().detectMultiScale(image, faceDetections);

		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));
		}

		String name;
		if (faceDetections.toArray().length > 0) {
			UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();
			name = randomUUIDString + ".jpg";
			Imgcodecs.imwrite(outputDir + name, image);
		} else {
			name = null;
		}

		return new FaceResult(name, faceDetections.toArray());
	}

	private static BufferedImage loadImageFromUrl(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	public static Mat bufferedImageToMat(BufferedImage bi) {
		if (bi.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			BufferedImage cvImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			cvImage.getGraphics().drawImage(bi, 0, 0, null);
			bi = cvImage;
		}
		
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		return mat;
	}

	private CascadeClassifier getFaceCascade() {
		if (this.faceCascade == null) {
			this.faceCascade = new CascadeClassifier(haarcascadeFace);
		}

		return this.faceCascade;
	}
}
