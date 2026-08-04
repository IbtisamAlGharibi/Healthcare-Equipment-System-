/* ============================================================
   Healthcare Equipment System — merged frontend JS
   Talks to a real Spring Boot backend with JWT auth
   (POST /api/auth/login, POST /api/auth/register).
   Every request after login carries: Authorization: Bearer <token>
   ============================================================ */

const API_BASE = "http://localhost:8080";

/* ---------------- auth (real JWT) ---------------- */
const TOKEN_KEY = "hes_jwt_token";

function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}

/* Decode a JWT payload client-side (no signature check — just reads
   the claims your JwtUtil put in there: subject + role). */
function parseJwt(token) {
  try {
    const payload = token.split(".")[1];
    const json = decodeURIComponent(
      atob(payload.replace(/-/g, "+").replace(/_/g, "/"))
        .split("")
        .map((c) => "%" + c.charCodeAt(0).toString(16).padStart(2, "0"))
        .join("")
    );
    return JSON.parse(json);
  } catch (e) {
    return null;
  }
}

function currentUser() {
  const token = getToken();
  if (!token) return null;
  const claims = parseJwt(token);
  if (!claims) return null;
  return { username: claims.sub, role: claims.role, exp: claims.exp };
}

function isAuthed() {
  const user = currentUser();
  if (!user) return false;
  if (user.exp && Date.now() >= user.exp * 1000) {
    clearToken();
    return false;
  }
  return true;
}

function logout() {
  clearToken();
  window.location.href = "login.html";
}

/* ---------------- role-based access control ----------------
   Mirrors the backend's SecurityConfig role rules, page by page:
   - ADMIN: everything, including user registration and reports.
   - LAB_STAFF: equipment (view only) + reservations (create/cancel).
   - TECHNICIAN: equipment (view only) + maintenance (create/complete).
   A page not listed for a role is simply not reachable by that role,
   and isn't shown in that role's sidebar either. */
const ROLE_PAGES = {
  ADMIN: ["dashboard", "laboratories", "equipment", "staff", "technicians", "reservations", "maintenance", "reports", "users"],
  LAB_STAFF: ["equipment", "reservations"],
  TECHNICIAN: ["equipment", "maintenance"],
};

// Where each role lands after login / when it tries to open a page it
// isn't authorized for.
const ROLE_HOME = {
  ADMIN: "index.html",
  LAB_STAFF: "reservations.html",
  TECHNICIAN: "maintenance.html",
};

function isPageAllowed(role, page) {
  const allowed = ROLE_PAGES[role];
  return Array.isArray(allowed) && allowed.includes(page);
}

function homePageFor(role) {
  return ROLE_HOME[role] || "login.html";
}

function requireAuth() {
  const page = document.body.dataset.page;
  if (page === "login") return;

  if (!isAuthed()) {
    window.location.href = "login.html";
    return;
  }

  const user = currentUser();
  if (!isPageAllowed(user.role, page)) {
    // Signed in, but this role has no business on this page —
    // send them to the page that's actually theirs, don't just
    // hide the buttons and hope for the best.
    window.location.href = homePageFor(user.role);
  }
}

requireAuth();

/* ---------------- core request helper ---------------- */
async function apiRequest(path, options = {}) {
  const url = `${API_BASE}${path}`;
  const token = getToken();
  const opts = {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    ...options,
  };
  if (options.headers) opts.headers = { ...opts.headers, ...options.headers };
  if (opts.body && typeof opts.body !== "string") {
    opts.body = JSON.stringify(opts.body);
  }

  let res;
  try {
    res = await fetch(url, opts);
  } catch (err) {
    throw new Error(
      `Could not reach the server at ${API_BASE}. Is the backend running?`
    );
  }

  if (res.status === 401) {
    // Token missing/expired/rejected — send back to login.
    clearToken();
    if (document.body.dataset.page !== "login") {
      toast("Your session expired. Please sign in again.", "error");
      setTimeout(() => (window.location.href = "login.html"), 1200);
    }
    throw new Error("Not authenticated.");
  }

  if (res.status === 403) {
    throw new Error("Your role doesn't have permission to do that.");
  }

  if (!res.ok) {
    let detail = "";
    try {
      const text = await res.text();
      detail = text ? ` — ${text}` : "";
    } catch (_) {}
    throw new Error(`Request failed (${res.status})${detail}`);
  }

  if (res.status === 204) return null;

  const contentType = res.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    return res.json();
  }
  const text = await res.text();
  return text || null;
}

const api = {
  get: (path) => apiRequest(path, { method: "GET" }),
  post: (path, body) => apiRequest(path, { method: "POST", body }),
  put: (path, body) => apiRequest(path, { method: "PUT", body }),
  del: (path) => apiRequest(path, { method: "DELETE" }),
};

/* ---------- Toast notifications ---------- */
function toast(message, type = "ok") {
  let holder = document.getElementById("toast");
  if (!holder) {
    holder = document.createElement("div");
    holder.id = "toast";
    document.body.appendChild(holder);
  }
  const item = document.createElement("div");
  item.className = "toast-item" + (type === "error" ? " error" : "");
  item.textContent = message;
  holder.appendChild(item);
  setTimeout(() => item.remove(), 4200);
}

function toastError(err) {
  console.error(err);
  toast(err.message || "Something went wrong.", "error");
}

/* ---------- Status chip rendering (shared visual vocabulary) ---------- */
const STATUS_MAP = {
  AVAILABLE: "ok",
  Available: "ok",
  ACTIVE: "ok",
  APPROVED: "ok",
  Approved: "ok",
  COMPLETE: "ok",
  COMPLETED: "ok",
  Completed: "ok",
  RESERVED: "warn",
  Reserved: "warn",
  PENDING: "warn",
  Pending: "warn",
  UNDER_MAINTENANCE: "danger",
  Maintenance: "danger",
  CANCELLED: "danger",
  Cancelled: "danger",
  INACTIVE: "neutral",
};

