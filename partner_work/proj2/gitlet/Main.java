package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Xuanhao Cui and Frederick Zhang
 */

public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
//            maybe have to use:
//            throw Utils.error("Please enter a command.", args);
//            System.exit(0);
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        Repository r = Repository.loadRepo();
        if (r == null && !args[0].equals("init")) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch(firstArg) {
            case "init":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                if (r != null) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    return;
                }
                r = new Repository();
                break;
            case "add":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                String filename = args[1];
                r.add(filename);
                break;
            case "commit":
                if (args.length == 1 || args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                    return;
                }
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                String message = args[1];
                r.commit(message);
                break;
            case "rm":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.rm(args[1]);
                break;
            case "log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.log();
                break;
            case "global-log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.globalLog();
                break;
            case "find":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.find(args[1]);
                break;
            case "status":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.status();
                break;
            case "checkout":
                if (args.length == 2) {
                    r.checkout(args[1]);
                }
                else if (args.length == 3) {
                    // checkout -- [file name]
                    if (!args[1].equals("--")) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    r.checkout(r.HEAD, args[2]);
                }
                else if (args.length == 4){
                    // checkout [commit id] -- [file name]
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    r.checkout(args[1], args[3]);
                }
                else {
                    System.out.println("Incorrect operands.");
                    return;
                }
                break;
            case "branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.branch(args[1]);
                break;
            case "rm-branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.rm_branch(args[1]);
                break;
            case "reset":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                r.reset(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                return;
        }
        r.storeRepo();
    }
}
