$(document).ready(function() {

    const dom = {

        init: function() {
            eventApplier.addEventToCartButton();
            eventApplier.addEventsToAddToCartButtons();
            eventApplier.addEventsToSupplierButtons();
        }

    };

    const event = {

        addToCart: function (event) {
            // let clickedButton = event.target;
            let productId = $(event.target).data("prod_id");
            ajax.getProductToCart(productId, responseHandler.updateCart);
        },

        toggleCart: function () {
            $("#cart").slideToggle();
        },

        sortBySupplier: function(event) {
            let id = $(event.target).attr("id");
            id = id.replace('supplier', '');
            ajax.getSupplierProducts(id, responseHandler.updateProducts);
        },

    };

    const eventApplier = {

        addEventsToSupplierButtons: function() {
            let buttons = $("button[id*='supplier']");
            buttons.click(event.sortBySupplier);
        },

        addEventToCartButton: function () {
            $('#cartButton').click(event.toggleCart);
        },

        addEventsToAddToCartButtons: function() {
            $('.addtocart').click(event.addToCart);
        },

    };

    const responseHandler = {

        updateCart: function (response) {
            let itemsNumber = response.itemsNumber;
            let totalPrice = response.totalPrice;
            $('#total-items').text(itemsNumber);
            $('#total-price').text(totalPrice);
            let products = response.shoppingCart;
            let cart = $("#cart");
            cart.empty();
            for (let i = 0; i<products.length;i++) {
                let wrapper = $('<div/>', {"class": "row"});
                let name = $('<p/>', {}).text(products[i].name);
                let quantity = $('<p/>', {}).text(products[i].quantity);
                let price = $('<p/>', {}).text(products[i].price);
                wrapper
                    .append(name)
                    .append(quantity)
                    .append(price)
                    .appendTo(cart);
            }
        },

        updateProducts: function(response) {
            $("#collectionName").text(response.collectionName);
            let productDiv = $("#products");
            productDiv.empty();
            for (let i = 0; response.collection.length; i++) {
                let outerWrapper = $('<div/>', {"class": "item col-xs-4 col-lg-4"});
                let wrapper = $('<div/>', {"class": "thumbnail"});
                let image = $('<img/>', {
                    "class": "group list-group-image",
                    src: "http://placehold.it/400x250/000/fff",
                });
                let innerWrapper = $('<div/>', {"class": "caption"});
                let productName = $('<h4/>', {"class": "group inner list-group-item-heading"})
                    .text(response.collection[i].name);
                let productDescription = $('<p/>', {"class": "group inner list-group-item-text"})
                    .text(response.collection[i].description);
                let row = $('<div/>', {"class": "row"});
                let priceWrapper = $('<div/>', {"class": "col-xs-12 col-md-6"});
                let price = $('<p/>', {"class": "lead"}).text(response.collection[i].price);
                let addToCartWrapper = $('<div/>', {"class": "col-xs-12 col-md-6"});
                let addToCart = $('<a/>', {
                    "class": "btn btn-success addtocart",
                    "data-prod_id": response.collection[i].id,
                }).text("Add To Cart").click(event.addToCart);
                priceWrapper.append(price);
                addToCartWrapper.append(addToCart);
                row.append(priceWrapper).append(addToCartWrapper);
                innerWrapper.append(productName);
                innerWrapper.append(productDescription);
                innerWrapper.append(row);
                wrapper.append(image);
                wrapper.append(innerWrapper);
                outerWrapper.append(wrapper);
                productDiv.append(outerWrapper);
            }
            eventApplier.addEventsToAddToCartButtons();
        },
    };

    const ajax = {

        getSupplierProducts: function(id, responseHandler) {
            $.ajax({
                type: "GET",
                url: "/api/get-supplier-products/" + id,
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },
        
        getProductToCart: function (id, responseHandler) {
            $.ajax({
                type: "GET",
                url: "/api/add-product/" + id,
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },
    };

    dom.init();

});
