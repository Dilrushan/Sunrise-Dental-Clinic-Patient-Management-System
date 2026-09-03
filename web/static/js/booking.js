(function() {
    const patientSelect = document.getElementById('patientSelect');
    const doctorSelect = document.getElementById('doctorSelect');

    async function loadDropdowns() {
        const [patientsRes, doctorsRes] = await Promise.all([
            APP.api('GET', '/api/patients/list'),
            APP.api('GET', '/api/doctors')
        ]);

        if (patientsRes.success) {
            APP.populateSelect('patientSelect', patientsRes.data || [], 'fullName', 'fullName', 'Select Patient');
        }
        if (doctorsRes.success) {
            const docs = doctorsRes.data || [];
            doctorSelect.innerHTML = '<option value="">Select Doctor</option>';
            docs.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.userId;
                opt.textContent = d.fullName + ' (ID: ' + d.userId + ')';
                doctorSelect.appendChild(opt);
            });
        }
    }

    document.getElementById('bookingForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const patientName = patientSelect.value;
        const contact = document.getElementById('contact').value.trim();
        const doctorId = parseInt(doctorSelect.value) || -1;
        const date = document.getElementById('date').value.trim();
        const visitType = document.getElementById('visitType').value;

        if (!patientName || patientName === 'Select Patient') {
            APP.showAlert('alert-box', 'Please select a patient.', 'warning');
            return;
        }
        if (!contact) {
            APP.showAlert('alert-box', 'Contact number is required.', 'warning');
            return;
        }
        if (doctorId <= 0) {
            APP.showAlert('alert-box', 'Please select a doctor.', 'warning');
            return;
        }
        if (!date || date.length < 10) {
            APP.showAlert('alert-box', 'Please enter a valid date (YYYY-MM-DD).', 'warning');
            return;
        }

        const res = await APP.api('POST', '/api/appointments', {
            patientName, contact, doctorId, date, visitType
        });

        if (res.success) {
            APP.showAlert('alert-box', 'Appointment Booked Successfully!', 'success');
            if (res.data && res.data.notification) {
                const n = res.data.notification;
                const toMatch = n.match(/To:\s*(.+)/);
                const subMatch = n.match(/Subject:\s*(.+)/);
                if (toMatch && subMatch) {
                    APP.showAlert('alert-box', 'Email Notification Sent - To: ' + toMatch[1].trim() + ' | Subject: ' + subMatch[1].trim(), 'success');
                }
            }
            document.getElementById('contact').value = '';
            document.getElementById('date').value = '';
            patientSelect.selectedIndex = 0;
            doctorSelect.selectedIndex = 0;
            document.getElementById('visitType').selectedIndex = 0;
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    });

    document.getElementById('logoutBtn').addEventListener('click', () => APP.logout());

    (async function init() {
        const user = await APP.checkAuth();
        if (user) sessionStorage.setItem('role', user.role);
        loadDropdowns();
    })();
})();
