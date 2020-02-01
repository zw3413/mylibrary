package cn.cloudbed.operation;

public class MainLuncher {

    static String sourcePath="/file/mylibrary/book";
    static String targetPath="/file/mylibrary/target";
    static String tempPath="/file/mylibrary/temp";
    static String formats="pdf,txt";

    public static void main(String[] args){
        printHelp();
        if(args==null ) printHelp();

        if(args.length>0){
            switch (args[0]){
                case "processDocument":
                    processDocument(args);
                    break;
            }
        }
    }

    public static void printHelp(){
        System.out.println("打印帮助信息");
        System.out.println("processDocument [sourcePath][targetPath][tempPath][formats]");
        System.out.println("sourcePath default="+sourcePath);
        System.out.println("targetPath default="+targetPath);
        System.out.println("tempPath default="+tempPath);
        System.out.println("formats default="+formats);
    }
    public static void processDocument(String[] args){
        try{
            sourcePath=args[1];
            targetPath=args[2];
            tempPath=args[3];
            formats=args[4];
        }catch (Exception e){

        }
        try {
            DocumentProcessOperation.readDocumentAndProcess(
                    sourcePath,
                    targetPath,
                    tempPath,
                    formats
            );
        }catch (Exception e){
            System.err.println("出现异常");
            e.printStackTrace();
        }
    }
}
