package com.biit.webforms.logger;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Logger)
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


import com.biit.logger.BiitLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines basic log behavior with log4j.properties. The info, warning, debug
 * and severe methods are reimplemented to ensure the JVM to load this class and
 * not optimize with BiitLogger directly.
 */
public class WebformsLogger extends BiitLogger {

    private static Logger logger = LoggerFactory.getLogger(WebformsLogger.class);

    /**
     * Events that have business meaning (i.e. creating category, deleting form,
     * ...). To follow user actions.
     */
    public static void info(String className, String message) {
        info(logger, className, message);
    }

    public static void info(String className, String messageTemplate, Object... arguments) {
        info(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not
     * allowed for this user, ...
     *
     * @param message
     */
    public static void warning(String className, String message) {
        warning(logger, className, message);
    }

    public static void warning(String className, String messageTemplate, Object... arguments) {
        warning(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application
     * access to a method, opening database connection, etc.
     */
    public static void debug(String className, String message) {
        debug(logger, className, message);
    }

    public static void debug(String className, String messageTemplate, Object... arguments) {
        debug(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * To log any not expected error that can cause application malfuncionality.
     *
     * @param message
     */
    public static void severe(String className, String message) {
        severe(logger, className, message);
    }

    public static void severe(String className, String messageTemplate, Object... arguments) {
        severe(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * To log java exceptions and log also the stack trace. If enabled, also can
     * send an email to the administrator to alert of the error.
     *
     * @param className
     * @param throwable
     */
    public static void errorMessage(String className, Throwable throwable) {
        String error = getStackTrace(throwable);
        errorMessageNotification(logger, className, error);
    }

    /**
     * Log a error message. If enabled, also can send an email to the
     * administrator to alert of the error.
     *
     * @param className
     * @param message
     */
    public static void errorMessage(String className, String message) {
        errorMessageNotification(logger, className, message);
    }
}
