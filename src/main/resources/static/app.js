console.log("📦 app.js loaded!");

const eventSource = new EventSource("/api/stream");

let gridInitialized = false;
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

  for (let row = 0; row < grid.rowCount; row++) {
    const tr = document.createElement("tr");

    for (let col = 0; col < grid.rowCount; col++) {
      const td = document.createElement("td");
      td.id = "td_" + row + "_" + col
      td.style.width = (100 / grid.rowCount) + "%";
      td.style.height = "50px";
      td.style.textAlign = "center";
      td.style.border = "1px solid #ccc";
      td.textContent = `${row},${col}`;

      if (row === grid.originRow && col === grid.originColumn) {
        td.classList.add("origin");
      }

      tr.appendChild(td);


    }

    table.appendChild(tr);
  }
}

function renderGrid(grid) {
  for (let row = 0; row < grid.oldLocations.length; row++) {
    for (let col = 0; col < grid.oldLocations[row].length; col++) {
      const location = grid.oldLocations[row][col];
      const cell = document.getElementById("td_" + location.row + "_" + location.column)
     cell.className = "";
}
}
  for (let row = 0; row < grid.newLocations.length; row++) {
    for (let col = 0; col < grid.newLocations[row].length; col++) {
      const location = grid.newLocations[row][col];
      const cell = document.getElementById("td_" + location.row + "_" + location.column)

     //cell.className = ""; // Reset all previous styles

     if (location.hasVehicle) {
       cell.classList.add("vehicle");
     } else if (location.target) {
       cell.classList.add("target");
     } else if (row === grid.originRow && col === grid.originColumn) {
       cell.classList.add("origin");
     }
    }
  }
  document.getElementById('traffic-table').style.display = 'none';
  document.getElementById('traffic-table').style.display = 'block';
}
