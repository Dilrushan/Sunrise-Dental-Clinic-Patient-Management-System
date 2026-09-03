(function() {
    const tbody = document.querySelector('#appointmentsTable tbody');

    async function loadData() {
        const res = await APP.api('GET', '/api/appointments');
        if (res.success) {
            renderTable(res.data || []);
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    }

    function renderTable(data) {
        tbody.innerHTML = '';
        data.forEach(row => {
            const tr = document.createElement('tr');
            tr.dataset.id = row.appointmentId;
            tr.dataset.treatment = row.treatmentPrescribed || '';
            tr.innerHTML =
                '<td>' + row.appointmentId + '</td>' +
                '<td>' + APP.escapeHtml(row.patientName) + '</td>' +
                '<td>' + APP.escapeHtml(row.contactNo) + '</td>' +
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
            APP.showAlert('alert-box', 'Please provide a name/contact to search.', 'warning');
            return;
        }
        const res = await APP.api('GET', '/api/appointments/search?q=' + encodeURIComponent(q));
        if (res.success) {
            renderTable(res.data || []);
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
            APP.showAlert('alert-box', 'Please select an appointment from the table.', 'warning');
            return;
        }
        const id = tr.dataset.id;
        const currentType = tr.cells[4].textContent;
        const newType = prompt('Update Visit Type (General/Specialist):', currentType);
        if (newType && newType.trim()) {
            const res = await APP.api('PUT', '/api/appointments/' + id + '/visit-type', { visitType: newType.trim() });
            if (res.success) {
                APP.showAlert('alert-box', 'Appointment updated!', 'success');
                loadData();
            } else {
                APP.showAlert('alert-box', res.message, 'error');
            }
        }
    });

    document.getElementById('calculateBtn').addEventListener('click', async function() {
        const tr = APP.getSelectedRow('appointmentsTable');
        if (!tr) {
            APP.showAlert('alert-box', 'Please select an appointment row to calculate/view bill.', 'warning');
            return;
        }
        const treatment = tr.dataset.treatment;
        if (!treatment) {
            APP.showAlert('alert-box', 'Cannot calculate bill. The doctor has not yet prescribed a treatment for this appointment.', 'warning');
            return;
        }
        const id = tr.dataset.id;
        const res = await APP.api('POST', '/api/billing/calculate', { appointmentId: parseInt(id) });
        if (res.success) {
            document.getElementById('billContent').textContent = res.data.billText;
            document.getElementById('billModal').classList.add('active');
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
