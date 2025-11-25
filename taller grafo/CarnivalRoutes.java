import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;

public class CarnivalRoutes {
    private static final List<String> LOCATIONS = Arrays.asList(
            "Carnival Plaza",
            "Nariño Square",
            "Panamerican Avenue",
            "Carnival Street",
            "Cultural Museum",
            "Hummingbird Monument",
            "Sun Square",
            "Children Park"
    );

    private static final Map<String, Map<String, Integer>> GRAPH = new HashMap<>();

    public static void main(String[] args) {
        for (String loc : LOCATIONS) {
            GRAPH.put(loc, new HashMap<>());
        }

        List<Object[]> connections = Arrays.asList(
                new Object[]{"Carnival Plaza", "Nariño Square", 3},
                new Object[]{"Nariño Square", "Panamerican Avenue", 5},
                new Object[]{"Panamerican Avenue", "Carnival Street", 2},
                new Object[]{"Carnival Street", "Cultural Museum", 4},
                new Object[]{"Cultural Museum", "Hummingbird Monument", 7},
                new Object[]{"Hummingbird Monument", "Sun Square", 3},
                new Object[]{"Sun Square", "Children Park", 4},
                new Object[]{"Children Park", "Carnival Plaza", 6},
                new Object[]{"Nariño Square", "Carnival Street", 4},
                new Object[]{"Carnival Plaza", "Children Park", 9}
        );

        for (Object[] c : connections) {
            addUndirectedEdge((String) c[0], (String) c[1], (Integer) c[2]);
        }

        System.out.println("Bienvenido al Sistema de Rutas del Carnaval (Pasto, Colombia)");
        System.out.println("\nLugares disponibles:");
        for (int i = 0; i < LOCATIONS.size(); i++) {
            System.out.println((i + 1) + ". " + LOCATIONS.get(i));
        }

        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("\nIngrese número de ubicación de salida: ");
            int startId = sc.nextInt() - 1;
            System.out.print("Ingrese número de ubicación de destino: ");
            int endId = sc.nextInt() - 1;

            if (startId < 0 || startId >= LOCATIONS.size() || endId < 0 || endId >= LOCATIONS.size()) {
                System.out.println("Selección inválida.");
                return;
            }

            String start = LOCATIONS.get(startId);
            String end = LOCATIONS.get(endId);

            PathResult result = dijkstra(start, end);
            if (result == null) {
                System.out.println("No hay ruta disponible entre las ubicaciones seleccionadas.");
                showGraph(null);
            } else {
                System.out.println("\nRuta más corta de " + start + " a " + end + ":");
                System.out.println(result.path.stream().collect(Collectors.joining(" -> ")));
                System.out.println("Distancia estimada: " + result.distance + " unidades");
                showGraph(result.path);
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida.");
        }
    }

    private static void addUndirectedEdge(String a, String b, int w) {
        GRAPH.get(a).put(b, w);
        GRAPH.get(b).put(a, w);
    }

    private static PathResult dijkstra(String start, String end) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        for (String v : LOCATIONS) dist.put(v, Integer.MAX_VALUE);
        dist.put(start, 0);

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (visited.contains(cur.name)) continue;
            visited.add(cur.name);
            if (cur.name.equals(end)) break;

