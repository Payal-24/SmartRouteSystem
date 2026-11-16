
const ROWS = 10;
const COLS = 10;

let start = { row: 0, col: 0 };
let end = { row: 9, col: 9 };

let walls = new Set();
let grid = document.getElementById("grid");
let result = document.getElementById("result");

let isRunning = false;

// Create grid
function createGrid() {

    grid.innerHTML = "";

    for (let r = 0; r < ROWS; r++) {

        for (let c = 0; c < COLS; c++) {

            const cell = document.createElement("div");

            cell.classList.add("cell");
            cell.dataset.row = r;
            cell.dataset.col = c;

            cell.addEventListener("click", () => {
                toggleWall(r, c);
            });

            grid.appendChild(cell);
        }
    }

    renderGrid();
}

// Update grid appearance
function renderGrid(path = []) {

    const cells = document.querySelectorAll(".cell");

    cells.forEach(cell => {

        const r = Number(cell.dataset.row);
        const c = Number(cell.dataset.col);

        cell.className = "cell";

        if (walls.has(`${r},${c}`)) {
            cell.classList.add("wall");
        }

        if (path.some(p => p.row === r && p.col === c)) {
            cell.classList.add("path");
        }

        if (r === start.row && c === start.col) {
            cell.classList.add("start");
        }

        if (r === end.row && c === end.col) {
            cell.classList.add("end");
        }
    });
}

// Add or remove obstacles
function toggleWall(r, c) {

    if (isRunning) return;

    if ((r === start.row && c === start.col) ||
        (r === end.row && c === end.col)) {
        return;
    }

    const key = `${r},${c}`;

    if (walls.has(key)) {
        walls.delete(key);
    } else {
        walls.add(key);
    }

    result.textContent = "Grid updated. Find your route!";
    renderGrid();
}

// Dijkstra algorithm

async function findShortestPath() {

    const gridData = Array.from({ length: ROWS }, (_, r) =>
        Array.from({ length: COLS }, (_, c) =>
            walls.has(`${r},${c}`) ? 1 : 0
        )
    );

    try {
        result.textContent = "Finding route using Java...";

        const response = await fetch("http://localhost:8081/api/route", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                grid: gridData,
                startRow: start.row,
                startCol: start.col,
                endRow: end.row,
                endCol: end.col
            })
        });

        if (!response.ok) {
            throw new Error("Backend error: " + response.status);
        }

        const data = await response.json();

        if (!data.found) {
            result.textContent = "No route found!";
            renderGrid();
            return;
        }

        renderGrid(data.path);

        result.textContent =
            `Shortest distance: ${data.distance} steps (Java Backend)`;

    } catch (error) {
        console.error(error);
        result.textContent =
            "Backend connection failed! Check Java server.";
    }
}


// Find route button
document.getElementById("findPath")
    .addEventListener("click", findShortestPath);

// Reset button
document.getElementById("reset")
    .addEventListener("click", () => {

        walls.clear();
        result.textContent = "Grid reset successfully!";

        renderGrid();
    });

// Initial grid
createGrid();