function statusChip(status) {
  if (status === null || status === undefined || status === "") {
    return `<span class="chip chip-neutral"><span class="led led-neutral"></span>—</span>`;
  }
  const key = STATUS_MAP[status] || "neutral";
  const label = String(status).replace(/_/g, " ");
  return `<span class="chip chip-${key}"><span class="led led-${key}"></span>${escapeHtml(
    label
  )}</span>`;
}

function activeChip(isActive) {
  return isActive
    ? `<span class="chip chip-ok"><span class="led led-ok"></span>Active</span>`
    : `<span class="chip chip-neutral"><span class="led led-neutral"></span>Inactive</span>`;
}

/* ---------- Small helpers ---------- */
function escapeHtml(str) {
  if (str === null || str === undefined) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

function fmtDate(d) {
  if (!d) return "—";
  return d;
}

function fmtTime(t) {
  if (!t) return "—";
  return String(t).slice(0, 5);
}

function qs(sel, root = document) {
  return root.querySelector(sel);
}
function qsa(sel, root = document) {
  return Array.from(root.querySelectorAll(sel));
}

/* ---------- Modal helpers ---------- */
function openModal(id) {
  qs("#" + id).classList.add("open");
}
function closeModal(id) {
  qs("#" + id).classList.remove("open");
}

/* ---------- Confirm delete helper ---------- */
function confirmAction(message) {
  return window.confirm(message);
}

/* ---------------- nav.js ---------------- */
/* Injects the sidebar nav into any page with <div id="sidebar"></div>.
   Set data-page="equipment" (etc) on <body> to highlight the active link. */

const NAV_LINKS = [
  { page: "dashboard", href: "index.html", label: "Dashboard" },
  { page: "laboratories", href: "laboratories.html", label: "Laboratories" },
  { page: "equipment", href: "equipment.html", label: "Equipment" },
  { page: "staff", href: "staff.html", label: "Laboratory Staff" },
  { page: "reservations", href: "reservations.html", label: "Reservations" },
  { page: "technicians", href: "technicians.html", label: "Technicians" },
  { page: "maintenance", href: "maintenance.html", label: "Maintenance" },
  { page: "users", href: "users.html", label: "Manage Users" },
];

(function renderNav() {
  const mount = document.getElementById("sidebar");
  if (!mount) return;
  const active = document.body.getAttribute("data-page");
  const user = currentUser();
  const links = user ? NAV_LINKS.filter((l) => isPageAllowed(user.role, l.page)) : [];
  mount.innerHTML = `
    <div class="sidebar-brand">
      HES
      <span>Healthcare Equipment System</span>
    </div>
    <nav>
      ${links
        .map(
          (l) => `
        <a class="nav-link${l.page === active ? " active" : ""}" href="${l.href}">
          <span class="dot"></span>${l.label}
        </a>`
        )
        .join("")}
    </nav>
    <div class="sidebar-foot">
      <div style="margin-bottom:8px;">
        ${escapeHtml(user ? user.username : "—")}
        ${user ? `<span class="chip chip-brand" style="margin-left:6px;">${escapeHtml(user.role)}</span>` : ""}
      </div>
      <button class="sidebar-logout" onclick="logout()">Log out</button>
    </div>
  `;
})();

/* ---------------- dashboard.js ---------------- */
/* Dashboard: pulls counts + report data from /api/report/*
   NOTE: three of these report endpoints (laboratory/equipment,
   laboratory/reservations, staff/top-reservations) currently return
   raw JPA entity lists rather than grouped counts, so we aggregate
   client-side. They also risk serialization errors on the backend
   (bidirectional entity relationships with no @JsonIgnore) — if one
   fails, we just show "No data yet." instead of breaking the page. */

function renderPairTable(tbodySelector, rows, colspan = 2) {
  const tbody = qs(tbodySelector);
  if (!rows || rows.length === 0) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="${colspan}">No data yet.</td></tr>`;
    return;
  }
  tbody.innerHTML = rows
    .map((row) => {
      const cells = Array.isArray(row) ? row : [row.label, row.count];
      return `<tr>${cells
        .map((c, i) =>
          i === 0
            ? `<td>${escapeHtml(c)}</td>`
            : `<td class="cell-mono">${escapeHtml(c)}</td>`
        )
        .join("")}</tr>`;
    })
    .join("");
}

// Groups a list of items into [{label, count}] sorted descending, using
// a function that pulls the group key out of each item.
function groupCount(list, keyFn) {
  const counts = new Map();
  (list || []).forEach((item) => {
    const key = keyFn(item);
    if (!key) return;
    counts.set(key, (counts.get(key) || 0) + 1);
  });
  return Array.from(counts.entries())
    .map(([label, count]) => ({ label, count }))
    .sort((a, b) => b.count - a.count);
}

async function loadEquipmentPerLab(tbodySelector) {
  try {
    const data = await api.get("/api/report/laboratory/equipment");
    const grouped = groupCount(data, (e) => e.laboratory?.name);
    renderPairTable(tbodySelector, grouped);
  } catch (err) {
    renderPairTable(tbodySelector, []);
  }
}

async function loadReservationsPerLab(tbodySelector) {
  try {
    const data = await api.get("/api/report/laboratory/reservations");
    const grouped = groupCount(data, (r) => r.equipment?.laboratory?.name);
    renderPairTable(tbodySelector, grouped);
  } catch (err) {
    renderPairTable(tbodySelector, []);
  }
}

async function loadTopStaff(tbodySelector) {
  try {
    const data = await api.get("/api/report/staff/top-reservations");
    const grouped = groupCount(data, (r) => r.laboratoryStaff?.name);
    renderPairTable(tbodySelector, grouped);
  } catch (err) {
    renderPairTable(tbodySelector, []);
  }
}

async function loadTopTech(tbodySelector) {
  try {
    const data = await api.get("/api/report/technician/top-maintenance");
    renderPairTable(tbodySelector, data);
  } catch (err) {
    renderPairTable(tbodySelector, []);
  }
}

async function loadCounts(prefix) {
  try {
    const [available, reserved, maintenance] = await Promise.all([
      api.get("/api/report/equipment/available-count"),
      api.get("/api/report/equipment/reserved-count"),
      api.get("/api/report/equipment/maintenance-count"),
    ]);
    qs(`#${prefix}Available`).textContent = available ?? 0;
    qs(`#${prefix}Reserved`).textContent = reserved ?? 0;
    qs(`#${prefix}Maintenance`).textContent = maintenance ?? 0;
  } catch (err) {
    toastError(err);
  }
}

