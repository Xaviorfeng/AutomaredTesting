import com.ibm.wala.classLoader.ShrikeBTMethod;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.cha.CHACallGraph;
import com.ibm.wala.ipa.callgraph.impl.AllApplicationEntrypoints;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.config.AnalysisScopeReader;

import java.io.File;
import java.util.*;
public class Selector {
    AnalysisScope scope;
    public Boolean CM;
    CHACallGraph cg;

    public void FindDependency(HashMap<Node, HashSet<Node>> graph) {
        for (CGNode node : cg) {
            String classInnerName = node.getMethod().getDeclaringClass().getName().toString();
            String signature = node.getMethod().getSignature();
            if (node.getMethod() instanceof ShrikeBTMethod) {
                ShrikeBTMethod method = (ShrikeBTMethod) node.getMethod();
                if ("Application".equals(method.getDeclaringClass().getClassLoader().toString())) {
                    Node left = new Node(node,classInnerName,signature,CM);
                    if (!graph.containsKey(left)) graph.put(left, new HashSet<Node>());
                    Iterator<CGNode> cgNodeIterator = cg.getPredNodes(node);
                    while (cgNodeIterator.hasNext()) {
                        CGNode dest = cgNodeIterator.next();
                        String classInnerName1 = dest.getMethod().getDeclaringClass().getName().toString();
                        String signature1 = dest.getMethod().getSignature();
                        if (dest.getMethod() instanceof ShrikeBTMethod) {
                            if ("Application".equals(dest.getMethod().getDeclaringClass().getClassLoader().toString())) {
                                Node right = new Node(dest,classInnerName1,signature1,CM);
                                graph.get(left).add(right);
                            }
                        }
                    }
                }
            }
        }
    }
    public void Selector(ChangeInfo change, HashMap<Node, HashSet<Node>> graph, HashMap<Node, HashSet<Node>> testGraph,HashSet<String> result) {
        Queue<Node> queue = new LinkedList<Node>();

        for (Node key : graph.keySet()) {
            if(CM){
                String str1 = key.getClassInnerName();
                if(change.classChange.contains(str1)) queue.add(key);
            }
            else{
                String str2 = key.getSignature();
                if(change.methodChange.contains(str2)) queue.add(key);
            }
        }
        HashSet<Node> vis = new HashSet<Node>();
        while (!queue.isEmpty()) {
            Node head = queue.poll();
            if(vis.contains(head)) continue;
            vis.add(head);
            if (graph.containsKey(head)) {
                queue.addAll(graph.get(head));
                if(CM){
                    for (Node node : graph.get(head)) {
                        if (testGraph.containsKey(node)){
                            for(Node node1 : testGraph.keySet()){
                                if(node.getClassInnerName().equals(node1.getClassInnerName()) && node1.IsTest()){
                                    result.add(node1.WholeInfo());
                                }
                            }
                        }
                    }
                }
                else{
                    for (Node node : graph.get(head)) {
                        if (testGraph.containsKey(node) && node.IsTest())
                            result.add(node.WholeInfo());
                    }
                }
            }
        }
    }
    public void init(boolean cm){
        try{
            scope = AnalysisScopeReader.readJavaScope("scope.txt",new File("exclusion.txt"),ClassLoader.getSystemClassLoader());
            this.CM = cm;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void addScope(String path){
        try{
            scope.addClassFileToScope(ClassLoaderReference.Application,new File(path));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void makeCallGraph(){
        try{
            ClassHierarchy cha = ClassHierarchyFactory.makeWithRoot(scope);
            Iterable<Entrypoint> eps = new AllApplicationEntrypoints(scope,cha);
            cg = new CHACallGraph(cha);
            cg.init(eps);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
