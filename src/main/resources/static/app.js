const safeGet = (id) => document.getElementById(id);

document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    renderCart();
    updateHeader();
    updateCartBadge();
    if (window.location.pathname.includes('admin.html')) loadAdminData();
});

function getCartKey() {
    const user = sessionStorage.getItem('loggedUser');
    return user ? 'cart_' + user : 'cart_guest';
}

function updateHeader() {
    const container = safeGet('auth-buttons');
    if (!container) return;
    const user = sessionStorage.getItem('loggedUser');
    const role = sessionStorage.getItem('loggedRole');

    if (user) {
        let adminBtn = (role === 'ADMIN') ? `<button onclick="window.location.href='admin.html'" style="background:#333; color:white; margin-right:10px;">PANEL</button>` : '';
        container.innerHTML = `
            ${adminBtn}
            <span style="margin-right:10px; font-size:12px;">Witaj, <b>${user}</b></span>
            <button class="btn-secondary" onclick="logout()">Wyloguj</button>
        `;
    } else {
        container.innerHTML = `
            <button class="btn-secondary" onclick="safeGet('registerModal').style.display='flex'">Rejestracja</button>
            <button onclick="safeGet('loginModal').style.display='flex'">Logowanie</button>
        `;
    }
}

async function handleLogin(event) {
    event.preventDefault();
    const res = await fetch('/api/auth/login', { method: 'POST', body: new URLSearchParams(new FormData(event.target)) });
    if (res.ok) {
        const data = await res.json();
        sessionStorage.setItem('loggedUser', data.username);
        sessionStorage.setItem('loggedRole', data.role);
        window.location.href = 'index.html';
    } else alert('Błąd logowania');
}

async function handleRegister(event) {
    event.preventDefault();
    const res = await fetch('/api/auth/register', { method: 'POST', body: new URLSearchParams(new FormData(event.target)) });
    if (res.ok) { alert('Zarejestrowano!'); window.location.reload(); }
}

function logout() { sessionStorage.clear(); window.location.href = 'index.html'; }

async function loadProducts() {
    const grid = safeGet('product-grid');
    if (!grid) return;

    try {
        const res = await fetch('/api/products');
        const products = await res.json();

        // Aktualizacja licznika wyników w nowym UI
        const countSpan = safeGet('product-count');
        if (countSpan) countSpan.innerText = products.length;

        grid.innerHTML = products.map(p => `
            <div class="product-card">
                <img src="${p.imageUrl || 'https://via.placeholder.com/300x200'}" alt="${p.name}">
                <h2>${p.name}</h2>
                <div class="price">od ${p.price} zł/m²</div>
                
                <button class="btn-add" onclick="addToCart(${p.id}, '${p.name}', ${p.price})">DODAJ DO KOSZYKA</button>
                
                <div class="qty-selector">
                    <button onclick="changeQtyUI(${p.id}, -1)">-</button>
                    <input type="number" id="qty-input-${p.id}" value="1" min="1">
                    <button onclick="changeQtyUI(${p.id}, 1)">+</button>
                </div>
            </div>
        `).join('');
    } catch (e) {
        console.error("Błąd wczytywania produktów:", e);
    }
}

function changeQtyUI(id, delta) {
    const input = safeGet(`qty-input-${id}`);
    let val = parseInt(input.value) + delta;
    if (val < 1) val = 1;
    input.value = val;
}

function addToCart(id, name, price) {
    const qty = parseInt(safeGet(`qty-input-${id}`).value);
    let cart = JSON.parse(localStorage.getItem(getCartKey()) || '[]');
    let item = cart.find(i => i.id === id);
    if (item) item.qty += qty; else cart.push({ id, name, price, qty });
    localStorage.setItem(getCartKey(), JSON.stringify(cart));
    updateCartBadge();
    alert('Dodano do koszyka!');
}

function renderCart() {
    const container = safeGet('cart-items');
    const totalEl = safeGet('cart-total');
    if (!container) return;
    let cart = JSON.parse(localStorage.getItem(getCartKey()) || '[]');
    let total = 0;
    if (cart.length === 0) {
        container.innerHTML = '<p>Koszyk jest pusty.</p>';
        if (totalEl) totalEl.innerText = '0.00 zł';
        return;
    }
    container.innerHTML = cart.map(i => {
        total += i.price * i.qty;
        return `<div class="cart-item"><span>${i.name} (x${i.qty})</span><button class="btn-remove" onclick="removeFromCart(${i.id})">Usuń</button></div>`;
    }).join('');
    if (totalEl) totalEl.innerText = total.toFixed(2) + ' zł';
}

function removeFromCart(id) {
    let cart = JSON.parse(localStorage.getItem(getCartKey()) || '[]');
    cart = cart.filter(i => i.id !== id);
    localStorage.setItem(getCartKey(), JSON.stringify(cart));
    renderCart(); updateCartBadge();
}

function updateCartBadge() {
    const badge = safeGet('cart-badge');
    if (!badge) return;
    const cart = JSON.parse(localStorage.getItem(getCartKey()) || '[]');
    const count = cart.reduce((s, i) => s + i.qty, 0);
    badge.innerText = count;
    badge.style.display = count > 0 ? 'block' : 'none';
}

async function loadAdminData() {
    const pRes = await fetch('/api/products');
    const products = await pRes.json();
    safeGet('admin-products').innerHTML = products.map(p => `<tr><td>${p.name}</td><td><button class="btn-remove" onclick="deleteProd(${p.id})">X</button></td></tr>`).join('');
    const uRes = await fetch('/api/users');
    const users = await uRes.json();
    safeGet('admin-users').innerHTML = users.map(u => `<tr><td>${u.username}</td><td><button class="btn-remove" onclick="deleteUser(${u.id})">X</button></td></tr>`).join('');
}

async function adminAddProduct() {
    const product = { name: safeGet('p-name').value, price: safeGet('p-price').value, manufacturer: safeGet('p-manuf').value, finishType: safeGet('p-finish').value, imageUrl: safeGet('p-img').value };
    await fetch('/api/products', { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(product) });
    loadAdminData();
}

async function deleteProd(id) { await fetch(`/api/products/${id}`, { method: 'DELETE' }); loadAdminData(); }
async function deleteUser(id) { await fetch(`/api/users/${id}`, { method: 'DELETE' }); loadAdminData(); }

async function proceedToCheckout() {
    if (JSON.parse(localStorage.getItem(getCartKey()) || '[]').length === 0) return alert("Koszyk pusty");
    if (sessionStorage.getItem('loggedUser') || confirm("Kupić jako gość?")) window.location.href = 'checkout.html';
    else safeGet('loginModal').style.display = 'flex';
}

async function submitOrder(event) {
    event.preventDefault();
    alert('Sukces!');
    localStorage.removeItem(getCartKey());
    window.location.href = 'index.html';
}