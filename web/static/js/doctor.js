(function() {
    const tbody = document.querySelector('#appointmentsTable tbody');

    async function loadData() {
        const res = await APP.api('GET', '/api/appointments/doctor');
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
            tr.innerHTML =
                '<td>' + row.appointmentId + '</td>' +
                '<td>' + APP.escapeHtml(row.patientName) + '</td>' +
                '<td>' + APP.escapeHtml(row.contactNo) + '</td>' +
                '<td>' + APP.escapeHtml(row.appointmentDate) + '</td>' +
                '<td>' + APP.escapeHtml(row.visitType) + '</td>' +
                '<td>' + APP.escapeHtml(row.treatmentPrescribed || 'Not prescribed') + '</td>';
            tbody.appendChild(tr);
        });
    }

    document.getElementById('savePrescriptionBtn').addEventListener('click', async function() {
        const treatment = document.getElementById('treatmentSelect').value;
        if (!treatment) {
            APP.showAlert('alert-box', 'Please select a valid treatment option.', 'warning');
            return;
        }
        const tr = APP.getSelectedRow('appointmentsTable');
        if (!tr) {
            APP.showAlert('alert-box', 'Please select an appointment from the table first.', 'warning');
            return;
        }
        const id = tr.dataset.id;
        const res = await APP.api('PUT', '/api/appointments/' + id + '/prescription', { treatment: treatment });
        if (res.success) {
            APP.showAlert('alert-box', 'Prescription saved: ' + treatment + '\nBilling can now be calculated by the receptionist.', 'success');
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
            document.getElementById('user-info').textContent = 'Welcome, Dr. ' + user.fullName;
            sessionStorage.setItem('role', user.role);
            if (user.role !== 'Doctor') { APP.redirectByRole(user.role); return; }
        }
    })();

    loadData();
})();
