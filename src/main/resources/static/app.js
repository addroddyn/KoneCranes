console.log("📦 app.js loaded!");

const eventSource = new EventSource("/api/stream");

let gridInitialized = false;
let cachedCells = [];
let frameHandle = null;

eventSource.addEventListener("grid", (event) => {
  const grid = JSON.parse(event.data);

  if (!gridInitialized) {
    initializeGrid(grid);
    gridInitialized = true;
  }

  if (frameHandle) cancelAnimationFrame(frameHandle);
  frameHandle = requestAnimationFrame(() => renderGrid(grid));
});

eventSource.onerror = function (e) {
  console.error("❌ SSE error:", e);
};

function initializeGrid(grid) {
  const table = document.getElementById("traffic-table");
  table.innerHTML = "";
  cachedCells = [];

  for (let row = 0; row < grid.locations.length; row++) {
    const tr = document.createElement("tr");
    const rowCache = [];

    for (let col = 0; col < grid.locations[row].length; col++) {
      const td = document.createElement("td");
      td.style.width = (100 / grid.locations.length) + "%";
      td.style.height = "50px";
      td.style.textAlign = "center";
      td.style.border = "1px solid #ccc";

      tr.appendChild(td);
      rowCache.push(td);
    }

    table.appendChild(tr);
    cachedCells.push(rowCache);
  }
}

function renderGrid(grid) {
  for (let row = 0; row < grid.locations.length; row++) {
    for (let col = 0; col < grid.locations[row].length; col++) {
      const cell = cachedCells[row][col];
      const location = grid.locations[row][col];

      // Default appearance
      cell.style.backgroundColor = "#eee";
      cell.textContent = `${location.row},${location.column}`;

      // 🟩 Vehicle
      if (location.hasVehicle === true) {
        cell.style.backgroundColor = "#4caf50";
      }

      // 🟦 Origin
      if (row === grid.originRow && col === grid.originColumn) {
        cell.style.backgroundColor = "#2196f3";
      }

      // 🎯 Target (overrides vehicle)
      if (location.target === true) {
        cell.style.backgroundColor = "#f32121";
      }
    }
  }
}