async function loadDashboard() {
  qs("#statAvailable").textContent = "—";
  qs("#statReserved").textContent = "—";
  qs("#statMaintenance").textContent = "—";

  await loadCounts("stat");
  loadEquipmentPerLab("#tblEquipmentPerLab tbody");
  loadReservationsPerLab("#tblReservationsPerLab tbody");
  loadTopStaff("#tblTopStaff tbody");
  loadTopTech("#tblTopTech tbody");
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "dashboard") return;
  loadDashboard();
  qs("#refreshBtn").addEventListener("click", loadDashboard);
});

/* ---------------- laboratories.js ---------------- */
/* Laboratories: /api/laboratories/*
   Matches LaboratoryRequestDTO (name, location, description, isActive)
   and LaboratoryResponseDTO (id, name, location, description, isActive,
   equipments[]). */

let allLabs = [];
let labActiveOnly = false;

function labId(lab) {
  return lab.id;
}

function applyLabActiveFilter(labs) {
  return labActiveOnly ? (labs || []).filter((l) => l.isActive !== false) : labs;
}

function renderLabs(labs) {
  const tbody = qs("#labRows");
  if (!labs || labs.length === 0) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="5">No laboratories found.</td></tr>`;
    return;
  }
  tbody.innerHTML = labs
    .map(
      (lab) => `
    <tr>
      <td><strong>${escapeHtml(lab.name)}</strong></td>
      <td>${escapeHtml(lab.location)}</td>
      <td class="muted">${escapeHtml(lab.description || "—")}</td>
      <td>${activeChip(lab.isActive)}</td>
      <td class="cell-actions">
        <button class="btn btn-secondary btn-sm" onclick="onEditLab(${labId(lab)})">Edit</button>
        <button class="btn btn-danger btn-sm" onclick="onDeleteLab(${labId(lab)})">Delete</button>
      </td>
    </tr>`
    )
    .join("");
}

async function loadLabs() {
  try {
    allLabs = await api.get("/api/laboratories/all");
    renderLabs(applyLabActiveFilter(allLabs));
  } catch (err) {
    toastError(err);
    renderLabs([]);
  }
}

function onEditLab(id) {
  const lab = allLabs.find((l) => labId(l) === id);
  if (!lab) return;
  qs("#labModalTitle").textContent = "Edit laboratory";
  qs("#labId").value = labId(lab);
  qs("#labName").value = lab.name || "";
  qs("#labLocation").value = lab.location || "";
  qs("#labDescription").value = lab.description || "";
  qs("#labActive").checked = lab.isActive !== false;
  openModal("labModal");
}

async function onDeleteLab(id) {
  if (!confirmAction("Deactivate this laboratory? This can't be undone here.")) return;
  try {
    await api.del(`/api/laboratories/delete/${id}`);
    toast("Laboratory deactivated.");
    loadLabs();
  } catch (err) {
    toastError(err);
  }
}

async function onSaveLab() {
  const id = qs("#labId").value;
  const payload = {
    name: qs("#labName").value.trim(),
    location: qs("#labLocation").value.trim(),
    description: qs("#labDescription").value.trim(),
    isActive: qs("#labActive").checked,
  };
  if (!payload.name || !payload.location || !payload.description) {
    toast("Name, location, and description are required.", "error");
    return;
  }
  try {
    if (id) {
      await api.put(`/api/laboratories/update/${id}`, payload);
      toast("Laboratory updated.");
    } else {
      await api.post("/api/laboratories/add", payload);
      toast("Laboratory added.");
    }
    closeModal("labModal");
    loadLabs();
  } catch (err) {
    toastError(err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "laboratories") return;
  loadLabs();

  qs("#btnAdd").addEventListener("click", () => {
    qs("#labModalTitle").textContent = "Add laboratory";
    qs("#labForm").reset();
    qs("#labId").value = "";
    qs("#labActive").checked = true;
    openModal("labModal");
  });

  qsa("[data-close]").forEach((btn) =>
    btn.addEventListener("click", () => closeModal("labModal"))
  );

  qs("#labSaveBtn").addEventListener("click", onSaveLab);

  qs("#btnFilter").addEventListener("click", async () => {
    const location = qs("#filterLocation").value.trim();
    if (!location) return loadLabs();
    try {
      const data = await api.get(
        `/api/laboratories/get-by-location?location=${encodeURIComponent(location)}`
      );
      allLabs = data;
      renderLabs(applyLabActiveFilter(data));
    } catch (err) {
      toastError(err);
    }
  });

  qs("#filterActiveOnly").addEventListener("change", (e) => {
    labActiveOnly = e.target.checked;
    renderLabs(applyLabActiveFilter(allLabs));
  });

  qs("#btnClearFilter").addEventListener("click", () => {
    qs("#filterLocation").value = "";
    qs("#filterActiveOnly").checked = false;
    labActiveOnly = false;
    loadLabs();
  });
});

/* ---------------- equipment.js ---------------- */
/* Equipment: /api/equipment/*
   Matches EquipmentRequestDTO (name, serialNumber, status, purchaseDate,
   isActive, laboratory_id — all required) and EquipmentResponseDTO
   (id, name, serialNumber, status, purchaseDate, laboratoryName,
   isActive — flat laboratory name, no nested object, no description). */

let allEquipment = [];
let allLabsForSelect = [];
let equipActiveOnly = false;

function equipId(e) {
  return e.id;
}

function labName(e) {
  return e.laboratoryName || "—";
}

function applyEquipActiveFilter(list) {
  return equipActiveOnly ? (list || []).filter((e) => e.isActive !== false) : list;
}

function renderEquipment(list) {
  const tbody = qs("#equipRows");
  const isAdmin = currentUser()?.role === "ADMIN";
  if (!list || list.length === 0) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="6">No equipment found.</td></tr>`;
    return;
  }
  tbody.innerHTML = list
    .map(
      (e) => `
    <tr>
      <td><strong>${escapeHtml(e.name)}</strong></td>
      <td class="cell-mono">${escapeHtml(e.serialNumber || "—")}</td>
      <td>${escapeHtml(labName(e))}</td>
      <td class="cell-mono">${fmtDate(e.purchaseDate)}</td>
      <td>${e.isActive === false ? `<span class="chip chip-danger">DEACTIVATED</span>` : statusChip(e.status)}</td>
      <td class="cell-actions">
        ${
          isAdmin
            ? `
        <button class="btn btn-secondary btn-sm" onclick="onEditEquip(${equipId(e)})">Edit</button>
        <button class="btn btn-danger btn-sm" onclick="onDeleteEquip(${equipId(e)})">Delete</button>`
            : ""
        }
      </td>
    </tr>`
    )
    .join("");
}

