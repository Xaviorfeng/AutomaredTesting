import java.util.*;
public class AutoTest {
    public static void main(String[] args){
        HashSet<String> result = start(args);
        assert result!=null;
        StringBuilder stringBuilder = new StringBuilder();
        for(String s:result){
            stringBuilder.append(s+"\n");
        }
        if(args[0].equals("-c")) FileUtils.writeFile("selection-class.txt",stringBuilder.toString());
        else FileUtils.writeFile("selection-method.txt",stringBuilder.toString());
    }
    public static HashSet<String> start(String[] args){
        Selector selector = new Selector();
        ArrayList<String> src = new ArrayList<String>();
        ArrayList<String> test = new ArrayList<String>();

        if(args[0]=="-c") selector.init(true);
        if(args[0]=="-m") selector.init(false);

        FileUtils.folderFind(args[1],src,test);
        for(String path:test){
            selector.addScope(path);
        }
        HashMap<Node,HashSet<Node>> testGraph = new HashMap<Node,HashSet<Node>>();
        selector.makeCallGraph();
        selector.FindDependency(testGraph);
        for(String path: src){
            selector.addScope(path);
        }
        HashMap<Node,HashSet<Node>> graph = new HashMap<Node,HashSet<Node>>();
        selector.makeCallGraph();
        selector.FindDependency(graph);
        ChangeInfo changeInfo = new ChangeInfo(args[2]);
        HashSet<String> result = new HashSet<String>();
        selector.Selector(changeInfo,graph,testGraph,result);
        System.out.println("selector");
        DotGenerator.GEN(graph,args[0].equals("-m")?1:0,"graph");
        return result;
    }
}
