const APP = {
    async api(method, url, body = null) {
        const opts = {
            method,
            credentials: 'same-origin',
            headers: {}
        };
        if (body) {
            opts.headers['Content-Type'] = 'application/json';
            opts.body = JSON.stringify(body);
        }
        try {
            const res = await fetch(url, opts);
            const data = await res.json();
            return data;
        } catch (e) {
            return { success: false, message: 'Network error: ' + e.message, data: null };
        }
    },

    async checkAuth() {
        const res = await APP.api('GET', '/api/me');
        if (res.success) return res.data;
        window.location.href = 'login.html';
        return null;
    },

    async logout() {
        if (!confirm('Are you sure you want to logout?')) return;
        await APP.api('POST', '/api/logout');
        window.location.href = 'login.html';
    },

    getRole() {
        return sessionStorage.getItem('role');
    },

    redirectByRole(role) {
        switch (role) {
            case 'Admin': window.location.href = 'admin.html'; break;
            case 'Doctor': window.location.href = 'doctor.html'; break;
            case 'Receptionist': window.location.href = 'receptionist.html'; break;
            default: window.location.href = 'login.html';
        }
    },

    showAlert(containerId, message, type) {
        const container = document.getElementById(containerId);
        if (!container) return;
        container.innerHTML = '<div class="alert alert-' + type + '">' + APP.escapeHtml(message) + '</div>';
        setTimeout(() => { container.innerHTML = ''; }, 5000);
    },

    escapeHtml(text) {
        if (!text) return '';
        const d = document.createElement('div');
        d.textContent = text;
        return d.innerHTML;
    },

    formatCurrency(amount) {
        return 'LKR ' + Number(amount).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    },

    populateSelect(selectId, items, valueField, labelField, placeholder) {
        const sel = document.getElementById(selectId);
        if (!sel) return;
        sel.innerHTML = '';
        if (placeholder) {
            const opt = document.createElement('option');
            opt.value = '';
            opt.textContent = placeholder;
            sel.appendChild(opt);
        }
        items.forEach(item => {
            const opt = document.createElement('option');
            opt.value = typeof valueField === 'function' ? valueField(item) : item[valueField];
            opt.textContent = typeof labelField === 'function' ? labelField(item) : item[labelField];
            sel.appendChild(opt);
        });
    },

    getSelectedRow(tableId) {
        const table = document.getElementById(tableId);
        if (!table) return null;
        const rows = table.querySelectorAll('tbody tr.selected');
        return rows.length > 0 ? rows[0] : null;
    },

    initTableSelection(tableId) {
        const table = document.getElementById(tableId);
        if (!table) return;
        table.addEventListener('click', function(e) {
            const tr = e.target.closest('tr');
            if (!tr || !tr.parentElement.matches('tbody')) return;
            table.querySelectorAll('tbody tr').forEach(r => r.classList.remove('selected'));
            tr.classList.add('selected');
        });
    }
};