async function loadLabsForSelect() {
  try {
    allLabsForSelect = await api.get("/api/laboratories/all");
    const sel = qs("#equipLab");
    sel.innerHTML = allLabsForSelect
      .map((l) => `<option value="${l.id}">${escapeHtml(l.name)}</option>`)
      .join("");
  } catch (err) {
    toastError(err);
  }
}

async function loadEquipment() {
  const mode = qs("#filterAvailable").value;
  try {
    allEquipment =
      mode === "available"
        ? await api.get("/api/equipment/available")
        : await api.get("/api/equipment");
    renderEquipment(applyEquipActiveFilter(allEquipment));
  } catch (err) {
    toastError(err);
    renderEquipment([]);
  }
}

function onEditEquip(id) {
  const e = allEquipment.find((x) => equipId(x) === id);
  if (!e) return;
  qs("#equipModalTitle").textContent = "Edit equipment";
  qs("#equipId").value = equipId(e);
  qs("#equipName").value = e.name || "";
  qs("#equipSerial").value = e.serialNumber || "";
  qs("#equipPurchaseDate").value = e.purchaseDate || "";
  // Response only gives us the lab's name, not its id — best effort match.
  const matchingLab = allLabsForSelect.find((l) => l.name === e.laboratoryName);
  if (matchingLab) qs("#equipLab").value = matchingLab.id;
  openModal("equipModal");
}

async function onDeleteEquip(id) {
  if (!confirmAction("Deactivate this equipment record?")) return;
  try {
    await api.del(`/api/equipment/${id}`);
    toast("Equipment deactivated.");
    loadEquipment();
  } catch (err) {
    toastError(err);
  }
}

function onChangeStatus(id, currentStatus) {
  qs("#statusEquipId").value = id;
  qs("#statusSelect").value = currentStatus || "Available";
  openModal("statusModal");
}

async function onSaveStatus() {
  const id = qs("#statusEquipId").value;
  const status = qs("#statusSelect").value;
  try {
    await api.put(`/api/equipment/${id}/status?status=${encodeURIComponent(status)}`);
    toast("Status updated.");
    closeModal("statusModal");
    loadEquipment();
  } catch (err) {
    toastError(err);
  }
}

async function onSaveEquip() {
  const id = qs("#equipId").value;
  const payload = {
    name: qs("#equipName").value.trim(),
    serialNumber: qs("#equipSerial").value.trim(),
    purchaseDate: qs("#equipPurchaseDate").value || null,
    laboratoryId: Number(qs("#equipLab").value) || null,
  };
  if (!payload.name || !payload.serialNumber || !payload.purchaseDate || !payload.laboratoryId) {
    toast("Name, serial number, purchase date, and laboratory are required.", "error");
    return;
  }
  try {
    if (id) {
      await api.put(`/api/equipment/${id}`, payload);
      toast("Equipment updated.");
    } else {
      await api.post("/api/equipment", payload);
      toast("Equipment added.");
    }
    closeModal("equipModal");
    loadEquipment();
  } catch (err) {
    toastError(err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "equipment") return;
  const isAdmin = currentUser()?.role === "ADMIN";
  loadLabsForSelect();
  loadEquipment();

  qs("#filterAvailable").addEventListener("change", loadEquipment);
  qs("#filterActiveOnly").addEventListener("change", (e) => {
    equipActiveOnly = e.target.checked;
    renderEquipment(applyEquipActiveFilter(allEquipment));
  });

  if (!isAdmin) {
    // View-only for LAB_STAFF / TECHNICIAN — the backend rejects their
    // writes anyway, so don't dangle a button that will just 403.
    qs("#btnAdd").style.display = "none";
    return;
  }

  qs("#btnAdd").addEventListener("click", () => {
    qs("#equipModalTitle").textContent = "Add equipment";
    qs("#equipForm").reset();
    qs("#equipId").value = "";
    openModal("equipModal");
  });

  qsa("[data-close]").forEach((btn) =>
    btn.addEventListener("click", () => closeModal("equipModal"))
  );
  qsa("[data-close-status]").forEach((btn) =>
    btn.addEventListener("click", () => closeModal("statusModal"))
  );

  qs("#equipSaveBtn").addEventListener("click", onSaveEquip);
  qs("#statusSaveBtn").addEventListener("click", onSaveStatus);
});

