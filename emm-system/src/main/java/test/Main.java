//package test;
//
//import com.lhc.emm.system.common.util.dbUtil.Layout.ERDiagram;
//
//import java.util.Map;
//
//
//public class Main {
//    public static void main(String[] args) {
//        ERDiagram erDiagram = new ERDiagram();
//
//        // 填充表格和关系
//
//
//        // ... 其他关系
//
//        // 应用Dagre布局
//        erDiagram.treeLayout();
//
//        // 打印表格坐标
//        Map<String, double[]> coordinates = erDiagram.getTableCoordinates();
//        for (String tableName : coordinates.keySet()) {
//            double[] coords = coordinates.get(tableName);
//            System.out.println("表格: " + tableName + ", X: " + coords[0] + ", Y: " + coords[1]);
//        }
//    }
//}
