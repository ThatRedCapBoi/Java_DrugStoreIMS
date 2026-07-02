package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Minimal Java2D vertical bar chart for the Generate Report graph (M02). Feed it
 * a label -&gt; value map via {@link #setData}; no external charting library.
 *
 * @author Itadori
 */
// Note: hand-drawn bars; swap for JFreeChart only if richer charts are needed
public class BarChartPanel extends JPanel {

    private Map<String, Integer> data = new LinkedHashMap<>();
    private static final Color BAR = new Color(18, 143, 242);
    private static final int PAD = 40; // margin for axes/labels

    public BarChartPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(500, 260));
    }

    public void setData(Map<String, Integer> data) {
        this.data = (data == null) ? new LinkedHashMap<String, Integer>() : data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        FontMetrics fm = g2.getFontMetrics();

        if (data.isEmpty()) {
            String msg = "No data";
            g2.setColor(Color.GRAY);
            g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
            return;
        }

        int max = 1;
        for (int v : data.values()) {
            max = Math.max(max, v);
        }

        int plotW = w - 2 * PAD;
        int plotH = h - 2 * PAD;
        int baseY = h - PAD;

        // axes
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(PAD, PAD, PAD, baseY);
        g2.drawLine(PAD, baseY, w - PAD, baseY);

        int n = data.size();
        int slot = plotW / n;
        int barW = Math.max(8, slot / 2);
        int i = 0;
        for (Map.Entry<String, Integer> e : data.entrySet()) {
            int barH = (int) Math.round((double) e.getValue() / max * plotH);
            int x = PAD + i * slot + (slot - barW) / 2;
            int y = baseY - barH;

            g2.setColor(BAR);
            g2.fillRect(x, y, barW, barH);

            // value on top
            g2.setColor(Color.DARK_GRAY);
            String val = String.valueOf(e.getValue());
            g2.drawString(val, x + (barW - fm.stringWidth(val)) / 2, y - 2);

            // category label under axis (truncated to slot width)
            String label = fit(e.getKey(), slot, fm);
            g2.drawString(label, x + (barW - fm.stringWidth(label)) / 2, baseY + fm.getAscent() + 2);
            i++;
        }
    }

    private String fit(String s, int maxW, FontMetrics fm) {
        if (fm.stringWidth(s) <= maxW) {
            return s;
        }
        String ell = "…";
        while (s.length() > 1 && fm.stringWidth(s + ell) > maxW) {
            s = s.substring(0, s.length() - 1);
        }
        return s + ell;
    }
}
