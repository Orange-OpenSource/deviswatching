package com.orange.clara.tool.exceptions;

/**
 * Copyright (C) 2016 Orange
 * <p>
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/Apache-2.0'.
 * <p>
 * Author: Arthur Halet
 * Date: 24/06/2016
 */
public class CrawlerGetContentException extends Exception {

    public CrawlerGetContentException(String message) {
        super(message);
    }

    public CrawlerGetContentException(String message, Throwable cause) {
        super(message, cause);
    }
}