package com.xetlab.jxlexcel;

public class JxlExcelException extends RuntimeException {

	public JxlExcelException() {

	}

	public JxlExcelException(String message) {
		super(message);
	}

	public JxlExcelException(Throwable cause) {
		super(cause);
	}

	public JxlExcelException(String message, Throwable cause) {
		super(message, cause);
	}

	public JxlExcelException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
