const state = {
    users: [],
    activeUserId: null,
};

const els = {
    status: document.querySelector('#status'),
    usersList: document.querySelector('#usersList'),
    itemsList: document.querySelector('#itemsList'),
    notesList: document.querySelector('#notesList'),
    activeUserLabel: document.querySelector('#activeUserLabel'),
    userForm: document.querySelector('#userForm'),
    itemForm: document.querySelector('#itemForm'),
    noteForm: document.querySelector('#noteForm'),
    tagFilter: document.querySelector('#tagFilter'),
    noteSearch: document.querySelector('#noteSearch'),
};

function setStatus(message, isError = false) {
    els.status.textContent = message;
    els.status.classList.toggle('error', isError);
}

function splitTags(value) {
    return value
        .split(',')
        .map((tag) => tag.trim())
        .filter(Boolean);
}

async function request(path, options = {}) {
    setStatus(`${options.method || 'GET'} ${path}`);
    const response = await fetch(path, {
        headers: {
            'Content-Type': 'application/json',
            ...options.headers,
        },
        ...options,
    });

    if (!response.ok) {
        const text = await response.text();
        throw new Error(text || `HTTP ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    const contentType = response.headers.get('content-type') || '';
    return contentType.includes('application/json') ? response.json() : response.text();
}

function userHeaders() {
    if (!state.activeUserId) {
        throw new Error('Сначала выберите пользователя');
    }

    return { 'X-Later-User-Id': state.activeUserId };
}

function renderUsers() {
    if (!state.users.length) {
        els.usersList.innerHTML = '<p class="empty">Пользователей пока нет.</p>';
        return;
    }

    els.usersList.innerHTML = state.users.map((user) => `
        <button class="user-card ${user.id === state.activeUserId ? 'active' : ''}" type="button" data-user-id="${user.id}">
            <span class="card-title">${user.firstName || ''} ${user.lastName || ''}</span>
            <span>${user.email || 'без email'}</span>
            <span class="meta">id: ${user.id} · ${user.state || 'state не задан'}</span>
        </button>
    `).join('');
}

function renderItems(items) {
    if (!items.length) {
        els.itemsList.innerHTML = '<p class="empty">Items не найдены.</p>';
        return;
    }

    els.itemsList.innerHTML = items.map((item) => `
        <article class="data-card">
            <div class="card-title">#${item.id} · ${item.url || 'без URL'}</div>
            <div class="meta">userId: ${item.userId ?? state.activeUserId}</div>
            <div class="tags">${(item.tags || []).map((tag) => `<span class="tag">${tag}</span>`).join('')}</div>
        </article>
    `).join('');
}

function renderNotes(notes) {
    if (!notes.length) {
        els.notesList.innerHTML = '<p class="empty">Notes не найдены.</p>';
        return;
    }

    els.notesList.innerHTML = notes.map((note) => `
        <article class="data-card">
            <div class="card-title">#${note.id} · item ${note.itemId}</div>
            <p>${note.text || ''}</p>
            <div class="meta">${note.itemUrl || 'URL не задан'} · ${note.dateOfNote || 'дата не задана'}</div>
        </article>
    `).join('');
}

async function loadUsers() {
    try {
        state.users = await request('/users');
        if (!state.activeUserId && state.users[0]) {
            state.activeUserId = state.users[0].id;
        }
        const active = state.users.find((user) => user.id === state.activeUserId);
        els.activeUserLabel.textContent = active ? `${active.firstName || active.email} · id ${active.id}` : 'Выберите пользователя';
        renderUsers();
        setStatus('Пользователи загружены');
    } catch (error) {
        setStatus(error.message, true);
    }
}

async function loadItems() {
    try {
        const tags = splitTags(els.tagFilter.value);
        const params = new URLSearchParams();
        tags.forEach((tag) => params.append('tags', tag));
        const query = params.toString() ? `?${params}` : '';
        const items = await request(`/items${query}`, { headers: userHeaders() });
        renderItems(items);
        setStatus('Items загружены');
    } catch (error) {
        setStatus(error.message, true);
    }
}

async function loadNotes(path = '/notes') {
    try {
        const notes = await request(path, { headers: userHeaders() });
        renderNotes(notes);
        setStatus('Notes загружены');
    } catch (error) {
        setStatus(error.message, true);
    }
}

els.usersList.addEventListener('click', (event) => {
    const card = event.target.closest('[data-user-id]');
    if (!card) {
        return;
    }

    state.activeUserId = Number(card.dataset.userId);
    const active = state.users.find((user) => user.id === state.activeUserId);
    els.activeUserLabel.textContent = active ? `${active.firstName || active.email} · id ${active.id}` : `id ${state.activeUserId}`;
    renderUsers();
    loadItems();
    loadNotes();
});

els.userForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const form = new FormData(els.userForm);
    const payload = {
        email: form.get('email'),
        firstName: form.get('firstName'),
        lastName: form.get('lastName'),
        registrationDate: new Date().toISOString().slice(0, 10),
        dateOfBirth: form.get('dateOfBirth'),
        state: 'ACTIVE',
    };

    try {
        const user = await request('/users', { method: 'POST', body: JSON.stringify(payload) });
        state.activeUserId = user.id;
        els.userForm.reset();
        await loadUsers();
    } catch (error) {
        setStatus(error.message, true);
    }
});

els.itemForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const form = new FormData(els.itemForm);
    const payload = {
        url: form.get('url'),
        tags: splitTags(form.get('tags') || ''),
    };

    try {
        await request('/items', { method: 'POST', headers: userHeaders(), body: JSON.stringify(payload) });
        els.itemForm.reset();
        await loadItems();
    } catch (error) {
        setStatus(error.message, true);
    }
});

els.noteForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const form = new FormData(els.noteForm);
    const payload = {
        itemId: Number(form.get('itemId')),
        text: form.get('text'),
    };

    try {
        await request('/notes', { method: 'POST', headers: userHeaders(), body: JSON.stringify(payload) });
        els.noteForm.reset();
        await loadNotes();
    } catch (error) {
        setStatus(error.message, true);
    }
});

document.querySelector('#refreshUsers').addEventListener('click', loadUsers);
document.querySelector('#loadItems').addEventListener('click', loadItems);
document.querySelector('#loadNotes').addEventListener('click', () => loadNotes());
document.querySelector('#searchByUrl').addEventListener('click', () => {
    const value = els.noteSearch.value.trim();
    if (value) {
        loadNotes(`/notes?url=${encodeURIComponent(value)}`);
    }
});
document.querySelector('#searchByTag').addEventListener('click', () => {
    const value = els.noteSearch.value.trim();
    if (value) {
        loadNotes(`/notes?tag=${encodeURIComponent(value)}`);
    }
});

loadUsers();
