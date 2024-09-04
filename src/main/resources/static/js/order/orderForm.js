// 화면 진입시 호출
productList = [];
window.onload = function () {
    var salePriceList = document.querySelectorAll('.sale_price');
    var basePriceList = document.querySelectorAll('.product_base_price');
    var priceList = document.querySelectorAll('.price');


    salePriceList.forEach(function (element) {
        var price = parseInt(element.getAttribute('data-price'));
        element.textContent = formatPrice(price) + "원";
    });

    basePriceList.forEach(function (element) {
        var price = parseInt(element.getAttribute('data-price'));
        element.textContent = formatPrice(price) + "원";
    });

    priceList.forEach(function (element) {
        var price = parseInt(element.getAttribute('data-price'));
        element.textContent = formatPrice(price) + "원";
    })
}

// 공동현관 출입 비밀번호
function togglePasswordField() {
    var passwordField = document.getElementById('entrance_pwd');
    var passwordRadio = document.getElementById('passwordRadio');

    if(passwordRadio.checked) {
        // 비밀번호 입력 필드 노출
        passwordField.style.display = 'block';
    } else {
        // 비밀번호 입력 필드 비노출
        passwordField.style.display = 'none';
    }
}

// 금액에 1000원 단위로 , 추가
function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 비밀번호 입력 검증
function validationPassword() {
    var passwordRadio = document.getElementById('passwordRadio');
    var passwordField = document.getElementById('entrance_pwd');

    if(passwordRadio.checked && passwordField.value.trim() === '') {
        event.preventDefault(); // 폼 제출 방지
        alert('공동현관 비밀번호가 입력되지 않았습니다.');
        passwordField.focus(); // 비밀번호 입력 폼으로 포커스 이동
    }
}

// 필수 입력 검증
function validationRequiredInfo(event) {
    var orderInfoFieldList = document.querySelectorAll('.order_info');

    for(var i = 0; i<orderInfoFieldList.length; i++) {
        if(orderInfoFieldList[i].value.trim() === '') {
            event.preventDefault(); // 폼 제출 방지
            alert('필수 입력 항목이 비어있습니다.');
            orderInfoFieldList[i].focus(); // 미입력 폼으로 포커스 이동
            return; // 함수 종료
        }
    }
}

// checkbox value
function getCheckboxValue(event) {
    var checkbox = document.getElementById('default_chk');

    if(checkbox.checked) {
        event.value = 'Y';
    } else {
        event.value = 'N';
    }
}

// submit
function onSubmit(event) {
    event.preventDefault();

    var custId = document.getElementById('cust_id').value;

    // validation
    validationPassword(event);
    validationRequiredInfo(event);

    var formData = {
        custId: document.getElementById('cust_id').value,
        custChk: document.getElementById('cust_chk').value,
        name: document.getElementById('name').value,
        orderRequestMessage: document.getElementById('ord_req_msg').value,
        pwd: null,
        isAllEbook: document.getElementById('is_all_ebook').value,
        isAllDawnDelivery: document.getElementById('is_all_dawn_delivery').value,
        dlvDate: document.getElementById('dlv_date').value,
        defaultChk: null,
        totalProdBasePrice: document.getElementById('total_prod_base_price').value,
        totalDiscPrice: document.getElementById('total_disc_price').value,
        totalPayPrice: document.getElementById('total_pay_price').value,
        dlvPrice: document.getElementById('dlv_price').value,
        totalOrdQty: document.getElementById('total_ord_qty').value,
        email: null,
        addressName: document.getElementById('address_name').value,
        telNum: document.getElementById('tel_num').value,
        zipCode: document.getElementById('zip_code').value,
        mainAddress: document.getElementById('main_address').value,
        detailAddress: document.getElementById('detail_address').value,
        commonEntrancePassword: document.getElementById('entrance_pwd').value,
        paymentMethod: document.getElementById('pay_method').value,
        productList: productList
    }

    if(custId == null || custId === '') {
        // 비회원
        formData.pwd = document.getElementById('non_cust_pwd').value;
        formData.custId = document.getElementById('non_cust_email').value;
        formData.email = document.getElementById('non_cust_email').value;
    } else {
        // 회원
        getCheckboxValue(event);
        formData.defaultChk = document.getElementById('default_chk').value;
        formData.email = document.getElementById('email').value
    }

    var jsonData = JSON.stringify(formData);

    console.log(jsonData);

    fetch('/payment/request', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: jsonData
    })
        .then(response => {
            //
        })
        .then(data => {
            console.log('Success:', data);
            window.location.href = '/payment/request';
        })
        .catch(error => {
            console.error(error);
        });
}