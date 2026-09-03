(function() {
    document.getElementById('patientForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const name = document.getElementById('patientName').value.trim();
        const address = document.getElementById('address').value.trim();
        const contact = document.getElementById('contactNumber').value.trim();
        const email = document.getElementById('email').value.trim();
        const history = document.getElementById('history').value.trim();
        const date = document.getElementById('appointmentDate').value.trim();
        const treatment = document.getElementById('treatmentType').value;

        if (!name || !contact) {
            APP.showAlert('alert-box', 'Patient Name and Contact Number are required fields.', 'warning');
            return;
        }
        if (name.length < 5) {
            APP.showAlert('alert-box', 'Patient name must be at least 5 characters long.', 'error');
            return;
        }
        if (!email || !/^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(email)) {
            APP.showAlert('alert-box', 'Please enter a valid email address.', 'error');
            return;
        }
        if (!date || date.length < 10) {
            APP.showAlert('alert-box', 'Please enter a valid appointment date (YYYY-MM-DD).', 'error');
            return;
        }
        if (!treatment) {
            APP.showAlert('alert-box', 'Please select a valid treatment option.', 'warning');
            return;
        }

        const res = await APP.api('POST', '/api/appointments/new-patient', {
            name, address, contact, email, history, doctorId: 1, date, visitType: treatment
        });

        if (res.success) {
            APP.showAlert('alert-box', 'Patient and appointment successfully registered!', 'success');
            this.reset();
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    });

    (async function init() {
        const user = await APP.checkAuth();
        if (user) sessionStorage.setItem('role', user.role);
    })();
})();
