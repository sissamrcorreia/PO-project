package xxl.exceptions;

import java.io.Serial;

/**
 * Class for representing an exception when, in the interval functions AVERAGE and PRODUCT, the contents of the cells of
 * the received interval are not all integers.
 */
public class ContentOfIntervalNotAllInteger extends Exception {

	@Serial
	private static final long serialVersionUID = 202308312359L;

}