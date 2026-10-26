/**
 * ONESHOP CLIENT JAVASCRIPT (Thành viên 2)
 */

document.addEventListener('DOMContentLoaded', function () {

    // 1. CHỌN CHI NHÁNH ĐANG XEM (SWITCH BRANCH AJAX)
    const branchSelector = document.getElementById('globalBranchSelect');
    if (branchSelector) {
        branchSelector.addEventListener('change', function () {
            const branchId = this.value;
            fetch('/auth/select-branch?branchId=' + branchId, {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    window.location.reload();
                }
            });
        });
    }

    // 2. THÊM VÀO GIỎ HÀNG BẰNG AJAX (ADD TO CART)
    document.querySelectorAll('.btn-add-to-cart').forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            const productId = this.getAttribute('data-product-id');
            const qtyInput = document.getElementById('productQty');
            const quantity = qtyInput ? qtyInput.value : 1;

            const formData = new FormData();
            formData.append('productId', productId);
            formData.append('quantity', quantity);

            fetch('/cart/add', {
                method: 'POST',
                body: formData
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    // Cập nhật số lượng trên icon giỏ hàng ở Header
                    const badge = document.getElementById('cartBadge');
                    if (badge) {
                        badge.innerText = data.cartCount;
                        badge.style.display = 'inline-block';
                    }
                    showToast('Thành công', data.message, 'success');
                } else {
                    showToast('Thông báo', data.message, 'warning');
                }
            })
            .catch(err => {
                console.error(err);
                showToast('Lỗi', 'Không thể thêm vào giỏ hàng!', 'danger');
            });
        });
    });

    // 3. THÔNG BÁO TOAST THỜI TRANG
    function showToast(title, message, type) {
        let toastContainer = document.getElementById('toastContainer');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.id = 'toastContainer';
            toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            toastContainer.style.zIndex = '9999';
            document.body.appendChild(toastContainer);
        }

        const toastId = 'toast_' + Date.now();
        const toastHtml = `
            <div id="${toastId}" class="toast align-items-center text-bg-${type} border-0 shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                        <strong>${title}:</strong> ${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        `;
        toastContainer.insertAdjacentHTML('beforeend', toastHtml);
        const toastEl = document.getElementById(toastId);
        const bsToast = new bootstrap.Toast(toastEl, { delay: 3500 });
        bsToast.show();
        toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
    }

    // 4. CHUYỂN ĐỔI HÌNH ẢNH TRONG TRANG CHI TIẾT
    document.querySelectorAll('.thumbnail-img').forEach(thumb => {
        thumb.addEventListener('click', function () {
            const mainImg = document.getElementById('mainProductImage');
            if (mainImg) {
                mainImg.src = this.src;
            }
            document.querySelectorAll('.thumbnail-img').forEach(t => t.classList.remove('active-thumb'));
            this.classList.add('active-thumb');
        });
    });
});
