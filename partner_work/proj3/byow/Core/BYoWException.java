package byow.Core;

/** General exception indicating a Gitlet error.  For fatal errors, the
 *  result of .getMessage() is the error message to be printed.
 *  @author P. N. Hilfinger
 */
class BYoWException extends RuntimeException {


    /** A GitletException with no message. */
    BYoWException() {
        super();
    }

    /** A GitletException MSG as its message. */
    BYoWException(String msg) {
        super(msg);
    }

}