            for (Map.Entry<String, Integer> e : GRAPH.get(cur.name).entrySet()) {
                String neighbor = e.getKey();
                int weight = e.getValue();
                if (dist.get(cur.name) == Integer.MAX_VALUE) continue;
                int alt = dist.get(cur.name) + weight;
                if (alt < dist.get(neighbor)) {
                    dist.put(neighbor, alt);
                    prev.put(neighbor, cur.name);
                    pq.add(new Node(neighbor, alt));
                }
            }
        }

        if (dist.get(end) == Integer.MAX_VALUE) return null;

        LinkedList<String> path = new LinkedList<>();
        String u = end;
        while (u != null) {
            path.addFirst(u);
            u = prev.get(u);
        }
        return new PathResult(path, dist.get(end));
    }

    private static class Node {
        String name;
        int dist;
        Node(String name, int dist) { this.name = name; this.dist = dist; }
    }

    private static class PathResult {
        List<String> path;
        int distance;
        PathResult(List<String> path, int distance) { this.path = path; this.distance = distance; }
    }

    private static void showGraph(List<String> path) {
        Set<EdgeKey> highlight = new HashSet<>();
        if (path != null && path.size() > 1) {
            for (int i = 0; i < path.size() - 1; i++) {
                highlight.add(new EdgeKey(path.get(i), path.get(i + 1)));
            }
        }
        JFrame frame = new JFrame("Main Carnival Routes - Pasto, Colombia");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new GraphPanel(GRAPH, LOCATIONS, highlight));
        frame.setVisible(true);
    }

    private static class EdgeKey {
        String a;
        String b;
        EdgeKey(String a, String b) {
            if (a.compareTo(b) <= 0) { this.a = a; this.b = b; }
            else { this.a = b; this.b = a; }
        }
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EdgeKey)) return false;
            EdgeKey other = (EdgeKey) o;
            return a.equals(other.a) && b.equals(other.b);
        }
        public int hashCode() { return Objects.hash(a, b); }
    }

    private static class GraphPanel extends JPanel {
        Map<String, Map<String, Integer>> graph;
        List<String> locations;
        Set<EdgeKey> highlight;

        GraphPanel(Map<String, Map<String, Integer>> graph, List<String> locations, Set<EdgeKey> highlight) {
            this.graph = graph;
            this.locations = locations;
            this.highlight = highlight;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int cx = w / 2;
            int cy = h / 2;
            int r = (int) (Math.min(w, h) * 0.35);
            Map<String, Point> pos = new HashMap<>();
            for (int i = 0; i < locations.size(); i++) {
                double ang = 2 * Math.PI * i / locations.size();
                int x = cx + (int) (r * Math.cos(ang));
                int y = cy + (int) (r * Math.sin(ang));
                pos.put(locations.get(i), new Point(x, y));
            }

            Set<EdgeKey> drawn = new HashSet<>();
            for (String u : locations) {
                for (Map.Entry<String, Integer> e : graph.get(u).entrySet()) {
                    String v = e.getKey();
                    int wgt = e.getValue();
                    EdgeKey key = new EdgeKey(u, v);
                    if (drawn.contains(key)) continue;
                    drawn.add(key);

                    Point p1 = pos.get(u);
                    Point p2 = pos.get(v);
                    boolean hl = highlight.contains(key);

                    g2.setStroke(new BasicStroke(hl ? 3f : 1.5f));
                    g2.setColor(hl ? Color.RED : Color.BLACK);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                    int mx = (p1.x + p2.x) / 2;
                    int my = (p1.y + p2.y) / 2;
                    String label = String.valueOf(wgt);
                    g2.setColor(Color.DARK_GRAY);
                    g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                    g2.drawString(label, mx + 5, my - 5);
                }
            }

            int nodeR = 24;
            for (String u : locations) {
                Point p = pos.get(u);
                boolean inPath = false;
                for (EdgeKey k : highlight) {
                    if (k.a.equals(u) || k.b.equals(u)) { inPath = true; break; }
                }
                g2.setColor(new Color(144, 238, 144));
                g2.fillOval(p.x - nodeR, p.y - nodeR, nodeR * 2, nodeR * 2);
                g2.setColor(inPath ? Color.RED : Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(p.x - nodeR, p.y - nodeR, nodeR * 2, nodeR * 2);

                g2.setColor(Color.BLACK);
                g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                FontMetrics fm = g2.getFontMetrics();
                int tx = p.x - fm.stringWidth(u) / 2;
                int ty = p.y + nodeR + fm.getAscent();
                g2.drawString(u, tx, ty);
            }

            g2.dispose();
        }
    }
}
