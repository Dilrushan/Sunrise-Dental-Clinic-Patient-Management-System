(function() {
    const form = document.getElementById('loginForm');
    const showPw = document.getElementById('showPassword');
    const clearBtn = document.getElementById('clearBtn');

    showPw.addEventListener('change', function() {
        const pw = document.getElementById('password');
        pw.type = this.checked ? 'text' : 'password';
    });

    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        if (!username || !password) {
            APP.showAlert('alert-box', 'Please enter both username and password.', 'warning');
            return;
        }
        const res = await APP.api('POST', '/api/login', { username, password });
        if (res.success) {
            sessionStorage.setItem('role', res.data.role);
            sessionStorage.setItem('fullName', res.data.fullName);
            sessionStorage.setItem('username', res.data.username);
            APP.redirectByRole(res.data.role);
        } else {
            APP.showAlert('alert-box', res.message, 'error');
        }
    });

    clearBtn.addEventListener('click', function() {
        document.getElementById('username').value = '';
        document.getElementById('password').value = '';
        document.getElementById('alert-box').innerHTML = '';
    });
})();