/* ---------------- staff.js ---------------- */
/* Laboratory Staff: /laboratory-staff/*
   Matches LaboratoryStaffRequestDTO/ResponseDTO exactly
   (name, email, phone, department[, isActive on response]). */

let allStaff = [];
let staffActiveOnly = false;

function staffId(s) {
  return s.id;
}

function applyStaffActiveFilter(list) {
  return staffActiveOnly ? (list || []).filter((s) => s.isActive !== false) : list;
}

function renderStaff(list) {
  const tbody = qs("#staffRows");
  if (!list || list.length === 0) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="6">No staff found.</td></tr>`;
    return;
  }
  tbody.innerHTML = list
    .map(
      (s) => `
    <tr>
      <td><strong>${escapeHtml(s.name)}</strong></td>
      <td class="cell-mono">${escapeHtml(s.email || "—")}</td>
      <td class="cell-mono">${escapeHtml(s.phone || "—")}</td>
      <td>${escapeHtml(s.department || "—")}</td>
      <td>${activeChip(s.isActive)}</td>
      <td class="cell-actions">
        <button class="btn btn-secondary btn-sm" onclick="onEditStaff(${staffId(s)})">Edit</button>
        <button class="btn btn-danger btn-sm" onclick="onDeleteStaff(${staffId(s)})">Delete</button>
      </td>
    </tr>`
    )
    .join("");
}

async function loadStaff() {
  try {
    allStaff = await api.get("/laboratory-staff");
    renderStaff(applyStaffActiveFilter(allStaff));
  } catch (err) {
    toastError(err);
    renderStaff([]);
  }
}

function onEditStaff(id) {
  const s = allStaff.find((x) => staffId(x) === id);
  if (!s) return;
  qs("#staffModalTitle").textContent = "Edit staff member";
  qs("#staffId").value = staffId(s);
  qs("#staffName").value = s.name || "";
  qs("#staffEmail").value = s.email || "";
  qs("#staffPhone").value = s.phone || "";
  qs("#staffDept").value = s.department || "";
  openModal("staffModal");
}

async function onDeleteStaff(id) {
  if (!confirmAction("Deactivate this staff member?")) return;
  try {
    await api.del(`/laboratory-staff/${id}`);
    toast("Staff member deactivated.");
    loadStaff();
  } catch (err) {
    toastError(err);
  }
}

async function onSaveStaff() {
  const id = qs("#staffId").value;
  const payload = {
    name: qs("#staffName").value.trim(),
    email: qs("#staffEmail").value.trim(),
    phone: qs("#staffPhone").value.trim(),
    department: qs("#staffDept").value.trim(),
  };
  if (!payload.name || !payload.email || !payload.phone || !payload.department) {
    toast("All fields are required.", "error");
    return;
  }
  try {
    if (id) {
      await api.put(`/laboratory-staff/${id}`, payload);
      toast("Staff member updated.");
    } else {
      await api.post("/laboratory-staff", payload);
      toast("Staff member added.");
    }
    closeModal("staffModal");
    loadStaff();
  } catch (err) {
    toastError(err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "staff") return;
  loadStaff();

  qs("#btnAdd").addEventListener("click", () => {
    qs("#staffModalTitle").textContent = "Add staff member";
    qs("#staffForm").reset();
    qs("#staffId").value = "";
    openModal("staffModal");
  });

  qsa("[data-close]").forEach((btn) =>
    btn.addEventListener("click", () => closeModal("staffModal"))
  );
  qs("#staffSaveBtn").addEventListener("click", onSaveStaff);

  qs("#btnFilter").addEventListener("click", async () => {
    const dept = qs("#filterDept").value.trim();
    if (!dept) return loadStaff();
    try {
      const data = await api.get(`/laboratory-staff/department/${encodeURIComponent(dept)}`);
      allStaff = data;
      renderStaff(applyStaffActiveFilter(data));
    } catch (err) {
      toastError(err);
    }
  });

  qs("#filterActiveOnly").addEventListener("change", (e) => {
    staffActiveOnly = e.target.checked;
    renderStaff(applyStaffActiveFilter(allStaff));
  });

  qs("#btnClearFilter").addEventListener("click", () => {
    qs("#filterDept").value = "";
    qs("#filterActiveOnly").checked = false;
    staffActiveOnly = false;
    loadStaff();
  });
});

/* ---------------- reservations.js ---------------- */
/* Reservations: /reservations/*
   Matches ReservationResponseDTO exactly: id, reservationDate,
   startTime, endTime, purpose, status, staffId, staffName,
   equipmentId, equipmentName (equipment fields may be null — the
   backend has that mapping commented out). */

   let allReservations = [];
   let allStaffForSelect = [];
   let allEquipForResSelect = [];
   let resActiveOnly = false;
   
   function resId(r) {
     return r.id;
   }
   
   function applyResActiveFilter(list) {
     return resActiveOnly ? (list || []).filter((r) => r.isActive !== false) : list;
   }
   
   /* REPLACED: Updated renderReservations with proper whitespacing & inline button safety */
   function renderReservations(list) {
     const tbody = qs("#resRows");
     const isAdmin = currentUser()?.role === "ADMIN";
     if (!list || list.length === 0) {
       tbody.innerHTML = `<tr class="empty-row"><td colspan="7">No reservations found.</td></tr>`;
       return;
     }
     tbody.innerHTML = list
       .map((r) => {
         const deleted = r.isActive === false;
         const statusUpper = (r.status || "").toUpperCase();
   
         return `
       <tr>
         <td class="cell-mono">${fmtDate(r.reservationDate)}</td>
         <td class="cell-mono">${fmtTime(r.startTime)}–${fmtTime(r.endTime)}</td>
         <td>${escapeHtml(r.purpose || "—")}</td>
         <td>${escapeHtml(r.staffName || "—")}</td>
         <td>${escapeHtml(r.equipmentName || "—")}</td>
         <td style="white-space:nowrap;">
           ${deleted ? `<span class="chip chip-danger">DELETED</span>` : statusChip(r.status)}
         </td>
         <td class="cell-actions" style="white-space:nowrap;">
           ${
             !deleted && isAdmin && statusUpper !== "APPROVED"
               ? `<button type="button" class="btn btn-secondary btn-sm" onclick="onApprove(${resId(r)})">Approve</button>`
               : ""
           }
           ${
             !deleted && statusUpper !== "CANCELLED"
               ? `<button type="button" class="btn btn-secondary btn-sm" onclick="onCancel(${resId(r)})">Cancel</button>`
               : ""
           }
           ${!deleted && isAdmin ? `<button type="button" class="btn btn-danger btn-sm" onclick="onDeleteRes(${resId(r)})">Delete</button>` : ""}
         </td>
       </tr>`;
       })
       .join("");
   }
   
   async function loadStaffForSelect() {
     try {
       allStaffForSelect = await api.get("/laboratory-staff");
       const opts = allStaffForSelect
         .map((s) => `<option value="${s.id}">${escapeHtml(s.name)}</option>`)
         .join("");
       qs("#resStaff").innerHTML = opts;
       qs("#filterStaff").innerHTML = `<option value="">All staff</option>` + opts;
     } catch (err) {
       toastError(err);
     }
   }
   
   async function loadEquipmentForResSelect() {
     try {
       allEquipForResSelect = await api.get("/api/equipment/available");
       qs("#resEquip").innerHTML = allEquipForResSelect
         .map((e) => `<option value="${e.id}">${escapeHtml(e.name)} (${escapeHtml(e.laboratoryName || "—")})</option>`)
         .join("");
     } catch (err) {
       toastError(err);
     }
   }
   
   async function loadReservations() {
     const staffId = qs("#filterStaff").value;
     try {
       allReservations = staffId
         ? await api.get(`/reservations/staff/${staffId}`)
         : await api.get("/reservations");
       renderReservations(applyResActiveFilter(allReservations));
     } catch (err) {
       toastError(err);
       renderReservations([]);
     }
   }
   
   /* REPLACED: Updated action functions to use finally blocks */
   async function onApprove(id) {
     try {
       await api.put(`/reservations/${id}/approve`);
       toast("Reservation approved.");
     } catch (err) {
       toastError(err);
     } finally {
       loadReservations();
     }
   }
   
   async function onCancel(id) {
     try {
       await api.put(`/reservations/${id}/cancel`);
       toast("Reservation cancelled.");
     } catch (err) {
       toastError(err);
     } finally {
       loadReservations();
     }
   }
   
   async function onDeleteRes(id) {
     if (!confirmAction("Delete this reservation?")) return;
     try {
       await api.del(`/reservations/${id}`);
       toast("Reservation deleted.");
     } catch (err) {
       toastError(err);
     } finally {
       loadReservations();
     }
   }
   
   async function onSaveRes() {
     const staffId = qs("#resStaff").value;
     const equipmentId = qs("#resEquip").value;
     const payload = {
       reservationDate: qs("#resDate").value,
       startTime: qs("#resStart").value,
       endTime: qs("#resEnd").value,
       purpose: qs("#resPurpose").value.trim(),
       equipmentId: Number(equipmentId) || null,
     };
     if (!staffId || !equipmentId || !payload.reservationDate || !payload.startTime || !payload.endTime || !payload.purpose) {
       toast("Please fill in staff, equipment, date, times, and purpose.", "error");
       return;
     }
     try {
       await api.post(`/reservations/${staffId}`, payload);
       toast("Reservation created.");
       closeModal("resModal");
       loadReservations();
     } catch (err) {
       toastError(err);
     }
   }
   
   document.addEventListener("DOMContentLoaded", () => {
     if (document.body.dataset.page !== "reservations") return;
     loadStaffForSelect().then(loadReservations);
     loadEquipmentForResSelect();
   
     qs("#btnAdd").addEventListener("click", () => {
       qs("#resForm").reset();
       loadEquipmentForResSelect();
       openModal("resModal");
     });
   
     qsa("[data-close]").forEach((btn) =>
       btn.addEventListener("click", () => closeModal("resModal"))
     );
     qs("#resSaveBtn").addEventListener("click", onSaveRes);
     qs("#filterStaff").addEventListener("change", loadReservations);
     qs("#filterActiveOnly").addEventListener("change", (e) => {
       resActiveOnly = e.target.checked;
       renderReservations(applyResActiveFilter(allReservations));
     });
   
     qs("#btnClearFilter").addEventListener("click", () => {
       qs("#filterStaff").value = "";
       qs("#filterActiveOnly").checked = false;
       resActiveOnly = false;
       loadReservations();
     });
   });
/* ---------------- technicians.js ---------------- */
/* Technicians: /api/technician/*
   Matches TechnicianRequestDTO/ResponseDTO exactly. */

let allTechs = [];

function techId(t) {
  return t.id;
}

function renderTechs(list) {
  const tbody = qs("#techRows");
  if (!list || list.length === 0) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="5">No technicians found.</td></tr>`;
    return;
  }
  tbody.innerHTML = list
    .map(
      (t) => `
    <tr>
      <td><strong>${escapeHtml(t.name)}</strong></td>
      <td class="cell-mono">${escapeHtml(t.phone || "—")}</td>
      <td>${escapeHtml(t.specialization || "—")}</td>
      <td>${activeChip(t.isActive)}</td>
      <td class="cell-actions">
        <button class="btn btn-secondary btn-sm" onclick="onEditTech(${techId(t)})">Edit</button>
        <button class="btn btn-danger btn-sm" onclick="onDeleteTech(${techId(t)})">Delete</button>
      </td>
    </tr>`
    )
    .join("");
}

