import com.ibm.wala.classLoader.ShrikeBTMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import java.lang.annotation.Annotation;

public class Node {
    CGNode cgNode;
    String ClassInnerName;
    String Signature;
    boolean CM;
    public Node(CGNode cgNode,String classInnerName,String signature,boolean cm){
        this.ClassInnerName = classInnerName;
        this.Signature = signature;
        this.CM = cm;
        this.cgNode = cgNode;
    }
    public String getClassInnerName(){
        return this.ClassInnerName;
    }
    public String getSignature(){
        return this.Signature;
    }

    @Override
    public String toString(){
        if(CM) return "\""+ClassInnerName+"\"";
        else return "\""+Signature+"\"";
    }

    @Override
    public boolean equals(Object obj){
        return WholeInfo().equals(((Node) obj).WholeInfo());
    }

    public String WholeInfo(){
        return ClassInnerName+" "+Signature;
    }
    @Override
    public int hashCode(){
        return WholeInfo().hashCode();
    }
    public boolean IsTest(){
        return cgNode.getMethod().getAnnotations().toString().contains("Test");
    }
}
