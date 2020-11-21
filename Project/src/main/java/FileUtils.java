import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
public class FileUtils {
    public static void folderFind(String path,ArrayList<String> src,ArrayList<String> test){
        //System.out.println(path);
        File file = new File(path);
        System.out.println(file.getName());
        if(file.exists()){
            if(file.listFiles()!=null){
                LinkedList<File> list = new LinkedList<File>(Arrays.asList(file.listFiles()));
                File testFile = null;
                File srcFile = null;
                for(File f:list){
                    //System.out.println(f.getName());
                    if(f.getName().equals("test-classes")){
                        testFile = f;
                    }
                    if(f.getName().equals("classes")){
                        srcFile = f;
                    }
                }
                list = new LinkedList<File>(Arrays.asList(testFile.listFiles()));
                addFilePath(list,test);
                list = new LinkedList<File>(Arrays.asList(srcFile.listFiles()));
                addFilePath(list,src);
            }
        }
        else System.out.println("找不到文件!");
    }
    public static void addFilePath(LinkedList<File> list,ArrayList<String> arrayList){
        while(!list.isEmpty()){
            File[] files = list.removeFirst().listFiles();
            if(files != null){
                for(File f : files){
                    if(f.isDirectory()) readDir(f,arrayList);
                    else arrayList.add(f.getPath());
                }
            }
        }
    }
    public static void readDir(File Dir,ArrayList<String> arrayList){
        File[] files = Dir.listFiles();
        if(files != null){
            for(File f : files){
                if(f.isDirectory()) readDir(f,arrayList);
                else arrayList.add(f.getPath());
            }
        }
    }
    public static void writeFile(String fileName,String data){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(data);
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