async function loadTechs() {
  const activeOnly = qs("#filterActiveOnly").checked;
  try {
    allTechs = activeOnly
      ? await api.get("/api/technician/active")
      : await api.get("/api/technician/all");
    renderTechs(allTechs);
  } catch (err) {
    toastError(err);
    renderTechs([]);
  }
}

function onEditTech(id) {
  const t = allTechs.find((x) => techId(x) === id);
  if (!t) return;
  qs("#techModalTitle").textContent = "Edit technician";
  qs("#techId").value = techId(t);
  qs("#techName").value = t.name || "";
  qs("#techPhone").value = t.phone || "";
  qs("#techSpec").value = t.specialization || "";
  openModal("techModal");
}

async function onDeleteTech(id) {
  if (!confirmAction("Deactivate this technician?")) return;
  try {
    await api.del(`/api/technician/${id}`);
    toast("Technician deactivated.");
    loadTechs();
  } catch (err) {
    toastError(err);
  }
}

async function onSaveTech() {
  const id = qs("#techId").value;
  const payload = {
    name: qs("#techName").value.trim(),
    phone: qs("#techPhone").value.trim(),
    specialization: qs("#techSpec").value.trim(),
  };
  if (!payload.name || !payload.phone || !payload.specialization) {
    toast("Name, phone, and specialization are required.", "error");
    return;
  }
  try {
    if (id) {
      await api.put(`/api/technician/${id}`, payload);
      toast("Technician updated.");
    } else {
      await api.post("/api/technician/add", payload);
      toast("Technician added.");
    }
    closeModal("techModal");
    loadTechs();
  } catch (err) {
    toastError(err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "technicians") return;
  loadTechs();

  qs("#btnAdd").addEventListener("click", () => {
    qs("#techModalTitle").textContent = "Add technician";
    qs("#techForm").reset();
    qs("#techId").value = "";
    openModal("techModal");
  });

  qsa("[data-close]").forEach((btn) =>
    btn.addEventListener("click", () => closeModal("techModal"))
  );
  qs("#techSaveBtn").addEventListener("click", onSaveTech);

  qs("#btnFilter").addEventListener("click", async () => {
    const spec = qs("#filterSpec").value.trim();
    const activeOnly = qs("#filterActiveOnly").checked;
    if (!spec) return loadTechs();
    try {
      const data = activeOnly
        ? await api.get(`/api/technician/active/specialization/${encodeURIComponent(spec)}`)
        : await api.get(`/api/technician/specialization/${encodeURIComponent(spec)}`);
      allTechs = data;
      renderTechs(data);
    } catch (err) {
      toastError(err);
    }
  });

  qs("#filterActiveOnly").addEventListener("change", () => {
    if (!qs("#filterSpec").value.trim()) loadTechs();
  });

  qs("#btnClearFilter").addEventListener("click", () => {
    qs("#filterSpec").value = "";
    qs("#filterActiveOnly").checked = false;
    loadTechs();
  });
});

