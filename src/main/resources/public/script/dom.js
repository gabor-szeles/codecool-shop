const dom = {

    addEventListeners: function () {
        $('.addtocart').click(dom.addToCartClick);
        $('#shoppingCart').on('show.bs.modal', dom.reviewCart);
        $('#shoppingCart').on('hide.bs.modal', dom.deleteModalContent);

    },

    refreshHeader: function (response) {
        var itemsNumber = response.itemsNumber;
        var totalPrice = response.totalPrice;
        $('#total-items').text(itemsNumber);
        $('#total-price').text(totalPrice);

    },

    addToCartClick: function (event) {
        var clickedButton = event.target;
        var productId = {"productid" : clickedButton.dataset.prod_id};
        $.ajax({
            type: "POST",
            url: "/api/additem",  // Send the login info to this page
            data: JSON.stringify(productId),
            dataType: "json",
            contentType: "application/json",
            success: dom.refreshHeader,
        });

    },

    reviewCart: function () {
        $.ajax({
            type: "POST",
            url: "/review",
            success: dom.drawReview,
        });
    },

    drawReview: function (response) {
        var list = JSON.parse(response).shoppingCart;
        for (let i = 0; i<list.length;i++) {
            console.log("INSIDE")
            $('#reviewTable').append(`
                <tr class="addedRow">
                    <td>${list[i].name}</td>
                    <td>${list[i].quantity}</td>
                    <td>${list[i].price}</td>
                </tr>
            `);

        }
    },

    deleteModalContent: function () {
        $('.addedRow').empty();
    }

}

dom.addEventListeners();