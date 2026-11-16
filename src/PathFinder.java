
import java.util.*;

public class PathFinder {

    static int ROWS = 5;
    static int COLS = 5;

    // 0 = Road, 1 = Obstacle
    static int[][] grid = {
        {0, 0, 0, 1, 0},
        {1, 1, 0, 1, 0},
        {0, 0, 0, 0, 0},
        {0, 1, 1, 1, 0},
        {0, 0, 0, 0, 0}
    };

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    static boolean isValid(int r, int c) {
        return r >= 0 && r < ROWS &&
               c >= 0 && c < COLS &&
               grid[r][c] == 0;
    }

    static void findPath(int sr, int sc, int er, int ec) {

        int[][] dist = new int[ROWS][COLS];

        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        int[][] parentR = new int[ROWS][COLS];
        int[][] parentC = new int[ROWS][COLS];

        PriorityQueue<int[]> pq = new PriorityQueue<>(
            (a, b) -> Integer.compare(a[2], b[2])
        );

        dist[sr][sc] = 0;
        pq.add(new int[]{sr, sc, 0});

        while (!pq.isEmpty()) {

            int[] current = pq.poll();

            int r = current[0];
            int c = current[1];
            int d = current[2];

            if (d > dist[r][c]) {
                continue;
            }

            if (r == er && c == ec) {
                break;
            }

            for (int i = 0; i < 4; i++) {

                int nr = r + dr[i];
                int nc = c + dc[i];

                if (isValid(nr, nc)) {

                    int newDist = d + 1;

                    if (newDist < dist[nr][nc]) {

                        dist[nr][nc] = newDist;

                        parentR[nr][nc] = r;
                        parentC[nr][nc] = c;

                        pq.add(new int[]{nr, nc, newDist});
                    }
                }
            }
        }

        if (dist[er][ec] == Integer.MAX_VALUE) {
            System.out.println("No path found!");
            return;
        }

        // Reconstruct shortest path
        int r = er;
        int c = ec;

        while (r != sr || c != sc) {

            grid[r][c] = 2;

            int pr = parentR[r][c];
            int pc = parentC[r][c];

            r = pr;
            c = pc;
        }

        grid[sr][sc] = 3;
        grid[er][ec] = 4;

        System.out.println("Shortest distance: " + dist[er][ec]);
        printGrid();
    }

    static void printGrid() {

        for (int[] row : grid) {

            for (int cell : row) {

                if (cell == 0) System.out.print(". ");
                else if (cell == 1) System.out.print("# ");
                else if (cell == 2) System.out.print("* ");
                else if (cell == 3) System.out.print("S ");
                else if (cell == 4) System.out.print("E ");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        // Start = (0,0), Destination = (4,4)
        findPath(0, 0, 4, 4);
    }
}
