package net.quranpalace.graph.gephi.plugin.layout.wos;

import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.gephi.layout.spi.LayoutUI;
import org.openide.util.lookup.ServiceProvider;

import javax.swing.*;

@ServiceProvider(service = LayoutBuilder.class)
public class WordsOfSectionLayoutBuilder implements LayoutBuilder {

    @Override
    public String getName() {
        return "Words of Section";
    }

    @Override
    public LayoutUI getUI() {
        return new LayoutUI() {

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public Icon getIcon() {
                return null;
            }

            @Override
            public JPanel getSimplePanel(Layout layout) {
                return null;
            }

            @Override
            public int getQualityRank() {
                return -1;
            }

            @Override
            public int getSpeedRank() {
                return -1;
            }
        };
    }

    @Override
    public Layout buildLayout() {
        return new WordsOfSectionLayout(this);
    }
}
