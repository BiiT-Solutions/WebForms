package com.biit.webforms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exceptions.ExecutableCanNotBeExecuted;
import com.biit.webforms.utils.exceptions.PathToExecutableNotFound;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotForm;

public class GraphvizApp {

	public static String GRAPHVIZ_PROPERTIES = "graphviz.properties";
	public static String GRAPHVIZ_BIN_PROPERTY = "gvbindir";
	/* Official variable name but it's not usually set */
	public static String GRAPHVIZ_SYSVAR = "GVBINDIR";
	public static String applicationPath = null;

	public enum ImgType {
		GIF("gif"), DOT("dot"), FIG("fig"), PDF("pdf"), PS("ps"), SVG("svg"), PNG("png"), ;

		private String type;

		private ImgType(String imgType) {
			this.type = imgType;
		}

		public String getType() {
			return type;
		}
	}

	/**
	 * Finds path to application. In order we give precedente to the path on the
	 * config file. If not found, (If there was no file we don't say anything
	 * 
	 * @throws PathToExecutableNotFound
	 * @throws ExecutableCanNotBeExecuted
	 */
	private static synchronized void findApplication() throws ExecutableCanNotBeExecuted, PathToExecutableNotFound {
		if (applicationPath == null) {
			try {
				applicationPath = OsUtils.findExecutablePropertiesFile(GRAPHVIZ_PROPERTIES, GRAPHVIZ_BIN_PROPERTY);
				if (applicationPath != null) {
					return;
				}
			} catch (FileNotFoundException e) {
				WebformsLogger.debug(GraphvizApp.class.getName(), "There was no properties file for graphviz");
			} catch (IOException e) {
				WebformsLogger.errorMessage(GraphvizApp.class.getName(), e);
			}

			applicationPath = OsUtils.findExecutableEnvironmentVariable(GRAPHVIZ_BIN_PROPERTY);
		}
	}

	/**
	 * This function gets dot code in string form and generates a image of
	 * {@code imgType} using temporal files. When the process is finished all
	 * temporal files are deleted and the result image is returned as a byte
	 * array.
	 * 
	 * @param dotCode
	 *            dot code in string form
	 * @param imgType
	 *            image file format
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static byte[] generateImageFromDotCode(String dotCode, ImgType imgType) throws IOException,
			InterruptedException {
		byte[] img_stream = null;

		try {
			findApplication();
			// Generates a temp file with the dot Code content
			File dotTemp = OsUtils.writeInTempFile("dotCode_", ".dot.tmp", dotCode);
			// Generates a temp file for the image
			File imgTemp = File.createTempFile("dotImage_", "." + imgType.getType());

			// Execution of Graphviz
			String[] args = { applicationPath, "-T" + imgType.getType(), dotTemp.getAbsolutePath(), "-o",
					imgTemp.getAbsolutePath() };
			OsUtils.execSynchronic(args);

			// Pass data to byte[]

			try (FileInputStream in = new FileInputStream(imgTemp.getAbsolutePath())) {
				img_stream = new byte[in.available()];
				in.read(img_stream);
			}

			// Delete both files.
//			if (dotTemp.delete() == false) {
//				WebformsLogger.warning(GraphvizApp.class.getName(), dotTemp.getAbsolutePath()
//						+ " could not be deleted.");
//			}
//			if (imgTemp.delete() == false) {
//				WebformsLogger.warning(GraphvizApp.class.getName(), imgTemp.getAbsolutePath()
//						+ " could not be deleted.");
//			}
		} catch (ExecutableCanNotBeExecuted e) {
			WebformsLogger.severe(GraphvizApp.class.getName(), "Executable can't be executed.");
		} catch (PathToExecutableNotFound e) {
			WebformsLogger.severe(GraphvizApp.class.getName(), "Specified path doesn't exist");
		}
		return img_stream;
	}

	/**
	 * Generates the graph image of a form with the type {@code imgType}
	 * 
	 * @param form
	 *            source form
	 * @param imgType
	 *            image file format
	 * @return Byte data of image
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static byte[] generateImage(Form form, ImgType imgType) throws IOException, InterruptedException {
		// Generate DotCode
		ExporterDotForm exporter = new ExporterDotForm();
		String dotCode = exporter.export(form);
		
		// Call Graphviz to make a render from dot code.
		return generateImageFromDotCode(dotCode, imgType);
	}

}
