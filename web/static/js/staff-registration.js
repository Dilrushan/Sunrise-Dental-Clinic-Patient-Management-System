(function() {
    const showPw = document.getElementById('showPassword');
    showPw.addEventListener('change', function() {
        const type = this.checked ? 'text' : 'password';
        document.getElementById('password').type = type;
        document.getElementById('confirmPassword').type = type;
    });

    document.getElementById('staffForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const role = document.getElementById('role').value;

        if (!username || !password || !confirmPassword) {
            APP.showAlert('alert-box', 'Username and Password cannot be empty.', 'error');
            return;
        }
        if (username.length < 5) {
            APP.showAlert('alert-box', 'Username must be at least 5 characters long.', 'error');
            return;
        }
        if (password.length < 8) {
            APP.showAlert('alert-box', 'Password must be at least 8 characters long.', 'error');
            return;
        }
        if (password !== confirmPassword) {
            APP.showAlert('alert-box', 'Passwords do not match!', 'error');
            return;
        }

        const res = await APP.api('POST', '/api/staff', {
            username, password, confirmPassword, fullName: username, role
        });

        if (res.success) {
            APP.showAlert('alert-box', res.message, 'success');
            this.reset();
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    });

    document.getElementById('logoutBtn').addEventListener('click', () => APP.logout());

    (async function init() {
        const user = await APP.checkAuth();
        if (user) {
            sessionStorage.setItem('role', user.role);
            if (user.role !== 'Admin') { APP.redirectByRole(user.role); return; }
        }
    })();
})();
