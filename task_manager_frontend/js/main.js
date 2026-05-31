// ============================================================
//                        STATE MANAGEMENT
// ============================================================
let tasks = [];
let currentUser = null;
let editingTask = null;
let deletingTask = null;
let users = JSON.parse(localStorage.getItem('tm_users') || '[]');

// ============================================================
//                      TOAST NOTIFICATIONS
// ============================================================
function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');

    const bgColor = type === 'success' ? 'bg-green-600' : type === 'error' ? 'bg-red-600' : 'bg-blue-600';
    toast.className = `toast ${bgColor} text-white text-sm px-4 py-3 rounded pointer-events-auto`;
    toast.textContent = message;

    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('toast-exit');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// ============================================================
//                     API HELPER FUNCTIONS
// ============================================================

// Helper: Get auth headers with token
function getAuthHeaders() {
    const token = localStorage.getItem('tm_auth_token');
    return {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` })
    };
}

// TODO: Replace with your actual API endpoint
const API_BASE_URL = 'https://task-manager-api-d71w.onrender.com/api/v1';

// ============================================================
//                     AUTHENTICATION FUNCTIONS
// ============================================================

function checkAuth() {
    const session = localStorage.getItem('tm_session');
    if (session) {
        currentUser = JSON.parse(session);
        loadTasks();
        showMain();
    }
}

function showMain() {
    document.getElementById('auth-screen').classList.add('hidden');
    document.getElementById('main-screen').classList.remove('hidden');
    document.getElementById('user-name').textContent = currentUser.username;
    loadTasks();
}

function showAuth() {
    document.getElementById('auth-screen').classList.remove('hidden');
    document.getElementById('main-screen').classList.add('hidden');
    currentUser = null;
    localStorage.removeItem('tm_session');
    localStorage.removeItem('tm_auth_token');
}

// TODO: Call your backend login API here
// Expected response: { token: "jwt_token", user: { id, name, email } }
async function handleLogin() {
    const username = document.getElementById('login-username').value.trim();
    const pass = document.getElementById('login-pass').value;
    const err = document.getElementById('login-error');

    if (!username || !pass) {
        err.textContent = 'Fill all fields';
        err.classList.remove('hidden');
        return;
    }

    // TODO: Replace this with:
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password: pass })
    });

    const data = await response.json();
    if (!response.ok) {
        err.textContent = data.message || 'Login failed';
        err.classList.remove('hidden');
        return;
    }

    const { user, token } = data.data;

    localStorage.setItem('tm_auth_token', token);
    currentUser = user;

    err.classList.add('hidden');
    currentUser = user;
    localStorage.setItem('tm_session', JSON.stringify(user));
    showToast('Login successful!', 'success');
    showMain();
}

// TODO: Call your backend register API here
// Expected response: { token: "jwt_token", user: { id, name, email } }
async function handleRegister() {
    const username = document.getElementById('reg-username').value.trim();
    const email = document.getElementById('reg-email').value.trim();
    const pass = document.getElementById('reg-pass').value;
    const err = document.getElementById('reg-error');

    if (!username || !email || !pass) {
        err.textContent = 'Fill all fields';
        err.classList.remove('hidden');
        return;
    }

    if (pass.length < 6) {
        err.textContent = 'Password must be 6+ chars';
        err.classList.remove('hidden');
        return;
    }

    // TODO: Replace this with:
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password: pass })
    });

    const data = await response.json();
    if (response.status != 201) {
        err.textContent = data.message || 'Registration failed';
        err.classList.remove('hidden');
        return;
    }

    err.classList.add('hidden');
    showToast('Registration successful!', 'success');
}

// ============================================================
//                      TASK FUNCTIONS
// ============================================================

// TODO: Call GET /api/tasks to fetch user's tasks
async function loadTasks() {
    document.getElementById('loading-state').classList.remove('hidden');
    document.getElementById('task-board').classList.add('hidden');

    // TODO: Uncomment and use this code:
    try {
        const response = await fetch(`${API_BASE_URL}/tasks`, {
            headers: getAuthHeaders()
        });
        if (!response.ok) throw new Error('Failed to load tasks');
        const data = await response.json();
        const pageResponse = data.data;
        tasks = pageResponse.content || [];
        renderTasks();
    } catch (error) {
        showToast(error.message, 'error');
        tasks = [];
        renderTasks();
    } finally {
        document.getElementById('loading-state').classList.add('hidden');
    }
}

// TODO: Call POST /api/tasks to create a new task
async function addTask() {
    const title = document.getElementById('task-title').value.trim();
    const desc = document.getElementById('task-desc').value.trim();
    const err = document.getElementById('add-error');

    if (!title) {
        err.textContent = 'Title required';
        err.classList.remove('hidden');
        return;
    }

    if (tasks.length >= 999) {
        document.getElementById('limit-warning').classList.remove('hidden');
        return;
    }

    err.classList.add('hidden');
    const btn = document.getElementById('add-task-btn');
    btn.disabled = true;
    btn.textContent = '...';

    // TODO: Replace this with:
    try {
        const response = await fetch(`${API_BASE_URL}/tasks`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                taskTitle: title,
                description: desc,
            })
        });
        if (response.status == 409) {
            const data = await response.json();
            showToast(data.message, 'error');
            return;
        }

        if (response.status != 201) throw new Error('Failed to create task');

        const data = await response.json();
        tasks.push(data.data);
        renderTasks();
        document.getElementById('task-title').value = '';
        document.getElementById('task-desc').value = '';
        showToast('Task added successfully!', 'success');
    } catch (error) {
        showToast(error.message, 'error');
    } finally {
        btn.disabled = false;
        btn.innerHTML = '<i data-lucide="plus" style="width:14px;height:14px;"></i> Add';
        lucide.createIcons();
    }
}

// TODO: Call PUT /api/tasks/:id to update a task
async function updateTask() {
    if (!editingTask) return;

    const btn = document.getElementById('edit-save');
    btn.disabled = true;
    btn.textContent = '...';

    const updated = {
        ...editingTask,
        taskTitle: document.getElementById('edit-title').value.trim() || editingTask.taskTitle,
        description: document.getElementById('edit-desc').value.trim(),
        stage: document.getElementById('edit-stage').value
    };

    // TODO: Replace this with:
    try {
        const response = await fetch(`${API_BASE_URL}/tasks/${editingTask.taskId}`, {
            method: 'PATCH',
            headers: getAuthHeaders(),
            body: JSON.stringify(updated)
        });
        if (!response.ok) throw new Error('Failed to update task');
        const index = tasks.findIndex(t => t.taskId === editingTask.taskId);
        if (index >= 0) tasks[index] = updated;
        renderTasks();
        document.getElementById('edit-modal').classList.add('hidden');
        showToast('Task updated!', 'success');
    } catch (error) {
        showToast(error.message, 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Save';
    }
}

// TODO: Call DELETE /api/tasks/:id to delete a task
async function deleteTask() {
    if (!deletingTask) return;

    const btn = document.getElementById('delete-confirm');
    btn.disabled = true;
    btn.textContent = '...';

    // TODO: Replace this with:
    try {
        const response = await fetch(`${API_BASE_URL}/tasks/${deletingTask.taskId}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        if (!response.ok) throw new Error('Failed to delete task');
        tasks = tasks.filter(t => t.taskId !== deletingTask.taskId);
        renderTasks();
        document.getElementById('delete-modal').classList.add('hidden');
        showToast('Task deleted!', 'success');
    } catch (error) {
        showToast(error.message, 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Delete';
    }
}

// ============================================================
//                      RENDER FUNCTIONS
// ============================================================

function renderTasks() {
    const board = document.getElementById('task-board');
    const empty = document.getElementById('empty-state');

    if (tasks.length === 0) {
        board.classList.add('hidden');
        empty.classList.remove('hidden');
        return;
    }

    board.classList.remove('hidden');
    empty.classList.add('hidden');

    const todo = tasks.filter(t => t.stage === 'TODO');
    const progress = tasks.filter(t => t.stage === 'IN_PROGRESS');
    const done = tasks.filter(t => t.stage === 'DONE');

    document.getElementById('todo-count').textContent = todo.length;
    document.getElementById('progress-count').textContent = progress.length;
    document.getElementById('done-count').textContent = done.length;

    renderColumn('todo-list', todo);
    renderColumn('progress-list', progress);
    renderColumn('done-list', done);
}

function renderColumn(id, items) {
    const container = document.getElementById(id);
    const existing = new Map([...container.children].map(el => [el.dataset.taskId, el]));

    items.forEach(item => {
        if (existing.has(item.taskId)) {
            const el = existing.get(item.taskId);
            el.querySelector('.task-title').textContent = item.taskTitle;
            el.querySelector('.task-desc').textContent = item.description || '';
            existing.delete(item.taskId);
        } else {
            container.appendChild(createTaskCard(item));
        }
    });

    existing.forEach(el => el.remove());
    lucide.createIcons();
}

function createTaskCard(task) {
    const div = document.createElement('div');
    div.className = 'task-card border border-black p-3 bg-white fade-in';
    div.dataset.taskId = task.taskId;
    div.innerHTML = `
        <div class="flex justify-between items-start gap-2">
          <div class="flex-1 min-w-0">
            <p class="task-title text-sm font-medium truncate">${escapeHtml(task.taskTitle)}</p>
            <p class="task-desc text-xs text-gray-600 truncate">${escapeHtml(task.description || '')}</p>
          </div>
          <div class="flex gap-1 shrink-0">
            <button class="edit-btn p-1.5 border border-black hover:bg-black hover:text-white transition" title="Edit">
              <i data-lucide="pencil" style="width:14px;height:14px;"></i>
            </button>
            <button class="del-btn p-1.5 border border-black hover:bg-black hover:text-white transition" title="Delete">
              <i data-lucide="trash-2" style="width:14px;height:14px;"></i>
            </button>
          </div>
        </div>
      `;
    div.querySelector('.edit-btn').onclick = () => openEdit(task);
    div.querySelector('.del-btn').onclick = () => openDelete(task);
    return div;
}

function escapeHtml(text) {
    const map = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
    return text.replace(/[&<>"']/g, m => map[m]);
}

// ============================================================
//                      MODAL FUNCTIONS
// ============================================================

function openEdit(task) {
    editingTask = task;
    document.getElementById('edit-title').value = task.taskTitle;
    document.getElementById('edit-desc').value = task.description || '';
    document.getElementById('edit-stage').value = task.stage;
    document.getElementById('edit-modal').classList.remove('hidden');
}

function openDelete(task) {
    deletingTask = task;
    document.getElementById('delete-modal').classList.remove('hidden');
}

// ============================================================
//                      EVENT LISTENERS
// ============================================================

// Auth form switching
document.getElementById('show-register').addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('login-form').classList.add('hidden');
    document.getElementById('register-form').classList.remove('hidden');
});

document.getElementById('show-login').addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('register-form').classList.add('hidden');
    document.getElementById('login-form').classList.remove('hidden');
});

// Auth submit
document.getElementById('login-btn').addEventListener('click', handleLogin);
document.getElementById('reg-btn').addEventListener('click', handleRegister);
document.getElementById('logout-btn').addEventListener('click', showAuth);

// Task actions
document.getElementById('add-task-btn').addEventListener('click', addTask);
document.getElementById('edit-save').addEventListener('click', updateTask);
document.getElementById('edit-cancel').addEventListener('click', () => {
    document.getElementById('edit-modal').classList.add('hidden');
});
document.getElementById('delete-confirm').addEventListener('click', deleteTask);
document.getElementById('delete-cancel').addEventListener('click', () => {
    document.getElementById('delete-modal').classList.add('hidden');
});

// Initialize
window.addEventListener('load', () => {
    lucide.createIcons();
    checkAuth();
});