import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class TegnUt extends JFrame {
    Oblig4 d;
    IntList theCoHull;
    int n, MAX_Y;
    ArrayList<Integer> x;
    ArrayList<Integer> y;

    TegnUt(Oblig4 d, IntList CoHull, String s) {
        theCoHull = CoHull;
        this.d = d;
        x = d.x;
        y = d.y;
        n = d.n;
        MAX_Y = d.MAX_Y;
        size = 500;
        margin = 50;
        scale = size / d.MAX_X + 0.8;
        setTitle("Oblig4, num points:" + n + " " + s);
        grafen = new Graph();
        getContentPane().add(grafen, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        // angir foretrukket storrelse paa dette lerretet.
        setPreferredSize(new Dimension(d.MAX_X + 2 * margin, d.MAX_Y + 2 * margin));
    }

    Graph grafen;
    int size, margin;
    double scale;

    class Graph extends JPanel {
        void drawPoint(int p, Graphics g) {
            int SIZE = 7;
            if (n <= 50)
                g.drawString(p + "(" + x.get(p) + "," + y.get(p) + ")", xDraw(x.get(p)) - SIZE / 2, yDraw(y.get(p)) - SIZE / 2);
            else if (n <= 200) g.drawString(p + "", xDraw(x.get(p)) - SIZE / 2, yDraw(y.get(p)) - SIZE / 2);
            g.drawOval(xDraw(x.get(p)) - SIZE / 2, yDraw(y.get(p)) - SIZE / 2, SIZE, SIZE);
            g.fillOval(xDraw(x.get(p)) - SIZE / 2, yDraw(y.get(p)) - SIZE / 2, SIZE, SIZE);
        }

        Graph() {
            setPreferredSize(new Dimension(size + 2 * margin + 10, size + 2 * margin + 10));
        }

        int xDraw(int x) {
            return (int) (x * scale) + margin;
        }

        int yDraw(int y) {
            return (int) ((MAX_Y - y) * scale + margin);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            for (int i = 0; i < n; i++) {
                drawPoint(i, g);
            }
                g.setColor(Color.red);
            // draw cohull
            int x2 = x.get(theCoHull.get(0)), y2 = y.get(theCoHull.get(0)), x1, y1;
            for (int i = 1; i < theCoHull.size(); i++) {
                y1 = y2;
                x1 = x2;
                x2 = x.get(theCoHull.get(i));
                y2 = y.get(theCoHull.get(i));
                g.drawLine(xDraw(x1), yDraw(y1), xDraw(x2), yDraw(y2));
            }

            g.drawLine(xDraw(x.get(theCoHull.get(theCoHull.size() - 1))),
                    yDraw(y.get(theCoHull.get(theCoHull.size() - 1))),
                    xDraw(x.get(theCoHull.get(0))), yDraw(y.get(theCoHull.get(0))));
        } // end paintComponent

    }  // end class Graph
}// end class DT