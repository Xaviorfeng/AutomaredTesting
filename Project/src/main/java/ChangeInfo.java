import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
public class ChangeInfo {
    HashSet<String> classChange = new HashSet<String>();
    HashSet<String> methodChange = new HashSet<String>();

    public ChangeInfo(String path){
        ArrayList<String> changes = new ArrayList<>();
        try {
            BufferedReader line = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String data = null;
            while ((data = line.readLine()) != null) {
                changes.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < changes.size(); i++) {
            String[] c = changes.get(i).split(" ");
            classChange.add(c[0]);
            methodChange.add(c[1]);
        }
//        System.out.println(path);
//        ArrayList<String> changes = new ArrayList<String>();
//        try {
//            BufferedReader line = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
//            while (line.readLine()!= null) {
//                System.out.println(line.readLine());
//                String content = line.readLine();
//                System.out.println(content);
//                changes.add(content);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < changes.size(); i++) {
//            String[] c = changes.get(i).split(" ");
//            classChange.add(c[0]);
//            methodChange.add(c[1]);
//        }
    }
}
