(function() {
    const tbody = document.querySelector('#appointmentsTable tbody');
    let allData = [];

    async function loadData() {
        const res = await APP.api('GET', '/api/appointments');
        if (res.success) {
            allData = res.data || [];
            renderTable(allData);
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    }

    function renderTable(data) {
        tbody.innerHTML = '';
        data.forEach(row => {
            const tr = document.createElement('tr');
            tr.dataset.id = row.appointmentId;
            tr.innerHTML =
                '<td>' + row.appointmentId + '</td>' +
                '<td>' + APP.escapeHtml(row.patientName) + '</td>' +
                '<td>' + APP.escapeHtml(row.contactNo) + '</td>' +
                '<td>' + row.doctorId + '</td>' +
                '<td>' + APP.escapeHtml(row.appointmentDate) + '</td>' +
                '<td>' + APP.escapeHtml(row.visitType) + '</td>' +
                '<td>' + APP.escapeHtml(row.treatmentPrescribed || 'Not prescribed') + '</td>' +
                '<td>' + APP.formatCurrency(row.fee) + '</td>';
            tbody.appendChild(tr);
        });
    }

    document.getElementById('searchBtn').addEventListener('click', async function() {
        const q = document.getElementById('searchInput').value.trim();
        if (!q) {
            APP.showAlert('alert-box', 'Please enter a name or contact number to search.', 'warning');
            return;
        }
        const res = await APP.api('GET', '/api/appointments/search?q=' + encodeURIComponent(q));
        if (res.success) {
            allData = res.data || [];
            renderTable(allData);
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    });

    document.getElementById('refreshBtn').addEventListener('click', function() {
        document.getElementById('searchInput').value = '';
        loadData();
        APP.showAlert('alert-box', 'Table refreshed.', 'success');
    });

    document.getElementById('updateBtn').addEventListener('click', async function() {
        const tr = APP.getSelectedRow('appointmentsTable');
        if (!tr) {
            APP.showAlert('alert-box', 'Please select an appointment row from the table.', 'warning');
            return;
        }
        const id = tr.dataset.id;
        const currentDate = tr.cells[4].textContent;
        const newDate = prompt('Enter new date (YYYY-MM-DD):', currentDate);
        if (newDate && newDate.trim() && newDate.trim() !== currentDate) {
            const res = await APP.api('PUT', '/api/appointments/' + id + '/date', { date: newDate.trim() });
            if (res.success) {
                APP.showAlert('alert-box', 'Appointment updated successfully!', 'success');
                loadData();
            } else {
                APP.showAlert('alert-box', res.message, 'error');
            }
        }
    });

    document.getElementById('deleteBtn').addEventListener('click', async function() {
        const tr = APP.getSelectedRow('appointmentsTable');
        if (!tr) {
            APP.showAlert('alert-box', 'Please select an appointment row from the table.', 'warning');
            return;
        }
        const id = tr.dataset.id;
        if (!confirm('Are you sure you want to delete appointment #' + id + '?')) return;
        const res = await APP.api('DELETE', '/api/appointments/' + id);
        if (res.success) {
            APP.showAlert('alert-box', 'Appointment deleted successfully.', 'success');
            if (res.data && res.data.notification) {
                const n = res.data.notification;
                const toMatch = n.match(/To:\s*(.+)/);
                const subMatch = n.match(/Subject:\s*(.+)/);
                if (toMatch && subMatch) {
                    APP.showAlert('alert-box', 'Email Notification Sent - To: ' + toMatch[1].trim() + ' | Subject: ' + subMatch[1].trim(), 'success');
                }
            }
            loadData();
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    });

    document.getElementById('logoutBtn').addEventListener('click', () => APP.logout());
    APP.initTableSelection('appointmentsTable');

    (async function init() {
        const user = await APP.checkAuth();
        if (user) {
            document.getElementById('user-info').textContent = 'Welcome, ' + user.fullName + ' (' + user.role + ')';
            sessionStorage.setItem('role', user.role);
        }
    })();

    loadData();
})();
