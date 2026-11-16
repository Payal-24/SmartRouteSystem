
package backend;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RouteController {

    record Cell(int row, int col) {}

    record RouteRequest(
        int[][] grid,
        int startRow,
        int startCol,
        int endRow,
        int endCol
    ) {}

    record RouteResponse(
        List<Cell> path,
        int distance,
        boolean found
    ) {}

    record Node(int row, int col, int distance) {}

    @PostMapping("/route")
    public RouteResponse findRoute(@RequestBody RouteRequest request) {

        int[][] grid = request.grid();

        int rows = grid.length;
        int cols = grid[0].length;

        int sr = request.startRow();
        int sc = request.startCol();
        int er = request.endRow();
        int ec = request.endCol();

        if (!valid(sr, sc, rows, cols) ||
            !valid(er, ec, rows, cols) ||
            grid[sr][sc] == 1 || grid[er][ec] == 1) {

            return new RouteResponse(List.of(), -1, false);
        }

        int[][] dist = new int[rows][cols];

        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        Cell[][] parent = new Cell[rows][cols];

        PriorityQueue<Node> pq = new PriorityQueue<>(
            Comparator.comparingInt(Node::distance)
        );

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        dist[sr][sc] = 0;
        pq.add(new Node(sr, sc, 0));

        while (!pq.isEmpty()) {

            Node current = pq.poll();

            int r = current.row();
            int c = current.col();
            int d = current.distance();

            if (d > dist[r][c]) {
                continue;
            }

            if (r == er && c == ec) {
                break;
            }

            for (int i = 0; i < 4; i++) {

                int nr = r + dr[i];
                int nc = c + dc[i];

                if (!valid(nr, nc, rows, cols)) {
                    continue;
                }

                if (grid[nr][nc] == 1) {
                    continue;
                }

                int newDist = d + 1;

                if (newDist < dist[nr][nc]) {

                    dist[nr][nc] = newDist;

                    parent[nr][nc] = new Cell(r, c);

                    pq.add(new Node(nr, nc, newDist));
                }
            }
        }

        if (dist[er][ec] == Integer.MAX_VALUE) {
            return new RouteResponse(List.of(), -1, false);
        }

        List<Cell> path = new ArrayList<>();

        int r = er;
        int c = ec;

        while (r != sr || c != sc) {

            path.add(new Cell(r, c));

            Cell previous = parent[r][c];

            r = previous.row();
            c = previous.col();
        }

        path.add(new Cell(sr, sc));

        Collections.reverse(path);

        return new RouteResponse(
            path,
            dist[er][ec],
            true
        );
    }

    private boolean valid(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows &&
               c >= 0 && c < cols;
    }
}
