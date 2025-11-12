package com.biit.webforms.utils;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.FilteredForm;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.exporters.dotgraph.ExporterDotFilteredForm;
import com.biit.webforms.exporters.dotgraph.ExporterDotForm;
import com.biit.webforms.exporters.dotgraph.impact.ExporterDotFormAddedElements;
import com.biit.webforms.exporters.dotgraph.impact.ExporterDotFormRemovedElements;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exceptions.ExecutableCanNotBeExecuted;
import com.biit.webforms.utils.exceptions.PathToExecutableNotFound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Helper class to generate the graphviz
 */
public class GraphvizApp {

    /* Official variable name but it's not usually set */
    public static final String GRAPHVIZ_SYSVAR = "GVBINDIR";
    public static String applicationPath = null;

    public enum ImgType {
        GIF("gif"), DOT("dot"), FIG("fig"), PDF("pdf"), PS("ps"), SVG("svg"), PNG("png");

        private String type;

        ImgType(String imgType) {
            this.type = imgType;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * Finds path to application. In order, we give precedence to the path on the
     * config file. If not found, (If there was no file we don't say anything
     *
     * @throws PathToExecutableNotFound
     * @throws ExecutableCanNotBeExecuted
     */
    private static synchronized void findApplication() throws ExecutableCanNotBeExecuted, PathToExecutableNotFound {
        if (applicationPath == null) {

            applicationPath = WebformsConfigurationReader.getInstance().getGraphvizBinPath();

            if (applicationPath != null) {
                return;
            }

            applicationPath = OsUtils.findExecutableEnvironmentVariable(GRAPHVIZ_SYSVAR);
        }
    }

    /**
     * This function gets dot code in string form and generates a image of
     * {@code imgType} using temporal files. When the process is finished all
     * temporal files are deleted and the result image is returned as a byte
     * array.
     *
     * @param dotCode dot code in string form
     * @param imgType image file format
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private static byte[] generateImageFromDotCode(String dotCode, ImgType imgType) throws IOException, InterruptedException {
        byte[] img_stream = null;

        try {
            findApplication();
            // Generates a temp file with the dot Code content
            File dotTemp = OsUtils.writeInTempFile("dotCode_", ".dot.tmp", dotCode);
            // Generates a temp file for the image
            File imgTemp = File.createTempFile("dotImage_", "." + imgType.getType());

            // Execution of Graphviz
            String[] args = {applicationPath, "-T" + imgType.getType(), dotTemp.getAbsolutePath(), "-o", imgTemp.getAbsolutePath()};
            OsUtils.execSynchronic(args);

            // Pass data to byte[]
            try {
                img_stream = Files.readAllBytes(imgTemp.toPath());
            } catch (IOException e) {
                WebformsLogger.warning(GraphvizApp.class.getName(), "'" + dotTemp.getAbsolutePath() + "' not read correctly.");
            }

            // Delete both files.
            if (!dotTemp.delete()) {
                WebformsLogger.warning(GraphvizApp.class.getName(), "'" + dotTemp.getAbsolutePath() + "' could not be deleted.");
            }
            if (!imgTemp.delete()) {
                WebformsLogger.warning(GraphvizApp.class.getName(), "'" + imgTemp.getAbsolutePath() + "' could not be deleted.");
            }
        } catch (ExecutableCanNotBeExecuted e) {
            WebformsLogger.severe(GraphvizApp.class.getName(), "Executable can't be executed.");
        } catch (PathToExecutableNotFound e) {
            WebformsLogger.severe(GraphvizApp.class.getName(), "Specified path doesn't exist");
        }
        return img_stream;
    }

    /**
     * Generates the graph image of a form with the type {@code imgType} if
     * {@code filter} is null no filter is used and all the form is rendered.
     *
     * @param form    source form
     * @param filter  data filter
     * @param imgType image file format
     * @return Byte data of image
     * @throws IOException
     * @throws InterruptedException
     */
    public static byte[] generateImage(Form form, TreeObject filter, ImgType imgType) throws IOException, InterruptedException {
        // Generate DotCode
        String dotCode;
        if (filter == null) {
            ExporterDotForm exporter = new ExporterDotForm();
            dotCode = exporter.export(form);
        } else {
            ExporterDotFilteredForm exporter = new ExporterDotFilteredForm();
            dotCode = exporter.export(new FilteredForm(form, filter));
        }

        // Call Graphviz to make a render from dot code.
        return generateImageFromDotCode(dotCode, imgType);
    }

    /**
     * Generates the dot graph image for the impact analysis. This function only
     * generates the version that shows the elements added to the form between
     * versions.
     *
     * @param formOldVersion
     * @param formNewVersion
     * @param imgType
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static byte[] generateImageImpactAnalysisRemovedElements(Form formOldVersion, Form formNewVersion, ImgType imgType)
            throws IOException, InterruptedException {
        // Generate DotCode
        String dotCode = null;
        ExporterDotFormRemovedElements exporter = new ExporterDotFormRemovedElements(formNewVersion);
        dotCode = exporter.export(formOldVersion);

        // Call Graphviz to make a render from dot code.
        return generateImageFromDotCode(dotCode, imgType);
    }

    /**
     * Generate the dot graph image for the impact analysis. This function only
     * generates the version that shows the elements removed to the form between
     * versions.
     *
     * @param formOldVersion
     * @param formNewVersion
     * @param imgType
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static byte[] generateImageImpactAnalysisAddedElements(Form formOldVersion, Form formNewVersion, ImgType imgType)
            throws IOException, InterruptedException {
        // Generate DotCode
        String dotCode = null;
        ExporterDotFormAddedElements exporter = new ExporterDotFormAddedElements(formOldVersion);
        dotCode = exporter.export(formNewVersion);

        // Call Graphviz to make a render from dot code.
        return generateImageFromDotCode(dotCode, imgType);
    }
}