/* ---------------- maintenance.js ---------------- */
/* Maintenance: /api/maintenances/*
   Matches MaintenanceResponseDTO exactly: id, maintenanceDate,
   description, status, technicianId, equipmentId (IDs only — no
   nested names), so we look names up from the loaded equipment/
   technician lists. */

let allMaint = [];
let allEquipForSelect = [];
let allTechForSelect = [];

function maintId(m) {
  return m.id;
}

function equipLabel(m) {
  const e = allEquipForSelect.find((x) => x.id === m.equipmentId);
  return e ? e.name : `#${m.equipmentId ?? "—"}`;
}

function techLabel(m) {
  const t = allTechForSelect.find((x) => x.id === m.technicianId);
  return t ? t.name : `#${m.technicianId ?? "—"}`;
}

function renderMaint(list) {
  const tbody = qs("#maintRows");
  const isAdmin = currentUser()?.role === "ADMIN";
  if (!list || list.length === 0) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="6">No maintenance records found.</td></tr>`;
    return;
  }
  tbody.innerHTML = list
    .map(
      (m) => `
    <tr>
      <td class="cell-mono">${fmtDate(m.maintenanceDate)}</td>
      <td>${escapeHtml(equipLabel(m))}</td>
      <td>${escapeHtml(techLabel(m))}</td>
      <td class="muted">${escapeHtml(m.description || "—")}</td>
      <td>${statusChip(m.status)}</td>
      <td class="cell-actions">
        ${
          m.status !== "Completed"
            ? `<button class="btn btn-secondary btn-sm" onclick="onComplete(${maintId(m)})">Mark complete</button>`
            : ""
        }
        ${isAdmin ? `<button class="btn btn-danger btn-sm" onclick="onDeleteMaint(${maintId(m)})">Delete</button>` : ""}
      </td>
    </tr>`
    )
    .join("");
}

async function loadSelects() {
  try {
    allEquipForSelect = await api.get("/api/equipment");
    qs("#maintEquip").innerHTML = allEquipForSelect
      .map((e) => `<option value="${e.id}">${escapeHtml(e.name)}</option>`)
      .join("");
  } catch (err) {
    toastError(err);
  }
  try {
    allTechForSelect = await api.get("/api/technician/all");
    qs("#maintTech").innerHTML = allTechForSelect
      .map((t) => `<option value="${t.id}">${escapeHtml(t.name)}</option>`)
      .join("");
  } catch (err) {
    toastError(err);
  }
}

async function loadMaint() {
  const status = qs("#filterStatus").value;
  try {
    allMaint = status
      ? await api.get(`/api/maintenances/status/${status}`)
      : await api.get("/api/maintenances/all");
    renderMaint(allMaint);
  } catch (err) {
    toastError(err);
    renderMaint([]);
  }
}

async function onComplete(id) {
  try {
    await api.put(`/api/maintenances/${id}/complete`);
    toast("Marked as complete.");
    loadMaint();
  } catch (err) {
    toastError(err);
  }
}

async function onDeleteMaint(id) {
  if (!confirmAction("Delete this maintenance record?")) return;
  try {
    await api.del(`/api/maintenances/${id}`);
    toast("Maintenance record deleted.");
    loadMaint();
  } catch (err) {
    toastError(err);
  }
}

async function onSaveMaint() {
  const payload = {
    equipmentId: Number(qs("#maintEquip").value),
    technicianId: Number(qs("#maintTech").value),
    maintenanceDate: qs("#maintDate").value,
    description: qs("#maintDesc").value.trim(),
  };
  if (!payload.equipmentId || !payload.technicianId || !payload.maintenanceDate || !payload.description) {
    toast("Please select equipment, technician, a date, and a description.", "error");
    return;
  }
  try {
    await api.post("/api/maintenances/add", payload);
    toast("Work order created.");
    closeModal("maintModal");
    loadMaint();
  } catch (err) {
    toastError(err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "maintenance") return;
  loadSelects().then(loadMaint);

  qs("#btnAdd").addEventListener("click", () => {
    qs("#maintForm").reset();
    openModal("maintModal");
  });

  qsa("[data-close]").forEach((btn) =>
    btn.addEventListener("click", () => closeModal("maintModal"))
  );
  qs("#maintSaveBtn").addEventListener("click", onSaveMaint);
  qs("#filterStatus").addEventListener("change", loadMaint);
  qs("#btnClearFilter").addEventListener("click", () => {
    qs("#filterStatus").value = "";
    loadMaint();
  });
});

/* ---------------- login.js (real JWT auth) ----------------
   Sign-in only. There is no self-registration: accounts are created by
   an admin from the Manage Users page (see users.js). */
document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "login") return;

  if (isAuthed()) {
    window.location.href = homePageFor(currentUser().role);
    return;
  }

  const loginForm = qs("#loginForm");

  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = qs("#loginUser").value.trim();
    const password = qs("#loginPass").value;
    if (!username || !password) {
      toast("Enter a username and password.", "error");
      return;
    }
    try {
      const res = await apiRequest("/api/auth/login", {
        method: "POST",
        body: { username, password },
      });
      setToken(res.token);
      
      // Save the user object/details locally so currentUser() can read the role
      if (res.user) {
        localStorage.setItem("user", JSON.stringify(res.user));
      } else {
        // Fallback if the backend returns role directly or wrapped differently
        localStorage.setItem("user", JSON.stringify({ username, role: res.role }));
      }

      window.location.href = homePageFor(currentUser().role);
    } catch (err) {
      toastError(err);
    }
  });
});

/* ---------------- users.js (admin-only account management) ----------------
   Manage Users: /api/auth/users, /api/auth/register — locked to
   ROLE_ADMIN by SecurityConfig. This is the only place login accounts
   get created; there is no public sign-up. */

let allUsers = [];

function renderUsers(list) {
  const tbody = qs("#userRows");
  if (!list || list.length === 0) {
    tbody.innerHTML = `<tr class="empty-row"><td colspan="3">No user accounts yet.</td></tr>`;
    return;
  }
  const me = currentUser();
  tbody.innerHTML = list
    .map(
      (u) => `
    <tr>
      <td><strong>${escapeHtml(u.username)}</strong></td>
      <td><span class="chip chip-brand">${escapeHtml(u.role)}</span></td>
      <td class="cell-actions">
        ${
          me && me.username === u.username
            ? `<span class="muted">This is you</span>`
            : `<button class="btn btn-danger btn-sm" onclick="onDeleteUser(${u.id})">Delete</button>`
        }
      </td>
    </tr>`
    )
    .join("");
}

async function loadUsers() {
  try {
    allUsers = await api.get("/api/auth/users");
    renderUsers(allUsers);
  } catch (err) {
    toastError(err);
    renderUsers([]);
  }
}

async function onDeleteUser(id) {
  if (!confirmAction("Revoke this account? They won't be able to sign in anymore.")) return;
  try {
    await api.del(`/api/auth/users/${id}`);
    toast("Account removed.");
    loadUsers();
  } catch (err) {
    toastError(err);
  }
}

async function onSaveUser() {
  const username = qs("#userUsername").value.trim();
  const password = qs("#userPassword").value;
  const role = qs("#userRole").value;
  if (!username || !password) {
    toast("Enter a username and password.", "error");
    return;
  }
  if (password.length < 6) {
    toast("Use a password with at least 6 characters.", "error");
    return;
  }
  try {
    await apiRequest("/api/auth/register", {
      method: "POST",
      body: { username, password, role },
    });
    toast("Account created.");
    closeModal("userModal");
    loadUsers();
  } catch (err) {
    toastError(err);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.body.dataset.page !== "users") return;
  loadUsers();

  qs("#btnAdd").addEventListener("click", () => {
    qs("#userForm").reset();
    openModal("userModal");
  });

  qsa("[data-close]").forEach((btn) =>
    btn.addEventListener("click", () => closeModal("userModal"))
  );
  qs("#userSaveBtn").addEventListener("click", onSaveUser);
});