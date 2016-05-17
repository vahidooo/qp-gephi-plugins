package net.quranpalace.graph.gephi.plugin.layout.wos;

import javafx.util.Pair;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.gephi.layout.spi.LayoutProperty;
import org.gephi.ui.propertyeditor.NodeColumnNumbersEditor;

import java.util.*;

public class WordsOfSectionLayout implements Layout {

    //Architecture
    private final LayoutBuilder builder;
    private GraphModel graphModel;
    //Flags
    private boolean executing = false;
    //Properties
    private Column column;
    private int areaSize;
    private float speed;

    private int lineMargin;
    private int wordMargin;

    private String sectionStr;


    public WordsOfSectionLayout(WordsOfSectionLayoutBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void resetPropertiesValues() {
        areaSize = 1000;
        speed = 100f;
        lineMargin = 5;
        wordMargin = 25;
        sectionStr = "1:11;12:12;13:50;51:83";
    }

    @Override
    public void initAlgo() {
        executing = true;
    }

    @Override
    public void goAlgo() {
        Graph graph = graphModel.getGraphVisible();
        graph.readLock();
        int nodeCount = graph.getNodeCount();
        Node[] nodes = graph.getNodes().toArray();

        Map<Pair<Integer, Integer>, List<Node>> sections = new HashMap<Pair<Integer, Integer>, List<Node>>();
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>();

        for (String s : sectionStr.split(";")) {
            Pair<Integer, Integer> pair = new Pair<Integer, Integer>(Integer.parseInt(s.split(":")[0]), Integer.parseInt(s.split(":")[1]));
            ArrayList<Node> list = new ArrayList<Node>();
            sections.put(pair, list);
            pairs.add(pair);
        }


        Arrays.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {

                int v1 = Integer.parseInt(((String) o1.getAttribute("address")).split(":")[1]);
                int v2 = Integer.parseInt(((String) o2.getAttribute("address")).split(":")[1]);

                Integer w1 = Integer.parseInt(((String) o1.getAttribute("address")).split(":")[2]);
                Integer w2 = Integer.parseInt(((String) o2.getAttribute("address")).split(":")[2]);

                if (v1 < v2) {
                    return -1;
                } else if (v1 > v2) {
                    return 1;
                } else {
                    return (w1.compareTo(w2));
                }
            }
        });

        for (Node node : nodes) {
//            System.out.println(node.getAttribute("address"));

            int verse = Integer.parseInt(((String) node.getAttribute("address")).split(":")[1]);

            for (Pair<Integer, Integer> pair : sections.keySet()) {
                if (pair.getKey() <= verse && verse <= pair.getValue()) {
                    sections.get(pair).add(node);
                }
            }
        }

//        int rows = (int) Math.round(Math.sqrt(nodeCount)) + 1;
//        int cols = (int) Math.round(Math.sqrt(nodeCount)) + 1;
        for (int i = 0; i < pairs.size(); i++) {
            for (int j = 0; j < sections.get(pairs.get(i)).size(); j++) {
                Node node = sections.get(pairs.get(i)).get(j);

                float x = -j * wordMargin;
                float y = (areaSize / 2f) - ((float) i / pairs.size()) * areaSize;

                float px = node.x();
                float py = node.y();

                node.setX(px + (x - px) * (speed / 10000f));
                node.setY(py + (y - py) * (speed / 10000f));

            }
        }

        graph.readUnlock();
    }

    @Override
    public void endAlgo() {
        executing = false;
    }

    @Override
    public boolean canAlgo() {
        return executing;
    }

    @Override
    public LayoutProperty[] getProperties() {
        List<LayoutProperty> properties = new ArrayList<LayoutProperty>();
        final String GRIDLAYOUT = "Grid Layout";

        try {
            properties.add(LayoutProperty.createProperty(
                    this, Column.class,
                    "Sorted By",
                    GRIDLAYOUT,
                    "The colums used to sort nodes",
                    "getColumn", "setColumn", NodeColumnNumbersEditor.class));
            properties.add(LayoutProperty.createProperty(
                    this, Integer.class,
                    "Area size",
                    GRIDLAYOUT,
                    "The area size",
                    "getAreaSize", "setAreaSize"));
            properties.add(LayoutProperty.createProperty(
                    this, Float.class,
                    "Speed",
                    GRIDLAYOUT,
                    "How fast are moving nodes",
                    "getSpeed", "setSpeed"));


            properties.add(LayoutProperty.createProperty(
                    this, Integer.class,
                    "Word margin",
                    GRIDLAYOUT,
                    "Margin around words",
                    "getWordMargin", "setWordMargin"));

            properties.add(LayoutProperty.createProperty(
                    this, Integer.class,
                    "Line margin",
                    GRIDLAYOUT,
                    "Distance of two lines",
                    "getLineMargin", "setLineMargin"));

            properties.add(LayoutProperty.createProperty(
                    this, String.class,
                    "Sections",
                    GRIDLAYOUT,
                    "Each section appears in a line",
                    "getSectionStr", "setSectionStr"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties.toArray(new LayoutProperty[0]);
    }

    @Override
    public LayoutBuilder getBuilder() {
        return builder;
    }

    @Override
    public void setGraphModel(GraphModel gm) {
        this.graphModel = gm;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Integer getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Integer area) {
        this.areaSize = area;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Integer getLineMargin() {
        return lineMargin;
    }

    public void setLineMargin(Integer lineMargin) {
        this.lineMargin = lineMargin;
    }

    public Integer getWordMargin() {
        return wordMargin;
    }

    public void setWordMargin(Integer wordMargin) {
        this.wordMargin = wordMargin;
    }

    public String getSectionStr() {
        return sectionStr;
    }

    public void setSectionStr(String sectionStr) {
        this.sectionStr = sectionStr.replaceAll("\\s+" , "");
    }
}
//<key attr.name="form" attr.type="string" for="node" id="form"/>
//<key attr.name="address" attr.type="string" for="node" id="address"/>